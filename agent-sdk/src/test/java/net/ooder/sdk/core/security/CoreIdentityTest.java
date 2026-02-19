package net.ooder.sdk.core.security;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreIdentityTest {
    
    @Test
    public void testIdentityCreation() {
        TestIdentity identity = new TestIdentity("user-001", "TestUser", IdentityType.USER);
        
        assertEquals("user-001", identity.getIdentityId());
        assertEquals("TestUser", identity.getName());
        assertEquals(IdentityType.USER, identity.getType());
    }
    
    @Test
    public void testIdentityAttributes() {
        TestIdentity identity = new TestIdentity("user-001", "TestUser", IdentityType.USER);
        
        identity.setAttribute("email", "test@example.com");
        identity.setAttribute("role", "admin");
        
        assertEquals("test@example.com", identity.getAttribute("email"));
        assertEquals("admin", identity.getAttribute("role"));
        assertTrue(identity.hasAttribute("email"));
        assertFalse(identity.hasAttribute("nonexistent"));
    }
    
    @Test
    public void testIdentityContext() {
        TestIdentity identity = new TestIdentity("user-001", "TestUser", IdentityType.USER);
        
        IdentityContext.setCurrentIdentity(identity);
        assertEquals(identity, IdentityContext.getCurrentIdentity());
        assertTrue(IdentityContext.hasIdentity());
        
        IdentityContext.clear();
        assertNull(IdentityContext.getCurrentIdentity());
        assertFalse(IdentityContext.hasIdentity());
    }
    
    static class TestIdentity implements CoreIdentity {
        
        private String identityId;
        private String name;
        private IdentityType type;
        private java.util.Map<String, Object> attributes = new java.util.HashMap<String, Object>();
        
        TestIdentity(String identityId, String name, IdentityType type) {
            this.identityId = identityId;
            this.name = name;
            this.type = type;
        }
        
        @Override
        public String getIdentityId() { return identityId; }
        
        @Override
        public String getName() { return name; }
        
        @Override
        public IdentityType getType() { return type; }
        
        @Override
        public java.util.Map<String, Object> getAttributes() { return attributes; }
        
        @Override
        public void setAttribute(String key, Object value) { attributes.put(key, value); }
        
        @Override
        public Object getAttribute(String key) { return attributes.get(key); }
        
        @Override
        public boolean hasAttribute(String key) { return attributes.containsKey(key); }
    }
}
