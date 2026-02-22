package net.ooder.nexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * ooderNexus Spring Boot 应用程序主入口类
 *
 * <p>ooderNexus 是一个基于 Ooder Agent 架构的 P2P AI 能力分发枢纽，
 * 采用 Spring Boot 框架构建，支持去中心化的 P2P 网络通信、AI 技能管理、
 * OpenWrt 路由器集成等功能。</p>
 *
 * <p>主要功能模块：</p>
 * <ul>
 *   <li>P2P 网络管理 - 去中心化组网，节点自动发现</li>
 *   <li>AI 技能中心 - 发布、分享、执行 AI 技能</li>
 *   <li>OpenWrt 集成 - 路由器系统监控和管理</li>
 *   <li>存储管理 - VFS 虚拟文件系统</li>
 *   <li>协议仿真 - MCP/Route 协议调试</li>
 * </ul>
 *
 * @author ooder Team
 * @version 2.0.0-openwrt-preview
 * @since 1.0.0
 * @see org.springframework.boot.SpringApplication
 */
@SpringBootApplication
@ComponentScan(basePackages = "net.ooder.nexus")
public class NexusSpringApplication {

    /**
     * 应用程序主入口方法
     *
     * <p>启动 Spring Boot 应用程序，初始化所有配置和组件。</p>
     *
     * @param args 命令行参数，可通过 --server.port=8081 等方式指定配置
     */
    public static void main(String[] args) {
        SpringApplication.run(NexusSpringApplication.class, args);
    }
}
