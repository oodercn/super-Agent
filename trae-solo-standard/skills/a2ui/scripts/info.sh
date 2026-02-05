#!/bin/bash

# 获取技能信息脚本

# 加载通用工具函数
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
if [[ -f "$SCRIPT_DIR/common.sh" ]]; then
    source "$SCRIPT_DIR/common.sh"
else
    echo '{"success": false, "error": "找不到 common.sh 脚本"}'
    exit 1
fi

# 主函数
main() {
    # 检查依赖项
    check_dependencies
    
    log "info" "获取技能信息"
    
    # 构建技能信息
    local skill_info=$(build_json 
        "name" "A2UI"
        "version" "0.2.0"
        "description" "提供图生代码功能，采用ooder-a2ui格式生成前端界面"
        "author" "OODER Team"
        "tags" '["ui", "frontend", "code-generation", "ai", "a2ui"]'
        "category" "UI_SKILL"
        "tools" '[{"name": "a2ui-api", "description": "通过HTTP API调用A2UI服务", "type": "http", "endpoint": "http://localhost:8081/api/a2ui"}]'
        "resources" '[{"name": "SKILL.md", "type": "documentation", "path": "SKILL.md"}, {"name": "scripts", "type": "directory", "path": "scripts"}, {"name": "references", "type": "directory", "path": "references"}]'
        "functions" '[{"name": "generate-ui", "description": "生成 UI 界面", "params": [{"name": "image", "type": "string", "description": "图片URL或base64编码"}, {"name": "format", "type": "string", "description": "输出格式(html/js/json)"}, {"name": "theme", "type": "string", "description": "主题(light/dark/purple)"}]}, {"name": "preview-ui", "description": "预览 UI 界面", "params": [{"name": "uiCode", "type": "string", "description": "UI代码"}, {"name": "format", "type": "string", "description": "代码格式(html/js)"}]}, {"name": "get-components", "description": "获取组件列表", "params": []}, {"name": "info", "description": "获取技能信息", "params": []}]'
        "apiEndpoints" '[{"name": "生成 UI 代码", "method": "POST", "endpoint": "http://localhost:8081/api/a2ui/generate-ui"}, {"name": "预览 UI 界面", "method": "POST", "endpoint": "http://localhost:8081/api/a2ui/preview-ui"}, {"name": "获取组件列表", "method": "GET", "endpoint": "http://localhost:8081/api/a2ui/components"}, {"name": "健康检查", "method": "GET", "endpoint": "http://localhost:8081/api/a2ui/health"}]'
        "compatibility" "trae-solo IDE"
        "standardCompliant" true
        "advantages" '["节省 Token", "标准化", "高效执行", "可扩展性", "安全可靠", "trae-solo IDE 优化", "多格式支持", "多主题支持"]'
        "limitations" '["网络依赖", "服务可用性", "参数大小", "执行时间"]'
    )
    
    # 构建响应
    local metadata=$(build_json 
        "function" "info" 
        "executionTime" "0" 
        "skillId" "a2ui"
        "timestamp" "$(date +%s)"
    )
    
    local response=$(build_response "true" "$skill_info" "" "$metadata")
    echo "$response"
}

# 执行主函数
main "$@"
