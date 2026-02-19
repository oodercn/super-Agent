
package net.ooder.sdk.infra.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);
    
    private final List<ErrorListener> listeners;
    private boolean logErrors = true;
    private boolean throwRuntimeErrors = false;
    
    public ErrorHandler() {
        this.listeners = new ArrayList<>();
    }
    
    public void handleError(String errorCode, String message) {
        handleError(errorCode, message, null);
    }
    
    public void handleError(String errorCode, String message, Throwable cause) {
        ErrorContext context = new ErrorContext();
        context.setErrorCode(errorCode);
        context.setMessage(message);
        context.setCause(cause);
        context.setTimestamp(System.currentTimeMillis());
        
        if (logErrors) {
            if (cause != null) {
                log.error("[{}] {} - Cause: {}", errorCode, message, cause.getMessage());
            } else {
                log.error("[{}] {}", errorCode, message);
            }
        }
        
        notifyListeners(context);
        
        if (throwRuntimeErrors) {
            throw new SDKException(errorCode, message, cause);
        }
    }
    
    public void handleWarning(String errorCode, String message) {
        log.warn("[{}] {}", errorCode, message);
        
        ErrorContext context = new ErrorContext();
        context.setErrorCode(errorCode);
        context.setMessage(message);
        context.setTimestamp(System.currentTimeMillis());
        context.setWarning(true);
        
        notifyListeners(context);
    }
    
    public SDKException createException(String errorCode, String message) {
        return new SDKException(errorCode, message);
    }
    
    public SDKException createException(String errorCode, String message, Throwable cause) {
        return new SDKException(errorCode, message, cause);
    }
    
    public void addListener(ErrorListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(ErrorListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(ErrorContext context) {
        for (ErrorListener listener : listeners) {
            try {
                listener.onError(context);
            } catch (Exception e) {
                log.warn("Error listener failed", e);
            }
        }
    }
    
    public void setLogErrors(boolean logErrors) {
        this.logErrors = logErrors;
    }
    
    public void setThrowRuntimeErrors(boolean throwRuntimeErrors) {
        this.throwRuntimeErrors = throwRuntimeErrors;
    }
    
    public static class ErrorContext {
        private String errorCode;
        private String message;
        private Throwable cause;
        private long timestamp;
        private boolean warning;
        private Object source;
        
        public String getErrorCode() { return errorCode; }
        public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public Throwable getCause() { return cause; }
        public void setCause(Throwable cause) { this.cause = cause; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public boolean isWarning() { return warning; }
        public void setWarning(boolean warning) { this.warning = warning; }
        
        public Object getSource() { return source; }
        public void setSource(Object source) { this.source = source; }
    }
    
    public interface ErrorListener {
        void onError(ErrorContext context);
    }
}
