package net.ooder.nexus.infrastructure.openwrt.bridge.exception;

/**
 * SSH 连接超时异常
 */
public class ConnectionTimeoutException extends RuntimeException {

    public ConnectionTimeoutException() {
        super();
    }

    public ConnectionTimeoutException(String message) {
        super(message);
    }

    public ConnectionTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionTimeoutException(Throwable cause) {
        super(cause);
    }
}
