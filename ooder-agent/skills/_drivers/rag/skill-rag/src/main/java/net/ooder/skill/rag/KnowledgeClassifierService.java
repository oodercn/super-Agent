package net.ooder.skill.rag;

import java.util.List;
import java.util.Map;

public interface KnowledgeClassifierService {

    String classify(String text);

    List<String> extractTags(String text);

    List<RagPipeline.DictEntity> extractEntities(String text);

    default Map<String, List<String>> batchClassify(List<String> texts) {
        Map<String, List<String>> result = new java.util.LinkedHashMap<>();
        for (String text : texts) {
            String category = classify(text);
            result.computeIfAbsent(category, k -> new java.util.ArrayList<>()).add(text);
        }
        return result;
    }
}
