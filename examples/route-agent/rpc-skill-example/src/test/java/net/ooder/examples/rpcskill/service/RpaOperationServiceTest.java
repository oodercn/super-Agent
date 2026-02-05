package net.ooder.examples.rpaskill.service;

import net.ooder.examples.rpaskill.config.RpaConfig;
import net.ooder.examples.rpaskill.model.RpaRequest;
import net.ooder.examples.rpaskill.model.RpaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RpaOperationService
 * 作者：ooderAI agent team   V0.6.0
 */
public class RpaOperationServiceTest {

    @Mock
    private RpaConfig rpaConfig;
    
    @Mock
    private SnAuthService snAuthService;

    private RpaOperationService rpaOperationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        rpaOperationService = new RpaOperationService(rpaConfig, snAuthService);

        // Set up default configuration
        when(rpaConfig.getToken()).thenReturn("default-token");
        when(rpaConfig.getUrl()).thenReturn("https://api.example.com/rpa");
        when(rpaConfig.getScript()).thenReturn("default-script");
        when(rpaConfig.getJsonF()).thenReturn("{\"result\":{\"type\":\"object\",\"properties\":{\"success\":{\"type\":\"boolean\"},\"data\":{\"type\":\"object\"},\"message\":{\"type\":\"string\"}}}}");
    }

    @Test
    public void testPerformRpaOperation_WithAllParams() {
        // Create request with all parameters
        RpaRequest request = new RpaRequest();
        request.setToken("test-token");
        request.setUrl("https://test-api.example.com/rpa");
        request.setScript("test-script");
        request.setJsonF("{\"result\":{\"type\":\"object\",\"properties\":{\"success\":{\"type\":\"boolean\"}}}}");

        // Execute RPA operation
        RpaResponse response = rpaOperationService.performRpaOperation(request);

        // Verify response
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertTrue(response.getData().containsKey("success"));
    }

    @Test
    public void testPerformRpaOperation_WithPartialParams() {
        // Create request with partial parameters (some null)
        RpaRequest request = new RpaRequest();
        request.setToken("test-token");
        request.setScript("test-script");
        // url and jsonF are null, should use defaults

        // Execute RPA operation
        RpaResponse response = rpaOperationService.performRpaOperation(request);

        // Verify response
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        // Default response structure should have success, data, message fields
        assertTrue(response.getData().containsKey("success"));
        assertTrue(response.getData().containsKey("data"));
        assertTrue(response.getData().containsKey("message"));
    }

    @Test
    public void testPerformRpaOperation_WithAllNullParams() {
        // Create request with all parameters null
        RpaRequest request = new RpaRequest();

        // Execute RPA operation
        RpaResponse response = rpaOperationService.performRpaOperation(request);

        // Verify response
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        // Default response structure should have success, data, message fields
        assertTrue(response.getData().containsKey("success"));
        assertTrue(response.getData().containsKey("data"));
        assertTrue(response.getData().containsKey("message"));
    }
}
