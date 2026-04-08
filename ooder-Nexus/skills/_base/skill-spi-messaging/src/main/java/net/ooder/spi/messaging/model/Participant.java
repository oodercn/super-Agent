package net.ooder.spi.messaging.model;

import lombok.Data;
import net.ooder.scene.message.queue.MessageParticipant;
import net.ooder.scene.message.queue.ParticipantType;
import java.util.HashMap;
import java.util.Map;

@Data
public class Participant {
    
    private String id;
    
    private String name;
    
    private ParticipantType type;
    
    private String role;
    
    private String avatar;
    
    private boolean online;
    
    private Map<String, Object> metadata;
    
    public static Participant fromMessageParticipant(MessageParticipant mp) {
        if (mp == null) return null;
        Participant p = new Participant();
        p.setId(mp.getId());
        p.setName(mp.getName());
        p.setType(mp.getType());
        p.setOnline(true);
        if (mp.getAttributes() != null) {
            p.setMetadata(new HashMap<>(mp.getAttributes()));
        }
        return p;
    }
    
    public MessageParticipant toMessageParticipant() {
        MessageParticipant mp;
        if (type == ParticipantType.USER) {
            mp = MessageParticipant.user(id, name);
        } else if (type == ParticipantType.VIRTUAL_AGENT) {
            mp = MessageParticipant.virtualAgent(id, name);
        } else if (type == ParticipantType.PHYSICAL_AGENT) {
            mp = MessageParticipant.physicalAgent(id, name);
        } else {
            mp = MessageParticipant.system();
        }
        if (metadata != null) {
            metadata.forEach(mp::setAttribute);
        }
        return mp;
    }
}
