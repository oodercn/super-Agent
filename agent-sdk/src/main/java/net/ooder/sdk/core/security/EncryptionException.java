package net.ooder.sdk.core.security;

public class EncryptionException extends Exception {
    
    private String algorithm;
    private String operation;
    
    public EncryptionException(String algorithm, String operation, String message) {
        super(message);
        this.algorithm = algorithm;
        this.operation = operation;
    }
    
    public EncryptionException(String algorithm, String operation, Throwable cause) {
        super(cause);
        this.algorithm = algorithm;
        this.operation = operation;
    }
    
    public String getAlgorithm() { return algorithm; }
    public String getOperation() { return operation; }
}
