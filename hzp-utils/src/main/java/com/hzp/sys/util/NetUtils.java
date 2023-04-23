package com.hzp.sys.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @author wangchun
 * @date 2021/3/15
 */
public final class NetUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);
    private static String LOCAL_IP;
    private final static String LOCAL_IP_PREFIX = "192";
    // 华新园机房内网IP
    private final static String LOCAL_IP_PREFIX2 = "172.30";

    public static String getLocalIpAddress() {
        if (LOCAL_IP != null) {
            return LOCAL_IP;
        }
        // 备选节点
        String backupLocalIp = null;
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && StringUtils.isBlank(backupLocalIp)) {
                        backupLocalIp = ip.getHostAddress();
                    }

                    if (ip != null && ip instanceof Inet4Address) {
                        if (ip.getHostAddress().startsWith(LOCAL_IP_PREFIX) || ip.getHostAddress().startsWith(LOCAL_IP_PREFIX)) {
                            LOCAL_IP = ip.getHostAddress();
                            return LOCAL_IP;
                        }
                    }
                }
            }

            // 无法找到192网卡ip，使用备选节点
            if (StringUtils.isBlank(LOCAL_IP) && StringUtils.isNotBlank(backupLocalIp)) {
                logger.info("backupLocalIp:{}", backupLocalIp);
                LOCAL_IP = backupLocalIp;
            }

            return LOCAL_IP;

        } catch (Exception e) {
            logger.error("本地IP地址获取失败,error:{}", e);
        }
        return null;
    }
}
