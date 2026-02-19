package net.ooder.sdk.southbound.protocol.impl;

import net.ooder.sdk.southbound.protocol.*;
import net.ooder.sdk.southbound.protocol.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

public class DiscoveryProtocolImplTest {
    
    private DiscoveryProtocolImpl discoveryProtocol;
    
    @Before
    public void setUp() {
        discoveryProtocol = new DiscoveryProtocolImpl();
    }
    
    @After
    public void tearDown() {
        discoveryProtocol.shutdown();
    }
    
    @Test
    public void testDiscover() throws Exception {
        DiscoveryRequest request = new DiscoveryRequest();
        request.setRequestId("req-001");
        
        DiscoveryResult result = discoveryProtocol.discover(request).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertNotNull(result.getPeers());
    }
    
    @Test
    public void testDiscoverPeers() throws Exception {
        java.util.List<PeerInfo> peers = discoveryProtocol.discoverPeers().get(10, TimeUnit.SECONDS);
        
        assertNotNull(peers);
    }
}
