
package net.ooder.sdk.infra.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class NetUtils {
    
    private NetUtils() {
    }
    
    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }
    
    public static String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "127.0.0.1";
        }
    }
    
    public static List<String> getAllLocalAddresses() {
        List<String> addresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements()) {
                    InetAddress address = addressEnumeration.nextElement();
                    String hostAddress = address.getHostAddress();
                    if (hostAddress != null && !hostAddress.contains(":")) {
                        addresses.add(hostAddress);
                    }
                }
            }
        } catch (SocketException e) {
            addresses.add("127.0.0.1");
        }
        return addresses;
    }
    
    public static String getPreferredLocalAddress() {
        List<String> addresses = getAllLocalAddresses();
        for (String addr : addresses) {
            if (!addr.startsWith("127.") && !addr.startsWith("169.254.")) {
                return addr;
            }
        }
        return addresses.isEmpty() ? "127.0.0.1" : addresses.get(0);
    }
    
    public static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }
    
    public static boolean isValidSocketAddress(String address) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        String[] parts = address.split(":");
        if (parts.length != 2) {
            return false;
        }
        try {
            int port = Integer.parseInt(parts[1]);
            return isValidPort(port);
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static String formatSocketAddress(String host, int port) {
        return host + ":" + port;
    }
    
    public static String[] parseSocketAddress(String address) {
        if (address == null || address.isEmpty()) {
            return null;
        }
        String[] parts = address.split(":");
        if (parts.length != 2) {
            return null;
        }
        try {
            Integer.parseInt(parts[1]);
            return parts;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public static boolean isReachable(String host, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static boolean isLocalAddress(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            return address.isLoopbackAddress() || address.isSiteLocalAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }
    
    public static String getMacAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            if (networkInterface != null) {
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X", mac[i]));
                        if (i < mac.length - 1) {
                            sb.append(":");
                        }
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
        }
        return "00:00:00:00:00:00";
    }
}
