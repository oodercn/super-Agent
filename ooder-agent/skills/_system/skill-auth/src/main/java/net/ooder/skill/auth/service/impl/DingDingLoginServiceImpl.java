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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 钉钉扫码登录服务实现
 */
@Service
public class DingDingLoginServiceImpl implements QrcodeLoginService {
    
    private static final Logger log = LoggerFactory.getLogger(DingDingLoginServiceImpl.class);
    
    @Autowired
    private QrcodeLoginConfig qrcodeLoginConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, QrcodeStatusDTO> qrcodeCache = new ConcurrentHashMap<>();
    
    @Override
    public QrcodeDTO getQrcode(String platform) {
        log.info("[DingDingLoginService] Getting qrcode for platform: {}", platform);
        
        QrcodeLoginConfig.PlatformConfig config = qrcodeLoginConfig.getPlatforms().get("dingding");
        if (config == null || !config.getEnabled()) {
            log.warn("DingDing login is not configured or disabled");
            return null;
        }
        
        String qrcodeId = UUID.randomUUID().toString();
        
        QrcodeDTO qrcode = new QrcodeDTO();
        qrcode.setQrcodeId(qrcodeId);
        qrcode.setPlatform("dingding");
        qrcode.setExpiresIn(qrcodeLoginConfig.getQrcodeExpireTime());
        
        try {
            String appId = config.getAppId();
            String redirectUri = config.getCallbackUrl();
            
            String authUrl = String.format(
                "https://login.dingtalk.com/oauth2/auth?redirect_uri=%s&client_id=%s&response_type=code&scope=openid&state=%s&prompt=consent",
                redirectUri,
                appId,
                qrcodeId
            );
            
            qrcode.setQrcodeUrl(authUrl);
            
            QrcodeStatusDTO status = new QrcodeStatusDTO();
            status.setQrcodeId(qrcodeId);
            status.setStatus("waiting");
            status.setPlatform("dingding");
            qrcodeCache.put(qrcodeId, status);
            
            log.info("Generated DingDing qrcode: {}", qrcodeId);
            
        } catch (Exception e) {
            log.error("Failed to generate DingDing qrcode", e);
            return null;
        }
        
        return qrcode;
    }
    
    @Override
    public QrcodeStatusDTO checkQrcodeStatus(String qrcodeId) {
        log.debug("[DingDingLoginService] Checking qrcode status: {}", qrcodeId);
        
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
        log.info("[DingDingLoginService] Handling callback for platform: {}, code: {}", platform, code);
        
        QrcodeLoginConfig.PlatformConfig config = qrcodeLoginConfig.getPlatforms().get("dingding");
        if (config == null) {
            log.error("DingDing login config not found");
            return null;
        }
        
        try {
            String tokenUrl = "https://api.dingtalk.com/v1.0/oauth2/userAccessToken";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            JSONObject requestBody = new JSONObject();
            requestBody.put("clientId", config.getAppId());
            requestBody.put("clientSecret", config.getAppSecret());
            requestBody.put("code", code);
            requestBody.put("grantType", "authorization_code");
            
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toJSONString(), headers);
            
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject tokenInfo = JSON.parseObject(response.getBody());
                String accessToken = tokenInfo.getString("accessToken");
                String unionId = tokenInfo.getString("unionId");
                String openId = tokenInfo.getString("openId");
                
                String userInfoUrl = "https://api.dingtalk.com/v1.0/contact/users/me";
                HttpHeaders userHeaders = new HttpHeaders();
                userHeaders.set("x-acs-dingtalk-access-token", accessToken);
                
                HttpEntity<String> userEntity = new HttpEntity<>(userHeaders);
                ResponseEntity<String> userResponse = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    userEntity,
                    String.class
                );
                
                if (userResponse.getStatusCode() == HttpStatus.OK) {
                    JSONObject userInfo = JSON.parseObject(userResponse.getBody());
                    String nickName = userInfo.getString("nickName");
                    String avatarUrl = userInfo.getString("avatarUrl");
                    
                    QrcodeStatusDTO status = new QrcodeStatusDTO();
                    status.setStatus("confirmed");
                    status.setToken("dingding-token-" + System.currentTimeMillis());
                    status.setUserId(unionId);
                    status.setUsername(nickName);
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
            log.error("Failed to handle DingDing callback", e);
        }
        
        return null;
    }
}
