package net.ooder.sdk.system.validation;

import net.ooder.sdk.system.validation.ValidParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.alibaba.fastjson.util.TypeUtils;

/**
 * 参数验证工具类，用于验证命令参数
 */
public class ParamValidator {
    private static final Logger log = LoggerFactory.getLogger(ParamValidator.class);

    /**
     * 验证参数映射是否符合方法上的注解约束
     * @param params 参数映射
     * @param method 方法
     * @return 是否通过验证
     */
    public static boolean validate(Map<String, Object> params, Method method) {
        if (params == null) {
            params = Collections.emptyMap();
        }

        ValidParam[] validParams = method.getAnnotationsByType(ValidParam.class);
        if (validParams.length == 0) {
            return true;
        }

        for (ValidParam validParam : validParams) {
            String paramName = validParam.name();
            Object paramValue = params.get(paramName);

            // 必填检查
            if (validParam.required() && paramValue == null) {
                log.error(generateErrorMessage(validParam, "is required"));
                return false;
            }

            // 非必填且为空，跳过后续检查
            if (!validParam.required() && paramValue == null) {
                continue;
            }

            // 枚举类型处理 - 使用fastjson进行转换
            if (!validParam.enumClass().equals(Void.class) && Enum.class.isAssignableFrom(validParam.enumClass())) {
                try {
                    // 使用fastjson的TypeUtils进行枚举转换
                    Object enumValue = TypeUtils.cast(paramValue, validParam.enumClass(), null);
                    if (enumValue == null) {
                        log.error(generateErrorMessage(validParam, "should be a valid value of enum " + validParam.enumClass().getName()));
                        return false;
                    }
                    
                    // 如果指定了允许的枚举值，验证是否在列表中
                    String[] enumValues = validParam.enumValues();
                    if (enumValues.length > 0) {
                        String valueName = ((Enum<?>) enumValue).name();
                        boolean found = false;
                        for (String enumValueStr : enumValues) {
                            if (enumValueStr.equals(valueName)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            log.error(generateErrorMessage(validParam, "should be one of " + String.join(", ", enumValues)));
                            return false;
                        }
                    }
                } catch (Exception e) {
                    log.error(generateErrorMessage(validParam, "failed to validate enum value: " + e.getMessage()));
                    return false;
                }
            }

            // 集合元素类型验证
            if (Collection.class.isAssignableFrom(validParam.type()) && !validParam.elementType().equals(Object.class)) {
                if (paramValue instanceof Collection) {
                    Collection<?> collection = (Collection<?>) paramValue;
                    for (Object element : collection) {
                        if (element != null && !validParam.elementType().isAssignableFrom(element.getClass())) {
                            log.error(generateErrorMessage(validParam, "should contain only elements of type " + validParam.elementType().getName()));
                            return false;
                        }
                    }
                } else {
                    log.error(generateErrorMessage(validParam, "should be a collection"));
                    return false;
                }
            }

            // 映射键值类型验证
            if (Map.class.isAssignableFrom(validParam.type()) && !validParam.keyType().equals(Object.class) && !validParam.elementType().equals(Object.class)) {
                if (paramValue instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) paramValue;
                    for (Map.Entry<?, ?> entry : map.entrySet()) {
                        if (entry.getKey() != null && !validParam.keyType().isAssignableFrom(entry.getKey().getClass())) {
                            log.error(generateErrorMessage(validParam, "should have keys of type " + validParam.keyType().getName()));
                            return false;
                        }
                        if (entry.getValue() != null && !validParam.elementType().isAssignableFrom(entry.getValue().getClass())) {
                            log.error(generateErrorMessage(validParam, "should have values of type " + validParam.elementType().getName()));
                            return false;
                        }
                    }
                } else {
                    log.error(generateErrorMessage(validParam, "should be a map"));
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 生成验证错误消息
     */
    private static String generateErrorMessage(ValidParam validParam, String reason) {
        if (!validParam.message().isEmpty()) {
            return validParam.message();
        }
        return String.format("Parameter '%s' validation failed: %s", validParam.name(), reason);
    }
}
