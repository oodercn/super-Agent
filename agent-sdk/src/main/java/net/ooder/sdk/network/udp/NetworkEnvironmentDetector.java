package net.ooder.sdk.network.udp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkEnvironmentDetector {
    
    public PortManager.NetworkEnvironment getEnvironment() {
        try {
            if (detectLocalEnvironment()) {
                return PortManager.NetworkEnvironment.LOCAL;
            }
            
            if (detectLanEnvironment()) {
                return PortManager.NetworkEnvironment.LAN;
            }
            
            if (detectIntranetEnvironment()) {
                return PortManager.NetworkEnvironment.INTRANET;
            }
        } catch (Exception e) {
            // 检测失败，默认返回 LOCAL
        }
        
        return PortManager.NetworkEnvironment.LOCAL;
    }
    
    public boolean detectLocalEnvironment() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.isLoopbackAddress();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean detectLanEnvironment() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }
                
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                        continue;
                    }
                    
                    String ipAddress = inetAddress.getHostAddress();
                    if (ipAddress.startsWith("192.168.") || 
                        ipAddress.startsWith("10.") || 
                        (ipAddress.startsWith("172.") && 
                         Integer.parseInt(ipAddress.split("\\.")[1]) >= 16 && 
                         Integer.parseInt(ipAddress.split("\\.")[1]) <= 31)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        
        return false;
    }
    
    public boolean detectIntranetEnvironment() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }
                
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) {
                        continue;
                    }
                    
                    String ipAddress = inetAddress.getHostAddress();
                    if (ipAddress.startsWith("10.") || 
                        (ipAddress.startsWith("172.") && 
                         Integer.parseInt(ipAddress.split("\\.")[1]) >= 16 && 
                         Integer.parseInt(ipAddress.split("\\.")[1]) <= 31)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        
        return false;
    }
    
    public String getBroadcastAddress() {
        PortManager.NetworkEnvironment env = getEnvironment();
        
        switch (env) {
            case LOCAL:
                return "127.0.0.1";
            case LAN:
                return "192.168.1.255";
            case INTRANET:
                return "255.255.255.255";
            default:
                return "255.255.255.255";
        }
    }
}