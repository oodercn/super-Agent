package net.ooder.spi.im.model;

import java.util.List;
import java.util.Map;

public class MessageContent {

    private MessageType type;
    private String text;
    private String markdown;
    private String title;
    private String content;
    private String url;
    private List<Attachment> attachments;
    private Map<String, Object> extra;

    public MessageContent() {}

    public MessageContent(MessageType type, String title, String content) {
        this.type = type;
        this.title = title;
        this.content = content;
    }

    public static MessageContent text(String content) {
        return new MessageContent(MessageType.TEXT, null, content);
    }

    public static MessageContent markdown(String title, String content) {
        MessageContent mc = new MessageContent(MessageType.MARKDOWN, title, content);
        mc.markdown = content;
        return mc;
    }

    public static MessageContent link(String title, String content, String url) {
        MessageContent mc = new MessageContent(MessageType.LINK, title, content);
        mc.url = url;
        return mc;
    }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getMarkdown() { return markdown; }
    public void setMarkdown(String markdown) { this.markdown = markdown; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public List<Attachment> getAttachments() { return attachments; }
    public void setAttachments(List<Attachment> attachments) { this.attachments = attachments; }
    public Map<String, Object> getExtra() { return extra; }
    public void setExtra(Map<String, Object> extra) { this.extra = extra; }

    public enum MessageType {
        TEXT, MARKDOWN, LINK, CARD, IMAGE, FILE, ACTION
    }

    public static class Attachment {
        private String type;
        private String url;
        private String name;
        private Long size;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getSize() { return size; }
        public void setSize(Long size) { this.size = size; }
    }
}
