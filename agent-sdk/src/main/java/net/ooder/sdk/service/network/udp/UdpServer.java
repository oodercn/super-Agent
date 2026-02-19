
package net.ooder.sdk.service.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.infra.exception.SDKException;

public class UdpServer {
    
    private static final Logger log = LoggerFactory.getLogger(UdpServer.class);
    
    private final int port;
    private final int bufferSize;
    private DatagramSocket socket;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService executor;
    private PacketHandler defaultHandler;
    
    public UdpServer(int port) {
        this(port, 65536);
    }
    
    public UdpServer(int port, int bufferSize) {
        this.port = port;
        this.bufferSize = bufferSize;
        this.executor = Executors.newFixedThreadPool(4);
    }
    
    public void start() throws SocketException {
        if (running.compareAndSet(false, true)) {
            socket = new DatagramSocket(port);
            log.info("UDP Server started on port: {}", port);
            executor.submit(this::receiveLoop);
        }
    }
    
    public void stop() {
        if (running.compareAndSet(true, false)) {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            executor.shutdown();
            log.info("UDP Server stopped");
        }
    }
    
    private void receiveLoop() {
        byte[] buffer = new byte[bufferSize];
        while (running.get()) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byte[] data = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), data, 0, packet.getLength());
                handlePacket(data, packet.getAddress(), packet.getPort());
            } catch (IOException e) {
                if (running.get()) {
                    log.error("Error receiving packet", e);
                }
            }
        }
    }
    
    private void handlePacket(byte[] data, InetAddress address, int port) {
        if (defaultHandler != null) {
            executor.submit(() -> {
                try {
                    defaultHandler.handle(data, address.getHostAddress() + ":" + port, 
                        new PacketContext(address, port, this));
                } catch (Exception e) {
                    log.error("Error handling packet", e);
                }
            });
        }
    }
    
    public void send(byte[] data, String host, int port) throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new SDKException("UDP_SERVER_NOT_STARTED", "UDP server is not started");
        }
        InetAddress address = InetAddress.getByName(host);
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        socket.send(packet);
    }
    
    public void setDefaultHandler(PacketHandler handler) {
        this.defaultHandler = handler;
    }
    
    public int getPort() { return port; }
    public boolean isRunning() { return running.get(); }
    
    public static class PacketContext {
        private final InetAddress address;
        private final int port;
        private final UdpServer server;
        
        public PacketContext(InetAddress address, int port, UdpServer server) {
            this.address = address;
            this.port = port;
            this.server = server;
        }
        
        public InetAddress getAddress() { return address; }
        public int getPort() { return port; }
        public UdpServer getServer() { return server; }
        
        public void reply(byte[] data) throws IOException {
            server.send(data, address.getHostAddress(), port);
        }
    }
}
