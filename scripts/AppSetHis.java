package com.app;

 
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import com.datastax.driver.core.Host;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.Metadata;

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
              rootLogger.setLevel(Level.INFO);
              rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
     }
   public static void main(String args[]) throws Exception {


     CommandLineParser parser = new BasicParser( );
     String keyInput  = "";
     keyInput  = "{\"timetag\":123456,\"name\":\"station1_YC_1\"}";
     String ipInput = "";
     String valueInput = "";
     String valueInput = "{\"status\":100,\"value\":100,\"timetag\":100}";
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
  
     JSONObject keyInputObj = JSON.parseObject(keyInput);//字符串转为json结构体  
     String name = keyInputObj.getString("name");
     int time = keyInputObj.getIntValue("timetag");//整型时间戳

    Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1").withPort(9042).build();
    Session session = cluster.connect(); 
   
   //创建表
    String cql = "CREATE KEYSPACE if not exists history WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}";
    session.execute(cql);
//    String cql1 = "CREATE TABLE if not exists history.test (key text,value text,time timestamp,PRIMARY KEY (key))";
    String cql1_1 = "CREATE TABLE if not exists history.";
    String cql1_2 = name + "_" + time; 
    String cql1_3 = " (key text,value text,time timestamp,PRIMARY KEY (key))";
    String cql1 = cql1_1 + cql1_2 + cql1_3 ; 
    session.execute(cql1);
    //数据写入 

   //String cql = "INSERT INTO mydb.test (a , b , c , d ) VALUES ( 'a2',4,'c2',6);";
   String cqlw_1 = "INSERT INTO history.";
   String cqlw_2 = name + "_" + time ; 
   String cqlw_3 = " (key,value,time) VALUES ('" + cqlw_2 + "', '" + valueInput + "', " + time + ")";
   String cqlw = cqlw_1 + cqlw_2 + cqlw_3 ; 
   session.execute(cqlw);
    //查询 
    //String cqlq_1 = "SELECT  FROM history WHERE key="
    String cqlq_1 = "SELECT ";
    String cqlq_2 = "value";
    Sring  cqlq_3 = "FROM history WHERE key="
    String cqlq_4 = name + "_" + time ;
    String cqlq = cqlq_1 + cqlq_2 + cqlq_3 + cqlq_4; 
    session.execute(cqlq);
    
    cluster.close(); 

    }
}



