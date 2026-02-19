
package net.ooder.sdk.service.security.auth;

public interface AuthProvider {
    
    AuthResult authenticate(String agentId, String credentials);
    
    boolean validateCredentials(String agentId, String credentials);
}
