#!/bin/bash

# 模拟数据提交脚本

# 构建模拟结果
result="{"
result+="\"success\": true,"
result+="\"message\": \"数据提交成功 (模拟模式)\","
result+="\"target\": \"database\","
result+="\"data\": {\"id\": 1, \"name\": \"Test\"},"
result+="\"options\": {\"mode\": \"insert\"},"
result+="\"mode\": \"simulation\","
result+="\"timestamp\": \"$(date +\"%Y-%m-%d %H:%M:%S\")\""
result+="}"

# 输出生成的结果
echo "$result"

# 退出成功
exit 0