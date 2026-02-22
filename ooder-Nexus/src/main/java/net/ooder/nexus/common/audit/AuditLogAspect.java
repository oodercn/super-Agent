package net.ooder.nexus.common.audit;

import net.ooder.nexus.domain.skill.model.SkillResourceLog;
import net.ooder.nexus.service.skill.SkillAuthService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class AuditLogAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditLogAspect.class);

    @Autowired
    private SkillAuthService skillAuthService;

    @Around("@annotation(net.ooder.nexus.common.audit.Auditable)")
    public Object auditLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable exception = null;
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Auditable auditable = method.getAnnotation(Auditable.class);
        
        String action = auditable.action();
        String resourceType = auditable.resourceType();
        String description = auditable.description();
        
        Map<String, Object> params = new HashMap<>();
        if (auditable.logParams()) {
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            if (paramNames != null && args != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    if (args[i] != null && !(args[i] instanceof HttpServletRequest)) {
                        params.put(paramNames[i], args[i]);
                    }
                }
            }
        }
        
        String skillId = extractParam(params, "skillId");
        String skillName = extractParam(params, "skillName");
        String userId = extractParam(params, "userId");
        String resourceId = extractParam(params, "resourceId");
        String resourceName = extractParam(params, "resourceName");
        String sceneId = extractParam(params, "sceneId");
        String sceneName = extractParam(params, "sceneName");
        String groupId = extractParam(params, "groupId");
        
        String ipAddress = getClientIp();
        
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            exception = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = exception == null ? SkillResourceLog.STATUS_SUCCESS : SkillResourceLog.STATUS_FAILED;
            
            String detail = description;
            if (exception != null) {
                detail = description + " - 错误: " + exception.getMessage();
            }
            
            if (skillId != null || userId != null) {
                try {
                    skillAuthService.logResourceAccess(
                            skillId != null ? skillId : "system",
                            skillName != null ? skillName : "系统",
                            userId != null ? userId : "anonymous",
                            action.isEmpty() ? "execute" : action,
                            resourceType.isEmpty() ? "api" : resourceType,
                            resourceId != null ? resourceId : "unknown",
                            resourceName != null ? resourceName : "未知资源",
                            sceneId,
                            sceneName,
                            groupId,
                            detail,
                            status
                    );
                } catch (Exception e) {
                    log.error("Failed to log audit entry", e);
                }
            }
            
            log.info("Audit: action={}, resource={}, userId={}, status={}, duration={}ms",
                    action, resourceType, userId, status == 1 ? "success" : "failed", duration);
        }
    }

    private String extractParam(Map<String, Object> params, String key) {
        Object value = params.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    @SuppressWarnings("unchecked")
    private String extractParam(Map<String, Object> params, String key, String mapKey) {
        Object value = params.get(key);
        if (value instanceof Map) {
            Object mapValue = ((Map<String, Object>) value).get(mapKey);
            return mapValue != null ? String.valueOf(mapValue) : null;
        }
        return null;
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ip = request.getHeader("X-Forwarded-For");
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getHeader("X-Real-IP");
                }
                if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                return ip;
            }
        } catch (Exception e) {
            log.debug("Failed to get client IP", e);
        }
        return "unknown";
    }
}
