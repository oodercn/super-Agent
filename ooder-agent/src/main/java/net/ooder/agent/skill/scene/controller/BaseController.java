package net.ooder.mvp.skill.scene.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected void logRequestStart(String methodName, Object params) {
        logger.info("[{}] request start, params: {}", methodName, params);
    }

    protected void logRequestEnd(String methodName, Object result, long elapsedTime) {
        logger.info("[{}] request end, elapsed: {}ms, result: {}", methodName, elapsedTime, result);
    }

    protected void logRequestError(String methodName, Exception e) {
        logger.error("[{}] request error: {}", methodName, e.getMessage(), e);
    }

    protected boolean isParamEmpty(Object param, String paramName) {
        if (param == null) {
            logger.warn("param {} is null", paramName);
            return true;
        }
        if (param instanceof String && ((String) param).trim().isEmpty()) {
            logger.warn("param {} is empty string", paramName);
            return true;
        }
        return false;
    }
}
