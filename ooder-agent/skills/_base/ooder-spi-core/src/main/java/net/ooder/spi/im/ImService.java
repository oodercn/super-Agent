package net.ooder.spi.im;

import net.ooder.spi.im.model.MessageContent;
import net.ooder.spi.im.model.SendResult;

import java.util.List;

public interface ImService {

    SendResult sendToUser(String platform, String userId, MessageContent content);

    SendResult sendToGroup(String platform, String groupId, MessageContent content);

    SendResult sendDing(String userId, String title, String content);

    SendResult sendMarkdown(String platform, String userId, String title, String markdown);

    List<String> getAvailablePlatforms();

    boolean isPlatformAvailable(String platform);

    String getPlatformName(String platform);
}
