package net.ooder.mvp.skill.scene.capability.controller;

import net.ooder.mvp.skill.scene.capability.service.KeyManagementService;
import net.ooder.mvp.skill.scene.capability.service.KeyManagementService.KeyInfo;
import net.ooder.mvp.skill.scene.capability.service.KeyManagementService.KeyGenerateRequest;
import net.ooder.mvp.skill.scene.capability.service.KeyManagementService.KeyAccessResult;
import net.ooder.mvp.skill.scene.dto.key.KeyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/keys")
@CrossOrigin(origins = "*")
public class KeyManagementController {

    private static final Logger log = LoggerFactory.getLogger(KeyManagementController.class);

    @Autowired
    private KeyManagementService keyManagementService;

    @GetMapping
    public ResponseEntity<List<KeyDTO>> listKeys(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String sceneGroupId) {
        
        log.info("[listKeys] type={}, status={}, userId={}, sceneGroupId={}", type, status, userId, sceneGroupId);
        
        List<KeyInfo> keys;
        if (userId != null) {
            keys = keyManagementService.getKeysByUser(userId);
        } else if (sceneGroupId != null) {
            keys = keyManagementService.getKeysByScene(sceneGroupId);
        } else {
            keys = keyManagementService.getAllKeys();
        }
        
        List<KeyDTO> result = new ArrayList<>();
        for (KeyInfo key : keys) {
            if (type != null && !type.isEmpty() && !type.equals(key.getKeyType()) && !type.equals(key.getScope())) {
                continue;
            }
            if (status != null && !status.isEmpty() && !status.equals(key.getStatus().name())) {
                continue;
            }
            result.add(KeyDTO.fromKeyInfo(key));
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{keyId}")
    public ResponseEntity<KeyDTO> getKey(@PathVariable String keyId) {
        log.info("[getKey] keyId={}", keyId);
        
        KeyInfo key = keyManagementService.getKey(keyId);
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(KeyDTO.fromKeyInfo(key));
    }

    @PostMapping
    public ResponseEntity<KeyDTO> createKey(@RequestBody Map<String, Object> request) {
        log.info("[createKey] request={}", request);
        
        KeyGenerateRequest generateRequest = new KeyGenerateRequest();
        generateRequest.setUserId("current-user");
        generateRequest.setSceneGroupId((String) request.get("sceneGroupId"));
        generateRequest.setInstallId((String) request.get("installId"));
        generateRequest.setScope((String) request.get("keyType"));
        generateRequest.setDescription((String) request.get("description"));
        generateRequest.setKeyName((String) request.get("keyName"));
        generateRequest.setKeyType((String) request.get("keyType"));
        generateRequest.setProvider((String) request.get("provider"));
        
        if (request.containsKey("allowedUsers")) {
            Object users = request.get("allowedUsers");
            if (users instanceof List) {
                generateRequest.setAllowedUsers((List<String>) users);
            }
        }
        
        if (request.containsKey("allowedRoles")) {
            Object roles = request.get("allowedRoles");
            if (roles instanceof List) {
                generateRequest.setAllowedRoles((List<String>) roles);
            }
        }
        
        if (request.containsKey("allowedScenes")) {
            Object scenes = request.get("allowedScenes");
            if (scenes instanceof List) {
                generateRequest.setAllowedScenes((List<String>) scenes);
            }
        }
        
        if (request.containsKey("expiresAt")) {
            Object expiresAt = request.get("expiresAt");
            if (expiresAt instanceof Number) {
                long expiresTime = ((Number) expiresAt).longValue() - System.currentTimeMillis();
                generateRequest.setExpireTimeMs(expiresTime);
            }
        }
        
        if (request.containsKey("maxUseCount")) {
            Map<String, Object> permissions = new HashMap<>();
            permissions.put("maxUseCount", request.get("maxUseCount"));
            generateRequest.setPermissions(permissions);
        }
        
        KeyInfo key = keyManagementService.generateKey(generateRequest);
        
        KeyDTO result = KeyDTO.fromKeyInfo(key);
        result.setKeyValue(key.getKeyValue());
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<KeyDTO> rotateKey(
            @PathVariable String keyId,
            @RequestBody(required = false) String newValue) {
        log.info("[rotateKey] keyId={}", keyId);
        
        KeyInfo key = keyManagementService.refreshKey(keyId);
        if (key == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(KeyDTO.fromKeyInfo(key));
    }

    @PostMapping("/{keyId}/revoke")
    public ResponseEntity<KeyDTO> revokeKey(@PathVariable String keyId) {
        log.info("[revokeKey] keyId={}", keyId);
        
        boolean success = keyManagementService.revokeKey(keyId);
        if (!success) {
            return ResponseEntity.notFound().build();
        }
        
        KeyInfo key = keyManagementService.getKey(keyId);
        return ResponseEntity.ok(KeyDTO.fromKeyInfo(key));
    }

    @PostMapping("/{keyId}/validate")
    public ResponseEntity<KeyValidateResultDTO> validateKey(
            @PathVariable String keyId,
            @RequestParam(required = false) String scope) {
        log.info("[validateKey] keyId={}, scope={}", keyId, scope);
        
        boolean valid = keyManagementService.validateKey(keyId, scope);
        
        KeyValidateResultDTO result = new KeyValidateResultDTO();
        result.setKeyId(keyId);
        result.setValid(valid);
        
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{keyId}/access")
    public ResponseEntity<KeyAccessResultDTO> accessResource(
            @PathVariable String keyId,
            @RequestBody Map<String, Object> request) {
        
        String resource = (String) request.get("resource");
        String action = (String) request.get("action");
        
        log.info("[accessResource] keyId={}, resource={}, action={}", keyId, resource, action);
        
        KeyAccessResult result = keyManagementService.accessResource(keyId, resource, action);
        
        KeyAccessResultDTO response = new KeyAccessResultDTO();
        response.setAllowed(result.isAllowed());
        response.setReason(result.getReason());
        
        if (result.getKeyInfo() != null) {
            response.setKeyInfo(KeyDTO.fromKeyInfo(result.getKeyInfo()));
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<KeyDTO>> getKeysByUser(@PathVariable String userId) {
        log.info("[getKeysByUser] userId={}", userId);
        
        List<KeyInfo> keys = keyManagementService.getKeysByUser(userId);
        
        List<KeyDTO> result = new ArrayList<>();
        for (KeyInfo key : keys) {
            result.add(KeyDTO.fromKeyInfo(key));
        }
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-scene/{sceneGroupId}")
    public ResponseEntity<List<KeyDTO>> getKeysByScene(@PathVariable String sceneGroupId) {
        log.info("[getKeysByScene] sceneGroupId={}", sceneGroupId);
        
        List<KeyInfo> keys = keyManagementService.getKeysByScene(sceneGroupId);
        
        List<KeyDTO> result = new ArrayList<>();
        for (KeyInfo key : keys) {
            result.add(KeyDTO.fromKeyInfo(key));
        }
        
        return ResponseEntity.ok(result);
    }
    
    public static class KeyValidateResultDTO {
        private String keyId;
        private boolean valid;
        
        public String getKeyId() { return keyId; }
        public void setKeyId(String keyId) { this.keyId = keyId; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
    }
    
    public static class KeyAccessResultDTO {
        private boolean allowed;
        private String reason;
        private KeyDTO keyInfo;
        
        public boolean isAllowed() { return allowed; }
        public void setAllowed(boolean allowed) { this.allowed = allowed; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
        public KeyDTO getKeyInfo() { return keyInfo; }
        public void setKeyInfo(KeyDTO keyInfo) { this.keyInfo = keyInfo; }
    }
}
