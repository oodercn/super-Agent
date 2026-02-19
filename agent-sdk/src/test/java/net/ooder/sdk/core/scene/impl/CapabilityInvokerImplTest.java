package net.ooder.sdk.core.scene.impl;

import net.ooder.sdk.api.scene.CapabilityInvoker;
import net.ooder.sdk.api.scene.CapabilityInvoker.CapabilityMetadata;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CapabilityInvokerImplTest {
    
    private CapabilityInvokerImpl invoker;
    
    @Before
    public void setUp() {
        invoker = new CapabilityInvokerImpl();
    }
    
    @After
    public void tearDown() {
        invoker.shutdown();
    }
    
    @Test
    public void testInvoke() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("key", "value");
        
        Object result = invoker.invoke("scene-001", "cap-001", params).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
    }
    
    @Test
    public void testInvokeWithoutSceneId() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("test", "data");
        
        Object result = invoker.invoke("cap-002", params).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
    }
    
    @Test
    public void testInvokeAsync() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("async", true);
        
        Object result = invoker.invokeAsync("scene-001", "cap-003", params).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
    }
    
    @Test
    public void testIsAvailable() throws Exception {
        invoker.registerHandler("scene-001", "cap-001", new Object());
        
        Boolean available = invoker.isAvailable("scene-001", "cap-001").get(10, TimeUnit.SECONDS);
        assertTrue(available);
        
        Boolean notAvailable = invoker.isAvailable("scene-001", "cap-999").get(10, TimeUnit.SECONDS);
        assertFalse(notAvailable);
    }
    
    @Test
    public void testGetMetadata() throws Exception {
        invoker.registerHandler("scene-001", "cap-001", new Object());
        
        CapabilityMetadata metadata = invoker.getMetadata("scene-001", "cap-001").get(10, TimeUnit.SECONDS);
        
        assertNotNull(metadata);
        assertEquals("cap-001", metadata.getCapId());
        assertEquals("scene-001", metadata.getSceneId());
    }
    
    @Test
    public void testGetMetadataNotExists() throws Exception {
        CapabilityMetadata metadata = invoker.getMetadata("scene-001", "cap-999").get(10, TimeUnit.SECONDS);
        
        assertNotNull(metadata);
        assertEquals("cap-999", metadata.getCapId());
    }
    
    @Test
    public void testInvokeWithFallback() throws Exception {
        invoker.registerHandler("scene-001", "fallback-cap", new Object());
        
        Map<String, Object> params = new HashMap<>();
        Object result = invoker.invokeWithFallback("scene-001", "non-existent", params, "fallback-cap")
            .get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
    }
    
    @Test
    public void testRegisterHandler() throws Exception {
        invoker.registerHandler("scene-001", "new-cap", new Object());
        
        Boolean available = invoker.isAvailable("scene-001", "new-cap").get(10, TimeUnit.SECONDS);
        assertTrue(available);
    }
    
    @Test
    public void testUnregisterHandler() throws Exception {
        invoker.registerHandler("scene-001", "temp-cap", new Object());
        
        Boolean available = invoker.isAvailable("scene-001", "temp-cap").get(10, TimeUnit.SECONDS);
        assertTrue(available);
        
        invoker.unregisterHandler("scene-001", "temp-cap");
        
        available = invoker.isAvailable("scene-001", "temp-cap").get(10, TimeUnit.SECONDS);
        assertFalse(available);
    }
    
    @Test
    public void testMetadataUpdate() throws Exception {
        invoker.registerHandler("scene-001", "cap-001", new Object());
        
        Map<String, Object> params = new HashMap<>();
        invoker.invoke("scene-001", "cap-001", params).get(10, TimeUnit.SECONDS);
        invoker.invoke("scene-001", "cap-001", params).get(10, TimeUnit.SECONDS);
        
        CapabilityMetadata metadata = invoker.getMetadata("scene-001", "cap-001").get(10, TimeUnit.SECONDS);
        assertEquals(2, metadata.getInvokeCount());
    }
}
