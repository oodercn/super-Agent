package net.ooder.sdk.network.packet;

import com.alibaba.fastjson.JSON;

public class AuthPacket extends UDPPacket {
    private String authType;
    private String authData;
    private String sessionKey;
    private boolean success;
    private String errorMessage;

    public AuthPacket() {
        super();
    }
    
    public static AuthPacket fromJson(String json) {
        return JSON.parseObject(json, AuthPacket.class);
    }
    
    @Override
    public String getType() {
        return "auth";
    }
    
    // Getter and Setter methods
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAuthData() {
        return authData;
    }

    public void setAuthData(String authData) {
        this.authData = authData;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    // Builder class
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AuthPacket packet = new AuthPacket();

        public Builder authType(String authType) {
            packet.setAuthType(authType);
            return this;
        }

        public Builder authData(String authData) {
            packet.setAuthData(authData);
            return this;
        }

        public Builder sessionKey(String sessionKey) {
            packet.setSessionKey(sessionKey);
            return this;
        }

        public Builder success(boolean success) {
            packet.setSuccess(success);
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            packet.setErrorMessage(errorMessage);
            return this;
        }

        public Builder senderId(String senderId) {
            packet.setSenderId(senderId);
            return this;
        }

        public Builder receiverId(String receiverId) {
            packet.setReceiverId(receiverId);
            return this;
        }

        public AuthPacket build() {
            return packet;
        }
    }
}