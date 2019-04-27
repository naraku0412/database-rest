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

public class AppSetHis
{
     private static final Logger logger = Logger.getLogger(AppSetHis.class);
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
    Cluster cluster = Cluster.builder()
                         .addContactPoint("127.0.0.1").withPort(9042)
                         .build();
    Session session = cluster.connect(); 
    String cql = "CREATE KEYSPACE if not exists mydb WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}";
    session.execute(cql);
    String cql1 = "CREATE TABLE if not exists mydb.test (a text,b int,c text,d int,PRIMARY KEY (a, b))";
    session.execute(cql1);
    String cql2 = "INSERT INTO mydb.test (a , b , c , d ) VALUES ( 'a2',4,'c2',6);";
    session.execute(cql2);
    cluster.close(); 

    }
}



