package net.ooder.sdk.core.collaboration;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoreMessageTest {
    
    @Test
    public void testMessageBuilder() {
        CoreMessage message = MessageBuilder.create()
            .source("agent-001")
            .target("agent-002")
            .type(MessageType.COMMAND)
            .payload("action", "execute")
            .payload("param", "value")
            .priority(8)
            .ttl(60000)
            .build();
        
        assertNotNull(message.getMessageId());
        assertEquals("agent-001", message.getSource());
        assertEquals("agent-002", message.getTarget());
        assertEquals(MessageType.COMMAND, message.getType());
        assertEquals(2, message.getPayload().size());
        assertEquals("execute", message.getPayload().get("action"));
        assertEquals(8, message.getPriority());
        assertEquals(60000, message.getTtl());
    }
    
    @Test
    public void testMessageWithCorrelationId() {
        CoreMessage message = MessageBuilder.create()
            .source("agent-001")
            .target("agent-002")
            .type(MessageType.RESPONSE)
            .correlationId("corr-123")
            .build();
        
        assertEquals("corr-123", message.getCorrelationId());
    }
    
    @Test
    public void testMessageWithHeaders() {
        CoreMessage message = MessageBuilder.create()
            .source("agent-001")
            .target("agent-002")
            .type(MessageType.EVENT)
            .header("Content-Type", "application/json")
            .header("X-Custom", "custom-value")
            .build();
        
        assertEquals(2, message.getHeaders().size());
        assertEquals("application/json", message.getHeaders().get("Content-Type"));
        assertEquals("custom-value", message.getHeaders().get("X-Custom"));
    }
    
    @Test
    public void testMessageTimestamp() {
        long before = System.currentTimeMillis();
        CoreMessage message = MessageBuilder.create()
            .source("agent-001")
            .target("agent-002")
            .type(MessageType.NOTIFICATION)
            .build();
        long after = System.currentTimeMillis();
        
        assertTrue(message.getTimestamp() >= before);
        assertTrue(message.getTimestamp() <= after);
    }
}
