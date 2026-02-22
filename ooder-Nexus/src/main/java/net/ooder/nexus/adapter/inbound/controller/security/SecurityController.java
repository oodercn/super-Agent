package net.ooder.nexus.adapter.inbound.controller.security;

import net.ooder.config.ResultModel;
import net.ooder.nexus.service.NexusSecurityService;
import net.ooder.sdk.api.security.KeyPair;
import net.ooder.sdk.api.security.TokenInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/security")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SecurityController {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    private NexusSecurityService securityService;

    @PostMapping("/keypair/generate")
    @ResponseBody
    public ResultModel<Map<String, String>> generateKeyPair() {
        log.info("Generate key pair requested");
        ResultModel<Map<String, String>> result = new ResultModel<Map<String, String>>();

        try {
            KeyPair keyPair = securityService.generateKeyPair();
            Map<String, String> data = new HashMap<String, String>();
            data.put("publicKey", keyPair.getPublicKey());
            data.put("privateKey", keyPair.getPrivateKey());
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Key pair generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate key pair", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to generate key pair: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/encrypt")
    @ResponseBody
    public ResultModel<String> encrypt(@RequestBody Map<String, String> request) {
        log.info("Encrypt requested");
        ResultModel<String> result = new ResultModel<String>();

        try {
            String data = request.get("data");
            String publicKey = request.get("publicKey");
            String encrypted = securityService.encrypt(data, publicKey);
            result.setData(encrypted);
            result.setRequestStatus(200);
            result.setMessage("Data encrypted successfully");
        } catch (Exception e) {
            log.error("Failed to encrypt data", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to encrypt: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/decrypt")
    @ResponseBody
    public ResultModel<String> decrypt(@RequestBody Map<String, String> request) {
        log.info("Decrypt requested");
        ResultModel<String> result = new ResultModel<String>();

        try {
            String encryptedData = request.get("encryptedData");
            String privateKey = request.get("privateKey");
            String decrypted = securityService.decrypt(encryptedData, privateKey);
            result.setData(decrypted);
            result.setRequestStatus(200);
            result.setMessage("Data decrypted successfully");
        } catch (Exception e) {
            log.error("Failed to decrypt data", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to decrypt: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/sign")
    @ResponseBody
    public ResultModel<String> sign(@RequestBody Map<String, String> request) {
        log.info("Sign requested");
        ResultModel<String> result = new ResultModel<String>();

        try {
            String data = request.get("data");
            String privateKey = request.get("privateKey");
            String signature = securityService.sign(data, privateKey);
            result.setData(signature);
            result.setRequestStatus(200);
            result.setMessage("Data signed successfully");
        } catch (Exception e) {
            log.error("Failed to sign data", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to sign: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/verify")
    @ResponseBody
    public ResultModel<Boolean> verify(@RequestBody Map<String, String> request) {
        log.info("Verify requested");
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String data = request.get("data");
            String signature = request.get("signature");
            String publicKey = request.get("publicKey");
            boolean valid = securityService.verify(data, signature, publicKey);
            result.setData(valid);
            result.setRequestStatus(200);
            result.setMessage(valid ? "Signature is valid" : "Signature is invalid");
        } catch (Exception e) {
            log.error("Failed to verify signature", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to verify: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/token/generate")
    @ResponseBody
    public ResultModel<String> generateToken(@RequestBody Map<String, Object> request) {
        log.info("Generate token requested");
        ResultModel<String> result = new ResultModel<String>();

        try {
            String subject = (String) request.get("subject");
            long expireMs = request.get("expireMs") != null ? ((Number) request.get("expireMs")).longValue() : 3600000L;
            String token = securityService.generateToken(subject, expireMs);
            result.setData(token);
            result.setRequestStatus(200);
            result.setMessage("Token generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate token", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to generate token: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/token/validate")
    @ResponseBody
    public ResultModel<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        log.info("Validate token requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String token = request.get("token");
            TokenInfo tokenInfo = securityService.validateToken(token);
            Map<String, Object> data = new HashMap<String, Object>();
            if (tokenInfo != null) {
                data.put("valid", tokenInfo.isValid());
                data.put("subject", tokenInfo.getSubject());
                data.put("issuedAt", tokenInfo.getIssuedAt());
                data.put("expiresAt", tokenInfo.getExpiresAt());
            } else {
                data.put("valid", false);
            }
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Token validated");
        } catch (Exception e) {
            log.error("Failed to validate token", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to validate token: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/token/revoke")
    @ResponseBody
    public ResultModel<Boolean> revokeToken(@RequestBody Map<String, String> request) {
        log.info("Revoke token requested");
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String token = request.get("token");
            securityService.revokeToken(token);
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Token revoked successfully");
        } catch (Exception e) {
            log.error("Failed to revoke token", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to revoke token: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/scene/key/generate")
    @ResponseBody
    public ResultModel<Map<String, String>> generateSceneKey(@RequestBody Map<String, String> request) {
        log.info("Generate scene key requested: {}", request.get("sceneId"));
        ResultModel<Map<String, String>> result = new ResultModel<Map<String, String>>();

        try {
            String sceneId = request.get("sceneId");
            KeyPair keyPair = securityService.generateSceneKey(sceneId);
            Map<String, String> data = new HashMap<String, String>();
            data.put("publicKey", keyPair.getPublicKey());
            data.put("sceneId", sceneId);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Scene key generated successfully");
        } catch (Exception e) {
            log.error("Failed to generate scene key", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to generate scene key: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/scene/key/rotate")
    @ResponseBody
    public ResultModel<Boolean> rotateSceneKey(@RequestBody Map<String, String> request) {
        log.info("Rotate scene key requested: {}", request.get("sceneId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String sceneId = request.get("sceneId");
            securityService.rotateSceneKey(sceneId);
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Scene key rotated successfully");
        } catch (Exception e) {
            log.error("Failed to rotate scene key", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to rotate scene key: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/peer/encrypt")
    @ResponseBody
    public ResultModel<String> encryptForPeer(@RequestBody Map<String, String> request) {
        log.info("Encrypt for peer requested");
        ResultModel<String> result = new ResultModel<String>();

        try {
            String sceneId = request.get("sceneId");
            String peerId = request.get("peerId");
            String data = request.get("data");
            String encrypted = securityService.encryptForPeer(sceneId, peerId, data);
            result.setData(encrypted);
            result.setRequestStatus(200);
            result.setMessage("Data encrypted for peer successfully");
        } catch (Exception e) {
            log.error("Failed to encrypt for peer", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to encrypt for peer: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/peer/decrypt")
    @ResponseBody
    public ResultModel<String> decryptFromPeer(@RequestBody Map<String, String> request) {
        log.info("Decrypt from peer requested");
        ResultModel<String> result = new ResultModel<String>();

        try {
            String sceneId = request.get("sceneId");
            String peerId = request.get("peerId");
            String encryptedData = request.get("encryptedData");
            String decrypted = securityService.decryptFromPeer(sceneId, peerId, encryptedData);
            result.setData(decrypted);
            result.setRequestStatus(200);
            result.setMessage("Data decrypted from peer successfully");
        } catch (Exception e) {
            log.error("Failed to decrypt from peer", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to decrypt from peer: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/health")
    @ResponseBody
    public ResultModel<Map<String, Object>> health() {
        log.info("Security health check requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            boolean healthy = securityService.isHealthy();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("healthy", healthy);
            result.setData(data);
            result.setRequestStatus(healthy ? 200 : 503);
            result.setMessage(healthy ? "Security service is healthy" : "Security service is unhealthy");
        } catch (Exception e) {
            log.error("Health check error", e);
            result.setRequestStatus(500);
            result.setMessage("Health check failed: " + e.getMessage());
        }

        return result;
    }
}
