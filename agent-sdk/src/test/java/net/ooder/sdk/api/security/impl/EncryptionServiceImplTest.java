package net.ooder.sdk.api.security.impl;

import net.ooder.sdk.api.security.SessionKey;
import net.ooder.sdk.api.security.SessionKeyStatus;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class EncryptionServiceImplTest {
    
    private EncryptionServiceImpl service;
    
    @Before
    public void setUp() {
        service = new EncryptionServiceImpl();
    }
    
    @After
    public void tearDown() {
        if (service != null) {
            service.shutdown();
        }
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(service);
    }
    
    @Test
    public void testGenerateSessionKey() throws Exception {
        SessionKey key = service.generateSessionKey("peer-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(key);
        assertNotNull(key.getSessionId());
        assertEquals("peer-001", key.getPeerId());
        assertNotNull(key.getKey());
        assertTrue(key.getExpiresAt() > key.getCreatedAt());
        assertEquals(SessionKeyStatus.ACTIVE, key.getStatus());
    }
    
    @Test
    public void testEncryptWithSessionKey() throws Exception {
        SessionKey key = service.generateSessionKey("peer-001").get(10, TimeUnit.SECONDS);
        
        byte[] data = "test data".getBytes();
        byte[] encrypted = service.encryptWithSessionKey(key.getSessionId(), data).get(10, TimeUnit.SECONDS);
        
        assertNotNull(encrypted);
    }
    
    @Test
    public void testDecryptWithSessionKey() throws Exception {
        SessionKey key = service.generateSessionKey("peer-001").get(10, TimeUnit.SECONDS);
        
        byte[] data = "test data".getBytes();
        byte[] encrypted = service.encryptWithSessionKey(key.getSessionId(), data).get(10, TimeUnit.SECONDS);
        byte[] decrypted = service.decryptWithSessionKey(key.getSessionId(), encrypted).get(10, TimeUnit.SECONDS);
        
        assertNotNull(decrypted);
        assertArrayEquals(data, decrypted);
    }
    
    @Test
    public void testEncryptWithSessionKeyInvalidSession() throws Exception {
        byte[] data = "test data".getBytes();
        
        try {
            service.encryptWithSessionKey("invalid-session", data).get(10, TimeUnit.SECONDS);
            fail("Expected exception");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Session key not found"));
        }
    }
    
    @Test
    public void testShutdown() {
        service.shutdown();
    }
}
