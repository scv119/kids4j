package com.zhihu.kids;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: shenchen
 * Date: 10/15/12
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static String getHostName() {
        String runtimeName =  ManagementFactory.getRuntimeMXBean().getName();
        return runtimeName.split("@")[1];
    }

    public static int    getPid() {
        String runtimeName =  ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(runtimeName.split("@")[0]);
    }

    public static void main(String args[]) {
        System.out.println(getHostName() + getPid());
    }
}
