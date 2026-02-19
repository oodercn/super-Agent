package net.ooder.sdk.api.security.impl;

import net.ooder.sdk.api.security.KeyPair;
import net.ooder.sdk.api.security.TokenInfo;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SecurityServiceImplTest {
    
    private SecurityServiceImpl service;
    
    @Before
    public void setUp() {
        service = new SecurityServiceImpl();
    }
    
    @After
    public void tearDown() {
        service.shutdown();
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(service);
    }
    
    @Test
    public void testGenerateKeyPair() {
        KeyPair keyPair = service.generateKeyPair();
        
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublicKey());
        assertNotNull(keyPair.getPrivateKey());
        assertEquals("RSA", keyPair.getAlgorithm());
    }
    
    @Test
    public void testGenerateKeyPairWithExpiry() {
        KeyPair keyPair = service.generateKeyPair(3600);
        
        assertNotNull(keyPair);
        assertTrue(keyPair.getExpiresAt() > System.currentTimeMillis());
    }
    
    @Test
    public void testEncryptDecrypt() {
        KeyPair keyPair = service.generateKeyPair();
        String originalData = "Hello, World!";
        
        String encrypted = service.encrypt(originalData, keyPair.getPublicKey());
        String decrypted = service.decrypt(encrypted, keyPair.getPrivateKey());
        
        assertNotNull(encrypted);
        assertEquals(originalData, decrypted);
    }
    
    @Test
    public void testEncryptAsync() throws Exception {
        KeyPair keyPair = service.generateKeyPair();
        String originalData = "Hello, Async!";
        
        String encrypted = service.encryptAsync(originalData, keyPair.getPublicKey()).get(10, TimeUnit.SECONDS);
        
        assertNotNull(encrypted);
    }
    
    @Test
    public void testDecryptAsync() throws Exception {
        KeyPair keyPair = service.generateKeyPair();
        String originalData = "Hello, Async!";
        
        String encrypted = service.encrypt(originalData, keyPair.getPublicKey());
        String decrypted = service.decryptAsync(encrypted, keyPair.getPrivateKey()).get(10, TimeUnit.SECONDS);
        
        assertEquals(originalData, decrypted);
    }
    
    @Test
    public void testSignVerify() {
        KeyPair keyPair = service.generateKeyPair();
        String data = "Data to sign";
        
        String signature = service.sign(data, keyPair.getPrivateKey());
        boolean verified = service.verify(data, signature, keyPair.getPublicKey());
        
        assertNotNull(signature);
        assertTrue(verified);
    }
    
    @Test
    public void testGenerateToken() {
        String token = service.generateToken("user-001", 3600000);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    
    @Test
    public void testGenerateTokenWithClaims() {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("role", "admin");
        
        String token = service.generateToken("user-001", claims, 3600000);
        
        assertNotNull(token);
    }
    
    @Test
    public void testValidateToken() {
        String token = service.generateToken("user-001", 3600000);
        
        TokenInfo tokenInfo = service.validateToken(token);
        
        assertNotNull(tokenInfo);
        assertEquals("user-001", tokenInfo.getSubject());
    }
    
    @Test
    public void testValidateTokenInvalid() {
        TokenInfo tokenInfo = service.validateToken("invalid-token");
        
        assertNull(tokenInfo);
    }
    
    @Test
    public void testValidateTokenNull() {
        TokenInfo tokenInfo = service.validateToken(null);
        
        assertNull(tokenInfo);
    }
    
    @Test
    public void testRevokeToken() {
        String token = service.generateToken("user-001", 3600000);
        
        service.revokeToken(token);
        
        TokenInfo tokenInfo = service.validateToken(token);
        assertNull(tokenInfo);
    }
    
    @Test
    public void testIsTokenRevoked() {
        String token = service.generateToken("user-001", 3600000);
        
        assertFalse(service.isTokenRevoked(token));
        
        service.revokeToken(token);
        
        assertTrue(service.isTokenRevoked(token));
    }
    
    @Test
    public void testShutdown() {
        service.shutdown();
    }
}
