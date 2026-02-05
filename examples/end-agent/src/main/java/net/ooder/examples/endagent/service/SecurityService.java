package net.ooder.examples.endagent.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class SecurityService {
    // JWT密钥（实际应用中应该从配置文件或密钥管理服务获取）
    private static final String JWT_SECRET = "ooder-secret-key-12345678901234567890";
    // JWT过期时间（毫秒）
    private static final long JWT_EXPIRATION = 3600000; // 1小时

    // 验证证书SN
    public boolean validateCertificateSn(String certSn) {
        // 验证SN是否为16位十六进制字符串
        if (certSn == null) {
            return false;
        }
        return certSn.matches("^[0-9A-Fa-f]{16}$");
    }

    // 解析X.509证书（简化实现）
    public X509Certificate parseCertificate(String certificateStr) throws CertificateException {
        // 实际应用中应该使用证书解析库解析证书
        // 这里简化处理，仅验证证书格式
        if (certificateStr == null || !certificateStr.contains("-----BEGIN CERTIFICATE-----")) {
            throw new CertificateException("Invalid certificate format");
        }
        // 返回null表示解析成功（实际应用中应该返回证书对象）
        return null;
    }

    // 验证JWT令牌
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(JWT_SECRET.getBytes())
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 从JWT令牌中获取声明
    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser()
            .setSigningKey(JWT_SECRET.getBytes())
            .parseClaimsJws(token)
            .getBody();
    }

    // 生成JWT令牌
    public String generateJwtToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, JWT_SECRET.getBytes())
            .compact();
    }

    // 验证API Key
    public boolean validateApiKey(String apiKey) {
        // 实际应用中应该从数据库或配置文件中验证API Key
        // 这里简化处理，仅验证API Key格式
        if (apiKey == null) {
            return false;
        }
        return apiKey.matches("^[0-9A-Fa-f]{32}$");
    }

    // 验证OAuth 2.0令牌（简化实现）
    public boolean validateOAuthToken(String token) {
        // 实际应用中应该调用OAuth 2.0服务验证令牌
        // 这里简化处理，仅验证令牌格式
        if (token == null) {
            return false;
        }
        return token.matches("^[0-9A-Za-z_-]+\\.[0-9A-Za-z_-]+\\.[0-9A-Za-z_-]*$");
    }

    // 验证数据签名
    public boolean validateSignature(String data, String signature, String publicKey) {
        // 实际应用中应该使用公钥验证签名
        // 这里简化处理，仅验证参数是否为空
        if (data == null || signature == null || publicKey == null) {
            return false;
        }
        return true;
    }

    // 生成数据签名
    public String generateSignature(String data, String privateKey) {
        // 实际应用中应该使用私钥生成签名
        // 这里简化处理，返回数据的Base64编码
        return Base64.getEncoder().encodeToString(data.getBytes());
    }
}