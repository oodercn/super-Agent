package net.ooder.nexus.core.protocol.model;

import java.io.Serializable;

/**
 * Command Digital Signature
 */
public class CommandSignature implements Serializable {
    private static final long serialVersionUID = 1L;

    private String algorithm;
    private String value;
    private String certificateFingerprint;

    public CommandSignature() {
    }

    public CommandSignature(String algorithm, String value) {
        this.algorithm = algorithm;
        this.value = value;
    }

    public String getAlgorithm() { return algorithm; }
    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
    public String getCertificateFingerprint() { return certificateFingerprint; }
    public void setCertificateFingerprint(String certificateFingerprint) { this.certificateFingerprint = certificateFingerprint; }

    @Override
    public String toString() {
        return "CommandSignature{" +
                "algorithm='" + algorithm + '\'' +
                ", value='" + (value != null ? value.substring(0, Math.min(20, value.length())) + "..." : null) + '\'' +
                ", certificateFingerprint='" + certificateFingerprint + '\'' +
                '}';
    }
}
