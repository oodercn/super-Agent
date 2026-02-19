package net.ooder.sdk.core.security;

import java.util.concurrent.CompletableFuture;

public interface CoreEncryption {
    
    String getAlgorithm();
    
    byte[] encrypt(byte[] data, EncryptionContext context) throws EncryptionException;
    
    byte[] decrypt(byte[] encryptedData, EncryptionContext context) throws EncryptionException;
    
    String generateKey();
    
    boolean verifySignature(byte[] data, byte[] signature, EncryptionContext context);
    
    byte[] sign(byte[] data, EncryptionContext context) throws EncryptionException;
    
    CompletableFuture<byte[]> encryptAsync(byte[] data, EncryptionContext context);
    
    CompletableFuture<byte[]> decryptAsync(byte[] encryptedData, EncryptionContext context);
}
