package net.ooder.skillcenter.service;

import net.ooder.skillcenter.model.SkillPackage;
import net.ooder.skillcenter.repository.SkillPackageRepository;
import net.ooder.skillcenter.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SkillPackageService {

    @Autowired
    private SkillPackageRepository repository;

    @Autowired
    private GitHubService gitHubService;

    public SkillPackage createSkillPackage(SkillPackage skillPackage) {
        return repository.save(skillPackage);
    }

    public Optional<SkillPackage> getSkillPackageById(Long id) {
        return repository.findById(id);
    }

    public Optional<SkillPackage> getSkillPackageBySkillId(String skillId) {
        return repository.findBySkillId(skillId);
    }

    public Optional<SkillPackage> getSkillPackageBySkillIdAndVersion(String skillId, String version) {
        return repository.findBySkillIdAndVersion(skillId, version);
    }

    public List<SkillPackage> getAllSkillPackages() {
        return repository.findAll();
    }

    public List<SkillPackage> getSkillPackagesByType(String type) {
        return repository.findByType(type);
    }

    public List<SkillPackage> getSkillPackagesByCapabilities(List<String> capabilities) {
        return repository.findByCapabilities(capabilities);
    }

    public List<SkillPackage> getSkillPackagesByScenes(List<String> scenes) {
        return repository.findByScenes(scenes);
    }

    public List<SkillPackage> searchSkillPackages(String keyword) {
        return repository.findByKeyword(keyword);
    }

    public SkillPackage updateSkillPackage(Long id, SkillPackage updatedSkillPackage) {
        Optional<SkillPackage> existing = repository.findById(id);
        if (existing.isPresent()) {
            SkillPackage skillPackage = existing.get();
            // Update fields
            skillPackage.setName(updatedSkillPackage.getName());
            skillPackage.setVersion(updatedSkillPackage.getVersion());
            skillPackage.setDescription(updatedSkillPackage.getDescription());
            skillPackage.setType(updatedSkillPackage.getType());
            skillPackage.setAuthor(updatedSkillPackage.getAuthor());
            skillPackage.setLicense(updatedSkillPackage.getLicense());
            skillPackage.setHomepage(updatedSkillPackage.getHomepage());
            skillPackage.setRepository(updatedSkillPackage.getRepository());
            skillPackage.setDocumentation(updatedSkillPackage.getDocumentation());
            skillPackage.setDownloadUrl(updatedSkillPackage.getDownloadUrl());
            skillPackage.setFileSize(updatedSkillPackage.getFileSize());
            skillPackage.setChecksum(updatedSkillPackage.getChecksum());
            skillPackage.setKeywords(updatedSkillPackage.getKeywords());
            return repository.save(skillPackage);
        }
        return null;
    }

    public void deleteSkillPackage(Long id) {
        repository.deleteById(id);
    }

    public void deleteSkillPackageBySkillId(String skillId) {
        Optional<SkillPackage> skillPackage = repository.findBySkillId(skillId);
        skillPackage.ifPresent(repository::delete);
    }

    public void syncGitHubRepository(Long skillPackageId) {
        Optional<SkillPackage> optional = repository.findById(skillPackageId);
        if (optional.isPresent()) {
            SkillPackage skillPackage = optional.get();
            // 使用GitHubService实现真正的GitHub仓库同步
            try {
                System.out.println("开始同步GitHub仓库: " + skillPackage.getSourceUrl());
                
                // 克隆GitHub仓库
                String repoDir = gitHubService.cloneRepository(
                        skillPackage.getSourceUrl(),
                        skillPackage.getBranch(),
                        skillPackage.getSkillId()
                );
                
                // 读取skill.yaml文件
                String skillYamlContent = gitHubService.readSkillYaml(repoDir, skillPackage.getSkillId());
                System.out.println("成功读取skill.yaml文件");
                
                // 构建技能包
                String skillPackagePath = gitHubService.buildSkillPackage(
                        repoDir,
                        skillPackage.getSkillId(),
                        skillPackage.getVersion()
                );
                System.out.println("成功构建技能包: " + skillPackagePath);
                
                // 更新技能包信息
                skillPackage.setBuildStatus("success");
                skillPackage.setLastSyncAt(new Date());
                // 生成下载URL
                String downloadUrl = "http://localhost:8080/api/skills/" + skillPackage.getSkillId() + "/download";
                skillPackage.setDownloadUrl(downloadUrl);
                repository.save(skillPackage);
                
                // 清理临时文件
                gitHubService.cleanup(repoDir);
                gitHubService.cleanup(skillPackagePath);
                
                System.out.println("GitHub仓库同步完成: " + skillPackage.getSkillId());
            } catch (Exception e) {
                System.err.println("GitHub仓库同步失败: " + e.getMessage());
                skillPackage.setBuildStatus("failed");
                repository.save(skillPackage);
            }
        }
    }

}
