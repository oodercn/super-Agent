
package net.ooder.sdk.service.security.cert;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SceneCA {
    
    private static final Logger log = LoggerFactory.getLogger(SceneCA.class);
    
    private final String caId;
    private final CertificateManager certificateManager;
    private final Map<String, SceneCertificate> sceneCertificates;
    private final Map<String, String> authorizedScenes;
    
    private KeyPair caKeyPair;
    private String caSubject;
    private volatile boolean initialized;
    
    public SceneCA(String caId) {
        this.caId = caId;
        this.certificateManager = new CertificateManager();
        this.sceneCertificates = new ConcurrentHashMap<>();
        this.authorizedScenes = new ConcurrentHashMap<>();
        this.initialized = false;
    }
    
    public void initialize() {
        this.caKeyPair = certificateManager.generateKeyPair();
        this.caSubject = "CN=SceneCA-" + caId + ",O=Ooder,C=CN";
        
        CertificateManager.CertificateInfo caCert = certificateManager.createCertificate(
            caSubject, caSubject, caKeyPair);
        
        initialized = true;
        log.info("Scene CA initialized: {}", caId);
    }
    
    public SceneCertificate issueSceneCertificate(String sceneId, String agentId) {
        if (!initialized) {
            throw new IllegalStateException("Scene CA not initialized");
        }
        
        String subject = "CN=" + agentId + ",OU=" + sceneId + ",O=Ooder,C=CN";
        KeyPair agentKeyPair = certificateManager.generateKeyPair();
        
        CertificateManager.CertificateInfo cert = certificateManager.createCertificate(
            subject, caSubject, agentKeyPair);
        
        SceneCertificate sceneCert = new SceneCertificate();
        sceneCert.setSceneId(sceneId);
        sceneCert.setAgentId(agentId);
        sceneCert.setCertificateInfo(cert);
        sceneCert.setPrivateKey(agentKeyPair.getPrivate());
        sceneCert.setIssueTime(System.currentTimeMillis());
        
        sceneCertificates.put(agentId, sceneCert);
        
        log.info("Issued scene certificate for agent: {} in scene: {}", agentId, sceneId);
        return sceneCert;
    }
    
    public boolean verifySceneCertificate(String agentId, String sceneId) {
        SceneCertificate sceneCert = sceneCertificates.get(agentId);
        if (sceneCert == null) {
            return false;
        }
        
        if (!sceneCert.getSceneId().equals(sceneId)) {
            return false;
        }
        
        return certificateManager.validateCertificate(
            sceneCert.getCertificateInfo().getSubject());
    }
    
    public void revokeSceneCertificate(String agentId) {
        SceneCertificate sceneCert = sceneCertificates.get(agentId);
        if (sceneCert != null) {
            certificateManager.revokeCertificate(
                sceneCert.getCertificateInfo().getSubject());
            sceneCertificates.remove(agentId);
            log.info("Revoked scene certificate for agent: {}", agentId);
        }
    }
    
    public void authorizeScene(String sceneId, String sceneKey) {
        authorizedScenes.put(sceneId, sceneKey);
        log.info("Authorized scene: {}", sceneId);
    }
    
    public void deauthorizeScene(String sceneId) {
        authorizedScenes.remove(sceneId);
        log.info("Deauthorized scene: {}", sceneId);
    }
    
    public boolean isSceneAuthorized(String sceneId) {
        return authorizedScenes.containsKey(sceneId);
    }
    
    public SceneCertificate getSceneCertificate(String agentId) {
        return sceneCertificates.get(agentId);
    }
    
    public String getCaId() { return caId; }
    public String getCaSubject() { return caSubject; }
    public boolean isInitialized() { return initialized; }
    
    public static class SceneCertificate {
        private String sceneId;
        private String agentId;
        private CertificateManager.CertificateInfo certificateInfo;
        private Object privateKey;
        private long issueTime;
        private Map<String, Object> attributes;
        
        public String getSceneId() { return sceneId; }
        public void setSceneId(String sceneId) { this.sceneId = sceneId; }
        
        public String getAgentId() { return agentId; }
        public void setAgentId(String agentId) { this.agentId = agentId; }
        
        public CertificateManager.CertificateInfo getCertificateInfo() { return certificateInfo; }
        public void setCertificateInfo(CertificateManager.CertificateInfo certificateInfo) { 
            this.certificateInfo = certificateInfo; 
        }
        
        public Object getPrivateKey() { return privateKey; }
        public void setPrivateKey(Object privateKey) { this.privateKey = privateKey; }
        
        public long getIssueTime() { return issueTime; }
        public void setIssueTime(long issueTime) { this.issueTime = issueTime; }
        
        public Map<String, Object> getAttributes() { 
            if (attributes == null) {
                attributes = new HashMap<>();
            }
            return attributes; 
        }
    }
}
