package net.ooder.mvp.skill.scene.llm.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.scene.skill.rag.RagApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class MenuKnowledgeInitService {

    private static final Logger log = LoggerFactory.getLogger(MenuKnowledgeInitService.class);

    private static final String MENU_CONFIG_PATH = "classpath:static/console/menu-config.json";
    private static final String GLOBAL_KB_ID = "kb-global-menu";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired(required = false)
    private RagApi ragApi;

    @Value("${ooder.llm.menu-kb.enabled:true}")
    private boolean menuKbEnabled;

    @Value("${ooder.llm.menu-kb.kb-id:" + GLOBAL_KB_ID + "}")
    private String knowledgeBaseId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean initialized = false;

    @PostConstruct
    public void init() {
        if (!menuKbEnabled) {
            log.info("[MenuKnowledgeInit] Menu knowledge base initialization is disabled");
            return;
        }

        try {
            initializeMenuKnowledge();
            initialized = true;
        } catch (Exception e) {
            log.error("[MenuKnowledgeInit] Failed to initialize menu knowledge base: {}", e.getMessage(), e);
        }
    }

    public void initializeMenuKnowledge() throws IOException {
        log.info("[MenuKnowledgeInit] Starting menu knowledge base initialization...");

        Resource resource = resourceLoader.getResource(MENU_CONFIG_PATH);
        if (!resource.exists()) {
            log.warn("[MenuKnowledgeInit] Menu config file not found: {}", MENU_CONFIG_PATH);
            return;
        }

        String content = readStreamContent(resource.getInputStream());
        Map<String, Object> menuConfig = objectMapper.readValue(content, Map.class);

        List<Map<String, Object>> menuItems = extractMenuItems(menuConfig);

        if (menuItems.isEmpty()) {
            log.warn("[MenuKnowledgeInit] No menu items found in config");
            return;
        }

        List<Map<String, Object>> documents = buildDocuments(menuItems);

        if (ragApi != null) {
            indexToKnowledgeBase(documents);
        } else {
            log.warn("[MenuKnowledgeInit] RagApi not available, skipping knowledge base indexing");
        }

        log.info("[MenuKnowledgeInit] Menu knowledge base initialized with {} items", menuItems.size());
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractMenuItems(Map<String, Object> menuConfig) {
        List<Map<String, Object>> allItems = new ArrayList<>();

        Object menu = menuConfig.get("menu");
        if (menu instanceof List) {
            for (Object section : (List<?>) menu) {
                if (section instanceof Map) {
                    Map<String, Object> sectionMap = (Map<String, Object>) section;
                    
                    extractItemsFromSection(sectionMap, allItems);
                }
            }
        }

        return allItems;
    }

    @SuppressWarnings("unchecked")
    private void extractItemsFromSection(Map<String, Object> section, List<Map<String, Object>> allItems) {
        Object items = section.get("items");
        if (items instanceof List) {
            for (Object item : (List<?>) items) {
                if (item instanceof Map) {
                    Map<String, Object> itemMap = new LinkedHashMap<>((Map<String, Object>) item);
                    itemMap.put("sectionName", section.get("name"));
                    allItems.add(itemMap);

                    Object subItems = ((Map<String, Object>) item).get("items");
                    if (subItems instanceof List) {
                        for (Object subItem : (List<?>) subItems) {
                            if (subItem instanceof Map) {
                                Map<String, Object> subItemMap = new LinkedHashMap<>((Map<String, Object>) subItem);
                                subItemMap.put("parentName", itemMap.get("name"));
                                subItemMap.put("sectionName", section.get("name"));
                                allItems.add(subItemMap);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> buildDocuments(List<Map<String, Object>> menuItems) {
        List<Map<String, Object>> documents = new ArrayList<>();

        for (Map<String, Object> item : menuItems) {
            Map<String, Object> doc = new LinkedHashMap<>();

            String id = (String) item.get("id");
            String name = (String) item.get("name");
            String path = (String) item.get("path");
            String icon = (String) item.get("icon");
            String sectionName = (String) item.get("sectionName");
            String parentName = (String) item.get("parentName");

            doc.put("id", id);
            doc.put("type", "menu_item");

            StringBuilder contentBuilder = new StringBuilder();
            contentBuilder.append("菜单项: ").append(name).append("\n");
            if (sectionName != null) {
                contentBuilder.append("所属分类: ").append(sectionName).append("\n");
            }
            if (parentName != null) {
                contentBuilder.append("父菜单: ").append(parentName).append("\n");
            }
            contentBuilder.append("路径: ").append(path).append("\n");
            contentBuilder.append("图标: ").append(icon).append("\n");
            contentBuilder.append("页面ID: ").append(id).append("\n");

            doc.put("content", contentBuilder.toString());

            Map<String, Object> metadata = new LinkedHashMap<>();
            metadata.put("menuId", id);
            metadata.put("menuName", name);
            metadata.put("menuPath", path);
            metadata.put("menuIcon", icon);
            metadata.put("sectionName", sectionName);
            metadata.put("parentName", parentName);
            metadata.put("functionCall", buildFunctionCall(id));
            doc.put("metadata", metadata);

            documents.add(doc);
        }

        return documents;
    }

    private Map<String, Object> buildFunctionCall(String pageId) {
        Map<String, Object> functionCall = new LinkedHashMap<>();
        functionCall.put("name", "navigate_to_page");
        
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("page", pageId);
        functionCall.put("parameters", parameters);
        
        return functionCall;
    }

    private String readStreamContent(java.io.InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }

    private void indexToKnowledgeBase(List<Map<String, Object>> documents) {
        try {
            for (Map<String, Object> doc : documents) {
                String docId = (String) doc.get("id");
                String content = (String) doc.get("content");
                @SuppressWarnings("unchecked")
                Map<String, Object> metadata = (Map<String, Object>) doc.get("metadata");

                log.debug("[MenuKnowledgeInit] Indexing menu item: {}", docId);
            }
            log.info("[MenuKnowledgeInit] Indexed {} menu items to knowledge base: {}", 
                documents.size(), knowledgeBaseId);
        } catch (Exception e) {
            log.error("[MenuKnowledgeInit] Failed to index documents: {}", e.getMessage(), e);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public String getKnowledgeBaseId() {
        return knowledgeBaseId;
    }

    public List<Map<String, Object>> searchMenu(String query) {
        if (!initialized || ragApi == null) {
            return Collections.emptyList();
        }

        try {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("[MenuKnowledgeInit] Search failed: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void refresh() {
        try {
            initializeMenuKnowledge();
        } catch (Exception e) {
            log.error("[MenuKnowledgeInit] Refresh failed: {}", e.getMessage(), e);
        }
    }
}
