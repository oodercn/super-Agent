package net.ooder.nexus.adapter.inbound.controller.skill;

import net.ooder.nexus.common.ResultModel;
import net.ooder.sdk.api.skill.SkillPackage;
import net.ooder.sdk.discovery.git.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/skillcenter/discovery")
public class SkillDiscoveryController {

    private static final Logger log = LoggerFactory.getLogger(SkillDiscoveryController.class);

    @Value("${skill.discovery.github.token:}")
    private String githubToken;

    @Value("${skill.discovery.github.owner:ooderCN}")
    private String githubOwner;

    @Value("${skill.discovery.github.skills-repo:skills}")
    private String githubSkillsRepo;

    @Value("${skill.discovery.gitee.token:}")
    private String giteeToken;

    @Value("${skill.discovery.gitee.owner:ooderCN}")
    private String giteeOwner;

    @Value("${skill.discovery.gitee.skills-repo:skills}")
    private String giteeSkillsRepo;

    @Value("${skill.discovery.github.enabled:true}")
    private boolean githubEnabled;

    @Value("${skill.discovery.gitee.enabled:true}")
    private boolean giteeEnabled;

    private GitHubDiscoverer gitHubDiscoverer;
    private GiteeDiscoverer giteeDiscoverer;

    private void initDiscoverers() {
        if (gitHubDiscoverer == null && githubEnabled) {
            gitHubDiscoverer = new GitHubDiscoverer(githubToken, githubOwner, githubSkillsRepo);
        }
        if (giteeDiscoverer == null && giteeEnabled) {
            giteeDiscoverer = new GiteeDiscoverer(giteeToken, giteeOwner, giteeSkillsRepo);
        }
    }

    @PostMapping("/scan")
    @ResponseBody
    public ResultModel<Map<String, Object>> scanSkills(@RequestBody(required = false) Map<String, Object> params) {
        log.info("Skill discovery scan requested: {}", params);
        initDiscoverers();

        try {
            List<String> sources = params != null ? (List<String>) params.get("sources") : null;
            if (sources == null || sources.isEmpty()) {
                sources = Arrays.asList("github", "gitee", "local_fs", "skill_center");
            }

            List<SkillPackage> allSkills = new ArrayList<>();
            Map<String, Object> statistics = new HashMap<>();

            for (String source : sources) {
                try {
                    List<SkillPackage> skills = discoverFromSource(source);
                    allSkills.addAll(skills);
                    statistics.put(source, skills.size());
                } catch (Exception e) {
                    log.error("Failed to discover from source: {}", source, e);
                    statistics.put(source + "_error", e.getMessage());
                }
            }

            statistics.put("total", allSkills.size());
            statistics.put("installed", 0);

            Map<String, Object> data = new HashMap<>();
            data.put("skills", allSkills);
            data.put("statistics", statistics);

            return ResultModel.success("Discovery completed", data);

        } catch (Exception e) {
            log.error("Skill discovery scan failed", e);
            return ResultModel.error("Discovery failed: " + e.getMessage());
        }
    }

    @PostMapping("/github")
    @ResponseBody
    public ResultModel<List<SkillPackage>> discoverFromGitHub(@RequestBody(required = false) Map<String, Object> params) {
        log.info("GitHub skill discovery requested: {}", params);
        initDiscoverers();

        try {
            if (!githubEnabled || gitHubDiscoverer == null) {
                return ResultModel.success("GitHub discovery not enabled", new ArrayList<>());
            }

            String owner = params != null ? (String) params.get("owner") : githubOwner;
            String skillsPath = params != null ? (String) params.get("skillsPath") : "skills";

            CompletableFuture<List<SkillPackage>> future = gitHubDiscoverer.discoverSkills(owner, skillsPath);
            List<SkillPackage> skills = future.get();

            return ResultModel.success("GitHub discovery completed: " + skills.size() + " skills found", skills);

        } catch (Exception e) {
            log.error("GitHub discovery failed", e);
            return ResultModel.error("GitHub discovery failed: " + e.getMessage());
        }
    }

    @PostMapping("/gitee")
    @ResponseBody
    public ResultModel<List<SkillPackage>> discoverFromGitee(@RequestBody(required = false) Map<String, Object> params) {
        log.info("Gitee skill discovery requested: {}", params);
        initDiscoverers();

        try {
            if (!giteeEnabled || giteeDiscoverer == null) {
                return ResultModel.success("Gitee discovery not enabled", new ArrayList<>());
            }

            String owner = params != null ? (String) params.get("owner") : giteeOwner;
            String skillsPath = params != null ? (String) params.get("skillsPath") : "skills";

            CompletableFuture<List<SkillPackage>> future = giteeDiscoverer.discoverSkills(owner, skillsPath);
            List<SkillPackage> skills = future.get();

            return ResultModel.success("Gitee discovery completed: " + skills.size() + " skills found", skills);

        } catch (Exception e) {
            log.error("Gitee discovery failed", e);
            return ResultModel.error("Gitee discovery failed: " + e.getMessage());
        }
    }

    @PostMapping("/manifest")
    @ResponseBody
    public ResultModel<String> getSkillManifest(@RequestBody Map<String, Object> params) {
        log.info("Get skill manifest requested: {}", params);
        initDiscoverers();

        try {
            String source = (String) params.get("source");
            String owner = (String) params.get("owner");
            String repoName = (String) params.get("repoName");

            CompletableFuture<String> future = null;

            if ("github".equals(source) && gitHubDiscoverer != null) {
                future = gitHubDiscoverer.getSkillManifestContent(owner, repoName);
            } else if ("gitee".equals(source) && giteeDiscoverer != null) {
                future = giteeDiscoverer.getSkillManifestContent(owner, repoName);
            }

            if (future != null) {
                String manifest = future.get();
                return ResultModel.success("Manifest retrieved", manifest);
            } else {
                return ResultModel.error("Unknown source: " + source, 400);
            }

        } catch (Exception e) {
            log.error("Get skill manifest failed", e);
            return ResultModel.error("Failed to get manifest: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("githubEnabled", githubEnabled);
        status.put("giteeEnabled", giteeEnabled);
        status.put("githubOwner", githubOwner);
        status.put("giteeOwner", giteeOwner);

        return ResultModel.success(status);
    }

    private List<SkillPackage> discoverFromSource(String source) throws Exception {
        switch (source) {
            case "github":
                if (gitHubDiscoverer != null && githubOwner != null && !githubOwner.isEmpty()) {
                    log.info("GitHub discovery: owner={}, skillsPath=skills", githubOwner);
                    return gitHubDiscoverer.discoverSkills(githubOwner, "skills").get();
                }
                log.info("GitHub discovery skipped: owner not configured");
                break;
            case "gitee":
                if (giteeDiscoverer != null && giteeOwner != null && !giteeOwner.isEmpty()) {
                    log.info("Gitee discovery: owner={}, skillsPath=skills", giteeOwner);
                    return giteeDiscoverer.discoverSkills(giteeOwner, "skills").get();
                }
                log.info("Gitee discovery skipped: owner not configured");
                break;
            case "local_fs":
                log.info("Local FS discovery: scanning ./skills directory");
                return discoverLocalSkills();
            case "skill_center":
                log.info("Skill center discovery: not implemented yet");
                break;
            default:
                log.warn("Unknown discovery source: {}", source);
        }
        return new ArrayList<>();
    }

    private List<SkillPackage> discoverLocalSkills() {
        List<SkillPackage> skills = new ArrayList<>();
        java.io.File skillsDir = new java.io.File("./skills");
        if (skillsDir.exists() && skillsDir.isDirectory()) {
            java.io.File[] dirs = skillsDir.listFiles(java.io.File::isDirectory);
            if (dirs != null) {
                for (java.io.File dir : dirs) {
                    java.io.File manifest = new java.io.File(dir, "skill-manifest.yaml");
                    if (manifest.exists()) {
                        SkillPackage pkg = new SkillPackage();
                        pkg.setSkillId(dir.getName());
                        pkg.setName(dir.getName());
                        pkg.setSource("local_fs");
                        pkg.setVersion("local");
                        skills.add(pkg);
                        log.info("Found local skill: {}", dir.getName());
                    }
                }
            }
        } else {
            log.info("Local skills directory not found: {}", skillsDir.getAbsolutePath());
        }
        return skills;
    }
}
