package com.app;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
//import org.json.JSONObject;
import java.io.*;
import redis.clients.jedis.Jedis;
import com.alibaba.fastjson.JSON;  
import com.alibaba.fastjson.JSONObject;  
import com.alibaba.fastjson.serializer.SerializerFeature; 

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class AppGet
{
     private static final Logger logger = Logger.getLogger(AppGet.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.INFO);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }

   public static void main(String args[]) throws Exception {
     
     CommandLineParser parser = new BasicParser( );
     String keyInput  = "getmodel";
     String ipInput = "127.0.0.1";
     Options options = new Options();

     options.addOption("k","keyInput", true, "The input key");
     options.addOption("i","ipInput", true, "The input ip");
     CommandLine commandLine = parser.parse( options, args );
     if( commandLine.hasOption('k') ) {
         keyInput = commandLine.getOptionValue('k');
     }
     if( commandLine.hasOption('i') ) {
         ipInput = commandLine.getOptionValue('i');
     }

     Jedis jedis = new Jedis(ipInput);
     
     String js = jedis.get(keyInput);
     JSONObject json = JSON.parseObject(js);
     //System.out.println(json);   
     String fail = "{\"replyCode\":{\"code\": -1,\"message\":\"查询的表不存在\"},}";
     JSONObject fjson = JSON.parseObject(fail);
     if(js!=null){
       //System.out.println(js);
       System.out.println(json);
       //logger.info("The data is get!");
    }else{
       //logger.warn("The data does not exist in the database!");
       System.out.println(fjson);
     }
   }
}

