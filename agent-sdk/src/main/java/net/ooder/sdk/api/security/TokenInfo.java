package net.ooder.sdk.api.security;

/**
 * Token Information
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class TokenInfo {

    private String subject;
    private String issuer;
    private long issuedAt;
    private long expiresAt;
    private boolean valid;
    private String error;
    private java.util.Map<String, Object> claims;

    public TokenInfo() {
        this.claims = new java.util.HashMap<String, Object>();
    }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }

    public long getIssuedAt() { return issuedAt; }
    public void setIssuedAt(long issuedAt) { this.issuedAt = issuedAt; }

    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public java.util.Map<String, Object> getClaims() { return claims; }
    public void setClaims(java.util.Map<String, Object> claims) { this.claims = claims; }

    public boolean isExpired() {
        return expiresAt > 0 && System.currentTimeMillis() > expiresAt;
    }
}
