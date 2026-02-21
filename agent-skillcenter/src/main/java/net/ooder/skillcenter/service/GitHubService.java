package net.ooder.skillcenter.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Date;

@Service
public class GitHubService {

    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/skillcenter";

    public GitHubService() {
        // 创建临时目录
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }

    /**
     * 从GitHub仓库克隆代码
     * @param repositoryUrl GitHub仓库URL
     * @param branch 分支
     * @param skillId 技能ID
     * @return 克隆后的目录路径
     * @throws Exception 克隆失败时抛出异常
     */
    public String cloneRepository(String repositoryUrl, String branch, String skillId) throws Exception {
        String repoDir = TEMP_DIR + "/" + skillId + "_" + System.currentTimeMillis();
        File repoDirectory = new File(repoDir);
        
        // 清理旧目录
        if (repoDirectory.exists()) {
            deleteDirectory(repoDirectory);
        }
        
        // 创建新目录
        repoDirectory.mkdirs();
        
        // 这里应该使用git命令克隆仓库
        // 目前模拟实现，直接从GitHub API获取文件
        System.out.println("克隆GitHub仓库: " + repositoryUrl + " (分支: " + branch + ")");
        
        // 模拟克隆过程
        Thread.sleep(1000);
        
        return repoDir;
    }

    /**
     * 读取GitHub仓库中的skill.yaml文件
     * @param repoDir 仓库目录
     * @param skillId 技能ID
     * @return skill.yaml文件内容
     * @throws Exception 读取失败时抛出异常
     */
    public String readSkillYaml(String repoDir, String skillId) throws Exception {
        // 模拟读取skill.yaml文件
        System.out.println("读取skill.yaml文件: " + repoDir);
        
        // 模拟读取过程
        Thread.sleep(500);
        
        // 返回模拟的skill.yaml内容
        return "apiVersion: skill.ooder.net/v1\n" +
               "kind: Skill\n" +
               "\n" +
               "metadata:\n" +
               "  id: " + skillId + "\n" +
               "  name: Test Skill\n" +
               "  version: 0.7.0\n" +
               "  description: Test skill from GitHub\n" +
               "  author: OODER Team\n" +
               "  license: Apache-2.0\n" +
               "  homepage: https://github.com/ooderCN/guper-Agent\n" +
               "  repository: https://github.com/ooderCN/guper-Agent.git\n" +
               "\n" +
               "spec:\n" +
               "  type: tool-skill\n" +
               "  runtime:\n" +
               "    language: java\n" +
               "    javaVersion: \"8\"\n" +
               "  capabilities:\n" +
               "    - id: test-capability\n" +
               "      name: Test Capability\n" +
               "      description: Test capability\n";
    }

    /**
     * 构建技能包
     * @param repoDir 仓库目录
     * @param skillId 技能ID
     * @param version 版本
     * @return 构建后的技能包路径
     * @throws Exception 构建失败时抛出异常
     */
    public String buildSkillPackage(String repoDir, String skillId, String version) throws Exception {
        String skillPackagePath = TEMP_DIR + "/" + skillId + "-" + version + ".skill";
        
        // 模拟构建过程
        System.out.println("构建技能包: " + skillPackagePath);
        
        // 模拟构建过程
        Thread.sleep(1500);
        
        // 创建空的技能包文件
        File skillPackageFile = new File(skillPackagePath);
        skillPackageFile.createNewFile();
        
        return skillPackagePath;
    }

    /**
     * 清理临时文件
     * @param path 路径
     */
    public void cleanup(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
    }

    /**
     * 删除目录
     * @param directory 目录
     */
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    /**
     * 从GitHub API获取文件内容
     * @param owner 仓库所有者
     * @param repo 仓库名称
     * @param path 文件路径
     * @param branch 分支
     * @return 文件内容
     * @throws Exception 获取失败时抛出异常
     */
    private String getFileFromGitHub(String owner, String repo, String path, String branch) throws Exception {
        String apiUrl = GITHUB_API_URL + "/repos/" + owner + "/" + repo + "/contents/" + path + "?ref=" + branch;
        
        URL url = new URL(apiUrl);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    /**
     * 解析GitHub仓库URL，获取所有者和仓库名称
     * @param repositoryUrl GitHub仓库URL
     * @return 包含所有者和仓库名称的数组
     */
    public String[] parseGitHubUrl(String repositoryUrl) {
        // 支持多种GitHub URL格式
        repositoryUrl = repositoryUrl.replace("https://github.com/", "")
                .replace("git@github.com:", "")
                .replace(".git", "");
        
        String[] parts = repositoryUrl.split("/");
        if (parts.length >= 2) {
            return new String[]{parts[0], parts[1]};
        }
        return new String[]{};
    }
}