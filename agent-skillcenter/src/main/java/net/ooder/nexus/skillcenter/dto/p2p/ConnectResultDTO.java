package net.ooder.nexus.skillcenter.dto.p2p;

import net.ooder.nexus.skillcenter.dto.BaseDTO;

public class ConnectResultDTO extends BaseDTO {

    private boolean success;
    private String message;
    private String address;
    private int port;
    private Long timestamp;

    public ConnectResultDTO() {
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
