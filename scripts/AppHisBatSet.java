package com.app;
import org.apache.commons.io.FileUtils;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import redis.clients.jedis.Jedis;
import java.lang.Math;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class AppHisBatSet
{
     private static final Logger logger = Logger.getLogger(AppHisBatSet.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.INFO);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }
   public static void main(String args[]) throws Exception {
     CommandLineParser parser = new BasicParser( );
     String keyInput  = "";
     String ipInput = "127.0.0.1";
     String valueInput = "";
     Options options = new Options();

     options.addOption("k","keyInput", true, "The input key");
     options.addOption("i","ipInput", true, "The input ip");
     options.addOption("v","valueInput", true, "The input value");
     CommandLine commandLine = parser.parse( options, args );
     if( commandLine.hasOption('k') ) {
         keyInput = commandLine.getOptionValue('k');
     }
     if( commandLine.hasOption('i') ) {
         ipInput = commandLine.getOptionValue('i');
     }
     if( commandLine.hasOption('v') ) {
         valueInput = commandLine.getOptionValue('v');
     }

     
     Jedis jedis = new Jedis(ipInput);
     keyInput  = "";
     valueInput = "";
     String list = "bie";
     for(int i=0; i<10; i++){ 
        long pid = (long)(Math.random()*10);//生成随机数pid
        String namePre = "station1_YC_";//站名前缀固定
        String spid = Long.toString(pid);//pid转成字符串
        short status = (short)(Math.random()*100);//生成100以内的随机整数
        //short value  = (short)(Math.random()*100);//生成100以内的随机整数
        float value  = (float)(Math.random()*100);//生成100以内的随机浮点数
        int timetag = (int)(new Date().getTime());//时间戳
        String time = Integer.toString(timetag);
        String name = namePre + spid + "_" + time;//数据存储键
        String valueStr = "{\"status\":100,\"value\":100,\"timetag\":100}";//输入数据形式
        logger.info("The data type is:" + valueStr);
        JSONObject valueObj = JSON.parseObject(valueStr);//字符砖转为json结构体
        valueObj.put("status",status); 
        valueObj.put("value",value); 
        valueObj.put("timetag",timetag); 
        valueInput = JSON.toJSONString(valueObj);//json结构体转为字符串
        logger.info("The input value is:" + valueInput);
        keyInput = name ;  
        logger.info("The key is:" + keyInput);
        jedis.lpush(list,keyInput);
        jedis.set(keyInput,valueInput);
        String keyPre = namePre + spid + "_" + "t";//键的前缀
        logger.info("The input keyPre is:" + keyPre);
        logger.info("The input time is:" + time);
        jedis.lpush(keyPre,time);//将时间存储到键的前缀
        logger.info("The data is set in the database!");
    }
  }
}    
