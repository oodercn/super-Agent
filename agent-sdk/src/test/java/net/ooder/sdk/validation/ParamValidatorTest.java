package net.ooder.sdk.validation;

import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.validation.ParamValidator;
import net.ooder.sdk.system.validation.ValidParam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ParamValidatorTest {

    private TestValidator validator;

    @BeforeEach
    void setUp() {
        validator = new TestValidator();
    }

    // 测试枚举值验证
    @Test
    void testEnumValidation() throws Exception {
        // 测试有效枚举值
        Map<String, Object> params1 = new HashMap<>();
        params1.put("commandType", CommandType.ROUTE_ADD);
        Method method1 = TestValidator.class.getDeclaredMethod("testEnumValidation", Map.class);
        assertTrue(ParamValidator.validate(params1, method1));

        // 测试无效枚举值（不同枚举类型）
        Map<String, Object> params2 = new HashMap<>();
        params2.put("commandType", TestEnum.VALUE1);
        assertFalse(ParamValidator.validate(params2, method1));

        // 测试枚举值白名单验证
        Map<String, Object> params3 = new HashMap<>();
        params3.put("limitedCommand", CommandType.ROUTE_ADD);
        Method method3 = TestValidator.class.getDeclaredMethod("testEnumValueValidation", Map.class);
        assertTrue(ParamValidator.validate(params3, method3));

        // 测试不在白名单中的枚举值
        Map<String, Object> params4 = new HashMap<>();
        params4.put("limitedCommand", CommandType.ROUTE_REMOVE);
        assertFalse(ParamValidator.validate(params4, method3));
    }

    // 测试字符串类型的枚举值验证
    @Test
    void testStringAsEnumValidation() throws Exception {
        // 测试有效字符串枚举值
        Map<String, Object> params1 = new HashMap<>();
        params1.put("commandString", "ROUTE_ADD");
        Method method1 = TestValidator.class.getDeclaredMethod("testStringAsEnumValidation", Map.class);
        assertTrue(ParamValidator.validate(params1, method1));

        // 测试无效字符串枚举值
        Map<String, Object> params2 = new HashMap<>();
        params2.put("commandString", "INVALID_COMMAND");
        assertFalse(ParamValidator.validate(params2, method1));
    }

    // 测试集合元素类型验证
    @Test
    void testCollectionElementTypeValidation() throws Exception {
        // 测试有效元素类型的集合
        Map<String, Object> params1 = new HashMap<>();
        List<String> validList = Arrays.asList("value1", "value2");
        params1.put("stringList", validList);
        Method method1 = TestValidator.class.getDeclaredMethod("testCollectionElementTypeValidation", Map.class);
        assertTrue(ParamValidator.validate(params1, method1));

        // 测试无效元素类型的集合
        Map<String, Object> params2 = new HashMap<>();
        List<Object> invalidList = Arrays.asList("value1", 123);
        params2.put("stringList", invalidList);
        assertFalse(ParamValidator.validate(params2, method1));
    }

    // 测试Map键值类型验证
    @Test
    void testMapKeyValueTypeValidation() throws Exception {
        // 测试有效键值类型的Map
        Map<String, Object> params1 = new HashMap<>();
        Map<String, Integer> validMap = new HashMap<>();
        validMap.put("key1", 1);
        validMap.put("key2", 2);
        params1.put("stringIntMap", validMap);
        Method method1 = TestValidator.class.getDeclaredMethod("testMapKeyValueTypeValidation", Map.class);
        assertTrue(ParamValidator.validate(params1, method1));

        // 测试无效键类型的Map
        Map<String, Object> params2 = new HashMap<>();
        Map<Integer, Integer> invalidKeyMap = new HashMap<>();
        invalidKeyMap.put(1, 1);
        params2.put("stringIntMap", invalidKeyMap);
        assertFalse(ParamValidator.validate(params2, method1));

        // 测试无效值类型的Map
        Map<String, Object> params3 = new HashMap<>();
        Map<String, String> invalidValueMap = new HashMap<>();
        invalidValueMap.put("key1", "value1");
        params3.put("stringIntMap", invalidValueMap);
        assertFalse(ParamValidator.validate(params3, method1));
    }

    // 测试类，包含各种验证场景的方法
    static class TestValidator {
        
        @ValidParam(name = "commandType", type = CommandType.class, enumClass = CommandType.class)
        public void testEnumValidation(Map<String, Object> params) {
            // 测试方法体
        }
        
        @ValidParam(name = "limitedCommand", type = CommandType.class, enumClass = CommandType.class, enumValues = {"ROUTE_ADD", "ROUTE_LIST"})
        public void testEnumValueValidation(Map<String, Object> params) {
            // 测试方法体
        }
        
        @ValidParam(name = "commandString", type = String.class, enumClass = CommandType.class)
        public void testStringAsEnumValidation(Map<String, Object> params) {
            // 测试方法体
        }
        
        @ValidParam(name = "stringList", type = List.class, elementType = String.class)
        public void testCollectionElementTypeValidation(Map<String, Object> params) {
            // 测试方法体
        }
        
        @ValidParam(name = "stringIntMap", type = Map.class, keyType = String.class, elementType = Integer.class)
        public void testMapKeyValueTypeValidation(Map<String, Object> params) {
            // 测试方法体
        }
    }
    
    // 测试枚举类型
    enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }
}
