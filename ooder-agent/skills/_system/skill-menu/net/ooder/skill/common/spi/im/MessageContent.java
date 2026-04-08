package net.ooder.skill.common.spi.im;

public class MessageContent {
    
    private MessageType type;
    private String title;
    private String content;
    private String url;
    
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
        return new MessageContent(MessageType.MARKDOWN, title, content);
    }
    
    public static MessageContent link(String title, String content, String url) {
        MessageContent mc = new MessageContent(MessageType.LINK, title, content);
        mc.setUrl(url);
        return mc;
    }
    
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
