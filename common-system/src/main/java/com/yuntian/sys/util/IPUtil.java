package com.yuntian.sys.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: yuntian
 * @Date: 2020/2/7 0007 16:37
 * @Description:
 */
public class IPUtil {

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"};

    /***
     * 获取客户端ip地址(可以穿透代理)
     * @param request
     * @return
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }


    /**
     * 获取本地ip 适合windows与linux
     * @return
     */
    public static String getLocalIP() {
        String localIp = "127.0.0.1";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                InetAddress ip = ni.getInetAddresses().nextElement();
                if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                    localIp = ip.getHostAddress();
                    break;
                }
            }
        } catch (Exception e) {
            try {
                localIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        }
        return localIp;
    }

}
