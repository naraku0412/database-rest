package com.app;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import redis.clients.jedis.Jedis;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class AppSet
{
     private static final Logger logger = Logger.getLogger(AppSet.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.INFO);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }
   public static void main(String args[]) throws Exception {
     CommandLineParser parser = new BasicParser( );
     String keyInput  = "getmodel";
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


     File file=new File("/workspaced/getModelDef");
     String content= FileUtils.readFileToString(file,"UTF-8");
     JSONObject jsonObject=new JSONObject(content);
     String js = jsonObject.toString();
     //System.out.println(jsonObject.toString())
     Jedis jedis = new Jedis(ipInput);
     jedis.set(keyInput,js);
     logger.info("The data is set in the database!");
    }
}    
