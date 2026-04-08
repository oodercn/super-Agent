package net.ooder.spi.im.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class MessageContent {
    
    private String text;
    
    private String markdown;
    
    private String title;
    
    private List<Attachment> attachments;
    
    private Map<String, Object> extra;
    
    public static MessageContent text(String text) {
        MessageContent mc = new MessageContent();
        mc.setText(text);
        return mc;
    }
    
    public static MessageContent markdown(String markdown) {
        MessageContent mc = new MessageContent();
        mc.setMarkdown(markdown);
        return mc;
    }
    
    @Data
    public static class Attachment {
        private String type;
        private String url;
        private String name;
        private Long size;
    }
}
