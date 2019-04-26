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

public class GetDayYCData
{
     private static final Logger logger = Logger.getLogger(GetDayYCData.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.WARN);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }

   public static void main(String args[]) throws Exception {
     
     CommandLineParser parser = new BasicParser( );
     String keyInput  = "{\"timetag\":123,\"pIDs\":[1,2,3,4],\"n\":4,\"name\":[\"station1_YC_1\",\"station1_YC_2\",\"station1_YC_3\",\"station1_YC_4\"]}";
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
     List<Integer> integers = JSON.parseArray(keyInputObj.getJSONArray("pIDs").toJSONString(),Integer.class);
     List<String> strings = JSON.parseArray(keyInputObj.getJSONArray("name").toJSONString(),String.class);
     String time = Integer.toString(keyInputObj.getIntValue("timetag"));//整型时间戳转字符串
     
     int dataLength = strings.size();//数据点长度
     logger.info("The dataLength is:" + dataLength);
     String[] membersList = new String[dataLength];
     Jedis jedis = new Jedis(ipInput);
     JSONArray jsonArray1 = new JSONArray();  
      
     //JSONObject jsonObject1 = new JSONObject();  
     JSONObject jsonObject2 = new JSONObject();  
     JSONObject getObj = new JSONObject();
     for(int i=0;i<dataLength;i++){
      membersList[i] = jedis.get(strings.get(i)+"_"+time);
      logger.info("The finding key is:" + (strings.get(i)+"_"+time));
      logger.info("The member list is:" + membersList[i]);
      if (membersList[i]!=null){
        getObj = JSON.parseObject(membersList[i]);//数据中心取出来的值
      }else{
        getObj = JSON.parseObject("{\"message\":\"查询的表不存在\"}");
      }
      
      logger.info("The input getObj is:" + getObj);
      JSONObject jsonObject1 = new JSONObject();
      jsonObject1.put("name",integers.get(i));
      jsonObject1.put("value",getObj);
      jsonArray1.add(i,jsonObject1);
     }//获取数据
      jsonObject2.put("name","station1_YC");
      jsonObject2.put("members",jsonArray1);
     
     String js = JSON.toJSONString(jsonObject2); 
     //System.out.println(json);   
     String fail = "{\"replyCode\":{\"code\": -1,\"message\":\"查询的表不存在\"},}";
     JSONObject fjson = JSON.parseObject(fail);
     if(js!=null){
       //System.out.println(js);
       System.out.println(js);
       //logger.info("The data is get!");
    }else{
       //logger.warn("The data does not exist in the database!");
       System.out.println(fjson);
     }
   }
}

