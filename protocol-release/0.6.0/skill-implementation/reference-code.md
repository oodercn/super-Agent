# Skill 协议栈参考代码

## 1. 文档概述

本文档提供了 Ooder AI Bridge 协议 v0.6.0 中 Skill 协议栈的参考实现代码，使用 Python + FastAPI 实现，展示了如何开发一个符合协议规范的 Skill 服务。

## 2. 项目结构

```
ooder-skill-example/
├── app/
│   ├── __init__.py
│   ├── main.py              # 入口文件
│   ├── api/
│   │   ├── __init__.py
│   │   ├── skill.py         # Skill 相关 API
│   │   ├── capability.py    # Capability 相关 API
│   │   └── command.py       # 命令执行 API
│   ├── models/
│   │   ├── __init__.py
│   │   ├── skill.py         # Skill 数据模型
│   │   ├── capability.py    # Capability 数据模型
│   │   └── command.py       # 命令执行模型
│   ├── services/
│   │   ├── __init__.py
│   │   ├── skill_service.py # Skill 服务
│   │   ├── capability_service.py # Capability 服务
│   │   └── command_service.py # 命令执行服务
│   ├── repository/
│   │   ├── __init__.py
│   │   ├── skill_repo.py    # Skill 数据访问
│   │   └── capability_repo.py # Capability 数据访问
│   ├── middleware/
│   │   ├── __init__.py
│   │   ├── auth.py          # 认证中间件
│   │   └── logging.py       # 日志中间件
│   ├── utils/
│   │   ├── __init__.py
│   │   ├── jwt.py           # JWT 工具
│   │   └── validation.py    # 验证工具
│   └── config/
│       ├── __init__.py
│       └── settings.py      # 配置管理
├── tests/
│   ├── __init__.py
│   ├── test_skill.py
│   └── test_capability.py
├── requirements.txt
├── Dockerfile
├── docker-compose.yml
└── README.md
```

## 3. 核心代码实现

### 3.1 入口文件 (main.py)

```python
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api import skill, capability, command
from app.middleware import logging
from app.config import settings

app = FastAPI(
    title="Ooder Skill Example",
    version="0.6.0",
    description="Ooder AI Bridge Protocol Skill Example"
)

# CORS 配置
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 注册中间件
app.add_middleware(logging.LoggingMiddleware)

# 注册路由
app.include_router(skill.router, prefix="/api/v1/skills", tags=["skills"])
app.include_router(capability.router, prefix="/api/v1/capabilities", tags=["capabilities"])
app.include_router(command.router, prefix="/api/v1/commands", tags=["commands"])

# 健康检查端点
@app.get("/health")
def health_check():
    return {
        "status": "healthy",
        "version": settings.app["version"],
        "timestamp": "2026-01-18T00:00:00Z"
    }
```

### 3.2 数据模型 (models/skill.py)

```python
from typing import List, Optional
from pydantic import BaseModel, Field
from datetime import datetime

class Endpoint(BaseModel):
    url: str = Field(..., description="Endpoint URL")
    protocol: str = Field(..., description="Protocol type: http, https, ws, wss")
    weight: int = Field(1, description="Load balancing weight")

class SkillBase(BaseModel):
    name: str = Field(..., description="Skill name")
    description: Optional[str] = Field(None, description="Skill description")
    version: str = Field(..., description="Skill version")
    endpoints: List[Endpoint] = Field(..., description="Skill endpoints")

class SkillCreate(SkillBase):
    pass

class SkillUpdate(BaseModel):
    name: Optional[str] = Field(None, description="Skill name")
    description: Optional[str] = Field(None, description="Skill description")
    status: Optional[str] = Field(None, description="Skill status")

class Skill(SkillBase):
    skill_id: str = Field(..., description="Skill unique identifier")
    status: str = Field(..., description="Skill status: active, inactive, maintenance")
    created_at: datetime = Field(..., description="Creation time")
    updated_at: datetime = Field(..., description="Update time")

    class Config:
        orm_mode = True
```

### 3.3 API 路由 (api/skill.py)

```python
from fastapi import APIRouter, Depends, HTTPException, status
from typing import List
from app.models.skill import Skill, SkillCreate, SkillUpdate
from app.services.skill_service import SkillService
from app.middleware.auth import get_current_user

router = APIRouter()

@router.get("/", response_model=List[Skill])
def get_skills(
    skip: int = 0,
    limit: int = 100,
    skill_service: SkillService = Depends(SkillService),
    current_user: str = Depends(get_current_user)
):
    """获取所有 Skill"""
    return skill_service.get_skills(skip=skip, limit=limit)

@router.get("/{skill_id}", response_model=Skill)
def get_skill(
    skill_id: str,
    skill_service: SkillService = Depends(SkillService),
    current_user: str = Depends(get_current_user)
):
    """根据 ID 获取 Skill"""
    skill = skill_service.get_skill(skill_id)
    if not skill:
        raise HTTPException(status_code=404, detail="Skill not found")
    return skill

@router.post("/", response_model=Skill, status_code=status.HTTP_201_CREATED)
def create_skill(
    skill: SkillCreate,
    skill_service: SkillService = Depends(SkillService),
    current_user: str = Depends(get_current_user)
):
    """创建新 Skill"""
    return skill_service.create_skill(skill)

@router.put("/{skill_id}", response_model=Skill)
def update_skill(
    skill_id: str,
    skill_update: SkillUpdate,
    skill_service: SkillService = Depends(SkillService),
    current_user: str = Depends(get_current_user)
):
    """更新 Skill"""
    skill = skill_service.update_skill(skill_id, skill_update)
    if not skill:
        raise HTTPException(status_code=404, detail="Skill not found")
    return skill

@router.delete("/{skill_id}", status_code=status.HTTP_204_NO_CONTENT)
def delete_skill(
    skill_id: str,
    skill_service: SkillService = Depends(SkillService),
    current_user: str = Depends(get_current_user)
):
    """删除 Skill"""
    if not skill_service.delete_skill(skill_id):
        raise HTTPException(status_code=404, detail="Skill not found")
    return None
```

### 3.4 服务层 (services/skill_service.py)

```python
from typing import List, Optional
from app.models.skill import Skill, SkillCreate, SkillUpdate
from app.repository.skill_repo import SkillRepository
from uuid import uuid4
from datetime import datetime

class SkillService:
    def __init__(self, skill_repo: SkillRepository = Depends(SkillRepository)):
        self.skill_repo = skill_repo

    def get_skills(self, skip: int = 0, limit: int = 100) -> List[Skill]:
        """获取所有 Skill"""
        return self.skill_repo.get_all(skip=skip, limit=limit)

    def get_skill(self, skill_id: str) -> Optional[Skill]:
        """根据 ID 获取 Skill"""
        return self.skill_repo.get_by_id(skill_id)

    def create_skill(self, skill_create: SkillCreate) -> Skill:
        """创建新 Skill"""
        skill_data = skill_create.dict()
        skill_data["skill_id"] = str(uuid4())
        skill_data["status"] = "active"
        skill_data["created_at"] = datetime.utcnow()
        skill_data["updated_at"] = datetime.utcnow()
        return self.skill_repo.create(skill_data)

    def update_skill(self, skill_id: str, skill_update: SkillUpdate) -> Optional[Skill]:
        """更新 Skill"""
        skill = self.skill_repo.get_by_id(skill_id)
        if not skill:
            return None
        
        update_data = skill_update.dict(exclude_unset=True)
        update_data["updated_at"] = datetime.utcnow()
        return self.skill_repo.update(skill_id, update_data)

    def delete_skill(self, skill_id: str) -> bool:
        """删除 Skill"""
        return self.skill_repo.delete(skill_id)
```

### 3.5 数据访问层 (repository/skill_repo.py)

```python
from typing import List, Optional, Dict, Any
from sqlalchemy.orm import Session
from app.models.skill import Skill
from app.database import get_db

class SkillRepository:
    def __init__(self, db: Session = Depends(get_db)):
        self.db = db

    def get_all(self, skip: int = 0, limit: int = 100) -> List[Skill]:
        """获取所有 Skill"""
        return self.db.query(Skill).offset(skip).limit(limit).all()

    def get_by_id(self, skill_id: str) -> Optional[Skill]:
        """根据 ID 获取 Skill"""
        return self.db.query(Skill).filter(Skill.skill_id == skill_id).first()

    def get_by_name(self, name: str) -> Optional[Skill]:
        """根据名称获取 Skill"""
        return self.db.query(Skill).filter(Skill.name == name).first()

    def create(self, skill_data: Dict[str, Any]) -> Skill:
        """创建新 Skill"""
        db_skill = Skill(**skill_data)
        self.db.add(db_skill)
        self.db.commit()
        self.db.refresh(db_skill)
        return db_skill

    def update(self, skill_id: str, update_data: Dict[str, Any]) -> Optional[Skill]:
        """更新 Skill"""
        db_skill = self.get_by_id(skill_id)
        if not db_skill:
            return None
        
        for field, value in update_data.items():
            setattr(db_skill, field, value)
        
        self.db.commit()
        self.db.refresh(db_skill)
        return db_skill

    def delete(self, skill_id: str) -> bool:
        """删除 Skill"""
        db_skill = self.get_by_id(skill_id)
        if not db_skill:
            return False
        
        self.db.delete(db_skill)
        self.db.commit()
        return True
```

### 3.6 命令执行 (api/command.py)

```python
from fastapi import APIRouter, Depends, HTTPException
from typing import Dict, Any
from app.models.command import CommandExecute, CommandResult
from app.services.command_service import CommandService
from app.middleware.auth import get_current_user

router = APIRouter()

@router.post("/execute", response_model=CommandResult)
def execute_command(
    command: CommandExecute,
    command_service: CommandService = Depends(CommandService),
    current_user: str = Depends(get_current_user)
):
    """执行命令"""
    try:
        result = command_service.execute(command)
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
```

### 3.7 认证中间件 (middleware/auth.py)

```python
from fastapi import Depends, HTTPException, status
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from jose import JWTError, jwt
from app.config import settings

security = HTTPBearer()

def get_current_user(credentials: HTTPAuthorizationCredentials = Depends(security)):
    """获取当前用户"""
    token = credentials.credentials
    try:
        payload = jwt.decode(
            token,
            settings.security["jwt_secret"],
            algorithms=["HS256"]
        )
        user_id: str = payload.get("sub")
        if user_id is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Could not validate credentials"
            )
        return user_id
    except JWTError:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Could not validate credentials"
        )
```

### 3.8 配置管理 (config/settings.py)

```python
from pydantic import BaseSettings
from typing import Dict, Any

class Settings(BaseSettings):
    app: Dict[str, Any] = {
        "name": "ooder-skill-example",
        "version": "0.6.0",
        "host": "0.0.0.0",
        "port": 8000,
        "debug": False
    }
    
    database: Dict[str, Any] = {
        "url": "postgresql://user:password@localhost:5432/skill_db",
        "pool_size": 10,
        "max_overflow": 20
    }
    
    security: Dict[str, Any] = {
        "jwt_secret": "your-secret-key-change-in-production",
        "jwt_expiry": 3600,
        "algorithm": "HS256"
    }
    
    ai_bridge: Dict[str, Any] = {
        "protocol_version": "0.6.0",
        "registry_url": "http://localhost:8080/api/v1/skills/register",
        "heartbeat_interval": 30
    }
    
    class Config:
        env_file = ".env"
        env_file_encoding = "utf-8"

settings = Settings()
```

## 4. 依赖文件 (requirements.txt)

```
fastapi==0.89.1
uvicorn==0.20.0
pydantic==1.10.4
sqlalchemy==1.4.46
psycopg2-binary==2.9.5
python-jose[cryptography]==3.3.0
python-multipart==0.0.5
passlib[bcrypt]==1.7.4
gunicorn==20.1.0
uvloop==0.17.0
httpx==0.23.3
pytest==7.2.1
requests==2.28.2
```

## 5. Docker 配置 (Dockerfile)

```dockerfile
FROM python:3.9-slim

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]
```

## 6. Docker Compose 配置 (docker-compose.yml)

```yaml
version: "3.8"
services:
  skill-service:
    build: .
    ports:
      - "8000:8000"
    environment:
      - APP_NAME=ooder-skill-example
      - APP_VERSION=0.6.0
      - DATABASE_URL=postgresql://user:password@db:5432/skill_db
      - SECURITY_JWT_SECRET=your-secret-key-change-in-production
    depends_on:
      - db
    volumes:
      - .:/app
  
  db:
    image: postgres:14
    environment:
      - POSTGRES_USER=user
      - POSTGRES_PASSWORD=password
      - POSTGRES_DB=skill_db
    volumes:
      - postgres_data:/var/lib/postgresql/data
  
  pgadmin:
    image: dpage/pgadmin4:6.18
    environment:
      - PGADMIN_DEFAULT_EMAIL=admin@example.com
      - PGADMIN_DEFAULT_PASSWORD=admin
    ports:
      - "5050:80"
    depends_on:
      - db

volumes:
  postgres_data:
```

## 7. 启动与测试

### 7.1 本地启动

```bash
# 安装依赖
pip install -r requirements.txt

# 启动服务
uvicorn app.main:app --reload
```

### 7.2 Docker 启动

```bash
# 构建镜像并启动服务
docker-compose up --build

# 查看日志
docker-compose logs -f
```

### 7.3 API 测试

```bash
# 使用 curl 测试健康检查
curl http://localhost:8000/health

# 使用 curl 测试获取所有 Skill
curl -X GET http://localhost:8000/api/v1/skills/

# 使用 curl 测试创建 Skill
curl -X POST http://localhost:8000/api/v1/skills/ \
  -H "Content-Type: application/json" \
  -d '{"name": "weather-skill", "description": "Weather forecast skill", "version": "1.0.0", "endpoints": [{"url": "http://localhost:8001", "protocol": "http", "weight": 1}]}'
```

## 8. 测试用例 (tests/test_skill.py)

```python
import pytest
from fastapi.testclient import TestClient
from app.main import app
from app.services.skill_service import SkillService
from app.repository.skill_repo import SkillRepository

client = TestClient(app)

@pytest.fixture
def test_skill():
    return {
        "name": "test-skill",
        "description": "Test skill",
        "version": "1.0.0",
        "endpoints": [
            {"url": "http://localhost:8001", "protocol": "http", "weight": 1}
        ]
    }

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json()["status"] == "healthy"

def test_create_skill(test_skill):
    response = client.post("/api/v1/skills/", json=test_skill)
    assert response.status_code == 201
    data = response.json()
    assert data["name"] == test_skill["name"]
    assert data["version"] == test_skill["version"]
    assert "skill_id" in data

def test_get_skills(test_skill):
    # 先创建一个 Skill
    client.post("/api/v1/skills/", json=test_skill)
    
    # 再获取所有 Skill
    response = client.get("/api/v1/skills/")
    assert response.status_code == 200
    data = response.json()
    assert isinstance(data, list)
    assert len(data) > 0
```

## 9. 运行测试

```bash
# 运行所有测试
pytest

# 运行指定测试文件
pytest tests/test_skill.py

# 运行测试并显示覆盖率
pytest --cov=app tests/
```

---

**Ooder Technology Co., Ltd.**
