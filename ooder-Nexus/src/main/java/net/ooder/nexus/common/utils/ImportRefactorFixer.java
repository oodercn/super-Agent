package net.ooder.nexus.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Import引用修复工具 - 批量修复所有引用旧包名的import
 */
public class ImportRefactorFixer {

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
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "src/main/java/net/ooder/nexus";

        System.out.println("=== Import Refactor Fixer ===\n");

        // 修复所有Java文件中的import
        fixAllJavaFiles(baseDir);

        System.out.println("\n=== Fix Summary ===");
        System.out.println("All import references have been updated!");
        System.out.println("\nNext step: Run 'mvn clean compile -s settings.xml' to verify");
    }

    private static void fixAllJavaFiles(String baseDir) throws IOException {
        Path directory = Paths.get(baseDir);

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
            System.out.println("  Fixed imports: " + file.getFileName());
        }
    }
}
