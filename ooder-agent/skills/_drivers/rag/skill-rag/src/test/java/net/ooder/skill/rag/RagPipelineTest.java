package net.ooder.skill.rag;

import net.ooder.skill.chat.model.KnowledgeDocument;
import net.ooder.skill.chat.service.KnowledgeService;
import net.ooder.skill.dict.dto.DictDTO;
import net.ooder.skill.dict.dto.DictItemDTO;
import net.ooder.skill.dict.service.DictService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RagPipelineTest {

    private RagPipeline ragPipeline;
    private KnowledgeService mockKnowledgeService;
    private DictService mockDictService;
    private KnowledgeClassifierService mockClassifierService;

    @BeforeEach
    void setUp() {
        ragPipeline = new RagPipeline();
        mockKnowledgeService = mock(KnowledgeService.class);
        mockDictService = mock(DictService.class);
        mockClassifierService = mock(KnowledgeClassifierService.class);

        ReflectionTestUtils.setField(ragPipeline, "knowledgeService", mockKnowledgeService);
        ReflectionTestUtils.setField(ragPipeline, "dictService", mockDictService);
        ReflectionTestUtils.setField(ragPipeline, "classifierService", mockClassifierService);
    }

    @Nested
    @DisplayName("ingestBusinessData - 业务数据摄入")
    class IngestTests {

        @Test
        void shouldIngestAndReturnDocument() {
            RagPipeline.BusinessDataIngestRequest request = new RagPipeline.BusinessDataIngestRequest();
            request.setTitle("测试文档");
            request.setContent("这是测试内容");
            request.setDataType("markdown");
            request.setSourceUserId("user001");
            request.setSyncToDict(false);

            KnowledgeDocument expectedDoc = new KnowledgeDocument();
            expectedDoc.setDocId("doc-001");
            expectedDoc.setTitle("测试文档");

            when(mockClassifierService.classify(anyString())).thenReturn("技术文档");
            when(mockKnowledgeService.uploadDocument(eq("user001"), eq("测试文档"),
                    eq("这是测试内容"), eq("markdown"))).thenReturn(expectedDoc);

            KnowledgeDocument result = ragPipeline.ingestBusinessData(request);

            assertNotNull(result);
            assertEquals("doc-001", result.getDocId());
            verify(mockClassifierService).classify(contains("测试文档"));
            verify(mockKnowledgeService).uploadDocument(eq("user001"), eq("测试文档"),
                    eq("这是测试内容"), eq("markdown"));
            verify(mockDictService, never()).refreshCache();
        }

        @Test
        void shouldUseSystemAsDefaultUserId() {
            RagPipeline.BusinessDataIngestRequest request = new RagPipeline.BusinessDataIngestRequest();
            request.setTitle("无用户文档");
            request.setContent("内容");
            request.setDataType("text");

            when(mockClassifierService.classify(anyString())).thenReturn("通用");
            when(mockKnowledgeService.uploadDocument(eq("system"), anyString(), anyString(), anyString()))
                    .thenReturn(new KnowledgeDocument());

            ragPipeline.ingestBusinessData(request);

            verify(mockKnowledgeService).uploadDocument(eq("system"), anyString(), anyString(), anyString());
        }

        @Test
        void shouldSyncToDictionaryWhenEnabled() {
            RagPipeline.BusinessDataIngestRequest request = new RagPipeline.BusinessDataIngestRequest();
            request.setTitle("同步字典");
            request.setContent("包含实体");
            request.setDataType("text");
            request.setSyncToDict(true);

            KnowledgeDocument doc = new KnowledgeDocument();
            doc.setDocId("doc-sync");
            doc.setContent("包含实体");

            List<RagPipeline.DictEntity> entities = List.of(
                new RagPipeline.DictEntity("组织", "公司", "OODER", "企业名称")
            );

            when(mockClassifierService.classify(anyString())).thenReturn("业务");
            when(mockKnowledgeService.uploadDocument(anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(doc);
            when(mockClassifierService.extractEntities(anyString())).thenReturn(entities);

            ragPipeline.ingestBusinessData(request);

            verify(mockClassifierService).extractEntities(contains("包含实体"));
            verify(mockDictService).refreshCache();
        }
    }

    @Nested
    @DisplayName("buildKnowledgeConfig - 知识配置构建")
    class BuildConfigTests {

        @Test
        void shouldBuildConfigWithSearchResults() {
            String query = "Java开发";
            when(mockKnowledgeService.search(eq(query), eq(5))).thenReturn(List.of("doc-1", "doc-2"));

            KnowledgeDocument doc1 = new KnowledgeDocument();
            doc1.setDocId("doc-1");
            doc1.setTitle("Java入门指南");
            doc1.setContent("Java是一种编程语言...");

            KnowledgeDocument doc2 = new KnowledgeDocument();
            doc2.setDocId("doc-2");
            doc2.setTitle("Spring框架");
            doc2.setContent("Spring是Java生态的核心框架...");

            when(mockKnowledgeService.getDocument("doc-1")).thenReturn(doc1);
            when(mockKnowledgeService.getDocument("doc-2")).thenReturn(doc2);

            DictDTO dictDTO = new DictDTO();
            dictDTO.setCode("scene-group-1");
            List<DictItemDTO> items = new ArrayList<>();
            DictItemDTO item = new DictItemDTO();
            item.setCode("status");
            item.setName("状态");
            item.setValue("1");
            items.add(item);
            dictDTO.setItems(items);
            when(mockDictService.getDictItems(eq("scene-group-1"))).thenReturn(items);

            RagPipeline.RagKnowledgeConfig config = ragPipeline.buildKnowledgeConfig("scene-group-1", query);

            assertNotNull(config);
            assertTrue(config.getKnowledgeContext().contains("Java入门指南"));
            assertTrue(config.getKnowledgeContext().contains("Spring框架"));
            assertFalse(config.getDictItems().isEmpty());
            assertNotNull(config.getSystemPromptTemplate());
        }

        @Test
        void shouldHandleEmptySearchResults() {
            when(mockKnowledgeService.search(anyString(), anyInt())).thenReturn(new ArrayList<>());

            RagPipeline.RagKnowledgeConfig config = ragPipeline.buildKnowledgeConfig(null, "不存在的查询");

            assertNotNull(config);
            assertEquals("", config.getKnowledgeContext());
        }

        @Test
        void shouldHandleSearchExceptionGracefully() {
            when(mockKnowledgeService.search(anyString(), anyInt()))
                    .thenThrow(new RuntimeException("搜索服务不可用"));

            RagPipeline.RagKnowledgeConfig config = ragPipeline.buildKnowledgeConfig(null, "test");

            assertNotNull(config);
            assertEquals("", config.getKnowledgeContext());
        }

        @Test
        void shouldFallbackToAllDictsWhenSceneGroupNotFound() {
            when(mockKnowledgeService.search(anyString(), anyInt())).thenReturn(new ArrayList<>());

            List<DictDTO> allDicts = new ArrayList<>();
            DictDTO d1 = new DictDTO();
            d1.setCode("dict1");
            List<DictItemDTO> items1 = new ArrayList<>();
            DictItemDTO i1 = new DictItemDTO();
            i1.setCode("k1"); i1.setName("v1");
            items1.add(i1);
            d1.setItems(items1);
            allDicts.add(d1);

            when(mockDictService.getDictItems(eq("unknown-scene"))).thenReturn(null);
            when(mockDictService.getAllDicts()).thenReturn(allDicts);

            RagPipeline.RagKnowledgeConfig config = ragPipeline.buildKnowledgeConfig("unknown-scene", "q");

            assertNotNull(config);
            assertFalse(config.getDictItems().isEmpty());
        }
    }

    @Nested
    @DisplayName("enhancePromptWithRAG - RAG提示增强")
    class EnhancePromptTests {

        @Test
        void shouldEnhancePromptWithRelatedDocs() {
            String query = "如何部署微服务";
            when(mockKnowledgeService.search(eq(query), eq(3))).thenReturn(List.of("d1", "d2"));

            KnowledgeDocument doc1 = new KnowledgeDocument();
            doc1.setDocId("d1");
            doc1.setTitle("微服务部署指南");
            doc1.setContent("使用Docker容器化部署...");

            when(mockKnowledgeService.getDocument("d1")).thenReturn(doc1);
            when(mockKnowledgeService.getDocument("d2")).thenReturn(null);

            String enhanced = ragPipeline.enhancePromptWithRAG(query, null, null);

            assertNotNull(enhanced);
            assertTrue(enhanced.contains("参考资料"));
            assertTrue(enhanced.contains("微服务部署指南"));
            assertTrue(enhanced.contains("[1]"));
        }

        @Test
        void shouldReturnNullWhenNoDocsFound() {
            when(mockKnowledgeService.search(anyString(), anyInt())).thenReturn(new ArrayList<>());

            String result = ragPipeline.enhancePromptWithRAG("无结果查询", null, null);

            assertNull(result);
        }

        @Test
        void shouldReturnNullOnException() {
            when(mockKnowledgeService.search(anyString(), anyInt()))
                    .thenThrow(new RuntimeException("知识库连接失败"));

            String result = ragPipeline.enhancePromptWithRAG("异常查询", null, null);

            assertNull(result);
        }

        @Test
        void shouldHandleMultipleDocuments() {
            when(mockKnowledgeService.search(eq("多文档"), eq(3)))
                    .thenReturn(List.of("a", "b", "c"));

            for (String id : List.of("a", "b", "c")) {
                KnowledgeDocument d = new KnowledgeDocument();
                d.setDocId(id); d.setTitle("文档" + id); d.setContent("内容" + id);
                when(mockKnowledgeService.getDocument(id)).thenReturn(d);
            }

            String enhanced = ragPipeline.enhancePromptWithRAG("多文档", null, null);

            assertNotNull(enhanced);
            assertTrue(enhanced.contains("[1]"));
            assertTrue(enhanced.contains("[2]"));
            assertTrue(enhanced.contains("[3]"));
        }
    }

    @Nested
    @DisplayName("searchRelated - 相关文档搜索")
    class SearchRelatedTests {

        @Test
        void shouldReturnRelatedDocuments() {
            when(mockKnowledgeService.search(eq("关键词"), eq(10)))
                    .thenReturn(List.of("s1"));

            KnowledgeDocument doc = new KnowledgeDocument();
            doc.setDocId("s1");
            when(mockKnowledgeService.getDocument("s1")).thenReturn(doc);

            List<KnowledgeDocument> results = ragPipeline.searchRelated("关键词", 10);

            assertFalse(results.isEmpty());
            assertEquals(1, results.size());
        }

        @Test
        void shouldSkipNullDocuments() {
            when(mockKnowledgeService.search(eq("null-doc"), eq(5)))
                    .thenReturn(List.of("null-id"));

            when(mockKnowledgeService.getDocument("null-id")).thenReturn(null);

            List<KnowledgeDocument> results = ragPipeline.searchRelated("null-doc", 5);

            assertTrue(results.isEmpty());
        }

        @Test
        void shouldReturnEmptyListOnException() {
            when(mockKnowledgeService.search(anyString(), anyInt()))
                    .thenThrow(new RuntimeException("超时"));

            List<KnowledgeDocument> results = ragPipeline.searchRelated("异常", 5);

            assertNotNull(results);
            assertTrue(results.isEmpty());
        }
    }

    @Nested
    @DisplayName("内部类 - BusinessDataIngestRequest")
    class InnerClassTests {

        @Test
        void shouldSupportSetterGetterPattern() {
            RagPipeline.BusinessDataIngestRequest req = new RagPipeline.BusinessDataIngestRequest();
            req.setTitle("标题");
            req.setContent("内容");
            req.setDataType("pdf");
            req.setSourceUserId("u1");
            req.setSyncToDict(true);

            assertEquals("标题", req.getTitle());
            assertEquals("内容", req.getContent());
            assertEquals("pdf", req.getDataType());
            assertEquals("u1", req.getSourceUserId());
            assertTrue(req.isSyncToDict());
        }
    }

    @Nested
    @DisplayName("内部类 - DictEntity record")
    class DictEntityTests {

        @Test
        void shouldCreateRecordCorrectly() {
            RagPipeline.DictEntity entity = new RagPipeline.DictEntity("人名", "张三", "Zhang San", "员工");

            assertEquals("人名", entity.category());
            assertEquals("张三", entity.key());
            assertEquals("Zhang San", entity.value());
            assertEquals("员工", entity.description());
        }

        @Test
        void shouldSupportEqualsAndHashCode() {
            RagPipeline.DictEntity e1 = new RagPipeline.DictEntity("类型", "k", "v", "desc");
            RagPipeline.DictEntity e2 = new RagPipeline.DictEntity("类型", "k", "v", "desc");
            RagPipeline.DictEntity e3 = new RagPipeline.DictEntity("其他", "k", "v", "desc");

            assertEquals(e1, e2);
            assertNotEquals(e1, e3);
            assertEquals(e1.hashCode(), e2.hashCode());
        }
    }

    @Nested
    @DisplayName("内部类 - RagKnowledgeConfig")
    class RagKnowledgeConfigTests {

        @Test
        void shouldSupportFullPropertyAccess() {
            RagPipeline.RagKnowledgeConfig config = new RagPipeline.RagKnowledgeConfig();
            config.setKnowledgeContext("上下文内容");
            List<Map<String, String>> items = new ArrayList<>();
            Map<String, String> entry = Map.of("key", "code", "value", "名称");
            items.add(entry);
            config.setDictItems(items);
            config.setSystemPromptTemplate("模板{{knowledge_context}}{{dict_items}}");

            assertEquals("上下文内容", config.getKnowledgeContext());
            assertEquals(1, config.getDictItems().size());
            assertEquals("模板{{knowledge_context}}{{dict_items}}", config.getSystemPromptTemplate());
        }
    }
}
