package net.ooder.sdk.terminal.discovery.impl;

import net.ooder.sdk.config.TerminalDiscoveryProperties;
import net.ooder.sdk.terminal.TerminalManager;
import net.ooder.sdk.terminal.discovery.TerminalDiscoverer;
import net.ooder.sdk.terminal.model.TerminalDevice;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LocalNetworkDiscoverer implements TerminalDiscoverer {
    private TerminalManager terminalManager;
    private boolean discoveryRunning = false;
    private ExecutorService executorService;
    
    @Autowired
    private TerminalDiscoveryProperties properties;
    
    public LocalNetworkDiscoverer(TerminalManager terminalManager) {
        this.terminalManager = terminalManager;
    }
    
    @Override
    public void setTerminalManager(TerminalManager terminalManager) {
        this.terminalManager = terminalManager;
    }
    
    @Override
    public void startDiscovery() {
        if (!discoveryRunning) {
            discoveryRunning = true;
            executorService = Executors.newSingleThreadExecutor();
            executorService.submit(this::discoveryTask);
        }
    }
    
    @Override
    public void stopDiscovery() {
        if (discoveryRunning) {
            discoveryRunning = false;
            if (executorService != null) {
                executorService.shutdown();
                try {
                    executorService.awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public boolean isDiscoveryRunning() {
        return discoveryRunning;
    }
    
    @Override
    public List<TerminalDevice> discoverDevices() {
        List<TerminalDevice> discoveredDevices = new ArrayList<>();
        
        try {
            // 获取本地网络接口
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                
                // 跳过回环接口和未启用的接口
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                
                // 获取接口的IP地址
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    
                    // 只处理IPv4地址
                    if (address.getAddress().length == 4) {
                        // 扫描同一网段的设备
                        scanNetwork(address, discoveredDevices);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        
        return discoveredDevices;
    }
    
    // 扫描网络
    private void scanNetwork(InetAddress address, List<TerminalDevice> discoveredDevices) {
        String ipAddress = address.getHostAddress();
        String networkPrefix = ipAddress.substring(0, ipAddress.lastIndexOf(".") + 1);
        
        // 扫描同一网段的设备（简单实现，实际项目中可能需要更复杂的扫描策略）
        ExecutorService scanExecutor = Executors.newFixedThreadPool(254);
        
        for (int i = 1; i <= 254; i++) {
            final String targetIp = networkPrefix + i;
            
            // 跳过自身IP
            if (targetIp.equals(ipAddress)) {
                continue;
            }
            
            scanExecutor.submit(() -> {
                try {
                    InetAddress targetAddress = InetAddress.getByName(targetIp);
                    if (targetAddress.isReachable(1000)) { // 1秒超时
                        // 创建设备对象
                        TerminalDevice device = new TerminalDevice(
                            targetAddress.getHostName(),
                            "unknown",
                            targetIp,
                            getMacAddress(targetIp)
                        );
                        
                        synchronized (discoveredDevices) {
                            discoveredDevices.add(device);
                        }
                        
                        // 通知终端管理器
                        if (terminalManager != null) {
                            ((net.ooder.sdk.terminal.impl.TerminalManagerImpl) terminalManager).addDiscoveredDevice(device);
                        }
                    }
                } catch (Exception e) {
                    // 忽略扫描错误
                }
            });
        }
        
        scanExecutor.shutdown();
        try {
            scanExecutor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // 获取MAC地址（简单实现，实际项目中可能需要更复杂的方法）
    private String getMacAddress(String ipAddress) {
        // 这里只是一个示例实现，实际项目中需要通过ARP等方式获取
        return "00:00:00:00:00:00";
    }
    
    // 发现任务
    private void discoveryTask() {
        while (discoveryRunning) {
            discoverDevices();
            
            // 使用配置的扫描间隔
            int scanInterval = properties != null ? properties.getScanInterval() : 30000;
            try {
                Thread.sleep(scanInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
