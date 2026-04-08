package net.ooder.spi.im.handler;

import java.util.Map;

@FunctionalInterface
public interface InboundHandler {
    void handle(String channel, Map<String, Object> rawMessage);
}
