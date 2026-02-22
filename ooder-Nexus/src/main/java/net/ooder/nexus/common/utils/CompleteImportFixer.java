package net.ooder.nexus.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * 完整Import修复工具 - 修复所有剩余的旧包引用
 */
public class CompleteImportFixer {

    // 完整的包映射规则
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

        // protocol 迁移到 core
        PACKAGE_MAPPINGS.put("net.ooder.nexus.protocol", "net.ooder.nexus.core.protocol");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.model.protocol", "net.ooder.nexus.core.protocol.model");

        // skill 迁移到 core
        PACKAGE_MAPPINGS.put("net.ooder.nexus.skill", "net.ooder.nexus.core.skill");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.skill.impl", "net.ooder.nexus.core.skill.impl");

        // storage 迁移到 core
        PACKAGE_MAPPINGS.put("net.ooder.nexus.storage", "net.ooder.nexus.core.storage");
        PACKAGE_MAPPINGS.put("net.ooder.nexus.storage.vfs", "net.ooder.nexus.core.storage.vfs");
    }

    public static void main(String[] args) throws IOException {
        String baseDir = "src/main/java/net/ooder/nexus";

        System.out.println("=== Complete Import Fixer ===\n");

        int fixedCount = 0;

        // 修复所有Java文件中的import
        Path directory = Paths.get(baseDir);

        List<Path> javaFiles = Files.walk(directory)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .collect(java.util.stream.Collectors.toList());

        for (Path file : javaFiles) {
            boolean fixed = fixFile(file);
            if (fixed) {
                fixedCount++;
                System.out.println("  Fixed: " + file.getFileName());
            }
        }

        System.out.println("\n=== Fix Summary ===");
        System.out.println("Total files fixed: " + fixedCount);
        System.out.println("\nNext step: Run 'mvn clean compile -s settings.xml' to verify");
    }

    private static boolean fixFile(Path file) throws IOException {
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
            return true;
        }
        return false;
    }
}
