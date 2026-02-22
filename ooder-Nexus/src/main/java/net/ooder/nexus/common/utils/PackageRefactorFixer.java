package net.ooder.nexus.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * 包重构修复工具 - 批量修复包声明和import引用
 */
public class PackageRefactorFixer {

    // 包映射规则: 旧包名 -> 新包名
    private static final Map<String, String> PACKAGE_MAPPINGS = new LinkedHashMap<>();

    static {
        // management/manager 合并
        PACKAGE_MAPPINGS.put("net.ooder.nexus.management", "net.ooder.nexus.infrastructure.management");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.manager", "net.ooder.nexus.infrastructure.management");

        // console 迁移
        PACKAGE_MAPPINGS.put("net.ooder.nexus.console", "net.ooder.nexus.adapter.inbound.console");

        // model 迁移到 domain
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.mcp", "net.ooder.nexus.domain.mcp.model");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.network", "net.ooder.nexus.domain.network.model");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.device", "net.ooder.nexus.domain.end.model");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.endagent", "net.ooder.nexus.domain.end.model");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.config", "net.ooder.nexus.domain.config.model");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.security", "net.ooder.nexus.domain.security.model");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.system", "net.ooder.nexus.domain.system.model");

        // 注意：model下的其他类保持不动，因为大部分还在原位置
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "src/main/java/net/ooder/nexus";

        System.out.println("=== Package Refactor Fixer ===\n");

        // 1. 修复 infrastructure/management 下的文件
        fixDirectory(baseDir + "/infrastructure/management");

        // 2. 修复 adapter/inbound/console 下的文件
        fixDirectory(baseDir + "/adapter/inbound/console");

        // 3. 修复 adapter/inbound/controller 下的文件
        fixDirectory(baseDir + "/adapter/inbound/controller");

        // 4. 修复 domain 下的文件
        fixDirectory(baseDir + "/domain");

        System.out.println("\n=== Fix Summary ===");
        System.out.println("Package declarations and imports have been updated!");
        System.out.println("\nNext steps:");
        System.out.println("1. Review the fixed files");
        System.out.println("2. Run: mvn clean compile -s settings.xml");
        System.out.println("3. Delete old directories after verification");
    }

    private static void fixDirectory(String dirPath) throws IOException {
        Path directory = Paths.get(dirPath);
        if (!Files.exists(directory)) {
            System.out.println("Directory not found: " + dirPath);
            return;
        }

        System.out.println("Fixing: " + dirPath);

        Files.walk(directory)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(file -> {
                    try {
                        fixFile(file);
                    } catch (IOException e) {
                        System.err.println("Error fixing " + file + ": " + e.getMessage());
                    }
                });
    }

    private static void fixFile(Path file) throws IOException {
        String content = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        String originalContent = content;

        // 按长度降序排序，避免部分匹配问题
        List<Map.Entry<String, String>> sortedMappings = new ArrayList<>(PACKAGE_MAPPINGS.entrySet());
        sortedMappings.sort((a, b) -> Integer.compare(b.getKey().length(), a.getKey().length()));

        for (Map.Entry<String, String> mapping : sortedMappings) {
            String oldPackage = mapping.getKey();
            String newPackage = mapping.getValue();

            // 替换 package 声明
            content = content.replaceAll(
                    "package " + oldPackage + ";",
                    "package " + newPackage + ";"
            );

            // 替换 import 语句
            content = content.replaceAll(
                    "import " + oldPackage + "\\.",
                    "import " + newPackage + "."
            );

            // 替换静态导入
            content = content.replaceAll(
                    "import static " + oldPackage + "\\.",
                    "import static " + newPackage + "."
            );
        }

        // 如果有修改，写回文件
        if (!content.equals(originalContent)) {
            Files.write(file, content.getBytes(StandardCharsets.UTF_8));
            System.out.println("  Fixed: " + file.getFileName());
        }
    }
}
