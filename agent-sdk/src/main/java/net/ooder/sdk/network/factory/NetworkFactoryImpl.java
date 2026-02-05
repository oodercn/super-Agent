package net.ooder.sdk.network.factory;

import net.ooder.sdk.network.udp.UDPSDK;
import net.ooder.sdk.network.udp.UDPConfig;
import net.ooder.sdk.network.udp.UDPMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 网络工厂实现类
 */
@Component
public class NetworkFactoryImpl implements NetworkFactory {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Override
    public UDPSDK createUDPSDK(UDPConfig config) throws Exception {
        if (applicationContext != null) {
            return applicationContext.getBean(UDPSDK.class);
        } else {
            // 在测试环境中，直接返回null，让调用方处理
            return null;
        }
    }
    
    @Override
    public UDPConfig createUDPConfig() {
        return UDPConfig.builder().build();
    }
    
    @Override
    public UDPMessageHandler createUDPMessageHandler() {
        return new UDPMessageHandler() {
            @Override
            public void onHeartbeat(net.ooder.sdk.network.packet.HeartbeatPacket packet) {
                // 默认心跳处理
            }
            
            @Override
            public void onCommand(net.ooder.sdk.network.packet.CommandPacket packet) {
                // 默认命令处理
            }
            
            @Override
            public void onStatusReport(net.ooder.sdk.network.packet.StatusReportPacket packet) {
                // 默认状态报告处理
            }
            
            @Override
            public void onAuth(net.ooder.sdk.network.packet.AuthPacket packet) {
                // 默认认证处理
            }
            
            @Override
            public void onTask(net.ooder.sdk.network.packet.TaskPacket packet) {
                // 默认任务处理
            }
            
            @Override
            public void onRoute(net.ooder.sdk.network.packet.RoutePacket packet) {
                // 默认路由处理
            }
            
            @Override
            public void onError(net.ooder.sdk.network.packet.UDPPacket packet, Exception e) {
                // 默认错误处理
            }
        };
    }
}
