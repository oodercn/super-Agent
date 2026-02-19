package net.ooder.sdk.capability.impl;

import net.ooder.sdk.capability.*;
import net.ooder.sdk.capability.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.Arrays;

public class CapabilityCenterImplTest {
    
    private CapabilityCenterImpl capabilityCenter;
    
    @Before
    public void setUp() {
        capabilityCenter = new CapabilityCenterImpl();
        capabilityCenter.initialize();
    }
    
    @After
    public void tearDown() {
        capabilityCenter.shutdown();
    }
    
    @Test
    public void testRegisterSpec() throws Exception {
        SpecDefinition definition = new SpecDefinition();
        definition.setName("Test Capability");
        definition.setType(CapabilityType.SKILL);
        definition.setVersion("1.0.0");
        
        CapabilitySpec spec = capabilityCenter.getSpecService().registerSpec(definition).get(10, TimeUnit.SECONDS);
        
        assertNotNull(spec);
        assertNotNull(spec.getSpecId());
        assertEquals("Test Capability", spec.getSpecName());
        assertEquals(CapabilityType.SKILL, spec.getType());
        assertEquals(SpecStatus.VALIDATED, spec.getStatus());
    }
    
    @Test
    public void testGetSpec() throws Exception {
        SpecDefinition definition = new SpecDefinition();
        definition.setName("Test Capability");
        definition.setType(CapabilityType.SKILL);
        definition.setVersion("1.0.0");
        
        CapabilitySpec created = capabilityCenter.getSpecService().registerSpec(definition).get(10, TimeUnit.SECONDS);
        CapabilitySpec retrieved = capabilityCenter.getSpecService().getSpec(created.getSpecId()).get(10, TimeUnit.SECONDS);
        
        assertNotNull(retrieved);
        assertEquals(created.getSpecId(), retrieved.getSpecId());
    }
    
    @Test
    public void testValidateSpec() throws Exception {
        SpecDefinition definition = new SpecDefinition();
        definition.setName("Test Capability");
        definition.setType(CapabilityType.SKILL);
        definition.setVersion("1.0.0");
        
        ValidationResult result = capabilityCenter.getSpecService().validateSpec(definition).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertTrue(result.isValid());
    }
    
    @Test
    public void testDistribute() throws Exception {
        SpecDefinition definition = new SpecDefinition();
        definition.setName("Test Capability");
        definition.setType(CapabilityType.SKILL);
        definition.setVersion("1.0.0");
        
        CapabilitySpec spec = capabilityCenter.getSpecService().registerSpec(definition).get(10, TimeUnit.SECONDS);
        
        DistRequest request = new DistRequest();
        request.setSpecId(spec.getSpecId());
        request.setTargetNodes(Arrays.asList("node-1", "node-2"));
        request.setStrategy(DistStrategy.IMMEDIATE);
        
        DistResult result = capabilityCenter.getDistService().distribute(request).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(2, result.getTotalNodes());
    }
    
    @Test
    public void testRegisterCapability() throws Exception {
        CapabilityRegistration registration = new CapabilityRegistration();
        registration.setSpecId("spec-001");
        registration.setNodeId("node-001");
        
        CapabilityInfo info = capabilityCenter.getMgtService().register(registration).get(10, TimeUnit.SECONDS);
        
        assertNotNull(info);
        assertNotNull(info.getCapabilityId());
        assertEquals(CapabilityState.REGISTERED, info.getState());
    }
}
