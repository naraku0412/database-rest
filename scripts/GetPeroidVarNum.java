package com.app;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
//import org.json.JSONObject;
import java.util.*;
import java.io.*;
import redis.clients.jedis.Jedis;
import com.alibaba.fastjson.JSON;  
import com.alibaba.fastjson.JSONObject;  
import com.alibaba.fastjson.JSONArray;  
import com.alibaba.fastjson.serializer.SerializerFeature; 

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class GetPeroidVarNum
{
     private static final Logger logger = Logger.getLogger(GetPeroidVarNum.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.INFO);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }

   public static void main(String args[]) throws Exception {
     
     CommandLineParser parser = new BasicParser( );
     String keyInput  = "{\"timetag0\":123,\"timetag1\":1111,\"pIDs\":[1,2,3,4],\"n\":4,\"name\":[\"station1_YC\",\"station1_YC\",\"station1_YC\",\"station1_YC\"]}";
     String ipInput = "127.0.0.1";
     Options options = new Options();
     options.addOption("k","keyInput", true, "The input key");
     options.addOption("i","ipInput", true, "The input ip");
     CommandLine commandLine = parser.parse( options, args );
     if( commandLine.hasOption('k') ) {
         keyInput = commandLine.getOptionValue('k');
     }
     logger.info("The input key instruct is:" + keyInput);
     if( commandLine.hasOption('i') ) {
         ipInput = commandLine.getOptionValue('i');
     }
     JSONObject keyInputObj = JSON.parseObject(keyInput);//字符串转为json结构体  
     List<Integer> integers = JSON.parseArray(keyInputObj.getJSONArray("pIDs").toJSONString(),Integer.class);//解析pid
     List<String> strings = JSON.parseArray(keyInputObj.getJSONArray("name").toJSONString(),String.class);//解析name
     int timeStart = keyInputObj.getIntValue("timetag0");//整型时间戳解析
     int timeEnd = keyInputObj.getIntValue("timetag1");//整型时间戳解析
       
     int dataLength = strings.size();//数据点长度
     logger.info("The dataLength is:" + dataLength);
     Jedis jedis = new Jedis(ipInput);
     long[] Num = new long[dataLength];//长整型数组用于存放结果
     for(int i=0;i<dataLength;i++){
      int nameId = integers.get(i);
      String nameIdStr = Integer.toString(nameId);
       logger.info("The time list of name is:" + (strings.get(i)+"_"+nameIdStr));
       List<String> listkey = jedis.lrange((strings.get(i)+"_"+nameIdStr),0,-1);
       logger.info("The timetag list is :" + listkey);
       long j=0;
       if (listkey.size() == 0){
          Num[i]=j;
       }else{
       for (int k=0;k<listkey.size();k++){
         int timeGet = Integer.parseInt(listkey.get(k));
         if ((timeGet>=timeStart)&&(timeGet<=timeEnd)){
          j++;
         }
       }
       Num[i]=j;
      }
     }
      jedis.close();
      String js = Arrays.toString(Num); 
      System.out.println(js); 
   }
}

