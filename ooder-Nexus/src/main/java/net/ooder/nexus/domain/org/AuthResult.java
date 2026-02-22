package net.ooder.nexus.domain.org;

import java.io.Serializable;
import java.util.List;

/**
 * 认证结果
 */
public class AuthResult implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    private OrgUser user;
    private String message;

    public AuthResult() {}

    public AuthResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static AuthResult success(OrgUser user, String accessToken) {
        AuthResult result = new AuthResult(true, "认证成功");
        result.setUser(user);
        result.setAccessToken(accessToken);
        result.setExpiresIn(7200);
        return result;
    }

    public static AuthResult fail(String message) {
        return new AuthResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public OrgUser getUser() {
        return user;
    }

    public void setUser(OrgUser user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
