
package net.ooder.sdk.service.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.infra.exception.SDKException;

public class UdpClient {
    
    private static final Logger log = LoggerFactory.getLogger(UdpClient.class);
    
    private int timeout;
    private final int bufferSize;
    private DatagramSocket socket;
    private final AtomicLong requestIdGenerator = new AtomicLong(0);
    private final ConcurrentMap<Long, CompletableFuture<byte[]>> pendingRequests = new ConcurrentHashMap<>();
    
    public UdpClient() {
        this(5000, 65536);
    }
    
    public UdpClient(int timeout, int bufferSize) {
        this.timeout = timeout;
        this.bufferSize = bufferSize;
    }
    
    public void connect() throws SocketException {
        if (socket == null || socket.isClosed()) {
            socket = new DatagramSocket();
            socket.setSoTimeout(timeout);
            log.info("UDP Client connected");
        }
    }
    
    public void disconnect() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            pendingRequests.clear();
            log.info("UDP Client disconnected");
        }
    }
    
    public void send(byte[] data, String host, int port) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new SDKException("UDP_CLIENT_NOT_CONNECTED", "UDP client is not connected");
        }
        
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }
    
    public byte[] sendAndReceive(byte[] data, String host, int port) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new SDKException("UDP_CLIENT_NOT_CONNECTED", "UDP client is not connected");
        }
        
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, address, port);
        socket.send(sendPacket);
        
        byte[] buffer = new byte[bufferSize];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        
        try {
            socket.receive(receivePacket);
            byte[] responseData = new byte[receivePacket.getLength()];
            System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), 
                responseData, 0, receivePacket.getLength());
            return responseData;
        } catch (SocketTimeoutException e) {
            throw new SDKException("UDP_TIMEOUT", "UDP request timed out");
        }
    }
    
    public CompletableFuture<byte[]> sendAsync(byte[] data, String host, int port) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return sendAndReceive(data, host, port);
            } catch (IOException e) {
                throw new RuntimeException("UDP request failed", e);
            }
        });
    }
    
    public boolean isConnected() {
        return socket != null && !socket.isClosed();
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
        if (socket != null && !socket.isClosed()) {
            try {
                socket.setSoTimeout(timeout);
            } catch (SocketException e) {
                log.warn("Failed to set socket timeout", e);
            }
        }
    }
    
    public int getTimeout() {
        return timeout;
    }
}
