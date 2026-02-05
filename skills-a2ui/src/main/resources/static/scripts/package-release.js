#!/usr/bin/env node
/**
 * 发布版本打包脚本
 * 
 * 将 dist/release/ 目录打包为 ZIP 文件，便于分发
 */

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const ROOT_DIR = path.resolve(__dirname, '..');
const RELEASE_DIR = path.join(ROOT_DIR, 'dist', 'release');
const OUTPUT_DIR = path.join(ROOT_DIR, 'dist');
const VERSION = require('../package.json').version;
const ZIP_NAME = `ood-ui-v${VERSION}.zip`;
const ZIP_PATH = path.join(OUTPUT_DIR, ZIP_NAME);

console.log('🚀 开始打包发布版本...');
console.log(`📁 发布目录: ${RELEASE_DIR}`);
console.log(`📦 输出文件: ${ZIP_PATH}`);
console.log(`🏷️  版本: v${VERSION}`);

// 检查发布目录是否存在
if (!fs.existsSync(RELEASE_DIR)) {
  console.error('❌ 发布目录不存在，请先运行构建脚本');
  process.exit(1);
}

// 获取文件列表
const files = [];
function collectFiles(dir, baseDir = RELEASE_DIR) {
  const items = fs.readdirSync(dir);
  
  for (const item of items) {
    const fullPath = path.join(dir, item);
    const relativePath = path.relative(baseDir, fullPath);
    const stat = fs.statSync(fullPath);
    
    if (stat.isDirectory()) {
      collectFiles(fullPath, baseDir);
    } else {
      files.push(relativePath);
    }
  }
}

collectFiles(RELEASE_DIR);

console.log(`📄 包含 ${files.length} 个文件`);

// 创建打包命令（Windows 使用 PowerShell，其他使用 zip）
if (process.platform === 'win32') {
  // Windows: 使用 PowerShell Compress-Archive
  console.log('🖥️  使用 PowerShell 压缩...');
  const cmd = `powershell -Command "Compress-Archive -Path '${RELEASE_DIR}\\*' -DestinationPath '${ZIP_PATH}' -Force"`;
  execSync(cmd, { stdio: 'inherit' });
} else {
  // Unix-like: 使用 zip 命令
  console.log('🐧 使用 zip 命令压缩...');
  const cmd = `cd "${RELEASE_DIR}" && zip -r "${ZIP_PATH}" .`;
  execSync(cmd, { stdio: 'inherit' });
}

console.log('✅ 打包完成！');
console.log(`📦 文件位置: ${ZIP_PATH}`);
console.log(`📊 文件大小: ${(fs.statSync(ZIP_PATH).size / 1024 / 1024).toFixed(2)} MB`);

// 生成简短的发布说明
const releaseNotes = path.join(OUTPUT_DIR, `RELEASE-v${VERSION}.md`);
fs.writeFileSync(releaseNotes, `# OOD UI v${VERSION} 发布说明

## 版本信息
- **版本号**: v${VERSION}
- **发布日期**: ${new Date().toISOString().split('T')[0]}
- **包含构建版本**: 传统 JavaScript 版本 + ES6 模块版本

## 构建内容
1. **传统版本** (\`traditional/\`)
   - 完整 JavaScript 库，兼容 IE9+
   - 包含所有 UI 组件
   - 样式和主题文件

2. **ES6 模块版本** (\`es6-modules/\`)
   - 现代模块系统，支持 Tree Shaking
   - 兼容层支持新旧代码混合
   - Webpack 打包版本

3. **共享资源** (\`resources/\`)
   - CSS 样式文件
   - 图标字体
   - 主题系统
   - 国际化语言包

4. **文档** (\`docs/\`)
   - API 参考
   - 使用指南
   - 快速开始

5. **示例** (\`examples/\`)
   - 传统用法示例
   - ES6 模块示例
   - 优化配置示例

## 使用建议
- **新项目**: 推荐使用 ES6 模块版本
- **旧项目升级**: 可选择传统版本或逐步迁移
- **混合架构**: ES6 版本包含兼容层

## 技术支持
- 详细文档: README.md
- 使用指南: USAGE.md
- 变更日志: CHANGELOG.md

---

**注意**: 本发布版本已移除所有开发相关文件，仅包含生产代码和必要文档。
`);

console.log(`📝 发布说明已生成: ${releaseNotes}`);