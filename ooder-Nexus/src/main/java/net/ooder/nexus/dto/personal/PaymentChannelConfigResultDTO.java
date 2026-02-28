package net.ooder.nexus.dto.personal;

import java.io.Serializable;

public class PaymentChannelConfigResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String channelId;
    private String status;
    private PaymentTestResultDTO testResult;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentTestResultDTO getTestResult() {
        return testResult;
    }

    public void setTestResult(PaymentTestResultDTO testResult) {
        this.testResult = testResult;
    }

    public static class PaymentTestResultDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Boolean success;
        private String message;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
