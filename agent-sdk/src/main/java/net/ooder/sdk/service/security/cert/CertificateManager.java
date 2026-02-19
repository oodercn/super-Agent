
package net.ooder.sdk.service.security.cert;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CertificateManager {
    
    private static final Logger log = LoggerFactory.getLogger(CertificateManager.class);
    
    private final Map<String, CertificateInfo> certificates;
    private final Map<String, KeyPair> keyPairs;
    private final SecureRandom secureRandom;
    
    private int keySize = 2048;
    private long defaultValidityDays = 365;
    
    public CertificateManager() {
        this.certificates = new ConcurrentHashMap<>();
        this.keyPairs = new ConcurrentHashMap<>();
        this.secureRandom = new SecureRandom();
    }
    
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(keySize, secureRandom);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to generate key pair", e);
            throw new RuntimeException("Key pair generation failed", e);
        }
    }
    
    public CertificateInfo createCertificate(String subject, String issuer, KeyPair keyPair) {
        CertificateInfo cert = new CertificateInfo();
        cert.setSubject(subject);
        cert.setIssuer(issuer);
        cert.setSerialNumber(generateSerialNumber());
        cert.setNotBefore(new Date());
        cert.setNotAfter(new Date(System.currentTimeMillis() + defaultValidityDays * 24 * 60 * 60 * 1000L));
        cert.setPublicKey(keyPair.getPublic());
        
        certificates.put(subject, cert);
        keyPairs.put(subject, keyPair);
        
        log.info("Created certificate for: {}", subject);
        return cert;
    }
    
    public CertificateInfo getCertificate(String subject) {
        return certificates.get(subject);
    }
    
    public KeyPair getKeyPair(String subject) {
        return keyPairs.get(subject);
    }
    
    public void revokeCertificate(String subject) {
        CertificateInfo cert = certificates.get(subject);
        if (cert != null) {
            cert.setRevoked(true);
            cert.setRevocationDate(new Date());
            log.info("Revoked certificate: {}", subject);
        }
    }
    
    public boolean validateCertificate(String subject) {
        CertificateInfo cert = certificates.get(subject);
        if (cert == null) {
            return false;
        }
        
        if (cert.isRevoked()) {
            return false;
        }
        
        Date now = new Date();
        if (now.before(cert.getNotBefore()) || now.after(cert.getNotAfter())) {
            return false;
        }
        
        return true;
    }
    
    public void renewCertificate(String subject) {
        CertificateInfo cert = certificates.get(subject);
        KeyPair keyPair = keyPairs.get(subject);
        
        if (cert != null && keyPair != null) {
            cert.setNotBefore(new Date());
            cert.setNotAfter(new Date(System.currentTimeMillis() + defaultValidityDays * 24 * 60 * 60 * 1000L));
            cert.setSerialNumber(generateSerialNumber());
            cert.setRevoked(false);
            
            log.info("Renewed certificate: {}", subject);
        }
    }
    
    private BigInteger generateSerialNumber() {
        return new BigInteger(64, secureRandom);
    }
    
    public void setKeySize(int keySize) { this.keySize = keySize; }
    public void setDefaultValidityDays(long days) { this.defaultValidityDays = days; }
    
    public static class CertificateInfo {
        private String subject;
        private String issuer;
        private BigInteger serialNumber;
        private Date notBefore;
        private Date notAfter;
        private PublicKey publicKey;
        private boolean revoked;
        private Date revocationDate;
        private Map<String, String> extensions;
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getIssuer() { return issuer; }
        public void setIssuer(String issuer) { this.issuer = issuer; }
        
        public BigInteger getSerialNumber() { return serialNumber; }
        public void setSerialNumber(BigInteger serialNumber) { this.serialNumber = serialNumber; }
        
        public Date getNotBefore() { return notBefore; }
        public void setNotBefore(Date notBefore) { this.notBefore = notBefore; }
        
        public Date getNotAfter() { return notAfter; }
        public void setNotAfter(Date notAfter) { this.notAfter = notAfter; }
        
        public PublicKey getPublicKey() { return publicKey; }
        public void setPublicKey(PublicKey publicKey) { this.publicKey = publicKey; }
        
        public boolean isRevoked() { return revoked; }
        public void setRevoked(boolean revoked) { this.revoked = revoked; }
        
        public Date getRevocationDate() { return revocationDate; }
        public void setRevocationDate(Date revocationDate) { this.revocationDate = revocationDate; }
        
        public Map<String, String> getExtensions() { 
            if (extensions == null) {
                extensions = new HashMap<>();
            }
            return extensions; 
        }
    }
}
