package net.ooder.spi.im;

import net.ooder.spi.im.model.MessageContent;
import net.ooder.spi.im.model.SendResult;
import net.ooder.spi.im.handler.InboundHandler;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ImDeliveryDriver extends ImService {

    CompletableFuture<SendResult> sendAsync(MessageContent content, DeliveryContext ctx);

    Map<String, SendResult> broadcast(DeliveryTemplate template, List<String> channels);

    Set<String> getAvailableChannels();

    void registerInboundHandler(String channel, InboundHandler handler);

    void handleInbound(String channel, Map<String, Object> rawMessage);

    record DeliveryContext(
        String channel,
        String receiver,
        String tenantId,
        String userId,
        Map<String, Object> extra
    ) {}

    record DeliveryTemplate(
        String msgType,
        String content,
        String title,
        Map<String, Object> extra
    ) {}
}
