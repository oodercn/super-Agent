package net.ooder.spi.rag.model;

public record RagRelatedDocument(
    String docId,
    String title,
    String content
) {}
