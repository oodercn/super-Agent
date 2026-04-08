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
 * 微信扫码登录服务实现
 */
@Service
public class WeixinLoginServiceImpl implements QrcodeLoginService {
    
    private static final Logger log = LoggerFactory.getLogger(WeixinLoginServiceImpl.class);
    
    @Autowired
    private QrcodeLoginConfig qrcodeLoginConfig;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, QrcodeStatusDTO> qrcodeCache = new ConcurrentHashMap<>();
    
    @Override
    public QrcodeDTO getQrcode(String platform) {
        log.info("[WeixinLoginService] Getting qrcode for platform: {}", platform);
        
        QrcodeLoginConfig.PlatformConfig config = qrcodeLoginConfig.getPlatforms().get("weixin");
        if (config == null || !config.getEnabled()) {
            log.warn("Weixin login is not configured or disabled");
            return null;
        }
        
        String qrcodeId = UUID.randomUUID().toString();
        
        QrcodeDTO qrcode = new QrcodeDTO();
        qrcode.setQrcodeId(qrcodeId);
        qrcode.setPlatform("weixin");
        qrcode.setExpiresIn(qrcodeLoginConfig.getQrcodeExpireTime());
        
        try {
            String appId = config.getAppId();
            String redirectUri = URLEncoder.encode(config.getCallbackUrl(), StandardCharsets.UTF_8);
            
            String authUrl = String.format(
                "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect",
                appId,
                redirectUri,
                qrcodeId
            );
            
            qrcode.setQrcodeUrl(authUrl);
            
            QrcodeStatusDTO status = new QrcodeStatusDTO();
            status.setQrcodeId(qrcodeId);
            status.setStatus("waiting");
            status.setPlatform("weixin");
            qrcodeCache.put(qrcodeId, status);
            
            log.info("Generated Weixin qrcode: {}", qrcodeId);
            
        } catch (Exception e) {
            log.error("Failed to generate Weixin qrcode", e);
            return null;
        }
        
        return qrcode;
    }
    
    @Override
    public QrcodeStatusDTO checkQrcodeStatus(String qrcodeId) {
        log.debug("[WeixinLoginService] Checking qrcode status: {}", qrcodeId);
        
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
        log.info("[WeixinLoginService] Handling callback for platform: {}, code: {}", platform, code);
        
        QrcodeLoginConfig.PlatformConfig config = qrcodeLoginConfig.getPlatforms().get("weixin");
        if (config == null) {
            log.error("Weixin login config not found");
            return null;
        }
        
        try {
            String tokenUrl = String.format(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                config.getAppId(),
                config.getAppSecret(),
                code
            );
            
            ResponseEntity<String> response = restTemplate.getForEntity(tokenUrl, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject tokenInfo = JSON.parseObject(response.getBody());
                String accessToken = tokenInfo.getString("access_token");
                String openId = tokenInfo.getString("openid");
                String unionId = tokenInfo.getString("unionid");
                
                String userInfoUrl = String.format(
                    "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                    accessToken,
                    openId
                );
                
                ResponseEntity<String> userResponse = restTemplate.getForEntity(userInfoUrl, String.class);
                
                if (userResponse.getStatusCode() == HttpStatus.OK) {
                    JSONObject userInfo = JSON.parseObject(userResponse.getBody());
                    String nickname = userInfo.getString("nickname");
                    String headImgUrl = userInfo.getString("headimgurl");
                    
                    QrcodeStatusDTO status = new QrcodeStatusDTO();
                    status.setStatus("confirmed");
                    status.setToken("weixin-token-" + System.currentTimeMillis());
                    status.setUserId(unionId != null ? unionId : openId);
                    status.setUsername(nickname);
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
            log.error("Failed to handle Weixin callback", e);
        }
        
        return null;
    }
}
