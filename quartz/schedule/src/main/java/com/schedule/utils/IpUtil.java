package com.schedule.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtil {

    /**
     * 获取本地Ip
     *
     * @return
     */
    public static String getLocalAddr() {
        Enumeration interfaces = null;

        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException var4) {
            var4.printStackTrace();
            return null;
        }

        while (interfaces.hasMoreElements()) {
            NetworkInterface ifc = (NetworkInterface) interfaces.nextElement();
            Enumeration addressesOfAnInterface = ifc.getInetAddresses();

            while (addressesOfAnInterface.hasMoreElements()) {
                InetAddress address = (InetAddress) addressesOfAnInterface.nextElement();
                if (address.isSiteLocalAddress()) {
                    return address.getHostAddress();
                }
            }
        }

        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            return ip;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getLocalHostName() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            return hostName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(getLocalAddr());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        System.out.println(InetAddress.getLocalHost().getHostName());
    }
}
