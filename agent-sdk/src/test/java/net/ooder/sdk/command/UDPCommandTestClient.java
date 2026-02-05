package net.ooder.sdk.command;

import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import net.ooder.sdk.network.packet.UDPPacket;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UDPCommandTestClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5680;

    public static void main(String[] args) {
        try {
            // 创建UDP套接字
            DatagramSocket socket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);

            // 创建命令数据包
            CommandPacket packet = new CommandPacket();
            packet.setOperation(CommandType.END_EXECUTE.getValue());
            packet.setSenderId("test-client");
            
            Map<String, Object> params = new HashMap<>();
            params.put("command", "echo");
            
            Map<String, Object> argsMap = new HashMap<>();
            argsMap.put("message", "Hello from UDP Command Test Client");
            params.put("args", argsMap);
            
            packet.setParams(params);

            // 将命令数据包转换为字节数组
            byte[] data = JSON.toJSONBytes(packet);

            // 创建数据报
            DatagramPacket datagramPacket = new DatagramPacket(data, data.length, serverAddress, SERVER_PORT);

            // 发送数据报
            System.out.println("Sending UDP command to " + SERVER_HOST + ":" + SERVER_PORT);
            socket.send(datagramPacket);
            System.out.println("UDP command sent successfully");

            // 关闭套接字
            socket.close();
        } catch (IOException e) {
            System.err.println("Error sending UDP command: " + e.getMessage());
            e.printStackTrace();
        }
    }
}