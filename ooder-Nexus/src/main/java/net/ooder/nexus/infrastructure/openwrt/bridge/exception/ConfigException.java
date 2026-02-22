package net.ooder.nexus.infrastructure.openwrt.bridge.exception;

/**
 * 配置异常
 */
public class ConfigException extends RuntimeException {

    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }
}
