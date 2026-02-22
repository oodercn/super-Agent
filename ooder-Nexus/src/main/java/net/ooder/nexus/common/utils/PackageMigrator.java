package net.ooder.nexus.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * 包迁移工具 - 批量迁移Java文件到新包结构
 */
public class PackageMigrator {

    // 迁移规则: 源包 -> 目标包
    private static final Map<String, String> PACKAGE_MAPPINGS = new LinkedHashMap<>();

    static {
        // Protocol模块
        PACKAGE_MAPPINGS.put("net.ooder.nexus.protocol", "net.ooder.nexus.core.protocol");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.protocol.adapter", "net.ooder.nexus.core.protocol.adapter");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.protocol", "net.ooder.nexus.core.protocol.model");

        // Storage模块
        PACKAGE_MAPPINGS.put("net.ooder.nexus.storage", "net.ooder.nexus.core.storage");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.storage.vfs", "net.ooder.nexus.core.storage.vfs");

        // Skill模块
        PACKAGE_MAPPINGS.put("net.ooder.nexus.skill", "net.ooder.nexus.core.skill");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.skill.impl", "net.ooder.nexus.core.skill.impl");
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "src/main/java/net/ooder/nexus";

        System.out.println("=== Package Migration Tool ===\n");

        // 1. 迁移 protocol 目录
        migrateDirectory(baseDir, "protocol", "core/protocol");

        // 2. 迁移 model/protocol 目录
        migrateDirectory(baseDir, "model/protocol", "core/protocol/model");

        // 3. 迁移 storage 目录
        migrateDirectory(baseDir, "storage", "core/storage");

        // 4. 迁移 skill 目录
        migrateDirectory(baseDir, "skill", "core/skill");

        System.out.println("\n=== Migration Summary ===");
        System.out.println("Files migrated successfully!");
        System.out.println("\nNext steps:");
        System.out.println("1. Review migrated files in core/ directory");
        System.out.println("2. Run: mvn clean compile -s settings.xml");
        System.out.println("3. Fix any remaining import issues in IDE");
        System.out.println("4. Delete old directories after verification");
    }

    private static void migrateDirectory(String baseDir, String sourceRelPath, String targetRelPath) throws IOException {
        Path sourceDir = Paths.get(baseDir, sourceRelPath);
        Path targetDir = Paths.get(baseDir, targetRelPath);

        if (!Files.exists(sourceDir)) {
            System.out.println("Skipping (not found): " + sourceRelPath);
            return;
        }

        System.out.println("Migrating: " + sourceRelPath + " -> " + targetRelPath);

        Files.walk(sourceDir)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(sourceFile -> {
                    try {
                        migrateFile(sourceFile, sourceDir, targetDir);
                    } catch (IOException e) {
                        System.err.println("Error migrating " + sourceFile + ": " + e.getMessage());
                    }
                });
    }

    private static void migrateFile(Path sourceFile, Path sourceDir, Path targetDir) throws IOException {
        // 计算相对路径
        Path relPath = sourceDir.relativize(sourceFile);
        Path targetFile = targetDir.resolve(relPath);

        // 创建目标目录
        Files.createDirectories(targetFile.getParent());

        // 读取并修改内容
        String content = new String(Files.readAllBytes(sourceFile), StandardCharsets.UTF_8);
        String modifiedContent = updatePackageDeclarations(content);

        // 写入新文件
        Files.write(targetFile, modifiedContent.getBytes(StandardCharsets.UTF_8));

        System.out.println("  Copied: " + relPath);
    }

    private static String updatePackageDeclarations(String content) {
        String result = content;

        // 按长度降序排序，避免部分匹配问题
        List<Map.Entry<String, String>> sortedMappings = new ArrayList<>(PACKAGE_MAPPINGS.entrySet());
        sortedMappings.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));

        for (Map.Entry<String, String> mapping : sortedMappings) {
            String oldPackage = mapping.getKey();
            String newPackage = mapping.getValue();

            // 替换 package 声明
            result = result.replaceAll(
                    "package " + oldPackage + ";",
                    "package " + newPackage + ";"
            );

            // 替换 import 语句
            result = result.replaceAll(
                    "import " + oldPackage + "\\.",
                    "import " + newPackage + "."
            );

            // 替换静态导入
            result = result.replaceAll(
                    "import static " + oldPackage + "\\.",
                    "import static " + newPackage + "."
            );
        }

        return result;
    }
}
