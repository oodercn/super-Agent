package net.ooder.sdk.command.handler;

import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.system.validation.ParamValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.ooder.sdk.network.packet.*;
import net.ooder.sdk.network.udp.UDPMessageHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的技能命令处理基类，提供命令处理的统一框架和参数验证功能
 */
public abstract class AbstractSkillCommandHandler implements UDPMessageHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractSkillCommandHandler.class);

    /**
     * 命令处理方法映射，键为命令类型，值为对应的处理方法
     */
    private final Map<CommandType, Method> commandHandlers = new HashMap<>();

    public AbstractSkillCommandHandler() {
        // 初始化命令处理方法映射
        initializeCommandHandlers();
    }

    /**
     * 初始化命令处理方法映射
     */
    private void initializeCommandHandlers() {
        try {
            // 查找所有带有@CommandHandler注解的方法
            for (Method method : this.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(CommandHandler.class)) {
                    CommandHandler annotation = method.getAnnotation(CommandHandler.class);
                    CommandType commandType = annotation.value();
                    commandHandlers.put(commandType, method);
                    method.setAccessible(true);
                    log.debug("Registered command handler for: {}", commandType);
                }
            }
        } catch (Exception e) {
            log.error("Failed to initialize command handlers", e);
        }
    }

    @Override
    public void onCommand(CommandPacket packet) {
        String operation = packet.getOperation();
        String sourceId = packet.getSource() != null ? packet.getSource().getId() : "unknown";
        log.info("Received command: {} from agent: {}", operation, sourceId);

        if (operation == null) {
            log.warn("Null command received");
            return;
        }

        // 尝试从操作字符串转换为 CommandType
        CommandType commandType = null;
        try {
            commandType = CommandType.fromValue(operation).get();
        } catch (Exception e) {
            log.warn("Unknown command type: {}", operation);
        }

        Method handlerMethod = commandHandlers.get(commandType);
        if (handlerMethod != null) {
            try {
                // 验证参数
                if (ParamValidator.validate((Map<String, Object>) packet.getPayload(), handlerMethod)) {
                    // 调用处理方法
                    handlerMethod.invoke(this, packet);
                } else {
                    log.error("Parameter validation failed for command: {}", operation);
                    // 可以发送错误响应
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.error("Error executing command handler for: {}", operation, e);
            }
        } else {
            log.warn("No handler registered for command: {}", operation);
            // 调用默认的命令处理方法
            handleUnknownCommand(packet);
        }
    }

    /**
     * 处理未知命令
     */
    protected void handleUnknownCommand(CommandPacket packet) {
        String operation = packet.getOperation();
        log.warn("Unknown command received: {}", operation);
    }

    @Override
    public void onHeartbeat(HeartbeatPacket packet) {
        log.debug("Received heartbeat from agent: {}", packet.getAgentId());
        // 默认实现，子类可以重写
    }

    @Override
    public void onStatusReport(StatusReportPacket packet) {
        log.info("Received status report from agent: {}", packet.getSenderId());
        log.debug("Report type: {}, Status: {}", packet.getReportType(), packet.getCurrentStatus());
        // 默认实现，子类可以重写
    }

    @Override
    public void onError(UDPPacket packet, Exception e) {
        log.error("UDP error occurred while processing packet: {}", packet, e);
        // 默认实现，子类可以重写
    }

    @Override
    public void onAuth(AuthPacket packet) {
        log.debug("Received auth packet from agent: {}", packet.getSenderId());
        // 默认实现，子类可以重写
    }

    @Override
    public void onTask(TaskPacket packet) {
        log.debug("Received task packet from agent: {}", packet.getSenderId());
        // 默认实现，子类可以重写
    }

    @Override
    public void onRoute(RoutePacket packet) {
        log.debug("Received route packet from agent: {}", packet.getSenderId());
        // 默认实现，子类可以重写
    }
}
