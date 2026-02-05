package net.ooder.sdk.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTestServer {
    private static final int PORT = 5680;

    public static void main(String[] args) {
        try {
            // 创建UDP套接字
            DatagramSocket socket = new DatagramSocket(PORT);
            System.out.println("UDP Test Server started on port " + PORT);

            // 准备接收缓冲区
            byte[] buffer = new byte[1024];

            while (true) {
                // 创建数据报
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // 接收数据包
                System.out.println("Waiting for UDP packet...");
                socket.receive(packet);

                // 处理接收到的数据
                byte[] data = new byte[packet.getLength()];
                System.arraycopy(buffer, 0, data, 0, packet.getLength());

                String message = new String(data);
                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();

                System.out.println("Received UDP packet from " + clientAddress.getHostAddress() + ":" + clientPort);
                System.out.println("Message: " + message);

                // 发送响应
                String response = "Hello from UDP Test Server";
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, clientAddress, clientPort);
                socket.send(responsePacket);
                System.out.println("Response sent to " + clientAddress.getHostAddress() + ":" + clientPort);
            }
        } catch (IOException e) {
            System.err.println("Error in UDP Test Server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}