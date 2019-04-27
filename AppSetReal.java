package com.app;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import java.util.*;
import java.io.*;
import redis.clients.jedis.*;
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

public class AppSetReal
{
     private static final Logger logger = Logger.getLogger(AppSetReal.class);
     static {
              Logger rootLogger = Logger.getRootLogger();
              rootLogger.setLevel(Level.DEBUG);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }
   public static void main(String args[]) throws Exception {


     CommandLineParser parser = new BasicParser( );
     String keyInput  = "";
     String ipInput = "";
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

     JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1);
        poolConfig.setMaxIdle(1);
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("127.0.0.1",6379));
        JedisCluster cluster = new JedisCluster(nodes, poolConfig);
        cluster.set("age", "18");
        cluster.lpush("realtime","age");
        System.out.println(cluster.get("age"));
        try {
                cluster.close();
        } catch (IOException e) {
                e.printStackTrace();
        }

    }
}



