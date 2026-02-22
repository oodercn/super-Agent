/**
 * 模拟数据服务
 * 为前端提供与后端API结构一致的模拟数据
 * 便于前端开发和测试，同时为后端开发提供数据结构参�? */

const MOCK_DATA = {
    // 模拟API基础URL
    API_BASE_URL: '/api/mcp',
    
    // 是否启用模拟数据
    ENABLE_MOCK: true,
    
    // 模拟数据延迟（毫秒）
    MOCK_DELAY: 300,
    
    // 模拟设备数据
    devices: [
        {
            id: 1,
            name: '客厅灯光',
            type: 'light',
            location: '客厅',
            status: 'online',
            ip: '192.168.1.101',
            mac: 'AA:BB:CC:DD:EE:01',
            lastActive: new Date().toLocaleString('zh-CN'),
            power: 15,
            brightness: 80,
            color: '#FFFFFF'
        },
        {
            id: 2,
            name: '卧室插座',
            type: 'socket',
            location: '卧室',
            status: 'online',
            ip: '192.168.1.102',
            mac: 'AA:BB:CC:DD:EE:02',
            lastActive: new Date().toLocaleString('zh-CN'),
            power: 0,
            voltage: 220,
            current: 0
        },
        {
            id: 3,
            name: '前门门锁',
            type: 'lock',
            location: '前门',
            status: 'online',
            ip: '192.168.1.103',
            mac: 'AA:BB:CC:DD:EE:03',
            lastActive: new Date().toLocaleString('zh-CN'),
            locked: true,
            battery: 85
        },
        {
            id: 4,
            name: '厨房摄像�?,
            type: 'camera',
            location: '厨房',
            status: 'warning',
            ip: '192.168.1.104',
            mac: 'AA:BB:CC:DD:EE:04',
            lastActive: new Date(Date.now() - 3600000).toLocaleString('zh-CN'),
            resolution: '1920x1080',
            storage: 75,
            battery: 45
        },
        {
            id: 5,
            name: '空调',
            type: 'other',
            location: '客厅',
            status: 'offline',
            ip: '192.168.1.105',
            mac: 'AA:BB:CC:DD:EE:05',
            lastActive: new Date(Date.now() - 86400000).toLocaleString('zh-CN'),
            temperature: 24,
            mode: 'cool',
            fanSpeed: 'auto'
        }
    ],
    
    // 模拟服务数据
    services: [
        {
            id: 'mcp',
            name: 'MCP Agent',
            type: 'core',
            status: 'running',
            version: '0.6.6',
            uptime: 3600,
            cpu: 15.2,
            memory: 256,
            disk: 1024,
            network: {
                packetsSent: 1200,
                packetsReceived: 1050,
                bytesSent: 1024000,
                bytesReceived: 896000
            },
            lastHeartbeat: Date.now() - 30000,
            endpoints: [
                'http://localhost:8091/api/mcp',
                'udp://localhost:9876'
            ],
            description: '核心MCP Agent服务，负责网络管理和命令处理'
        },
        {
            id: 'skillcenter',
            name: 'Skill Center',
            type: 'service',
            status: 'running',
            version: '0.6.6',
            uptime: 3500,
            cpu: 8.7,
            memory: 192,
            disk: 512,
            network: {
                packetsSent: 850,
                packetsReceived: 720,
                bytesSent: 768000,
                bytesReceived: 640000
            },
            lastHeartbeat: Date.now() - 45000,
            endpoints: [
                'http://localhost:8091/api/skills'
            ],
            description: '技能中心服务，管理和协调各种技能模�?
        },
        {
            id: 'skillflow',
            name: 'Skill Flow',
            type: 'workflow',
            status: 'warning',
            version: '0.6.6',
            uptime: 3200,
            cpu: 22.5,
            memory: 384,
            disk: 768,
            network: {
                packetsSent: 1500,
                packetsReceived: 1350,
                bytesSent: 1280000,
                bytesReceived: 1152000
            },
            lastHeartbeat: Date.now() - 75000,
            endpoints: [
                'http://localhost:8091/api/workflow'
            ],
            description: '技能工作流服务，处理复杂的技能执行流�?
        },
        {
            id: 'vfs',
            name: 'Virtual File System',
            type: 'storage',
            status: 'stopped',
            version: '0.6.6',
            uptime: 0,
            cpu: 0,
            memory: 0,
            disk: 2048,
            network: {
                packetsSent: 0,
                packetsReceived: 0,
                bytesSent: 0,
                bytesReceived: 0
            },
            lastHeartbeat: 0,
            endpoints: [
                'http://localhost:8091/api/vfs'
            ],
            description: '虚拟文件系统服务，管理文件存储和访问'
        }
    ],
    
    // 模拟仪表盘数�?    dashboard: {
        systemStatus: '正常',
        networkCount: 12,
        cpuUsage: '25%',
        memoryUsage: '45%',
        diskUsage: '32%',
        systemLoad: '0.8',
        requestRate: '1000/�?,
        temperature: '45°C',
        uptime: '24小时',
        agentCount: 5,
        serviceCount: 4,
        skillCount: 12,
        commandCount: 256
    },
    
    // 模拟网络设备数据
    networkDevices: [
        {
            id: 1,
            name: '主路由器',
            type: 'router',
            ip: '192.168.1.1',
            mac: 'AA:BB:CC:DD:EE:00',
            status: 'online',
            vendor: 'TP-Link',
            model: 'TL-WDR7660',
            firmware: 'v1.0.0',
            uptime: 86400,
            bandwidth: {
                upload: 100,
                download: 1000
            }
        },
        {
            id: 2,
            name: '交换�?,
            type: 'switch',
            ip: '192.168.1.2',
            mac: 'AA:BB:CC:DD:EE:01',
            status: 'online',
            vendor: 'D-Link',
            model: 'DGS-1008D',
            firmware: 'v2.0.0',
            uptime: 43200,
            bandwidth: {
                upload: 1000,
                download: 1000
            }
        }
    ],
    
    // 模拟API响应
    mockResponses: {
        '/api/home/devices': function(params) {
            let devices = [...MOCK_DATA.devices];
            
            // 应用过滤
            if (params.status && params.status !== 'all') {
                devices = devices.filter(device => device.status === params.status);
            }
            
            if (params.type && params.type !== 'all') {
                devices = devices.filter(device => device.type === params.type);
            }
            
            if (params.search) {
                const searchTerm = params.search.toLowerCase();
                devices = devices.filter(device => 
                    device.name.toLowerCase().includes(searchTerm) ||
                    device.location.toLowerCase().includes(searchTerm)
                );
            }
            
            return {
                status: 'success',
                message: '设备数据获取成功',
                devices: devices,
                total: devices.length,
                timestamp: Date.now()
            };
        },
        
        '/api/home/devices/:id': function(params, id) {
            const device = MOCK_DATA.devices.find(d => d.id === parseInt(id));
            if (device) {
                return {
                    status: 'success',
                    message: '设备详情获取成功',
                    device: device,
                    timestamp: Date.now()
                };
            } else {
                return {
                    status: 'error',
                    message: '设备不存�?,
                    code: 'DEVICE_NOT_FOUND',
                    timestamp: Date.now()
                };
            }
        },
        
        '/api/mcp/management/agents': function(params) {
            return {
                status: 'success',
                message: 'MCP Agent实例获取成功',
                summary: {
                    total: 1,
                    running: 1,
                    stopped: 0,
                    error: 0
                },
                data: [
                    {
                        id: 'default',
                        name: 'Default MCP Agent',
                        type: 'local',
                        status: 'running',
                        version: '0.6.6',
                        endpoint: 'localhost:9876',
                        heartbeatInterval: 30000,
                        description: '默认MCP Agent实例，用于本地开发和测试'
                    }
                ],
                timestamp: Date.now()
            };
        },
        
        '/api/mcp/status': function(params) {
            return {
                status: 'success',
                message: '系统状态获取成�?,
                data: {
                    systemStatus: MOCK_DATA.dashboard.systemStatus,
                    networkCount: MOCK_DATA.dashboard.networkCount,
                    cpuUsage: MOCK_DATA.dashboard.cpuUsage,
                    memoryUsage: MOCK_DATA.dashboard.memoryUsage,
                    diskUsage: MOCK_DATA.dashboard.diskUsage,
                    systemLoad: MOCK_DATA.dashboard.systemLoad,
                    uptime: MOCK_DATA.dashboard.uptime,
                    agentCount: MOCK_DATA.dashboard.agentCount,
                    serviceCount: MOCK_DATA.dashboard.serviceCount,
                    skillCount: MOCK_DATA.dashboard.skillCount
                },
                timestamp: Date.now()
            };
        }
    },
    
    // 模拟API请求
    async fetch(url, options = {}) {
        // 如果禁用模拟数据，直接返回真实请�?        if (!MOCK_DATA.ENABLE_MOCK) {
            return fetch(url, options);
        }
        
        // 模拟网络延迟
        await new Promise(resolve => setTimeout(resolve, MOCK_DATA.MOCK_DELAY));
        
        // 解析URL
        const urlObj = new URL(url, window.location.origin);
        const path = urlObj.pathname;
        const params = {};
        
        // 解析查询参数
        urlObj.searchParams.forEach((value, key) => {
            params[key] = value;
        });
        
        // 处理路径参数
        let matchedPath = null;
        let pathParams = {};
        
        for (const mockPath in MOCK_DATA.mockResponses) {
            const regex = new RegExp(`^${mockPath.replace(/:\w+/g, '([^/]+)')}$`);
            const match = path.match(regex);
            if (match) {
                matchedPath = mockPath;
                // 提取路径参数
                const paramNames = mockPath.match(/:\w+/g) || [];
                paramNames.forEach((paramName, index) => {
                    const name = paramName.substring(1);
                    pathParams[name] = match[index + 1];
                });
                break;
            }
        }
        
        // 调用对应的模拟响应函�?        if (matchedPath) {
            const responseData = MOCK_DATA.mockResponses[matchedPath](params, pathParams.id);
            
            // 模拟响应对象
            return {
                ok: true,
                status: 200,
                statusText: 'OK',
                json: async () => responseData,
                text: async () => JSON.stringify(responseData)
            };
        } else {
            // 模拟404响应
            return {
                ok: false,
                status: 404,
                statusText: 'Not Found',
                json: async () => ({
                    status: 'error',
                    message: 'API endpoint not found',
                    code: 'ENDPOINT_NOT_FOUND',
                    timestamp: Date.now()
                }),
                text: async () => JSON.stringify({
                    status: 'error',
                    message: 'API endpoint not found',
                    code: 'ENDPOINT_NOT_FOUND',
                    timestamp: Date.now()
                })
            };
        }
    },
    
    // 获取模拟数据
    getData(type, params = {}) {
        switch (type) {
            case 'devices':
                return MOCK_DATA.devices;
            case 'services':
                return MOCK_DATA.services;
            case 'dashboard':
                return MOCK_DATA.dashboard;
            case 'networkDevices':
                return MOCK_DATA.networkDevices;
            default:
                return [];
        }
    },
    
    // 更新模拟数据
    updateData(type, id, data) {
        switch (type) {
            case 'devices':
                const deviceIndex = MOCK_DATA.devices.findIndex(d => d.id === id);
                if (deviceIndex !== -1) {
                    MOCK_DATA.devices[deviceIndex] = { ...MOCK_DATA.devices[deviceIndex], ...data };
                    return true;
                }
                return false;
            
            case 'services':
                const serviceIndex = MOCK_DATA.services.findIndex(s => s.id === id);
                if (serviceIndex !== -1) {
                    MOCK_DATA.services[serviceIndex] = { ...MOCK_DATA.services[serviceIndex], ...data };
                    return true;
                }
                return false;
            
            default:
                return false;
        }
    },
    
    // 添加模拟数据
    addData(type, data) {
        switch (type) {
            case 'devices':
                const newDevice = {
                    id: Math.max(...MOCK_DATA.devices.map(d => d.id)) + 1,
                    ...data
                };
                MOCK_DATA.devices.push(newDevice);
                return newDevice;
            
            case 'services':
                const newService = {
                    id: `service_${Date.now()}`,
                    ...data
                };
                MOCK_DATA.services.push(newService);
                return newService;
            
            default:
                return null;
        }
    },
    
    // 删除模拟数据
    deleteData(type, id) {
        switch (type) {
            case 'devices':
                const deviceIndex = MOCK_DATA.devices.findIndex(d => d.id === id);
                if (deviceIndex !== -1) {
                    MOCK_DATA.devices.splice(deviceIndex, 1);
                    return true;
                }
                return false;
            
            case 'services':
                const serviceIndex = MOCK_DATA.services.findIndex(s => s.id === id);
                if (serviceIndex !== -1) {
                    MOCK_DATA.services.splice(serviceIndex, 1);
                    return true;
                }
                return false;
            
            default:
                return false;
        }
    }
};

// 导出模拟数据服务
if (typeof module !== 'undefined' && module.exports) {
    module.exports = MOCK_DATA;
}
