package net.ooder.nexus.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Result 单元测试
 *
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 2.0.0
 */
public class ResultTest {

    @Test
    public void testSuccessWithoutData() {
        Result<Void> result = Result.success();

        assertEquals("success", result.getStatus());
        assertEquals("操作成功", result.getMessage());
        assertNull(result.getData());
        assertEquals(200, result.getCode());
        assertTrue(result.getTimestamp() > 0);
    }

    @Test
    public void testSuccessWithData() {
        String data = "test data";
        Result<String> result = Result.success(data);

        assertEquals("success", result.getStatus());
        assertEquals("操作成功", result.getMessage());
        assertEquals(data, result.getData());
        assertEquals(200, result.getCode());
    }

    @Test
    public void testSuccessWithMessageAndData() {
        String message = "查询成功";
        String data = "test data";
        Result<String> result = Result.success(message, data);

        assertEquals("success", result.getStatus());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
        assertEquals(200, result.getCode());
    }

    @Test
    public void testErrorDefault() {
        Result<Void> result = Result.error();

        assertEquals("error", result.getStatus());
        assertEquals("操作失败", result.getMessage());
        assertNull(result.getData());
        assertEquals(500, result.getCode());
    }

    @Test
    public void testErrorWithMessage() {
        String message = "自定义错误消息";
        Result<Void> result = Result.error(message);

        assertEquals("error", result.getStatus());
        assertEquals(message, result.getMessage());
        assertNull(result.getData());
        assertEquals(500, result.getCode());
    }

    @Test
    public void testErrorWithMessageAndCode() {
        String message = "未找到资源";
        int code = 404;
        Result<Void> result = Result.error(message, code);

        assertEquals("error", result.getStatus());
        assertEquals(message, result.getMessage());
        assertNull(result.getData());
        assertEquals(code, result.getCode());
    }

    @Test
    public void testErrorWithMessageDataAndCode() {
        String message = "参数错误";
        String data = "详细错误信息";
        int code = 400;
        Result<String> result = Result.error(message, data, code);

        assertEquals("error", result.getStatus());
        assertEquals(message, result.getMessage());
        assertEquals(data, result.getData());
        assertEquals(code, result.getCode());
    }

    @Test
    public void testSettersAndGetters() {
        Result<String> result = new Result<>();

        result.setStatus("success");
        result.setMessage("测试消息");
        result.setData("测试数据");
        result.setCode(200);
        result.setTimestamp(1234567890L);

        assertEquals("success", result.getStatus());
        assertEquals("测试消息", result.getMessage());
        assertEquals("测试数据", result.getData());
        assertEquals(200, result.getCode());
        assertEquals(1234567890L, result.getTimestamp());
    }

    @Test
    public void testToString() {
        Result<String> result = Result.success("test");
        String str = result.toString();

        assertNotNull(str);
        assertTrue(str.contains("success"));
        assertTrue(str.contains("test"));
    }

    @Test
    public void testGenericType() {
        // 测试不同类型的数据
        Result<Integer> intResult = Result.success(42);
        assertEquals(42, intResult.getData());

        Result<Boolean> boolResult = Result.success(true);
        assertEquals(true, boolResult.getData());

        Result<Double> doubleResult = Result.success(3.14);
        assertEquals(3.14, doubleResult.getData());
    }
}
