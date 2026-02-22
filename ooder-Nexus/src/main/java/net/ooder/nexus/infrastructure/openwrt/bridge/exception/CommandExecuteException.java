package net.ooder.nexus.infrastructure.openwrt.bridge.exception;

/**
 * 命令执行异常
 */
public class CommandExecuteException extends RuntimeException {

    private int exitCode;

    public CommandExecuteException() {
        super();
    }

    public CommandExecuteException(String message) {
        super(message);
    }

    public CommandExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExecuteException(Throwable cause) {
        super(cause);
    }

    public CommandExecuteException(String message, int exitCode) {
        super(message);
        this.exitCode = exitCode;
    }

    public CommandExecuteException(String message, int exitCode, Throwable cause) {
        super(message, cause);
        this.exitCode = exitCode;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
}
