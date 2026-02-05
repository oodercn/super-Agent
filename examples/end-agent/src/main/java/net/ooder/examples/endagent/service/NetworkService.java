package net.ooder.examples.endagent.service;

import net.ooder.examples.endagent.model.AiBridgeMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NetworkService {
    private final int BROADCAST_PORT = 9010;
    private final String BROADCAST_ADDRESS = "255.255.255.255";
    private DatagramSocket socket;
    private final ExecutorService executorService;
    private final AiBridgeProtocolService aiBridgeProtocolService;

    public NetworkService(@Lazy AiBridgeProtocolService aiBridgeProtocolService) {
        this.executorService = Executors.newCachedThreadPool();
        this.aiBridgeProtocolService = aiBridgeProtocolService;
        initializeSocket();
        startListening();
    }

    // 初始化UDP socket
    private void initializeSocket() {
        try {
            socket = new DatagramSocket(BROADCAST_PORT);
            socket.setBroadcast(true);
            System.out.println("NetworkService initialized on port " + BROADCAST_PORT);
        } catch (SocketException e) {
            System.err.println("Failed to initialize NetworkService: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 发送消息
    public void send(String message, String target) throws IOException {
        if (socket == null) {
            throw new IOException("NetworkService not initialized");
        }

        byte[] buffer = message.getBytes();
        InetAddress address;

        if (target.equals("broadcast")) {
            address = InetAddress.getByName(BROADCAST_ADDRESS);
        } else {
            address = InetAddress.getByName(target);
        }

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, BROADCAST_PORT);
        socket.send(packet);
        System.out.println("Sent message to " + target + ": " + message);
    }

    // 开始监听消息
    private void startListening() {
        executorService.submit(() -> {
            byte[] buffer = new byte[65536];
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    String sender = packet.getAddress().getHostAddress();

                    System.out.println("Received message from " + sender + ": " + message);

                    // 处理消息并发送响应
                    executorService.submit(() -> {
                        try {
                            AiBridgeMessage response = aiBridgeProtocolService.handleMessage(message);
                            if (response != null) {
                                send(com.alibaba.fastjson.JSON.toJSONString(response), sender);
                            }
                        } catch (Exception e) {
                            System.err.println("Error handling message: " + e.getMessage());
                            e.printStackTrace();
                        }
                    });

                } catch (IOException e) {
                    System.err.println("Error receiving message: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    // 关闭服务
    public void shutdown() {
        if (socket != null) {
            socket.close();
        }
        executorService.shutdown();
    }
}
