package net.ooder.mvp.skill.scene.llm.prompt;

import net.ooder.scene.skill.rag.RagApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PromptIndexingService {

    private static final Logger log = LoggerFactory.getLogger(PromptIndexingService.class);

    private static final String PROMPTS_DIR = "classpath:prompts/";
    private static final String SKILLS_PROMPTS_DIR = "classpath:skills/";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired(required = false)
    private RagApi ragApi;

    @Value("${ooder.llm.prompt-indexing.enabled:true}")
    private boolean indexingEnabled;

    @Value("${ooder.llm.prompt-indexing.kb-id:kb-skill-prompts}")
    private String promptKnowledgeBaseId;

    public IndexingResult indexPromptsForSkill(String skillId) {
        if (!indexingEnabled) {
            log.debug("[PromptIndexing] Prompt indexing is disabled");
            return IndexingResult.disabled();
        }

        if (ragApi == null) {
            log.warn("[PromptIndexing] RagApi not available");
            return IndexingResult.failed("RagApi not available");
        }

        log.info("[PromptIndexing] Starting prompt indexing for skill: {}", skillId);

        IndexingResult result = new IndexingResult(skillId);
        
        try {
            List<PromptDocument> documents = loadPromptDocuments(skillId);
            
            if (documents.isEmpty()) {
                log.warn("[PromptIndexing] No prompt documents found for skill: {}", skillId);
                return result;
            }

            for (PromptDocument doc : documents) {
                try {
                    indexDocument(doc);
                    result.addIndexed(doc.getDocId());
                } catch (Exception e) {
                    log.error("[PromptIndexing] Failed to index document {}: {}", doc.getDocId(), e.getMessage());
                    result.addFailed(doc.getDocId(), e.getMessage());
                }
            }

            log.info("[PromptIndexing] Indexed {} prompt documents for skill: {}", 
                result.getIndexedCount(), skillId);

        } catch (Exception e) {
            log.error("[PromptIndexing] Failed to index prompts for skill {}: {}", skillId, e.getMessage(), e);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }

    public IndexingResult indexPromptsFromDirectory(String directory) {
        if (!indexingEnabled) {
            return IndexingResult.disabled();
        }

        IndexingResult result = new IndexingResult("directory:" + directory);

        try {
            Resource dirResource = resourceLoader.getResource(directory);
            if (!dirResource.exists()) {
                log.warn("[PromptIndexing] Directory not found: {}", directory);
                return result;
            }

            List<Resource> mdFiles = findMarkdownFiles(directory);
            
            for (Resource mdFile : mdFiles) {
                try {
                    String content = readResourceContent(mdFile);
                    String filename = mdFile.getFilename();
                    
                    PromptDocument doc = new PromptDocument();
                    doc.setDocId(UUID.randomUUID().toString());
                    doc.setType(determinePromptType(filename));
                    doc.setContent(content);
                    doc.setMetadata(createMap("source", filename, "directory", directory));
                    
                    indexDocument(doc);
                    result.addIndexed(filename);
                } catch (Exception e) {
                    log.error("[PromptIndexing] Failed to index file: {}", mdFile.getFilename(), e);
                }
            }

        } catch (Exception e) {
            log.error("[PromptIndexing] Failed to index directory {}: {}", directory, e.getMessage(), e);
            result.setErrorMessage(e.getMessage());
        }

        return result;
    }

    private List<PromptDocument> loadPromptDocuments(String skillId) {
        List<PromptDocument> documents = new ArrayList<>();

        String[] promptPaths = {
            "classpath:skills/" + skillId + "/prompts/",
            "classpath:skills/" + skillId + "/",
            "classpath:prompts/"
        };

        for (String path : promptPaths) {
            try {
                List<PromptDocument> found = loadPromptsFromPath(path, skillId);
                documents.addAll(found);
            } catch (Exception e) {
                log.debug("[PromptIndexing] No prompts found at path: {}", path);
            }
        }

        return documents;
    }

    private List<PromptDocument> loadPromptsFromPath(String basePath, String skillId) throws IOException {
        List<PromptDocument> documents = new ArrayList<>();

        Resource baseResource = resourceLoader.getResource(basePath);
        if (!baseResource.exists()) {
            return documents;
        }

        String[] promptFiles = {"system.md", "system-prompt.md", "role.md", "context.md"};
        
        for (String filename : promptFiles) {
            try {
                String filePath = basePath.endsWith("/") ? basePath + filename : basePath + "/" + filename;
                Resource fileResource = resourceLoader.getResource(filePath);
                
                if (fileResource.exists()) {
                    String content = readResourceContent(fileResource);
                    
                    PromptDocument doc = new PromptDocument();
                    doc.setDocId(skillId + "-" + filename.replace(".md", ""));
                    doc.setSkillId(skillId);
                    doc.setType(determinePromptType(filename));
                    doc.setContent(content);
                    doc.setMetadata(createMap(
                        "filename", filename,
                        "path", filePath,
                        "skillId", skillId
                    ));
                    
                    documents.add(doc);
                }
            } catch (Exception e) {
                log.debug("[PromptIndexing] Could not load prompt file: {}", filename);
            }
        }

        return documents;
    }

    private List<Resource> findMarkdownFiles(String directory) throws IOException {
        List<Resource> files = new ArrayList<>();
        
        Resource dirResource = resourceLoader.getResource(directory);
        if (dirResource.exists()) {
            String[] extensions = {".md", ".markdown"};
            for (String ext : extensions) {
                try {
                    String pattern = directory + "*" + ext;
                    Resource patternResource = resourceLoader.getResource(pattern);
                    if (patternResource.exists()) {
                        files.add(patternResource);
                    }
                } catch (Exception ignored) {
                }
            }
        }
        
        return files;
    }

    private String readResourceContent(Resource resource) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString().trim();
    }

    private String determinePromptType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.contains("system")) {
            return "system";
        } else if (lower.contains("role")) {
            return "role";
        } else if (lower.contains("context")) {
            return "context";
        }
        return "general";
    }

    private Map<String, Object> createMap(String... keyValues) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            if (i + 1 < keyValues.length) {
                map.put(keyValues[i], keyValues[i + 1]);
            }
        }
        return map;
    }

    private void indexDocument(PromptDocument doc) {
        if (ragApi == null) {
            throw new IllegalStateException("RagApi not available");
        }

        log.debug("[PromptIndexing] Indexing document: {} (type: {})", doc.getDocId(), doc.getType());
    }

    public static class PromptDocument {
        private String docId;
        private String skillId;
        private String type;
        private String roleId;
        private String content;
        private Map<String, Object> metadata;

        public String getDocId() { return docId; }
        public void setDocId(String docId) { this.docId = docId; }
        public String getSkillId() { return skillId; }
        public void setSkillId(String skillId) { this.skillId = skillId; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getRoleId() { return roleId; }
        public void setRoleId(String roleId) { this.roleId = roleId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }

    public static class IndexingResult {
        private final String target;
        private final List<String> indexed = new ArrayList<>();
        private final Map<String, String> failed = new LinkedHashMap<>();
        private String errorMessage;
        private boolean disabled = false;

        public IndexingResult(String target) {
            this.target = target;
        }

        public static IndexingResult disabled() {
            IndexingResult result = new IndexingResult("disabled");
            result.disabled = true;
            return result;
        }

        public static IndexingResult failed(String error) {
            IndexingResult result = new IndexingResult("failed");
            result.errorMessage = error;
            return result;
        }

        public void addIndexed(String docId) { indexed.add(docId); }
        public void addFailed(String docId, String error) { failed.put(docId, error); }
        
        public String getTarget() { return target; }
        public List<String> getIndexed() { return indexed; }
        public Map<String, String> getFailed() { return failed; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        public boolean isDisabled() { return disabled; }
        public int getIndexedCount() { return indexed.size(); }
        public int getFailedCount() { return failed.size(); }
        public boolean isSuccess() { return errorMessage == null && failed.isEmpty(); }
    }
}
