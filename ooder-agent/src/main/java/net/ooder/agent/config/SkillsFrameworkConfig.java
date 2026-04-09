package net.ooder.agent.config;

import net.ooder.skills.api.SkillPackageManager;
import net.ooder.skills.core.impl.SkillPackageManagerImpl;
import net.ooder.skills.core.discovery.LocalDiscoverer;
import net.ooder.skills.core.discovery.GitRepositoryDiscovererAdapter;
import net.ooder.skills.common.enums.DiscoveryMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 技能框架配置类，配置技能包管理器和发现器
 */
@Configuration
public class SkillsFrameworkConfig {

    private static final Logger log = LoggerFactory.getLogger(SkillsFrameworkConfig.class);

    @Value("${ooder.skills.gitee.token:}")
    private String giteeToken;

    @Value("${ooder.skills.gitee.owner:ooderCN}")
    private String giteeOwner;

    @Value("${ooder.skills.gitee.repo:skills}")
    private String giteeRepo;

    @Value("${ooder.skills.gitee.branch:master}")
    private String giteeBranch;

    @Value("${ooder.skills.github.token:}")
    private String githubToken;

    @Value("${ooder.skills.github.owner:ooderCN}")
    private String githubOwner;

    @Value("${ooder.skills.github.repo:skills}")
    private String githubRepo;

    @Value("${ooder.skills.github.branch:main}")
    private String githubBranch;

    @Value("${ooder.skills.root-path:./skills}")
    private String skillRootPath;

    @Bean
    public SkillPackageManager skillPackageManager() {
        log.info("[SkillsFrameworkConfig] Creating SkillPackageManager");
        
        SkillPackageManagerImpl manager = new SkillPackageManagerImpl();
        manager.setSkillRootPath(skillRootPath);
        
        LocalDiscoverer localDiscoverer = new LocalDiscoverer();
        localDiscoverer.setSkillsDirectory(skillRootPath);
        manager.setDiscoverer(DiscoveryMethod.LOCAL_FS, localDiscoverer);
        log.info("[SkillsFrameworkConfig] Local discoverer configured: {}", skillRootPath);
        
        if (giteeToken == null || giteeToken.isEmpty()) {
            log.warn("[SkillsFrameworkConfig] Gitee token is not configured. Creating Gitee discoverer without token for public repository access only.");
        }
        
        GitRepositoryDiscovererAdapter giteeDiscoverer = new GitRepositoryDiscovererAdapter("gitee");
        giteeDiscoverer.setSource("gitee");
        giteeDiscoverer.setDefaultOwner(giteeOwner);
        giteeDiscoverer.setDefaultRepo(giteeRepo);
        giteeDiscoverer.setDefaultBranch(giteeBranch);
        if (giteeToken != null && !giteeToken.isEmpty()) {
            giteeDiscoverer.setGiteeToken(giteeToken);
            log.info("[SkillsFrameworkConfig] Gitee discoverer configured with token: {}/{}:{}", 
                giteeOwner, giteeRepo, giteeBranch);
        } else {
            log.info("[SkillsFrameworkConfig] Gitee discoverer configured without token (public repository access only): {}/{}:{}", 
                giteeOwner, giteeRepo, giteeBranch);
        }
        manager.setGiteeDiscoverer(giteeDiscoverer);
        
        if (githubToken != null && !githubToken.isEmpty()) {
            GitRepositoryDiscovererAdapter githubDiscoverer = new GitRepositoryDiscovererAdapter("github");
            githubDiscoverer.setSource("github");
            githubDiscoverer.setGithubToken(githubToken);
            githubDiscoverer.setDefaultOwner(githubOwner);
            githubDiscoverer.setDefaultRepo(githubRepo);
            githubDiscoverer.setDefaultBranch(githubBranch);
            manager.setGitHubDiscoverer(githubDiscoverer);
            log.info("[SkillsFrameworkConfig] GitHub discoverer configured: {}/{}:{}", 
                githubOwner, githubRepo, githubBranch);
        }
        
        return manager;
    }
}
