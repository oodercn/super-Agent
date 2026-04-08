/**
 * 能力配置公共模块
 * 17种能力定义、地址映射、Profile默认配置
 */

(function(global) {
    'use strict';

    var CAPABILITY_ADDRESS_MAP = {
        'sys': '00',
        'org': '08',
        'auth': '10',
        'vfs': '20',
        'db': '28',
        'llm': '30',
        'know': '38',
        'comm': '40',
        'mon': '48',
        'payment': '50',
        'media': '58',
        'search': '60',
        'sched': '68',
        'sec': '70',
        'iot': '78',
        'net': '80',
        'util': 'F0'
    };

    var CAPABILITIES = [
        { 
            address: 'sys', name: '系统管理', icon: 'ri-settings-3-line', color: '#6366f1', 
            defaultDriver: 'skill-sys-registry', enabled: true, dbRequired: false,
            configFields: [
                { name: 'registry', label: '注册中心', type: 'text', default: 'local', required: false },
                { name: 'heartbeatInterval', label: '心跳间隔(秒)', type: 'number', default: 30, required: false }
            ]
        },
        { 
            address: 'org', name: '组织管理', icon: 'ri-team-line', color: '#8b5cf6', 
            defaultDriver: 'skill-org-local', enabled: true, dbRequired: false,
            configFields: [
                { name: 'sourceType', label: '组织源', type: 'select', options: ['local', 'dingding', 'feishu', 'wecom', 'ldap'], default: 'local', required: false, recommended: true, desc: '选择组织数据来源' },
                { name: 'defaultOrg', label: '默认组织', type: 'text', default: 'default', required: false },
                { name: 'maxMembers', label: '最大成员数', type: 'number', default: 100, required: false }
            ],
            extendedConfig: {
                dingding: [
                    { name: 'appKey', label: 'AppKey', type: 'text', default: '', required: true },
                    { name: 'appSecret', label: 'AppSecret', type: 'password', default: '', required: true },
                    { name: 'corpId', label: 'CorpId', type: 'text', default: '', required: true }
                ],
                feishu: [
                    { name: 'appId', label: 'AppId', type: 'text', default: '', required: true },
                    { name: 'appSecret', label: 'AppSecret', type: 'password', default: '', required: true }
                ],
                wecom: [
                    { name: 'corpId', label: 'CorpId', type: 'text', default: '', required: true },
                    { name: 'agentId', label: 'AgentId', type: 'text', default: '', required: true },
                    { name: 'secret', label: 'Secret', type: 'password', default: '', required: true }
                ],
                ldap: [
                    { name: 'url', label: 'LDAP地址', type: 'text', default: 'ldap://localhost:389', required: true },
                    { name: 'baseDn', label: 'Base DN', type: 'text', default: 'dc=example,dc=com', required: true },
                    { name: 'bindDn', label: 'Bind DN', type: 'text', default: '', required: true },
                    { name: 'bindPassword', label: 'Bind密码', type: 'password', default: '', required: true }
                ]
            }
        },
        { 
            address: 'auth', name: '认证授权', icon: 'ri-shield-line', color: '#10b981', 
            defaultDriver: 'skill-auth-user', enabled: true, dbRequired: false,
            configFields: [
                { name: 'authType', label: '认证方式', type: 'select', options: ['local', 'ldap', 'oauth2', 'saml'], default: 'local', required: false, recommended: true, desc: '选择认证方式' },
                { name: 'sessionTimeout', label: '会话超时(分钟)', type: 'number', default: 30, required: false },
                { name: 'maxLoginAttempts', label: '最大登录尝试', type: 'number', default: 5, required: false }
            ]
        },
        { 
            address: 'vfs', name: '虚拟文件系统', icon: 'ri-folder-line', color: '#f5970b', 
            defaultDriver: 'skill-vfs-local', enabled: true, dbRequired: false,
            configFields: [
                { name: 'storageType', label: '存储类型', type: 'select', options: ['local', 's3', 'oss', 'minio', 'database'], default: 'local', required: false, recommended: true, desc: '选择存储后端类型' },
                { name: 'basePath', label: '存储路径', type: 'text', default: './data/files', required: false, recommended: true },
                { name: 'maxFileSize', label: '最大文件大小(MB)', type: 'number', default: 100, required: false }
            ],
            extendedConfig: {
                s3: [
                    { name: 'region', label: 'Region', type: 'text', default: 'us-east-1', required: true },
                    { name: 'accessKeyId', label: 'Access Key ID', type: 'text', default: '', required: true },
                    { name: 'secretAccessKey', label: 'Secret Access Key', type: 'password', default: '', required: true },
                    { name: 'bucketName', label: 'Bucket名称', type: 'text', default: '', required: true }
                ],
                oss: [
                    { name: 'endpoint', label: 'Endpoint', type: 'text', default: 'https://oss-cn-hangzhou.aliyuncs.com', required: true },
                    { name: 'accessKeyId', label: 'Access Key ID', type: 'text', default: '', required: true },
                    { name: 'accessKeySecret', label: 'Access Key Secret', type: 'password', default: '', required: true },
                    { name: 'bucketName', label: 'Bucket名称', type: 'text', default: '', required: true }
                ],
                minio: [
                    { name: 'endpoint', label: 'Endpoint', type: 'text', default: 'http://localhost:9000', required: true },
                    { name: 'accessKey', label: 'Access Key', type: 'text', default: '', required: true },
                    { name: 'secretKey', label: 'Secret Key', type: 'password', default: '', required: true },
                    { name: 'bucketName', label: 'Bucket名称', type: 'text', default: '', required: true }
                ]
            }
        },
        { 
            address: 'db', name: '数据库', icon: 'ri-database-2-line', color: '#db2777', 
            defaultDriver: 'skill-db-mysql', enabled: true, dbRequired: true,
            configFields: [
                { name: 'dbType', label: '数据库类型', type: 'select', options: ['mysql', 'postgres', 'sqlite'], default: 'mysql', required: false, recommended: true },
                { name: 'host', label: '主机地址', type: 'text', default: 'localhost', required: true },
                { name: 'port', label: '端口', type: 'number', default: 3306, required: true },
                { name: 'database', label: '数据库名', type: 'text', default: 'ooder', required: true },
                { name: 'username', label: '用户名', type: 'text', default: 'root', required: true },
                { name: 'password', label: '密码', type: 'password', default: '', required: true },
                { name: 'maxPoolSize', label: '连接池大小', type: 'number', default: 10, required: false }
            ]
        },
        { 
            address: 'llm', name: '大语言模型', icon: 'ri-robot-line', color: '#9334ea', 
            defaultDriver: 'skill-llm-deepseek', enabled: true, dbRequired: false,
            configFields: [
                { name: 'provider', label: '提供商', type: 'select', options: ['deepseek', 'openai', 'ollama', 'qianwen', 'azure'], default: 'deepseek', required: false, recommended: true, desc: '选择LLM提供商' },
                { name: 'model', label: '模型名称', type: 'text', default: 'deepseek-chat', required: false, recommended: true },
                { name: 'apiKey', label: 'API Key', type: 'password', default: '', required: true, desc: 'API密钥(ollama本地可留空)' },
                { name: 'baseUrl', label: 'API地址', type: 'text', default: '', required: false },
                { name: 'maxTokens', label: '最大Token数', type: 'number', default: 4096, required: false },
                { name: 'temperature', label: '温度参数', type: 'number', default: 0.7, required: false }
            ]
        },
        { 
            address: 'know', name: '知识库', icon: 'ri-book-line', color: '#f5970b', 
            defaultDriver: 'skill-know-rag', enabled: false, dbRequired: true,
            configFields: [
                { name: 'vectorDb', label: '向量数据库', type: 'select', options: ['milvus', 'pinecone', 'chroma', 'local'], default: 'milvus', required: false, recommended: true },
                { name: 'embeddingModel', label: '嵌入模型', type: 'text', default: 'text-embedding-ada-002', required: false },
                { name: 'topK', label: '检索数量', type: 'number', default: 5, required: false },
                { name: 'scoreThreshold', label: '相似度阈值', type: 'number', default: 0.7, required: false }
            ]
        },
        { 
            address: 'comm', name: '消息通信', icon: 'ri-message-3-line', color: '#f97b72', 
            defaultDriver: 'skill-comm-notify', enabled: true, dbRequired: false,
            configFields: [
                { name: 'channels', label: '通知渠道', type: 'select', options: ['console', 'email', 'sms', 'webhook', 'dingding', 'wechat'], default: 'console', required: false, recommended: true, desc: '选择通知渠道' },
                { name: 'retryCount', label: '重试次数', type: 'number', default: 3, required: false }
            ],
            extendedConfig: {
                email: [
                    { name: 'smtpHost', label: 'SMTP主机', type: 'text', default: 'smtp.example.com', required: true },
                    { name: 'smtpPort', label: 'SMTP端口', type: 'number', default: 587, required: true },
                    { name: 'smtpUser', label: 'SMTP用户名', type: 'text', default: '', required: true },
                    { name: 'smtpPassword', label: 'SMTP密码', type: 'password', default: '', required: true },
                    { name: 'fromAddress', label: '发件人地址', type: 'text', default: '', required: false }
                ],
                dingding: [
                    { name: 'webhook', label: 'Webhook地址', type: 'text', default: '', required: true },
                    { name: 'secret', label: '加签密钥', type: 'password', default: '', required: false }
                ],
                webhook: [
                    { name: 'webhookUrl', label: 'Webhook URL', type: 'text', default: '', required: true }
                ]
            }
        },
        { 
            address: 'mon', name: '系统监控', icon: 'ri-line-chart-line', color: '#10b981', 
            defaultDriver: 'skill-mon-health', enabled: true, dbRequired: false,
            configFields: [
                { name: 'checkInterval', label: '检查间隔(秒)', type: 'number', default: 60, required: false },
                { name: 'alertThreshold', label: '告警阈值(%)', type: 'number', default: 80, required: false }
            ]
        },
        { 
            address: 'payment', name: '支付功能', icon: 'ri-bank-card-line', color: '#8b5cf6', 
            defaultDriver: null, enabled: false, dbRequired: false,
            configFields: [
                { name: 'provider', label: '支付提供商', type: 'select', options: ['alipay', 'wechat', 'stripe'], default: 'alipay', required: false },
                { name: 'merchantId', label: '商户ID', type: 'text', default: '', required: false }
            ]
        },
        { 
            address: 'media', name: '媒体处理', icon: 'ri-image-line', color: '#f5970b', 
            defaultDriver: null, enabled: false, dbRequired: false,
            configFields: [
                { name: 'maxWidth', label: '最大宽度', type: 'number', default: 1920, required: false },
                { name: 'maxHeight', label: '最大高度', type: 'number', default: 1080, required: false },
                { name: 'quality', label: '压缩质量(%)', type: 'number', default: 85, required: false }
            ]
        },
        { 
            address: 'search', name: '搜索引擎', icon: 'ri-search-line', color: '#f97b72', 
            defaultDriver: 'skill-search-es', enabled: true, dbRequired: true,
            configFields: [
                { name: 'engine', label: '搜索引擎', type: 'select', options: ['elasticsearch', 'solr', 'meilisearch', 'local'], default: 'elasticsearch', required: false, recommended: true },
                { name: 'host', label: '主机地址', type: 'text', default: 'localhost', required: false },
                { name: 'port', label: '端口', type: 'number', default: 9200, required: false },
                { name: 'indexPrefix', label: '索引前缀', type: 'text', default: 'ooder', required: false }
            ]
        },
        { 
            address: 'sched', name: '任务调度', icon: 'ri-calendar-line', color: '#f5970b', 
            defaultDriver: 'skill-sched-quartz', enabled: true, dbRequired: false,
            configFields: [
                { name: 'poolSize', label: '线程池大小', type: 'number', default: 10, required: false },
                { name: 'misfireThreshold', label: '错过阈值(毫秒)', type: 'number', default: 60000, required: false }
            ]
        },
        { 
            address: 'sec', name: '安全防护', icon: 'ri-lock-line', color: '#db2777', 
            defaultDriver: 'skill-sec-access', enabled: true, dbRequired: false,
            configFields: [
                { name: 'encryption', label: '加密算法', type: 'select', options: ['AES', 'RSA', 'DES'], default: 'AES', required: false },
                { name: 'keySize', label: '密钥长度', type: 'number', default: 256, required: false }
            ]
        },
        { 
            address: 'iot', name: '物联网', icon: 'ri-cpu-line', color: '#8b5cf6', 
            defaultDriver: null, enabled: false, dbRequired: false,
            configFields: [
                { name: 'protocol', label: '通信协议', type: 'select', options: ['mqtt', 'coap', 'http'], default: 'mqtt', required: false },
                { name: 'broker', label: 'Broker地址', type: 'text', default: 'localhost:1883', required: false }
            ]
        },
        { 
            address: 'net', name: '网络代理', icon: 'ri-global-line', color: '#10b981', 
            defaultDriver: 'skill-net-proxy', enabled: true, dbRequired: false,
            configFields: [
                { name: 'proxyType', label: '代理类型', type: 'select', options: ['http', 'https', 'socks5', 'none'], default: 'none', required: false, recommended: true },
                { name: 'proxyHost', label: '代理主机', type: 'text', default: '', required: false },
                { name: 'proxyPort', label: '代理端口', type: 'number', default: 0, required: false },
                { name: 'timeout', label: '超时时间(秒)', type: 'number', default: 30, required: false }
            ]
        },
        { 
            address: 'util', name: '工具类', icon: 'ri-tools-line', color: '#4f46e5', 
            defaultDriver: 'skill-util-report', enabled: true, dbRequired: false,
            configFields: [
                { name: 'reportFormat', label: '报表格式', type: 'select', options: ['pdf', 'excel', 'html'], default: 'pdf', required: false },
                { name: 'tempDir', label: '临时目录', type: 'text', default: './temp', required: false }
            ]
        }
    ];

    var CATEGORY_CONFIG = {};
    
    var USER_FACING_CATEGORIES = [];

    function initCategoryConfig() {
        if (typeof CategoryService === 'undefined') {
            console.warn('CategoryService 未加载，使用默认分类配置');
            var defaultCategories = [
                { code: 'org', name: '组织服务', icon: 'ri-team-line', color: '#8b5cf6', userFacing: true },
                { code: 'vfs', name: '存储服务', icon: 'ri-database-2-line', color: '#f5970b', userFacing: true },
                { code: 'llm', name: 'LLM服务', icon: 'ri-brain-line', color: '#9334ff', userFacing: true },
                { code: 'knowledge', name: '知识服务', icon: 'ri-book-line', color: '#10b981', userFacing: true },
                { code: 'biz', name: '业务场景', icon: 'ri-briefcase-line', color: '#f97316', userFacing: true },
                { code: 'sys', name: '系统管理', icon: 'ri-settings-3-line', color: '#6366f1', userFacing: false },
                { code: 'msg', name: '消息通讯', icon: 'ri-message-3-line', color: '#f97b72', userFacing: true },
                { code: 'ui', name: 'UI生成', icon: 'ri-palette-line', color: '#ec4899', userFacing: true },
                { code: 'payment', name: '支付服务', icon: 'ri-bank-card-line', color: '#8b5cf6', userFacing: true },
                { code: 'media', name: '媒体发布', icon: 'ri-edit-line', color: '#f5970b', userFacing: true },
                { code: 'util', name: '工具服务', icon: 'ri-tools-line', color: '#4f46e5', userFacing: true },
                { code: 'nexus-ui', name: 'Nexus界面', icon: 'ri-layout-line', color: '#6366f1', userFacing: false }
            ];
            defaultCategories.forEach(function(cat) {
                CATEGORY_CONFIG[cat.code] = {
                    name: cat.name,
                    icon: cat.icon,
                    color: cat.color,
                    desc: '',
                    userFacing: cat.userFacing
                };
                if (cat.userFacing) {
                    USER_FACING_CATEGORIES.push(cat.code);
                }
            });
            return Promise.resolve();
        }
        return CategoryService.loadCategories().then(function(categories) {
            categories.forEach(function(cat) {
                CATEGORY_CONFIG[cat.code] = {
                    name: cat.name,
                    icon: cat.icon,
                    color: cat.color,
                    desc: cat.desc || '',
                    userFacing: cat.userFacing
                };
                if (cat.userFacing) {
                    USER_FACING_CATEGORIES.push(cat.code);
                }
            });
        });
    }
    
    var BIZ_SUBCATEGORIES = [
        { id: 'hr', name: '人力资源', icon: 'ri-user-add-line', desc: '招聘、入职、培训、绩效管理' },
        { id: 'crm', name: '客户管理', icon: 'ri-contacts-line', desc: '客户信息、跟进、转化管理' },
        { id: 'finance', name: '财务管理', icon: 'ri-money-cny-box-line', desc: '报销、预算、财务审批' },
        { id: 'approval', name: '审批流程', icon: 'ri-checkbox-line', desc: '各类审批流程自动化' },
        { id: 'project', name: '项目协作', icon: 'ri-folder-line', desc: '项目管理、任务分配、进度跟踪' },
        { id: 'worklog', name: '工作日志', icon: 'ri-file-text-line', desc: '日志汇报、提醒、汇总分析' },
        { id: 'qa', name: '质检管理', icon: 'ri-clipboard-check-line', desc: '质量检查、问题追踪' },
        { id: 'scenario', name: '通用业务', icon: 'ri-briefcase-line', desc: '通用业务场景能力' }
    ];

    var PROFILE_DEFAULTS = {
        micro: {
            llm: { provider: 'ollama', model: 'llama3', maxTokens: 2048, apiKey: '' },
            db: { dbType: 'mysql', maxPoolSize: 5 },
            vfs: { storageType: 'local', maxFileSize: 50 },
            know: { enabled: false }
        },
        small: {
            llm: { provider: 'deepseek', model: 'deepseek-chat', maxTokens: 4096 },
            db: { dbType: 'mysql', maxPoolSize: 10 },
            vfs: { storageType: 'local', maxFileSize: 100 },
            know: { enabled: true, topK: 5 }
        },
        large: {
            llm: { provider: 'deepseek', model: 'deepseek-chat', maxTokens: 8192 },
            db: { dbType: 'postgres', maxPoolSize: 30 },
            vfs: { storageType: 's3', maxFileSize: 500 },
            know: { enabled: true, vectorDb: 'milvus', topK: 10 }
        },
        enterprise: {
            llm: { provider: 'openai', model: 'gpt-4', maxTokens: 8192 },
            db: { dbType: 'postgres', maxPoolSize: 50 },
            vfs: { storageType: 's3', maxFileSize: 1024 },
            know: { enabled: true, vectorDb: 'milvus', topK: 20 }
        }
    };

    var PROFILE_CONFIG_STEPS = {
        micro: [
            { 
                step: 1, 
                title: '基础配置', 
                description: '配置核心必需项',
                fields: ['llm.apiKey', 'db.host', 'db.port', 'db.database', 'db.username', 'db.password'],
                required: true
            },
            { 
                step: 2, 
                title: '快速确认', 
                description: '确认默认配置',
                fields: ['vfs.basePath', 'vfs.storageType'],
                required: false
            },
            { 
                step: 3, 
                title: '完成', 
                description: '保存配置',
                fields: [],
                required: false
            }
        ],
        small: [
            { 
                step: 1, 
                title: '核心配置', 
                description: '配置LLM和数据库',
                fields: ['llm.*', 'db.*'],
                required: true
            },
            { 
                step: 2, 
                title: '团队集成', 
                description: '配置组织和消息通知',
                fields: ['org.*', 'comm.*'],
                required: false,
                recommended: true
            },
            { 
                step: 3, 
                title: '知识库', 
                description: '配置知识库功能',
                fields: ['know.*'],
                required: false
            },
            { 
                step: 4, 
                title: '完成', 
                description: '保存配置',
                fields: [],
                required: false
            }
        ],
        large: [
            { 
                step: 1, 
                title: '核心配置', 
                description: '配置LLM和数据库',
                fields: ['llm.*', 'db.*'],
                required: true
            },
            { 
                step: 2, 
                title: '存储配置', 
                description: '配置文件存储和向量库',
                fields: ['vfs.*', 'know.vectorDb', 'know.topK'],
                required: true
            },
            { 
                step: 3, 
                title: '企业集成', 
                description: '配置组织和认证',
                fields: ['org.*', 'auth.*'],
                required: false,
                recommended: true
            },
            { 
                step: 4, 
                title: '消息通知', 
                description: '配置通知渠道',
                fields: ['comm.*'],
                required: false,
                recommended: true
            },
            { 
                step: 5, 
                title: '安全监控', 
                description: '配置安全和监控',
                fields: ['sec.*', 'mon.*'],
                required: false
            },
            { 
                step: 6, 
                title: '完成', 
                description: '保存配置',
                fields: [],
                required: false
            }
        ],
        enterprise: [
            { 
                step: 1, 
                title: '核心配置', 
                description: '配置LLM和数据库',
                fields: ['llm.*', 'db.*'],
                required: true
            },
            { 
                step: 2, 
                title: '存储配置', 
                description: '配置文件存储和向量库',
                fields: ['vfs.*', 'know.*'],
                required: true
            },
            { 
                step: 3, 
                title: '企业集成', 
                description: '配置组织和认证',
                fields: ['org.*', 'auth.*'],
                required: true
            },
            { 
                step: 4, 
                title: '消息通知', 
                description: '配置多渠道通知',
                fields: ['comm.*'],
                required: true
            },
            { 
                step: 5, 
                title: '安全监控', 
                description: '配置安全和监控',
                fields: ['sec.*', 'mon.*', 'search.*'],
                required: false
            },
            { 
                step: 6, 
                title: '完成', 
                description: '保存配置',
                fields: [],
                required: false
            }
        ]
    };

    var REQUIRED_FIELDS_BY_PROFILE = {
        micro: ['db.host', 'db.port', 'db.database', 'db.username', 'db.password'],
        small: ['llm.apiKey', 'db.host', 'db.port', 'db.database', 'db.username', 'db.password'],
        large: ['llm.apiKey', 'db.host', 'db.port', 'db.database', 'db.username', 'db.password'],
        enterprise: ['llm.apiKey', 'db.host', 'db.port', 'db.database', 'db.username', 'db.password']
    };

    var RECOMMENDED_FIELDS_BY_PROFILE = {
        micro: ['vfs.basePath'],
        small: ['org.sourceType', 'comm.channels', 'know.enabled'],
        large: ['org.sourceType', 'auth.authType', 'comm.channels', 'vfs.storageType', 'know.vectorDb'],
        enterprise: ['org.sourceType', 'auth.authType', 'comm.channels', 'vfs.storageType', 'know.vectorDb', 'search.engine']
    };

    function getAddressHex(address) {
        return CAPABILITY_ADDRESS_MAP[address] || 'FF';
    }

    function getCapabilityByAddress(address) {
        for (var i = 0; i < CAPABILITIES.length; i++) {
            if (CAPABILITIES[i].address === address) {
                return CAPABILITIES[i];
            }
        }
        return null;
    }

    function getCategoryConfig(address) {
        var config = CATEGORY_CONFIG[address];
        if (config) return config;
        return CategoryService.getInfo(address);
    }

    function getProfileSteps(profile) {
        return PROFILE_CONFIG_STEPS[profile] || PROFILE_CONFIG_STEPS.micro;
    }

    function getRequiredFields(profile) {
        return REQUIRED_FIELDS_BY_PROFILE[profile] || REQUIRED_FIELDS_BY_PROFILE.micro;
    }

    function getRecommendedFields(profile) {
        return RECOMMENDED_FIELDS_BY_PROFILE[profile] || RECOMMENDED_FIELDS_BY_PROFILE.micro;
    }

    function getDbRequiredCapabilities() {
        return CAPABILITIES.filter(function(cap) {
            return cap.dbRequired === true;
        }).map(function(cap) {
            return cap.address;
        });
    }

    function getEnabledCapabilities(profile) {
        var defaults = PROFILE_DEFAULTS[profile] || PROFILE_DEFAULTS.micro;
        return CAPABILITIES.filter(function(cap) {
            if (defaults[cap.address] && defaults[cap.address].enabled === false) {
                return false;
            }
            return cap.enabled;
        }).map(function(cap) {
            return cap.address;
        });
    }

    initCategoryConfig();

    global.OoderCapability = {
        CAPABILITIES: CAPABILITIES,
        CATEGORY_CONFIG: CATEGORY_CONFIG,
        PROFILE_DEFAULTS: PROFILE_DEFAULTS,
        PROFILE_CONFIG_STEPS: PROFILE_CONFIG_STEPS,
        REQUIRED_FIELDS_BY_PROFILE: REQUIRED_FIELDS_BY_PROFILE,
        RECOMMENDED_FIELDS_BY_PROFILE: RECOMMENDED_FIELDS_BY_PROFILE,
        ADDRESS_MAP: CAPABILITY_ADDRESS_MAP,
        getAddressHex: getAddressHex,
        getCapabilityByAddress: getCapabilityByAddress,
        getCategoryConfig: getCategoryConfig,
        getProfileSteps: getProfileSteps,
        getRequiredFields: getRequiredFields,
        getRecommendedFields: getRecommendedFields,
        getDbRequiredCapabilities: getDbRequiredCapabilities,
        getEnabledCapabilities: getEnabledCapabilities
    };

})(typeof window !== 'undefined' ? window : this);
