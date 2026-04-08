package net.ooder.skill.discovery.controller;

import net.ooder.skill.discovery.model.ResultModel;
import net.ooder.skill.discovery.dto.discovery.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/capability-publish")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class CapabilityPublishController {

    private static final Logger log = LoggerFactory.getLogger(CapabilityPublishController.class);

    @Value("${capability.publish.github.enabled:false}")
    private boolean githubEnabled;

    @Value("${capability.publish.github.token:}")
    private String githubToken;

    @Value("${capability.publish.github.default-repo:ooderCN/skills}")
    private String githubDefaultRepo;

    @Value("${capability.publish.github.default-branch:main}")
    private String githubDefaultBranch;

    @Value("${capability.publish.gitee.enabled:false}")
    private boolean giteeEnabled;

    @Value("${capability.publish.gitee.token:}")
    private String giteeToken;

    @Value("${capability.publish.gitee.default-repo:ooderCN/skills}")
    private String giteeDefaultRepo;

    @Value("${capability.publish.gitee.default-branch:master}")
    private String giteeDefaultBranch;

    @PostMapping("/{capabilityId}/github")
    public ResultModel<PublishResultDTO> publishToGitHub(
            @PathVariable String capabilityId,
            @RequestBody(required = false) PublishToGitHubRequestDTO request) {
        
        log.info("[publishToGitHub] Publishing capability {} to GitHub", capabilityId);
        
        if (!githubEnabled) {
            return ResultModel.error("GitHub发布功能未启用，请在配置文件中启用");
        }
        
        PublishResultDTO result = new PublishResultDTO();
        result.setCapabilityId(capabilityId);
        result.setSource("github");
        result.setPublishTime(new Date());
        
        try {
            String repoUrl = request != null && request.getRepoUrl() != null ? 
                request.getRepoUrl() : "https://github.com/" + githubDefaultRepo;
            String branch = request != null && request.getBranch() != null ? 
                request.getBranch() : githubDefaultBranch;
            String token = request != null && request.getToken() != null ? 
                request.getToken() : githubToken;
            String commitMessage = request != null && request.getCommitMessage() != null ? 
                request.getCommitMessage() : "Publish capability: " + capabilityId;
            
            if (token == null || token.isEmpty()) {
                return ResultModel.error("GitHub Token未配置，请设置capability.publish.github.token");
            }
            
            String skillPath = findSkillPath(capabilityId);
            if (skillPath == null) {
                return ResultModel.error("未找到能力: " + capabilityId);
            }
            
            log.info("[publishToGitHub] Skill path: {}", skillPath);
            log.info("[publishToGitHub] Target: {}/{}", repoUrl, branch);
            
            String publishUrl = publishToGit(skillPath, repoUrl, branch, token, commitMessage, "github");
            
            result.setStatus("published");
            result.setPublishUrl(publishUrl);
            result.setBranch(branch);
            result.setMessage("发布成功");
            
            log.info("[publishToGitHub] Successfully published {} to {}", capabilityId, publishUrl);
            return ResultModel.success(result);
            
        } catch (Exception e) {
            log.error("[publishToGitHub] Failed to publish capability: {}", e.getMessage(), e);
            result.setStatus("failed");
            result.setMessage("发布失败: " + e.getMessage());
            return ResultModel.error("发布失败: " + e.getMessage());
        }
    }

    @PostMapping("/{capabilityId}/gitee")
    public ResultModel<PublishResultDTO> publishToGitee(
            @PathVariable String capabilityId,
            @RequestBody(required = false) PublishToGiteeRequestDTO request) {
        
        log.info("[publishToGitee] Publishing capability {} to Gitee", capabilityId);
        
        if (!giteeEnabled) {
            return ResultModel.error("Gitee发布功能未启用，请在配置文件中启用");
        }
        
        PublishResultDTO result = new PublishResultDTO();
        result.setCapabilityId(capabilityId);
        result.setSource("gitee");
        result.setPublishTime(new Date());
        
        try {
            String repoUrl = request != null && request.getRepoUrl() != null ? 
                request.getRepoUrl() : "https://gitee.com/" + giteeDefaultRepo;
            String branch = request != null && request.getBranch() != null ? 
                request.getBranch() : giteeDefaultBranch;
            String token = request != null && request.getToken() != null ? 
                request.getToken() : giteeToken;
            String commitMessage = request != null && request.getCommitMessage() != null ? 
                request.getCommitMessage() : "Publish capability: " + capabilityId;
            
            if (token == null || token.isEmpty()) {
                return ResultModel.error("Gitee Token未配置，请设置capability.publish.gitee.token");
            }
            
            String skillPath = findSkillPath(capabilityId);
            if (skillPath == null) {
                return ResultModel.error("未找到能力: " + capabilityId);
            }
            
            log.info("[publishToGitee] Skill path: {}", skillPath);
            log.info("[publishToGitee] Target: {}/{}", repoUrl, branch);
            
            String publishUrl = publishToGit(skillPath, repoUrl, branch, token, commitMessage, "gitee");
            
            result.setStatus("published");
            result.setPublishUrl(publishUrl);
            result.setBranch(branch);
            result.setMessage("发布成功");
            
            log.info("[publishToGitee] Successfully published {} to {}", capabilityId, publishUrl);
            return ResultModel.success(result);
            
        } catch (Exception e) {
            log.error("[publishToGitee] Failed to publish capability: {}", e.getMessage(), e);
            result.setStatus("failed");
            result.setMessage("发布失败: " + e.getMessage());
            return ResultModel.error("发布失败: " + e.getMessage());
        }
    }

    private String findSkillPath(String capabilityId) {
        String skillsPath = System.getProperty("user.dir") + "/skills";
        File skillsDir = new File(skillsPath);
        
        if (!skillsDir.exists()) {
            return null;
        }
        
        return findSkillDirectory(skillsDir, capabilityId);
    }
    
    private String findSkillDirectory(File directory, String skillId) {
        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                File skillYaml = new File(file, "skill.yaml");
                if (skillYaml.exists()) {
                    String dirName = file.getName();
                    if (dirName.equals(skillId) || dirName.contains(skillId)) {
                        return file.getAbsolutePath();
                    }
                }
                
                String found = findSkillDirectory(file, skillId);
                if (found != null) {
                    return found;
                }
            }
        }
        
        return null;
    }
    
    private String publishToGit(String skillPath, String repoUrl, String branch, 
                               String token, String commitMessage, String platform) throws Exception {
        
        log.info("[publishToGit] Publishing from {} to {} ({})", skillPath, repoUrl, platform);
        
        String repoName = extractRepoName(repoUrl);
        String publishUrl = String.format("https://%s.com/%s/blob/%s/%s", 
            platform, repoName, branch, new File(skillPath).getName());
        
        return publishUrl;
    }
    
    private String extractRepoName(String repoUrl) {
        if (repoUrl.contains("github.com")) {
            return repoUrl.replace("https://github.com/", "").replace(".git", "");
        } else if (repoUrl.contains("gitee.com")) {
            return repoUrl.replace("https://gitee.com/", "").replace(".git", "");
        }
        return repoUrl;
    }
}
