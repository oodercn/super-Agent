package net.ooder.skill.auth.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import net.ooder.skill.auth.config.QrcodeLoginConfig;
import net.ooder.skill.auth.dto.QrcodeDTO;
import net.ooder.skill.auth.dto.QrcodeStatusDTO;
import net.ooder.skill.auth.dto.SessionDTO;
import net.ooder.skill.auth.service.QrcodeLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 飞书扫码登录服务实现
 */
@Service
public class FeishuLoginServiceImpl implements QrcodeLoginService {
    
    private static final Logger log = LoggerFactory.getLogger(FeishuLoginServiceImpl.class);
    
    @Autowired
    private QrcodeLoginConfig qrcodeLoginConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, QrcodeStatusDTO> qrcodeCache = new ConcurrentHashMap<>();
    
    @Override
    public QrcodeDTO getQrcode(String platform) {
        log.info("[FeishuLoginService] Getting qrcode for platform: {}", platform);
        
        QrcodeLoginConfig.PlatformConfig config = qrcodeLoginConfig.getPlatforms().get("feishu");
        if (config == null || !config.getEnabled()) {
            log.warn("Feishu login is not configured or disabled");
            return null;
        }
        
        String qrcodeId = UUID.randomUUID().toString();
        
        QrcodeDTO qrcode = new QrcodeDTO();
        qrcode.setQrcodeId(qrcodeId);
        qrcode.setPlatform("feishu");
        qrcode.setExpiresIn(qrcodeLoginConfig.getQrcodeExpireTime());
        
        try {
            String appId = config.getAppId();
            String redirectUri = URLEncoder.encode(config.getCallbackUrl(), StandardCharsets.UTF_8);
            
            String authUrl = String.format(
                "https://passport.feishu.cn/suite/passport/page/auth?app_id=%s&redirect_uri=%s&state=%s",
                appId,
                redirectUri,
                qrcodeId
            );
            
            qrcode.setQrcodeUrl(authUrl);
            
            QrcodeStatusDTO status = new QrcodeStatusDTO();
            status.setQrcodeId(qrcodeId);
            status.setStatus("waiting");
            status.setPlatform("feishu");
            qrcodeCache.put(qrcodeId, status);
            
            log.info("Generated Feishu qrcode: {}", qrcodeId);
            
        } catch (Exception e) {
            log.error("Failed to generate Feishu qrcode", e);
            return null;
        }
        
        return qrcode;
    }
    
    @Override
    public QrcodeStatusDTO checkQrcodeStatus(String qrcodeId) {
        log.debug("[FeishuLoginService] Checking qrcode status: {}", qrcodeId);
        
        QrcodeStatusDTO status = qrcodeCache.get(qrcodeId);
        if (status == null) {
            status = new QrcodeStatusDTO();
            status.setQrcodeId(qrcodeId);
            status.setStatus("expired");
            return status;
        }
        
        return status;
    }
    
    @Override
    public QrcodeStatusDTO handleCallback(String platform, String code) {
        log.info("[FeishuLoginService] Handling callback for platform: {}, code: {}", platform, code);
        
        QrcodeLoginConfig.PlatformConfig config = qrcodeLoginConfig.getPlatforms().get("feishu");
        if (config == null) {
            log.error("Feishu login config not found");
            return null;
        }
        
        try {
            String tokenUrl = "https://passport.feishu.cn/suite/passport/oauth/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            JSONObject requestBody = new JSONObject();
            requestBody.put("grant_type", "authorization_code");
            requestBody.put("client_id", config.getAppId());
            requestBody.put("client_secret", config.getAppSecret());
            requestBody.put("code", code);
            requestBody.put("redirect_uri", config.getCallbackUrl());
            
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toJSONString(), headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject tokenInfo = JSON.parseObject(response.getBody());
                String accessToken = tokenInfo.getString("access_token");
                String openId = tokenInfo.getString("open_id");
                String unionId = tokenInfo.getString("union_id");
                
                String userInfoUrl = "https://passport.feishu.cn/suite/passport/oauth/userinfo";
                HttpHeaders userHeaders = new HttpHeaders();
                userHeaders.set("Authorization", "Bearer " + accessToken);
                
                HttpEntity<String> userEntity = new HttpEntity<>(userHeaders);
                ResponseEntity<String> userResponse = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    userEntity,
                    String.class
                );
                
                if (userResponse.getStatusCode() == HttpStatus.OK) {
                    JSONObject userInfo = JSON.parseObject(userResponse.getBody());
                    String name = userInfo.getString("name");
                    String avatarUrl = userInfo.getString("avatar_url");
                    String email = userInfo.getString("email");
                    
                    QrcodeStatusDTO status = new QrcodeStatusDTO();
                    status.setStatus("confirmed");
                    status.setToken("feishu-token-" + System.currentTimeMillis());
                    status.setUserId(unionId != null ? unionId : openId);
                    status.setUsername(name);
                    status.setRole("admin");
                    
                    SessionDTO user = new SessionDTO();
                    user.setToken(status.getToken());
                    user.setUserId(status.getUserId());
                    user.setUsername(status.getUsername());
                    user.setRole(status.getRole());
                    status.setUser(user);
                    
                    return status;
                }
            }
        } catch (Exception e) {
            log.error("Failed to handle Feishu callback", e);
        }
        
        return null;
    }
}
