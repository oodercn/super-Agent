/**
 * Nexus Mock 数据类
 * 
 * 本文件封装了所有 Nexus 页面中使用的假数据，用于开发和测试。
 * 
 * 配置说明：
 * - 本类中的所有数据都是模拟数据，仅用于开发和测试
 * - 生产环境中应该使用真实的 API 接口数据
 * - Mock 数据与真实 API 响应格式保持一致
 * 
 * @module nexusSDK
 * @version 1.0.0
 */

/**
 * Mock 数据类
 * 封装所有 Nexus 功能模块的假数据
 */
class nexusMockData {
    /**
     * 获取仪表盘数据
     * @returns {Object} 仪表盘数据
     */
    static getDashboardData() {
        return {
            endAgentCount: 12,
            linkCount: 8,
            networkStatus: '正常',
            commandCount: 156,
            packetsSent: 1245,
            packetsReceived: 1189,
            packetsFailed: 12,
            networkStatusText: '正常',
            totalCommands: 156,
            successfulCommands: 148,
            failedCommands: 5,
            timeoutCommands: 3
        };
    }

    /**
     * 获取系统状态
     * @returns {Object} 系统状态数据
     */
    static getSystemStatus() {
        return {
            overview: {
                version: "v1.0.0",
                uptime: "24天 12小时 30分钟",
                status: "正常运行",
                lastRestart: new Date(Date.now() - 24 * 24 * 60 * 60 * 1000).toISOString(),
                timestamp: new Date().toISOString()
            },
            resources: {
                cpu: {
                    usage: 12,
                    cores: 4,
                    load: 0.8
                },
                memory: {
                    usage: 25,
                    total: 8,
                    used: 2
                },
                disk: {
                    usage: 15,
                    total: 100,
                    used: 15
                },
                network: {
                    usage: 10,
                    upload: 2,
                    download: 8
                }
            },
            services: [
                {
                    name: "Nexus Service",
                    status: "running",
                    pid: 1234,
                    startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
                    memory: "512MB",
                    cpu: "2%"
                },
                {
                    name: "Web Console",
                    status: "running",
                    pid: 1235,
                    startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
                    memory: "256MB",
                    cpu: "1%"
                },
                {
                    name: "API Service",
                    status: "running",
                    pid: 1236,
                    startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
                    memory: "128MB",
                    cpu: "1%"
                },
                {
                    name: "Monitoring Service",
                    status: "running",
                    pid: 1237,
                    startTime: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
                    memory: "64MB",
                    cpu: "0.5%"
                }
            ],
            events: [
                {
                    timestamp: new Date(Date.now() - 300000).toISOString(),
                    level: "INFO",
                    source: "system",
                    message: "系统状态检查完成: 所有服务正常"
                },
                {
                    timestamp: new Date(Date.now() - 600000).toISOString(),
                    level: "WARNING",
                    source: "network",
                    message: "网络连接暂时中断，已自动恢复"
                },
                {
                    timestamp: new Date(Date.now() - 900000).toISOString(),
                    level: "INFO",
                    source: "service",
                    message: "API Service 重启完成"
                },
                {
                    timestamp: new Date(Date.now() - 1200000).toISOString(),
                    level: "ERROR",
                    source: "terminal",
                    message: "终端 endagent-003 连接失败"
                }
            ]
        };
    }

    /**
     * 获取安全状态
     * @returns {Object} 安全状态数据
     */
    static getSecurityStatus() {
        return {
            securityStatus: '安全',
            userCount: 5,
            activeSessions: 2,
            securityEvents: 0
        };
    }

    /**
     * 获取用户列表
     * @returns {Array} 用户列表
     */
    static getUsers() {
        return [
            {
                id: 1,
                username: "admin",
                role: "enterprise",
                status: "active",
                lastLogin: new Date(Date.now() - 3600000).toISOString()
            },
            {
                id: 2,
                username: "user1",
                role: "personal",
                status: "active",
                lastLogin: new Date(Date.now() - 7200000).toISOString()
            },
            {
                id: 3,
                username: "user2",
                role: "home",
                status: "inactive",
                lastLogin: new Date(Date.now() - 86400000).toISOString()
            },
            {
                id: 4,
                username: "user3",
                role: "personal",
                status: "active",
                lastLogin: new Date(Date.now() - 172800000).toISOString()
            },
            {
                id: 5,
                username: "user4",
                role: "home",
                status: "active",
                lastLogin: new Date(Date.now() - 259200000).toISOString()
            }
        ];
    }

    /**
     * 获取权限列表
     * @returns {Object} 权限列表
     */
    static getPermissions() {
        return {
            personal: ['查看仪表盘', '管理终端', '查看网络状态'],
            home: ['查看仪表盘', '管理终端', '查看网络状态', '管理网络设置'],
            enterprise: ['查看仪表盘', '管理终端', '查看网络状态', '管理网络设置', '管理用户', '修改系统配置', '查看系统日志']
        };
    }

    /**
     * 获取安全日志
     * @returns {Array} 安全日志列表
     */
    static getSecurityLogs() {
        return [
            {
                timestamp: new Date(Date.now() - 3600000).toISOString(),
                event: "登录成功",
                user: "admin",
                ip: "192.168.1.100"
            },
            {
                timestamp: new Date(Date.now() - 7200000).toISOString(),
                event: "登录成功",
                user: "user1",
                ip: "192.168.1.101"
            },
            {
                timestamp: new Date(Date.now() - 86400000).toISOString(),
                event: "密码修改",
                user: "admin",
                ip: "192.168.1.100"
            }
        ];
    }

    /**
     * 获取健康检查数据
     * @returns {Object} 健康检查数据
     */
    static getHealthData() {
        return {
            overall: {
                status: "healthy",
                message: "系统运行正常，所有组件状态良好",
                score: 98,
                lastCheck: new Date().toISOString(),
                duration: 1.2
            },
            components: [
                {
                    name: "Nexus核心",
                    status: "healthy",
                    message: "核心服务运行正常",
                    metrics: {
                        cpu: 12,
                        memory: 25
                    }
                },
                {
                    name: "网络服务",
                    status: "healthy",
                    message: "网络连接正常",
                    metrics: {
                        connections: 12,
                        packetLoss: 0
                    }
                },
                {
                    name: "终端代理",
                    status: "warning",
                    message: "部分终端离线",
                    metrics: {
                        activeEndpoints: 5,
                        totalEndpoints: 6,
                        offlineEndpoints: 1
                    }
                },
                {
                    name: "存储服务",
                    status: "healthy",
                    message: "存储状态正常",
                    metrics: {
                        freeSpace: 85,
                        filesystem: "正常"
                    }
                }
            ],
            resources: {
                cpu: 12,
                memory: 25,
                disk: 15,
                network: 10
            },
            services: [
                {
                    name: "Nexus Service",
                    status: "running",
                    port: 9876,
                    startTime: new Date(Date.now() - 3600000).toISOString(),
                    responseTime: 15
                },
                {
                    name: "Web Console",
                    status: "running",
                    port: 8080,
                    startTime: new Date(Date.now() - 3600000).toISOString(),
                    responseTime: 20
                },
                {
                    name: "API Service",
                    status: "running",
                    port: 8081,
                    startTime: new Date(Date.now() - 3600000).toISOString(),
                    responseTime: 10
                },
                {
                    name: "Monitoring Service",
                    status: "running",
                    port: 9090,
                    startTime: new Date(Date.now() - 3600000).toISOString(),
                    responseTime: 5
                }
            ],
            logs: [
                {
                    timestamp: new Date(Date.now() - 300000).toISOString(),
                    level: "INFO",
                    message: "健康检查完成: 系统状态良好"
                },
                {
                    timestamp: new Date(Date.now() - 600000).toISOString(),
                    level: "INFO",
                    message: "网络检查完成: 所有连接正常"
                },
                {
                    timestamp: new Date(Date.now() - 900000).toISOString(),
                    level: "WARNING",
                    message: "终端代理检查: 1个终端离线"
                },
                {
                    timestamp: new Date(Date.now() - 1200000).toISOString(),
                    level: "INFO",
                    message: "服务检查完成: 所有服务运行正常"
                }
            ]
        };
    }

    /**
     * 获取终端列表
     * @returns {Array} 终端列表
     */
    static getEndAgents() {
        return [
            {
                id: "endagent-001",
                name: "智能灯泡1",
                type: "light",
                ip: "192.168.1.100",
                mac: "AA:BB:CC:DD:EE:FF",
                status: "active",
                brightness: 80,
                color: "#FFFFFF",
                lastUpdate: new Date().toISOString()
            },
            {
                id: "endagent-002",
                name: "智能插座1",
                type: "socket",
                ip: "192.168.1.101",
                mac: "AA:BB:CC:DD:EE:GG",
                status: "active",
                power: "on",
                voltage: 220,
                lastUpdate: new Date().toISOString()
            },
            {
                id: "endagent-003",
                name: "摄像头1",
                type: "camera",
                ip: "192.168.1.102",
                mac: "AA:BB:CC:DD:EE:HH",
                status: "inactive",
                resolution: "1080p",
                storage: "128GB",
                lastUpdate: new Date().toISOString()
            },
            {
                id: "endagent-004",
                name: "智能音箱1",
                type: "speaker",
                ip: "192.168.1.103",
                mac: "AA:BB:CC:DD:EE:II",
                status: "active",
                volume: 50,
                lastUpdate: new Date().toISOString()
            },
            {
                id: "endagent-005",
                name: "温控器1",
                type: "thermostat",
                ip: "192.168.1.104",
                mac: "AA:BB:CC:DD:EE:JJ",
                status: "active",
                temperature: 25,
                humidity: 45,
                lastUpdate: new Date().toISOString()
            },
            {
                id: "endagent-006",
                name: "智能门锁1",
                type: "doorlock",
                ip: "192.168.1.105",
                mac: "AA:BB:CC:DD:EE:KK",
                status: "active",
                lockStatus: "locked",
                battery: 80,
                lastUpdate: new Date().toISOString()
            }
        ];
    }

    /**
     * 获取协议处理器列表
     * @returns {Array} 协议处理器列表
     */
    static getProtocolHandlers() {
        return [
            {
                commandType: "MCP_REGISTER",
                name: "注册命令处理器",
                description: "处理Nexus注册命令",
                registeredTime: new Date().toISOString(),
                status: "active"
            },
            {
                commandType: "MCP_DEREGISTER",
                name: "注销命令处理器",
                description: "处理Nexus注销命令",
                registeredTime: new Date().toISOString(),
                status: "active"
            },
            {
                commandType: "MCP_HEARTBEAT",
                name: "心跳命令处理器",
                description: "处理Nexus心跳命令",
                registeredTime: new Date().toISOString(),
                status: "active"
            },
            {
                commandType: "MCP_STATUS",
                name: "状态命令处理器",
                description: "处理Nexus状态命令",
                registeredTime: new Date().toISOString(),
                status: "active"
            },
            {
                commandType: "MCP_DISCOVER",
                name: "发现命令处理器",
                description: "处理Nexus发现命令",
                registeredTime: new Date().toISOString(),
                status: "active"
            }
        ];
    }

    /**
     * 获取日志列表
     * @returns {Array} 日志列表
     */
    static getLogs() {
        return [
            {
                id: 1,
                timestamp: new Date(Date.now() - 300000).toISOString(),
                level: "ERROR",
                source: "network",
                message: "网络连接失败: 无法连接到终端 endagent-003",
                details: "连接超时，尝试重连失败，可能是网络问题或终端离线。"
            },
            {
                id: 2,
                timestamp: new Date(Date.now() - 600000).toISOString(),
                level: "WARNING",
                source: "service",
                message: "服务重启: API Service 自动重启",
                details: "服务内存使用过高，触发自动重启机制。"
            },
            {
                id: 3,
                timestamp: new Date(Date.now() - 900000).toISOString(),
                level: "INFO",
                source: "system",
                message: "系统状态检查: 所有服务正常运行",
                details: "CPU使用率: 12%, 内存使用率: 25%, 磁盘使用率: 15%"
            },
            {
                id: 4,
                timestamp: new Date(Date.now() - 1200000).toISOString(),
                level: "DEBUG",
                source: "terminal",
                message: "终端注册: 新终端 endagent-004 注册成功",
                details: "终端类型: Windows PC, IP地址: 192.168.1.104"
            },
            {
                id: 5,
                timestamp: new Date(Date.now() - 1500000).toISOString(),
                level: "INFO",
                source: "network",
                message: "网络拓扑更新: 新增连接 endagent-001 -> endagent-002",
                details: "连接类型: 有线，带宽: 100Mbps"
            }
        ];
    }

    /**
     * 获取配置数据
     * @returns {Object} 配置数据
     */
    static getConfigs() {
        return {
            system: {
                version: "v1.0.0",
                name: "Nexus",
                mode: "normal",
                logLevel: "INFO"
            },
            network: {
                udpPort: 8080,
                heartbeatInterval: 30000,
                heartbeatTimeout: 60000,
                networkTimeout: 10000
            },
            terminal: {
                maxTerminals: 100,
                terminalTimeout: 300,
                terminalReconnectAttempts: 3,
                terminalReconnectDelay: 5
            },
            service: {
                apiPort: 8081,
                webConsolePort: 8082,
                serviceTimeout: 60,
                maxConnections: 1000
            }
        };
    }

    /**
     * 获取配置历史
     * @returns {Array} 配置历史列表
     */
    static getConfigHistory() {
        return [
            {
                id: 1,
                timestamp: new Date(Date.now() - 3600000).toISOString(),
                user: "admin",
                action: "修改",
                details: "更新网络配置：UDP端口从8080改为8081"
            },
            {
                id: 2,
                timestamp: new Date(Date.now() - 7200000).toISOString(),
                user: "admin",
                action: "修改",
                details: "更新日志级别：从DEBUG改为INFO"
            },
            {
                id: 3,
                timestamp: new Date(Date.now() - 86400000).toISOString(),
                user: "system",
                action: "初始化",
                details: "系统首次启动，加载默认配置"
            }
        ];
    }

    /**
     * 格式化时间戳
     * @param {number} timestamp - 时间戳
     * @returns {string} 格式化后的时间字符串
     */
    static formatTimestamp(timestamp) {
        const date = new Date(timestamp);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }

    /**
     * 格式化数字
     * @param {number} number - 数字
     * @returns {string} 格式化后的数字字符串
     */
    static formatNumber(number) {
        return new Intl.NumberFormat('zh-CN').format(number);
    }

    /**
     * 创建 API 响应
     * @param {*} data - 响应数据
     * @param {string} message - 响应消息
     * @returns {Object} API 响应对象
     */
    static createApiResponse(data, message = '操作成功') {
        return {
            status: 'success',
            message: message,
            data: data,
            timestamp: Date.now()
        };
    }

    /**
     * 创建错误响应
     * @param {string} message - 错误消息
     * @returns {Object} 错误响应对象
     */
    static createErrorResponse(message = '操作失败') {
        return {
            status: 'error',
            message: message,
            timestamp: Date.now()
        };
    }
}

/**
 * 导出 Mock 数据类
 */
if (typeof module !== 'undefined' && module.exports) {
    module.exports = nexusMockData;
}

