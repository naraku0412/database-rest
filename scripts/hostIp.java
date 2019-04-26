//package com.app;

import java.net.InetAddress;
 
public class hostIp {
   public static String hostip() 
    throws Exception {
      InetAddress addr = InetAddress.getLocalHost();
      System.out.println("Local HostAddress: "+addr.getHostAddress());
      String hostname = addr.getHostName();
      System.out.println("Local host name: "+hostname);
      return addr.getHostAddress();
   }
}

