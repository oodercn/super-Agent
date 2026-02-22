package net.ooder.nexus.skillcenter.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础Controller类 - 符合 ooderNexus 规范
 * 包含所有Controller的通用逻辑
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 记录请求开始
     * @param methodName 方法名
     * @param params 参数
     */
    protected void logRequestStart(String methodName, Object params) {
        logger.info("[{}] 请求开始，参数: {}", methodName, params);
    }

    /**
     * 记录请求结束
     * @param methodName 方法名
     * @param result 结果
     * @param elapsedTime 耗时(毫秒)
     */
    protected void logRequestEnd(String methodName, Object result, long elapsedTime) {
        logger.info("[{}] 请求结束，耗时: {}ms，结果: {}", methodName, elapsedTime, result);
    }

    /**
     * 记录请求异常
     * @param methodName 方法名
     * @param e 异常
     */
    protected void logRequestError(String methodName, Exception e) {
        logger.error("[{}] 请求异常: {}", methodName, e.getMessage(), e);
    }

    /**
     * 验证参数是否为空
     * @param param 参数
     * @param paramName 参数名
     * @return 是否为空
     */
    protected boolean isParamEmpty(Object param, String paramName) {
        if (param == null) {
            logger.warn("参数 {} 为空", paramName);
            return true;
        }
        if (param instanceof String && ((String) param).trim().isEmpty()) {
            logger.warn("参数 {} 为空字符串", paramName);
            return true;
        }
        return false;
    }
}