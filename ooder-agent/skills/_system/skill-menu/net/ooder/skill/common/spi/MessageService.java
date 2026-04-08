package net.ooder.skill.common.spi;

import net.ooder.skill.common.spi.message.Message;
import net.ooder.skill.common.spi.message.SceneNotification;
import net.ooder.skill.common.spi.message.SendMessageResult;

import java.util.List;

public interface MessageService {
    
    SendMessageResult sendMessage(Message message);
    
    List<SendMessageResult> batchSendMessages(List<Message> messages);
    
    SendMessageResult sendSceneNotification(
        String sceneId, 
        List<String> userIds, 
        SceneNotification notification
    );
}
