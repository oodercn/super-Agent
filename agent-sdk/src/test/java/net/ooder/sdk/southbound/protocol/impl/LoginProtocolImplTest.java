package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class LoginProtocolImplTest {
    
    private LoginProtocolImpl loginProtocol;
    
    @Before
    public void setUp() {
        loginProtocol = new LoginProtocolImpl();
    }
    
    @After
    public void tearDown() {
        loginProtocol.shutdown();
    }
    
    @Test
    public void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");
        
        LoginResult result = loginProtocol.login(request).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getSessionId());
    }
    
    @Test
    public void testLogout() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");
        
        LoginResult loginResult = loginProtocol.login(request).get(10, TimeUnit.SECONDS);
        loginProtocol.logout(loginResult.getSessionId()).get(10, TimeUnit.SECONDS);
        
        SessionInfo session = loginProtocol.getSession(loginResult.getSessionId()).get(10, TimeUnit.SECONDS);
        assertNull(session);
    }
    
    @Test
    public void testGetSession() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("testpass");
        
        LoginResult loginResult = loginProtocol.login(request).get(10, TimeUnit.SECONDS);
        
        SessionInfo session = loginProtocol.getSession(loginResult.getSessionId()).get(10, TimeUnit.SECONDS);
        assertNotNull(session);
        assertEquals("testuser", session.getUserName());
    }
}
