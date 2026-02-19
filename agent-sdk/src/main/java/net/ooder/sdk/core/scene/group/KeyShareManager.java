
package net.ooder.sdk.core.scene.group;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ooder.sdk.api.scene.SceneGroupKey;
import net.ooder.sdk.api.scene.SceneGroupKey.KeyShare;
import net.ooder.sdk.api.scene.SceneMember;
import net.ooder.sdk.common.constants.SDKConstants;

public class KeyShareManager {
    
    private static final Logger log = LoggerFactory.getLogger(KeyShareManager.class);
    
    private final SecureRandom random = new SecureRandom();
    
    private int threshold = SDKConstants.SHAMIR_THRESHOLD;
    private int totalShares = SDKConstants.SHAMIR_MIN_SHARES;
    
    public SceneGroupKey generateKey(String sceneGroupId) {
        SceneGroupKey key = new SceneGroupKey();
        key.setSceneGroupId(sceneGroupId);
        key.setKeyValue(generateRandomKey());
        key.setThreshold(threshold);
        key.setTotalShares(totalShares);
        key.setCreateTime(System.currentTimeMillis());
        key.setExpireTime(System.currentTimeMillis() + 86400000);
        
        log.info("Generated key for scene group: {}", sceneGroupId);
        
        return key;
    }
    
    public SceneGroupKey reconstructKey(String sceneGroupId, List<KeyShare> shares) {
        if (shares == null || shares.size() < threshold) {
            log.error("Not enough shares to reconstruct key: need {}, got {}", 
                threshold, shares != null ? shares.size() : 0);
            return null;
        }
        
        SceneGroupKey key = new SceneGroupKey();
        key.setSceneGroupId(sceneGroupId);
        key.setKeyValue(reconstructFromShares(shares));
        key.setThreshold(threshold);
        key.setTotalShares(totalShares);
        key.setCreateTime(System.currentTimeMillis());
        
        log.info("Reconstructed key for scene group: {}", sceneGroupId);
        
        return key;
    }
    
    public void distributeShares(String sceneGroupId, SceneGroupKey key, List<SceneMember> members) {
        if (key == null || members == null || members.isEmpty()) {
            return;
        }
        
        List<KeyShare> shares = generateShares(key.getKeyValue(), members.size());
        key.setShares(shares);
        
        for (int i = 0; i < members.size() && i < shares.size(); i++) {
            shares.get(i).setAgentId(members.get(i).getAgentId());
        }
        
        log.info("Distributed {} shares for scene group: {}", shares.size(), sceneGroupId);
    }
    
    private String generateRandomKey() {
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        
        StringBuilder sb = new StringBuilder();
        for (byte b : keyBytes) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }
    
    private List<KeyShare> generateShares(String keyValue, int count) {
        List<KeyShare> shares = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            KeyShare share = new KeyShare();
            share.setShareIndex(i + 1);
            share.setShareData(generateShareData(keyValue, i + 1));
            shares.add(share);
        }
        
        return shares;
    }
    
    private String generateShareData(String keyValue, int index) {
        byte[] shareBytes = new byte[16];
        random.nextBytes(shareBytes);
        
        byte[] keyBytes = hexToBytes(keyValue);
        for (int i = 0; i < 16 && i < keyBytes.length; i++) {
            shareBytes[i] ^= keyBytes[i % keyBytes.length];
            shareBytes[i] ^= (byte) index;
        }
        
        StringBuilder sb = new StringBuilder();
        for (byte b : shareBytes) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }
    
    private String reconstructFromShares(List<KeyShare> shares) {
        if (shares.isEmpty()) {
            return null;
        }
        
        byte[] result = new byte[16];
        for (KeyShare share : shares) {
            byte[] shareBytes = hexToBytes(share.getShareData());
            for (int i = 0; i < 16 && i < shareBytes.length; i++) {
                result[i] ^= shareBytes[i];
                result[i] ^= (byte) share.getShareIndex();
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for (byte b : result) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }
    
    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
    
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
    
    public void setTotalShares(int totalShares) {
        this.totalShares = totalShares;
    }
}
