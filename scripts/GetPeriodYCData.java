package com.app;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import java.util.*;
import java.io.*;
import redis.clients.jedis.Jedis;
import com.alibaba.fastjson.JSON;  
import com.alibaba.fastjson.JSONObject;  
import com.alibaba.fastjson.JSONArray;  
import com.alibaba.fastjson.serializer.SerializerFeature; 
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature; 

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class GetPeriodYCData
{
     private static final Logger logger = Logger.getLogger(GetPeriodYCData.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.WARN);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }

   public static void main(String args[]) throws Exception {
     
     CommandLineParser parser = new BasicParser( );
     String keyInput  = "{\"timetag0\":123,\"timetag1\":1111,\"pIDs\":[1,2,3,4],\"n\":4,\"name\":[\"station1_YC_1\",\"station1_YC_2\",\"station1_YC_3\",\"station1_YC_4\"]}";
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
     //输入json数据解析
     JSONObject keyInputObj = JSON.parseObject(keyInput);//字符串转为json结构体  
     List<Integer> integers = JSON.parseArray(keyInputObj.getJSONArray("pIDs").toJSONString(),Integer.class);//解析pid
     List<String> strings = JSON.parseArray(keyInputObj.getJSONArray("name").toJSONString(),String.class);//解析name
     int timeStart = keyInputObj.getIntValue("timetag0");//整型时间戳解析
     int timeEnd = keyInputObj.getIntValue("timetag1");//整型时间戳解析
     int dataLength = strings.size();//数据点长度
     logger.info("The dataLength is:" + dataLength);
     Jedis jedis = new Jedis(ipInput);
     List<Integer> sTimetag=new ArrayList();//用于存放符合条件的timetag
     List<String> sKey = new ArrayList();//用于存放符合条件的key;
     List<Integer> sNamePre = new ArrayList();//用于存放符合条件的name前缀;
     for(int i=0;i<dataLength;i++){
      int nameId = integers.get(i);
      String nameIdStr = Integer.toString(nameId);
       logger.info("The name of timetag list is:" + (strings.get(i)+"_"+"t"));
       List<String> listkey = jedis.lrange((strings.get(i)+"_"+"t"),0,-1);//取出nameId对应的timetag
       logger.info("The timetag list is :" + listkey);
       logger.info("To determine the timetag list:" + !listkey.isEmpty()); 
       if (!listkey.isEmpty()){
       for (int k=0;k<listkey.size();k++){
         int timeGet = Integer.parseInt(listkey.get(k));
         logger.info("The get timetag is:" + timeGet);
         logger.info("The get timetag is in the finding scope:" + ((timeGet>=timeStart)&&(timeGet<=timeEnd)));
         logger.info("The time start is:" + timeStart);
         logger.info("The time end is:" + timeEnd);
         if ((timeGet>=timeStart)&&(timeGet<=timeEnd)){
          sKey.add(strings.get(i)+"_"+listkey.get(k));
          sTimetag.add(timeGet);
          sNamePre.add(integers.get(i));
          logger.info("The satisfied time list is :" + sTimetag);
          logger.info("The satisfied key list is :" + sKey);
          logger.info("The satisfied name pre is :" + sNamePre);
         }
       }//end for
      }//end if 
     }//end for
      //断面数据读取
      JSONArray jsonArray1 = new JSONArray();//json数组
      JSONObject getObj = new JSONObject(true);
      JSONObject jsonObject2 = new JSONObject(true);
      String js;
      if(!sTimetag.isEmpty()){
        String[] membersList = new String[sTimetag.size()];//用于存放数据中心的取出值
        for(int i=0;i<sTimetag.size();i++){
          membersList[i] = jedis.get(sKey.get(i));
          JSONObject jsonObject1 = new JSONObject(true);
          getObj = JSON.parseObject(membersList[i],Feature.OrderedField);
          jsonObject1.put("name", sNamePre.get(i));
          jsonObject1.put("value",getObj);
          jsonArray1.add(i,jsonObject1); 
        }//end for
          jsonObject2.put("name","station1_YC");
          jsonObject2.put("members",jsonArray1);
          js = JSON.toJSONString(jsonObject2);
      }else{
        js = "{\"replyCode\":{\"code\": -1,\"message\":\"查询的表不存在\"},}";
      }
      jedis.close();
      String formatJs =  ForMatJSONStr.format(js);//格式化输出json
      System.out.println(formatJs); 
   }
}
