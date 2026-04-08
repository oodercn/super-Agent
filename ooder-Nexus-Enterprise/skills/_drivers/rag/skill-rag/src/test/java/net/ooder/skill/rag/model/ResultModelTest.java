package net.ooder.skill.rag.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultModelTest {

    @Nested
    @DisplayName("success 工厂方法")
    class SuccessTests {

        @Test
        void successWithDataShouldSetCode200() {
            ResultModel<String> result = ResultModel.success("data-value");
            assertEquals(200, result.getCode());
            assertEquals("success", result.getMessage());
            assertEquals("data-value", result.getData());
            assertTrue(result.getTimestamp() > 0);
        }

        @Test
        void successWithDataAndMessageShouldSetCustomMessage() {
            ResultModel<Integer> result = ResultModel.success(42, "操作成功");
            assertEquals(200, result.getCode());
            assertEquals("操作成功", result.getMessage());
            assertEquals(42, result.getData());
        }

        @Test
        void successWithNullDataShouldWork() {
            ResultModel<Object> result = ResultModel.success(null);
            assertEquals(200, result.getCode());
            assertNull(result.getData());
        }
    }

    @Nested
    @DisplayName("error 工厂方法")
    class ErrorTests {

        @Test
        void errorShouldSetCode500() {
            ResultModel<Void> result = ResultModel.error("服务器错误");
            assertEquals(500, result.getCode());
            assertEquals("服务器错误", result.getMessage());
            assertNull(result.getData());
        }

        @Test
        void errorWithEmptyMessageShouldWork() {
            ResultModel<Void> result = ResultModel.error("");
            assertEquals(500, result.getCode());
            assertEquals("", result.getMessage());
        }
    }

    @Nested
    @DisplayName("Setter/Getter 一致性")
    class PropertyTests {

        @Test
        void shouldAllowManualConstruction() {
            ResultModel<String> model = new ResultModel<>();
            model.setCode(201);
            model.setMessage("已创建");
            model.setData("created-id");
            model.setTimestamp(1000L);

            assertEquals(201, model.getCode());
            assertEquals("已创建", model.getMessage());
            assertEquals("created-id", model.getData());
            assertEquals(1000L, model.getTimestamp());
        }
    }

    @Nested
    @DisplayName("泛型支持")
    class GenericTests {

        @Test
        void shouldSupportStringType() {
            ResultModel<String> r = ResultModel.success("文本数据");
            assertInstanceOf(String.class, r.getData());
        }

        @Test
        void shouldSupportListType() {
            ResultModel<java.util.List<String>> r = ResultModel.success(java.util.List.of("a", "b"));
            assertEquals(2, r.getData().size());
        }

        @Test
        void shouldSupportMapType() {
            ResultModel<java.util.Map<String, Object>> r = ResultModel.success(
                java.util.Map.of("key", "value")
            );
            assertEquals("value", r.getData().get("key"));
        }
    }
}
