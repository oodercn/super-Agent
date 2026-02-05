/**
 * 发布版本清理脚本
 * 用于移除开发相关的文件和过程文档
 */

const fs = require('fs');
const path = require('path');
const { promisify } = require('util');

const readdir = promisify(fs.readdir);
const stat = promisify(fs.stat);
const unlink = promisify(fs.unlink);
const rmdir = promisify(fs.rmdir);

// 需要移除的文件模式
const excludePatterns = [
  // 测试文件
  'test-*.html',
  'test-*.js',
  // 测试文档
  'TEST-*.md',
  // 过程文档
  'README-ES6-UPGRADE.md',
  '*REPORT*.md',
  // 开发配置
  '.eslintrc.js',
  '.babelrc',
  'conf.js',
  // 测试报告
  'test-reports/**',
  // AI学习文档
  'docs/ai-learning/**'
];

// 需要保留的文件（例外）
const keepFiles = [
  'build/build_ood_release.bat',
  'scripts/clean-release.js',
  'package.json',
  'webpack.release.js',
  'dist/release/**'
];

// 匹配模式函数
function matchesPattern(filename, pattern) {
  if (pattern.includes('*')) {
    const regex = new RegExp('^' + pattern.replace(/\*/g, '.*') + '$');
    return regex.test(filename);
  }
  return filename === pattern;
}

// 检查是否应该保留文件
function shouldKeepFile(filepath) {
  for (const pattern of keepFiles) {
    if (matchesPattern(filepath, pattern)) {
      return true;
    }
  }
  return false;
}

// 检查是否应该删除文件
function shouldDeleteFile(filepath) {
  for (const pattern of excludePatterns) {
    if (matchesPattern(filepath, pattern)) {
      return true;
    }
  }
  return false;
}

// 递归遍历目录并收集需要删除的文件
async function collectFilesToDelete(dir, relativePath = '') {
  const filesToDelete = [];
  const entries = await readdir(dir);
  
  for (const entry of entries) {
    const fullPath = path.join(dir, entry);
    const relPath = relativePath ? path.join(relativePath, entry) : entry;
    const fileStat = await stat(fullPath);
    
    // 检查是否应该保留
    if (shouldKeepFile(relPath)) {
      console.log(`✓ 保留: ${relPath}`);
      continue;
    }
    
    if (fileStat.isDirectory()) {
      // 递归处理子目录
      const subFiles = await collectFilesToDelete(fullPath, relPath);
      filesToDelete.push(...subFiles);
      
      // 检查目录是否为空（可能可以删除）
      const subEntries = await readdir(fullPath);
      if (subEntries.length === 0) {
        filesToDelete.push({ path: fullPath, relPath: relPath, isDirectory: true });
      }
    } else {
      // 检查是否应该删除
      if (shouldDeleteFile(relPath)) {
        filesToDelete.push({ path: fullPath, relPath: relPath, isDirectory: false });
      }
    }
  }
  
  return filesToDelete;
}

// 删除文件
async function deleteFiles(files) {
  let deletedCount = 0;
  let errorCount = 0;
  
  console.log(`\n📋 找到 ${files.length} 个待删除文件/目录\n`);
  
  for (const file of files) {
    try {
      if (file.isDirectory) {
        await rmdir(file.path);
        console.log(`✓ 删除目录: ${file.relPath}`);
      } else {
        await unlink(file.path);
        console.log(`✓ 删除文件: ${file.relPath}`);
      }
      deletedCount++;
    } catch (error) {
      console.error(`✗ 删除失败: ${file.relPath} - ${error.message}`);
      errorCount++;
    }
  }
  
  return { deletedCount, errorCount };
}

// 主函数
async function main() {
  console.log('🔍 开始清理发布版本...\n');
  
  try {
    // 收集需要删除的文件
    const filesToDelete = await collectFilesToDelete('.');
    
    // 显示统计信息
    console.log(`\n📊 统计信息:`);
    console.log(`- 待删除文件: ${filesToDelete.length}`);
    
    // 确认（安全起见，先只显示不删除）
    console.log('\n⚠️  安全模式：只显示待删除文件，不实际删除');
    console.log('   如需实际删除，请修改代码中的实际操作部分\n');
    
    // 显示待删除文件列表
    filesToDelete.forEach(file => {
      console.log(`  ${file.isDirectory ? '[目录]' : '[文件]'} ${file.relPath}`);
    });
    
    console.log(`\n✅ 清理分析完成！`);
    console.log(`📝 如需实际删除，请运行: node scripts/clean-release.js --delete`);
    
  } catch (error) {
    console.error('❌ 清理过程出错:', error);
    process.exit(1);
  }
}

// 解析命令行参数
const args = process.argv.slice(2);
const shouldDelete = args.includes('--delete');

if (shouldDelete) {
  console.log('⚠️  警告：将实际删除文件！');
  console.log('   继续执行吗？(输入 yes 确认)');
  
  // 等待用户确认（简单实现）
  process.stdin.once('data', async (data) => {
    if (data.toString().trim().toLowerCase() === 'yes') {
      // 实际执行删除
      await main(true);
      process.exit(0);
    } else {
      console.log('取消操作');
      process.exit(0);
    }
  });
} else {
  // 只分析不删除
  main();
}