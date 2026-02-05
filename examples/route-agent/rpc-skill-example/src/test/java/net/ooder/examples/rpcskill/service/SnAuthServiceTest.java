package net.ooder.examples.rpaskill.service;

import net.ooder.examples.rpaskill.config.AuthConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for SnAuthService
 */
public class SnAuthServiceTest {

    @Mock
    private AuthConfig authConfig;

    private SnAuthService snAuthService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        snAuthService = new SnAuthService(authConfig);

        // Set up authorized SNs
        List<String> authorizedSNs = Arrays.asList(
                "SN1234567890ABCDEF",
                "SN0987654321ABCDEF"
        );

        // Set up auth config
        when(authConfig.getAuthorizedSns()).thenReturn(authorizedSNs);
        when(authConfig.isValidationEnabled()).thenReturn(true);
    }

    @Test
    public void testValidateSn_AuthorizedSn() {
        // Test with an authorized SN
        String authorizedSn = "SN1234567890ABCDEF";
        boolean result = snAuthService.validateSn(authorizedSn);
        assertTrue(result);
    }

    @Test
    public void testValidateSn_UnauthorizedSn() {
        // Test with an unauthorized SN
        String unauthorizedSn = "SN1111111111111111";
        boolean result = snAuthService.validateSn(unauthorizedSn);
        assertFalse(result);
    }

    @Test
    public void testValidateSn_NullSn() {
        // Test with null SN
        boolean result = snAuthService.validateSn(null);
        assertFalse(result);
    }

    @Test
    public void testValidateSn_EmptySn() {
        // Test with empty SN
        boolean result = snAuthService.validateSn("");
        assertFalse(result);
    }

    @Test
    public void testValidateSn_ValidationDisabled() {
        // Test with validation disabled
        when(authConfig.isValidationEnabled()).thenReturn(false);
        
        String anySn = "SN9999999999999999";
        boolean result = snAuthService.validateSn(anySn);
        
        // Should return true when validation is disabled
        assertTrue(result);
    }

    @Test
    public void testValidateSnWithException_AuthorizedSn() throws SnAuthService.UnauthorizedException {
        // Test with an authorized SN - should not throw exception
        String authorizedSn = "SN1234567890ABCDEF";
        snAuthService.validateSnWithException(authorizedSn);
        // If we get here, no exception was thrown, test passed
    }

    @Test
    public void testValidateSnWithException_UnauthorizedSn() {
        // Test with an unauthorized SN - should throw exception
        String unauthorizedSn = "SN1111111111111111";
        
        assertThrows(SnAuthService.UnauthorizedException.class, () -> {
            snAuthService.validateSnWithException(unauthorizedSn);
        });
    }
}
