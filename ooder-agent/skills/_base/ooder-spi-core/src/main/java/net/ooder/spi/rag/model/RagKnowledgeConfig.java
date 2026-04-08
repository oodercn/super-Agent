package net.ooder.spi.rag.model;

public record RagKnowledgeConfig(
    String knowledgeContext,
    java.util.List<java.util.Map<String, String>> dictItems,
    String systemPromptTemplate
) {}
