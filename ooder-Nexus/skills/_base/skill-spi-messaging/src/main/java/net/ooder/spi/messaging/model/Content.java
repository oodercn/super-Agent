package net.ooder.spi.messaging.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class Content {
    
    private String text;
    
    private String markdown;
    
    private List<Attachment> attachments;
    
    private Map<String, Object> payload;
    
    public static Content fromObject(Object obj) {
        if (obj == null) return null;
        Content content = new Content();
        if (obj instanceof String) {
            content.setText((String) obj);
        } else if (obj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) obj;
            content.setPayload(map);
            if (map.containsKey("text")) {
                content.setText(String.valueOf(map.get("text")));
            }
            if (map.containsKey("markdown")) {
                content.setMarkdown(String.valueOf(map.get("markdown")));
            }
        } else if (obj instanceof Content) {
            Content other = (Content) obj;
            content.setText(other.getText());
            content.setMarkdown(other.getMarkdown());
            content.setAttachments(other.getAttachments());
            content.setPayload(other.getPayload());
        } else {
            content.setText(obj.toString());
        }
        return content;
    }
    
    public Object toObject() {
        if (text != null && markdown == null && attachments == null && payload == null) {
            return text;
        }
        return this;
    }
    
    @Data
    public static class Attachment {
        private String type;
        private String url;
        private String name;
        private Long size;
        private String mimeType;
    }
}
