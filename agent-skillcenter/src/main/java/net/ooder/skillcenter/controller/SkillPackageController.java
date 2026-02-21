package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.model.SkillPackage;
import net.ooder.skillcenter.model.GitHubSkillRequest;
import net.ooder.skillcenter.service.SkillPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillPackageController {

    @Autowired
    private SkillPackageService service;

    @GetMapping
    public ResponseEntity<List<SkillPackage>> getAllSkillPackages() {
        List<SkillPackage> skillPackages = service.getAllSkillPackages();
        return new ResponseEntity<>(skillPackages, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SkillPackage>> searchSkillPackages(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "capabilities", required = false) List<String> capabilities,
            @RequestParam(value = "scenes", required = false) List<String> scenes,
            @RequestParam(value = "limit", defaultValue = "100") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset
    ) {
        List<SkillPackage> result;
        if (keyword != null) {
            result = service.searchSkillPackages(keyword);
        } else if (type != null) {
            result = service.getSkillPackagesByType(type);
        } else if (capabilities != null && !capabilities.isEmpty()) {
            result = service.getSkillPackagesByCapabilities(capabilities);
        } else if (scenes != null && !scenes.isEmpty()) {
            result = service.getSkillPackagesByScenes(scenes);
        } else {
            result = service.getAllSkillPackages();
        }
        
        // Apply pagination
        int start = Math.min(offset, result.size());
        int end = Math.min(start + limit, result.size());
        List<SkillPackage> paginatedResult = result.subList(start, end);
        
        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillPackage> getSkillPackageById(@PathVariable("id") String skillId) {
        return service.getSkillPackageBySkillId(skillId)
                .map(skillPackage -> new ResponseEntity<>(skillPackage, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<String>> getSkillPackageVersions(@PathVariable("id") String skillId) {
        // For simplicity, return a fixed version list
        // In a real implementation, you would query the database for all versions
        List<String> versions = new ArrayList<>();
        versions.add("0.7.0");
        versions.add("0.6.6");
        versions.add("0.6.5");
        return new ResponseEntity<>(versions, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SkillPackage> createSkillPackage(@RequestBody SkillPackage skillPackage) {
        SkillPackage created = service.createSkillPackage(skillPackage);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/github")
    public ResponseEntity<SkillPackage> createGitHubSkillPackage(@RequestBody GitHubSkillRequest request) {
        SkillPackage skillPackage = new SkillPackage();
        skillPackage.setSkillId(request.getSkillId());
        skillPackage.setName(request.getName());
        skillPackage.setVersion(request.getVersion());
        skillPackage.setDescription(request.getDescription());
        skillPackage.setType(request.getType());
        skillPackage.setAuthor(request.getAuthor());
        skillPackage.setLicense(request.getLicense());
        skillPackage.setHomepage(request.getHomepage());
        skillPackage.setRepository(request.getRepository());
        skillPackage.setDocumentation(request.getDocumentation());
        skillPackage.setSourceType("github");
        skillPackage.setSourceUrl(request.getSourceUrl());
        skillPackage.setBranch(request.getBranch() != null ? request.getBranch() : "main");
        skillPackage.setBuildStatus("pending");
        skillPackage.setKeywords(request.getKeywords());
        
        SkillPackage created = service.createSkillPackage(skillPackage);
        
        // 异步触发GitHub仓库同步和构建
        service.syncGitHubRepository(created.getId());
        
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SkillPackage> updateSkillPackage(
            @PathVariable("id") Long id,
            @RequestBody SkillPackage skillPackage
    ) {
        SkillPackage updated = service.updateSkillPackage(id, skillPackage);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillPackage(@PathVariable("id") String skillId) {
        service.deleteSkillPackageBySkillId(skillId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadSkillPackage(
            @PathVariable("id") String skillId,
            @RequestParam(value = "version", required = false) String version
    ) {
        // For simplicity, return a dummy byte array
        // In a real implementation, you would fetch the actual skill package file
        byte[] dummyContent = "Skill package content".getBytes();
        return new ResponseEntity<>(dummyContent, HttpStatus.OK);
    }

}
