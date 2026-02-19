package net.ooder.sdk.core.network;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class CoreConnectionTest {
    
    private TestConnection connection;
    
    @Before
    public void setUp() {
        connection = new TestConnection();
    }
    
    @Test
    public void testGetConnectionId() {
        assertNotNull(connection.getConnectionId());
    }
    
    @Test
    public void testGetType() {
        assertEquals(ConnectionType.HTTP, connection.getType());
    }
    
    @Test
    public void testGetState() {
        assertEquals(ConnectionState.DISCONNECTED, connection.getState());
    }
    
    @Test
    public void testSetTimeout() {
        connection.setTimeout(5000);
        assertEquals(5000, connection.getTimeout());
    }
    
    @Test
    public void testConnect() throws Exception {
        connection.connect().get();
        assertEquals(ConnectionState.CONNECTED, connection.getState());
    }
    
    @Test
    public void testDisconnect() throws Exception {
        connection.connect().get();
        connection.disconnect().get();
        assertEquals(ConnectionState.DISCONNECTED, connection.getState());
    }
    
    static class TestConnection implements CoreConnection {
        
        private String connectionId = "conn-test-001";
        private ConnectionType type = ConnectionType.HTTP;
        private ConnectionState state = ConnectionState.DISCONNECTED;
        private long timeout = 30000;
        private ConnectionListener listener;
        
        @Override
        public String getConnectionId() { return connectionId; }
        
        @Override
        public ConnectionType getType() { return type; }
        
        @Override
        public ConnectionState getState() { return state; }
        
        @Override
        public java.util.concurrent.CompletableFuture<Void> connect() {
            return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                state = ConnectionState.CONNECTING;
                if (listener != null) listener.onStateChanged(this, ConnectionState.DISCONNECTED, ConnectionState.CONNECTING);
                state = ConnectionState.CONNECTED;
                if (listener != null) listener.onConnected(this);
                return null;
            });
        }
        
        @Override
        public java.util.concurrent.CompletableFuture<Void> disconnect() {
            return java.util.concurrent.CompletableFuture.supplyAsync(() -> {
                state = ConnectionState.DISCONNECTING;
                if (listener != null) listener.onStateChanged(this, ConnectionState.CONNECTED, ConnectionState.DISCONNECTING);
                state = ConnectionState.DISCONNECTED;
                if (listener != null) listener.onDisconnected(this);
                return null;
            });
        }
        
        @Override
        public java.util.concurrent.CompletableFuture<byte[]> send(byte[] data) {
            return java.util.concurrent.CompletableFuture.completedFuture(data);
        }
        
        @Override
        public java.util.concurrent.CompletableFuture<byte[]> receive() {
            return java.util.concurrent.CompletableFuture.completedFuture(new byte[0]);
        }
        
        @Override
        public void setConnectionListener(ConnectionListener listener) {
            this.listener = listener;
        }
        
        @Override
        public void setTimeout(long timeoutMs) {
            this.timeout = timeoutMs;
        }
        
        @Override
        public long getTimeout() {
            return timeout;
        }
    }
}
