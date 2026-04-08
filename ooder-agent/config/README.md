# ApexOS 配置说明

## 配置文件位置

- `config/application.yml` - 外部配置文件（优先级高于内置配置）
- `src/main/resources/application.yml` - 内置默认配置

## 主要配置项

### 服务端口
```yaml
server:
  port: 8086
```

### LLM 配置
```yaml
ooder:
  llm:
    provider: qianwen  # 可选: qianwen, deepseek, baidu
    qianwen:
      api-key: ${QIANWEN_API_KEY:}  # 环境变量或直接填写
      model: qwen-plus
```

### 数据存储
```yaml
os:
  datasource:
    url: jdbc:sqlite:./data/os.db
```

### 日志配置
```yaml
logging:
  file:
    name: ./logs/apexos.log
```

## 环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| QIANWEN_API_KEY | 通义千问API密钥 | 空 |
| DEEPSEEK_API_KEY | DeepSeek API密钥 | 空 |
| BAIDU_API_KEY | 百度千帆API密钥 | 空 |
| GITEE_TOKEN | Gitee访问令牌 | 空 |
| GITHUB_TOKEN | GitHub访问令牌 | 空 |

## 配置优先级

1. 外部配置文件 `config/application.yml`
2. 环境变量
3. 内置配置文件 `application.yml`
