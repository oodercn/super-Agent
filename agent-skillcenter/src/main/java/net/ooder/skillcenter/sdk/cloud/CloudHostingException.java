package net.ooder.skillcenter.sdk.cloud;

public class CloudHostingException extends Exception {
    private String provider;
    private String errorCode;
    private int httpStatusCode;

    public CloudHostingException(String message) {
        super(message);
    }

    public CloudHostingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudHostingException(String provider, String errorCode, String message) {
        super(message);
        this.provider = provider;
        this.errorCode = errorCode;
    }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public int getHttpStatusCode() { return httpStatusCode; }
    public void setHttpStatusCode(int httpStatusCode) { this.httpStatusCode = httpStatusCode; }
}
