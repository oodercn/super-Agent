package net.ooder.skill.rag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KnowledgeClassifierServiceTest {

    @Nested
    @DisplayName("batchClassify 默认方法实现")
    class BatchClassifyTests {

        @Test
        void shouldGroupTextsByCategory() {
            KnowledgeClassifierService service = mock(KnowledgeClassifierService.class);
            when(service.classify("技术问题")).thenReturn("技术");
            when(service.classify("财务报销")).thenReturn("财务");
            when(service.classify("代码调试")).thenReturn("技术");

            when(service.batchClassify(anyList())).thenCallRealMethod();

            Map<String, List<String>> result = service.batchClassify(
                List.of("技术问题", "财务报销", "代码调试")
            );

            assertEquals(2, result.size());
            assertEquals(2, result.get("技术").size());
            assertTrue(result.get("技术").contains("技术问题"));
            assertTrue(result.get("技术").contains("代码调试"));
            assertEquals(1, result.get("财务").size());
            assertTrue(result.get("财务").contains("财务报销"));
        }

        @Test
        void shouldHandleEmptyInput() {
            KnowledgeClassifierService service = mock(KnowledgeClassifierService.class);
            when(service.batchClassify(anyList())).thenCallRealMethod();

            Map<String, List<String>> result = service.batchClassify(List.of());

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        void shouldHandleSingleInput() {
            KnowledgeClassifierService service = mock(KnowledgeClassifierService.class);
            when(service.classify("唯一文本")).thenReturn("分类A");
            when(service.batchClassify(anyList())).thenCallRealMethod();

            Map<String, List<String>> result = service.batchClassify(List.of("唯一文本"));

            assertEquals(1, result.size());
            assertTrue(result.containsKey("分类A"));
            assertEquals(1, result.get("分类A").size());
        }

        @Test
        void shouldPreserveInsertionOrderViaLinkedHashMap() {
            KnowledgeClassifierService service = mock(KnowledgeClassifierService.class);
            when(service.classify("A")).thenReturn("X");
            when(service.classify("B")).thenReturn("Y");
            when(service.classify("C")).thenReturn("X");
            when(service.batchClassify(anyList())).thenCallRealMethod();

            Map<String, List<String>> result = service.batchClassify(List.of("A", "B", "C"));

            assertInstanceOf(LinkedHashMap.class, result);
            var keys = result.keySet().toArray();
            assertEquals("X", keys[0]);
            assertEquals("Y", keys[1]);
        }
    }
}
