package net.ooder.nexus.skillcenter.controller;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.dto.SkillResultDTO;
import net.ooder.skillcenter.service.SkillService;
import net.ooder.skillcenter.service.ExecutionService;
import net.ooder.skillcenter.discovery.GitDiscoveryService;
import net.ooder.nexus.skillcenter.model.ResultModel;
import net.ooder.nexus.skillcenter.dto.skill.*;
import net.ooder.nexus.skillcenter.dto.admin.SkillQueryDTO;
import net.ooder.nexus.skillcenter.dto.share.*;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.manager.GroupManager;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.dto.scene.SkillInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class SkillController extends BaseController {

    private final SkillService skillService;
    private final ExecutionService executionService;
    private final SkillMarketManager marketManager;
    private final SkillManager skillManager;
    private final GroupManager groupManager;
    private final Map<String, SkillShareDTO> shareMap;

    @Autowired
    private GitDiscoveryService gitDiscoveryService;

    public SkillController(SkillService skillService, ExecutionService executionService) {
        this.skillService = skillService;
        this.executionService = executionService;
        this.marketManager = SkillMarketManager.getInstance();
        this.skillManager = SkillManager.getInstance();
        this.groupManager = GroupManager.getInstance();
        this.shareMap = new HashMap<>();
    }

    @PostMapping("/skills/list")
    public ResultModel<PageResult<SkillDTO>> getAllSkills(@RequestBody SkillQueryDTO query) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getAllSkills", query);

        try {
            String category = query.getCategory();
            String status = query.getStatus();
            String keyword = query.getKeyword();
            int pageNum = query.getPageNum();
            int pageSize = query.getPageSize();

            PageResult<SkillDTO> result = skillService.getAllSkills(category, status, keyword, pageNum, pageSize);
            logRequestEnd("getAllSkills", result, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("getAllSkills", e);
            return ResultModel.error(500, "获取技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/get")
    public ResultModel<SkillDTO> getSkillById(@RequestBody SkillIdDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSkillById", request);

        try {
            String skillId = request.getSkillId();
            if (isParamEmpty(skillId, "skillId")) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            SkillDTO skill = skillService.getSkillById(skillId);
            if (skill == null) {
                return ResultModel.notFound("技能不存在");
            }
            logRequestEnd("getSkillById", skill, System.currentTimeMillis() - startTime);
            return ResultModel.success(skill);
        } catch (Exception e) {
            logRequestError("getSkillById", e);
            return ResultModel.error(500, "获取技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/add")
    public ResultModel<SkillDTO> addSkill(@RequestBody SkillDTO skillDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("addSkill", skillDTO);

        try {
            SkillDTO result = skillService.addSkill(skillDTO);
            logRequestEnd("addSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("添加技能成功", result);
        } catch (Exception e) {
            logRequestError("addSkill", e);
            return ResultModel.error(500, "添加技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/update")
    public ResultModel<SkillDTO> updateSkill(@RequestBody SkillDTO skillDTO) {
        long startTime = System.currentTimeMillis();
        logRequestStart("updateSkill", skillDTO);

        try {
            String skillId = skillDTO.getId();
            if (isParamEmpty(skillId, "skillId")) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            SkillDTO result = skillService.updateSkill(skillId, skillDTO);
            if (result == null) {
                return ResultModel.notFound("技能不存在");
            }
            logRequestEnd("updateSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("更新技能成功", result);
        } catch (Exception e) {
            logRequestError("updateSkill", e);
            return ResultModel.error(500, "更新技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/delete")
    public ResultModel<Boolean> deleteSkill(@RequestBody SkillIdDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("deleteSkill", request);

        try {
            String skillId = request.getSkillId();
            if (isParamEmpty(skillId, "skillId")) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            boolean result = skillService.deleteSkill(skillId);
            logRequestEnd("deleteSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("删除技能成功", result);
        } catch (Exception e) {
            logRequestError("deleteSkill", e);
            return ResultModel.error(500, "删除技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}/approve")
    public ResultModel<Boolean> approveSkill(@PathVariable String skillId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("approveSkill", skillId);

        try {
            boolean result = skillService.approveSkill(skillId);
            logRequestEnd("approveSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("审核技能成功", result);
        } catch (Exception e) {
            logRequestError("approveSkill", e);
            return ResultModel.error(500, "审核技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}/reject")
    public ResultModel<Boolean> rejectSkill(@PathVariable String skillId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("rejectSkill", skillId);

        try {
            boolean result = skillService.rejectSkill(skillId);
            logRequestEnd("rejectSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("拒绝技能成功", result);
        } catch (Exception e) {
            logRequestError("rejectSkill", e);
            return ResultModel.error(500, "拒绝技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/{skillId}/execute")
    public ResultModel<ExecutionResultDTO> executeSkill(
            @PathVariable String skillId,
            @RequestBody SkillExecutionDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("executeSkill", request);

        try {
            if (isParamEmpty(skillId, "skillId")) {
                return ResultModel.badRequest("技能ID不能为空");
            }

            SkillResultDTO skillResult = executionService.executeSkill(skillId, request.getParameters());

            ExecutionResultDTO result = new ExecutionResultDTO();
            result.setExecutionId(skillResult.getExecutionId());
            result.setStatus(skillResult.getStatus());
            result.setOutput(skillResult.getOutput() != null ? String.valueOf(skillResult.getOutput()) : null);
            result.setExecutionTime(skillResult.getExecutionTime());

            logRequestEnd("executeSkill", result, System.currentTimeMillis() - startTime);
            return ResultModel.success("技能执行成功", result);
        } catch (Exception e) {
            logRequestError("executeSkill", e);
            return ResultModel.error(500, "技能执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/discovery/scan")
    public ResultModel<DiscoveryScanResultDTO> scanBySource(@RequestBody DiscoveryScanDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("scanBySource", request);

        try {
            String source = request.getSource();
            List<SkillListing> skills = scanFromSource(source, request);
            
            DiscoveryScanResultDTO result = new DiscoveryScanResultDTO();
            result.setSource(source);
            result.setSkillCount(skills.size());
            result.setSkills(skills.stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList()));
            result.setScanTime(System.currentTimeMillis() - startTime);

            logRequestEnd("scanBySource", skills.size() + " skills from " + source, System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("scanBySource", e);
            return ResultModel.error(500, "扫描失败: " + e.getMessage());
        }
    }

    private List<SkillListing> scanFromSource(String source, DiscoveryScanDTO request) {
        switch (source) {
            case "github":
                return scanFromGitHub(request);
            case "gitee":
                return scanFromGitee(request);
            case "skill_center":
                return scanFromSkillCenter(request);
            case "udp_broadcast":
                return scanFromUdpBroadcast(request);
            case "local_fs":
                return scanFromLocalFs(request);
            case "mdns_dns_sd":
                return scanFromMdns(request);
            case "dht_kademlia":
                return scanFromDht(request);
            default:
                return marketManager.getAllSkills();
        }
    }

    private List<SkillListing> scanFromGitHub(DiscoveryScanDTO request) {
        try {
            String owner = request.getOwner();
            String skillsPath = request.getSkillsPath();
            
            List<SkillInfoDTO> packages;
            if (owner != null && !owner.isEmpty()) {
                if (skillsPath != null && !skillsPath.isEmpty()) {
                    packages = gitDiscoveryService.discoverFromGitHub(owner, skillsPath).get();
                } else {
                    packages = gitDiscoveryService.discoverFromGitHub(owner).get();
                }
            } else {
                packages = gitDiscoveryService.discoverFromGitHub().get();
            }
            
            return packages.stream()
                .map(this::convertSkillPackageToListing)
                .limit(request.getLimit() > 0 ? request.getLimit() : 50)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logRequestError("scanFromGitHub", e);
            return new ArrayList<>();
        }
    }

    private List<SkillListing> scanFromGitee(DiscoveryScanDTO request) {
        try {
            String owner = request.getOwner();
            String skillsPath = request.getSkillsPath();
            
            List<SkillInfoDTO> packages;
            if (owner != null && !owner.isEmpty()) {
                if (skillsPath != null && !skillsPath.isEmpty()) {
                    packages = gitDiscoveryService.discoverFromGitee(owner, skillsPath).get();
                } else {
                    packages = gitDiscoveryService.discoverFromGitee(owner).get();
                }
            } else {
                packages = gitDiscoveryService.discoverFromGitee().get();
            }
            
            return packages.stream()
                .map(this::convertSkillPackageToListing)
                .limit(request.getLimit() > 0 ? request.getLimit() : 50)
                .collect(Collectors.toList());
        } catch (Exception e) {
            logRequestError("scanFromGitee", e);
            return new ArrayList<>();
        }
    }

    private SkillListing convertSkillPackageToListing(SkillInfoDTO pkg) {
        SkillListing listing = new SkillListing();
        listing.setSkillId(pkg.getSkillId());
        listing.setName(pkg.getName() != null ? pkg.getName() : pkg.getSkillId());
        listing.setDescription(pkg.getDescription());
        listing.setVersion(pkg.getVersion());
        listing.setType("git");
        listing.setRepository(pkg.getAuthor());
        listing.setDownloadUrl(pkg.getCategory());
        return listing;
    }

    private List<SkillListing> scanFromSkillCenter(DiscoveryScanDTO request) {
        return marketManager.getAllSkills().stream()
            .limit(request.getLimit() > 0 ? request.getLimit() : 50)
            .collect(Collectors.toList());
    }

    private List<SkillListing> scanFromUdpBroadcast(DiscoveryScanDTO request) {
        return marketManager.getAllSkills().stream()
            .filter(s -> "api".equals(s.getType()) || "integration".equals(s.getType()))
            .limit(request.getLimit() > 0 ? request.getLimit() : 20)
            .collect(Collectors.toList());
    }

    private List<SkillListing> scanFromLocalFs(DiscoveryScanDTO request) {
        return marketManager.getAllSkills().stream()
            .filter(s -> "local".equals(s.getType()) || "utility".equals(s.getType()))
            .limit(request.getLimit() > 0 ? request.getLimit() : 30)
            .collect(Collectors.toList());
    }

    private List<SkillListing> scanFromMdns(DiscoveryScanDTO request) {
        return marketManager.getAllSkills().stream()
            .filter(s -> "service".equals(s.getType()) || "api".equals(s.getType()))
            .limit(request.getLimit() > 0 ? request.getLimit() : 15)
            .collect(Collectors.toList());
    }

    private List<SkillListing> scanFromDht(DiscoveryScanDTO request) {
        return marketManager.getAllSkills().stream()
            .filter(s -> "p2p".equals(s.getType()) || "distributed".equals(s.getType()))
            .limit(request.getLimit() > 0 ? request.getLimit() : 10)
            .collect(Collectors.toList());
    }

    @PostMapping("/skills/discovery/sources")
    public ResultModel<List<DiscoverySourceDTO>> getDiscoverySources() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDiscoverySources", null);

        try {
            List<DiscoverySourceDTO> sources = Arrays.asList(
                createSource("github", "GitHub", "GitHub仓库技能发现", gitDiscoveryService.isGitHubEnabled()),
                createSource("gitee", "Gitee", "Gitee仓库技能发现", gitDiscoveryService.isGiteeEnabled()),
                createSource("skill_center", "技能中心", "SkillCenter API发现", true),
                createSource("udp_broadcast", "UDP广播", "局域网广播发现", false),
                createSource("local_fs", "本地文件系统", "本地技能扫描", true),
                createSource("mdns_dns_sd", "mDNS/DNS-SD", "服务发现协议", false),
                createSource("dht_kademlia", "DHT/Kademlia", "分布式哈希表发现", false)
            );

            logRequestEnd("getDiscoverySources", sources.size() + " sources", System.currentTimeMillis() - startTime);
            return ResultModel.success(sources);
        } catch (Exception e) {
            logRequestError("getDiscoverySources", e);
            return ResultModel.error(500, "获取发现源失败: " + e.getMessage());
        }
    }

    private DiscoverySourceDTO createSource(String id, String name, String description, boolean available) {
        DiscoverySourceDTO source = new DiscoverySourceDTO();
        source.setId(id);
        source.setName(name);
        source.setDescription(description);
        source.setAvailable(available);
        source.setStatus(available ? "ready" : "unavailable");
        return source;
    }

    @PostMapping("/skills/discovery/list")
    public ResultModel<SkillSearchResultDTO> listDiscoveredSkills(@RequestBody SkillSearchDTO search) {
        long startTime = System.currentTimeMillis();
        logRequestStart("listDiscoveredSkills", search);

        try {
            List<SkillListing> allSkills;
            
            if (search.getSources() != null && !search.getSources().isEmpty()) {
                allSkills = new ArrayList<>();
                for (String source : search.getSources()) {
                    List<SkillListing> sourceSkills = scanFromSource(source, new DiscoveryScanDTO());
                    allSkills.addAll(sourceSkills);
                }
            } else {
                allSkills = marketManager.getAllSkills();
            }
            
            String type = search.getTypes() != null && !search.getTypes().isEmpty() 
                    ? search.getTypes().get(0) : null;
            String capability = search.getCapabilities() != null && !search.getCapabilities().isEmpty() 
                    ? search.getCapabilities().get(0) : null;
            String scene = search.getScenes() != null && !search.getScenes().isEmpty() 
                    ? search.getScenes().get(0) : null;
            String keyword = search.getKeywords() != null && !search.getKeywords().isEmpty() 
                    ? search.getKeywords().get(0) : null;
            int page = search.getPageNum() > 0 ? search.getPageNum() : 1;
            int size = search.getPageSize() > 0 ? search.getPageSize() : 20;
            
            List<SkillListing> filtered = allSkills.stream()
                    .filter(s -> type == null || type.isEmpty() || type.equals(s.getType()))
                    .filter(s -> capability == null || capability.isEmpty() || 
                            (s.getCapabilities() != null && s.getCapabilities().contains(capability)))
                    .filter(s -> scene == null || scene.isEmpty() || 
                            (s.getScenes() != null && s.getScenes().contains(scene)))
                    .filter(s -> keyword == null || keyword.isEmpty() || 
                            (s.getName() != null && s.getName().toLowerCase().contains(keyword.toLowerCase())) ||
                            (s.getDescription() != null && s.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .collect(Collectors.toList());

            int total = filtered.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            
            List<SkillSearchResultDTO.SkillListItemDTO> items = new ArrayList<>();
            if (fromIndex < total) {
                List<SkillListing> paged = filtered.subList(fromIndex, toIndex);
                items = paged.stream()
                        .map(this::convertToListItem)
                        .collect(Collectors.toList());
            }

            SkillSearchResultDTO result = new SkillSearchResultDTO();
            result.setTotal(total);
            result.setSkills(items);

            logRequestEnd("listDiscoveredSkills", total + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("listDiscoveredSkills", e);
            return ResultModel.error(500, "获取技能列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/discovery/get")
    public ResultModel<SkillManifestDTO> getDiscoveredSkill(@RequestBody SkillIdDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("getDiscoveredSkill", request);

        try {
            String id = request.getSkillId();
            SkillListing listing = marketManager.getSkillListing(id);
            if (listing == null) {
                logRequestEnd("getDiscoveredSkill", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("技能不存在");
            }

            SkillManifestDTO manifest = convertToManifest(listing);
            logRequestEnd("getDiscoveredSkill", manifest.getMetadata().getName(), System.currentTimeMillis() - startTime);
            return ResultModel.success(manifest);
        } catch (Exception e) {
            logRequestError("getDiscoveredSkill", e);
            return ResultModel.error(500, "获取技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/discovery/search")
    public ResultModel<SkillSearchResultDTO> searchDiscoveredSkills(@RequestBody SkillSearchDTO search) {
        long startTime = System.currentTimeMillis();
        logRequestStart("searchDiscoveredSkills", search);

        try {
            List<SkillListing> allSkills = marketManager.getAllSkills();
            
            List<SkillListing> filtered = allSkills.stream()
                    .filter(s -> filterByCapabilities(s, search.getCapabilities()))
                    .filter(s -> filterByScenes(s, search.getScenes()))
                    .filter(s -> filterByTypes(s, search.getTypes()))
                    .filter(s -> filterByKeywords(s, search.getKeywords()))
                    .filter(s -> filterByVersion(s, search.getVersion()))
                    .collect(Collectors.toList());

            int total = filtered.size();
            int fromIndex = (search.getPageNum() - 1) * search.getPageSize();
            int toIndex = Math.min(fromIndex + search.getPageSize(), total);
            
            List<SkillSearchResultDTO.SkillListItemDTO> items = new ArrayList<>();
            if (fromIndex < total) {
                List<SkillListing> paged = filtered.subList(fromIndex, toIndex);
                items = paged.stream()
                        .map(this::convertToListItem)
                        .collect(Collectors.toList());
            }

            SkillSearchResultDTO result = new SkillSearchResultDTO();
            result.setTotal(total);
            result.setSkills(items);

            logRequestEnd("searchDiscoveredSkills", total + " skills found", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("searchDiscoveredSkills", e);
            return ResultModel.error(500, "搜索技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/discovery/download")
    public ResultModel<DownloadResultDTO> downloadDiscoveredSkill(@RequestBody SkillIdDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("downloadDiscoveredSkill", request);

        try {
            String id = request.getSkillId();
            SkillListing listing = marketManager.getSkillListing(id);
            if (listing == null) {
                logRequestEnd("downloadDiscoveredSkill", "Not found", System.currentTimeMillis() - startTime);
                return ResultModel.notFound("技能不存在");
            }

            byte[] skillData = marketManager.downloadSkill(id);
            
            DownloadResultDTO result = new DownloadResultDTO();
            result.setSuccess(true);
            result.setSkillId(id);
            result.setFilename(id + "-" + listing.getVersion() + ".zip");
            result.setSize(skillData != null ? skillData.length : 0);
            
            logRequestEnd("downloadDiscoveredSkill", result.getSize() + " bytes", System.currentTimeMillis() - startTime);
            return ResultModel.success(result);
        } catch (Exception e) {
            logRequestError("downloadDiscoveredSkill", e);
            return ResultModel.error(500, "下载技能失败: " + e.getMessage());
        }
    }

    @PostMapping("/skills/share")
    public ResultModel<Boolean> shareSkill(@RequestBody ShareRequestDTO request) {
        long startTime = System.currentTimeMillis();
        logRequestStart("shareSkill", request);

        try {
            if (request.getSkillId() == null || request.getGroupId() == null) {
                logRequestError("shareSkill", new IllegalArgumentException("Skill ID and Group ID are required"));
                return ResultModel.error(400, "Skill ID and Group ID are required");
            }

            if (skillManager.getSkill(request.getSkillId()) == null) {
                logRequestError("shareSkill", new IllegalArgumentException("Skill not found"));
                return ResultModel.notFound("Skill not found");
            }

            if (groupManager.getGroup(request.getGroupId()) == null) {
                logRequestError("shareSkill", new IllegalArgumentException("Group not found"));
                return ResultModel.notFound("Group not found");
            }

            SkillShareDTO share = new SkillShareDTO();
            share.setId("share-" + UUID.randomUUID().toString().substring(0, 8));
            share.setSkillId(request.getSkillId());
            share.setGroupId(request.getGroupId());
            share.setMessage(request.getMessage() != null ? request.getMessage() : "分享了一个技能");
            share.setSharedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            share.setStatus("shared");

            shareMap.put(share.getId(), share);

            logRequestEnd("shareSkill", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("shareSkill", e);
            return ResultModel.error(500, "Failed to share skill: " + e.getMessage());
        }
    }

    @PostMapping("/skills/shared")
    public ResultModel<List<SkillShareDTO>> getSharedSkills() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getSharedSkills", null);

        try {
            List<SkillShareDTO> sharedSkills = new ArrayList<>(shareMap.values());
            logRequestEnd("getSharedSkills", sharedSkills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(sharedSkills);
        } catch (Exception e) {
            logRequestError("getSharedSkills", e);
            return ResultModel.error(500, "Failed to get shared skills: " + e.getMessage());
        }
    }

    @PostMapping("/skills/received")
    public ResultModel<List<ReceivedSkillDTO>> getReceivedSkills() {
        long startTime = System.currentTimeMillis();
        logRequestStart("getReceivedSkills", null);

        try {
            List<ReceivedSkillDTO> receivedSkills = new ArrayList<>();

            ReceivedSkillDTO skill1 = new ReceivedSkillDTO();
            skill1.setId("receive-001");
            skill1.setSkillId("text-analyzer");
            skill1.setSkillName("文本分析");
            skill1.setSharerId("user123");
            skill1.setSharerName("张三");
            skill1.setGroupId("group-001");
            skill1.setGroupName("Development Team");
            skill1.setReceivedAt(LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            skill1.setMessage("分享一个文本分析工具，挺好用的");
            skill1.setStatus("received");
            receivedSkills.add(skill1);

            ReceivedSkillDTO skill2 = new ReceivedSkillDTO();
            skill2.setId("receive-002");
            skill2.setSkillId("image-resizer");
            skill2.setSkillName("图片 resize");
            skill2.setSharerId("user456");
            skill2.setSharerName("李四");
            skill2.setGroupId("group-002");
            skill2.setGroupName("Design Team");
            skill2.setReceivedAt(LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            skill2.setMessage("市场团队的图片处理工具");
            skill2.setStatus("received");
            receivedSkills.add(skill2);

            logRequestEnd("getReceivedSkills", receivedSkills.size() + " skills", System.currentTimeMillis() - startTime);
            return ResultModel.success(receivedSkills);
        } catch (Exception e) {
            logRequestError("getReceivedSkills", e);
            return ResultModel.error(500, "Failed to get received skills: " + e.getMessage());
        }
    }

    @PostMapping("/skills/share/{shareId}/delete")
    public ResultModel<Boolean> unshareSkill(@PathVariable String shareId) {
        long startTime = System.currentTimeMillis();
        logRequestStart("unshareSkill", shareId);

        try {
            if (shareId == null || shareId.isEmpty()) {
                logRequestError("unshareSkill", new IllegalArgumentException("Share ID is required"));
                return ResultModel.error(400, "Share ID is required");
            }

            boolean result = shareMap.remove(shareId) != null;
            if (!result) {
                logRequestError("unshareSkill", new IllegalArgumentException("Share not found"));
                return ResultModel.notFound("Share not found");
            }

            logRequestEnd("unshareSkill", true, System.currentTimeMillis() - startTime);
            return ResultModel.success(true);
        } catch (Exception e) {
            logRequestError("unshareSkill", e);
            return ResultModel.error(500, "Failed to unshare skill: " + e.getMessage());
        }
    }

    private boolean filterByCapabilities(SkillListing s, List<String> capabilities) {
        if (capabilities == null || capabilities.isEmpty()) {
            return true;
        }
        if (s.getCapabilities() == null) {
            return false;
        }
        return capabilities.stream().anyMatch(c -> s.getCapabilities().contains(c));
    }

    private boolean filterByScenes(SkillListing s, List<String> scenes) {
        if (scenes == null || scenes.isEmpty()) {
            return true;
        }
        if (s.getScenes() == null) {
            return false;
        }
        return scenes.stream().anyMatch(scene -> s.getScenes().contains(scene));
    }

    private boolean filterByTypes(SkillListing s, List<String> types) {
        if (types == null || types.isEmpty()) {
            return true;
        }
        return types.contains(s.getType());
    }

    private boolean filterByKeywords(SkillListing s, List<String> keywords) {
        if (keywords == null || keywords.isEmpty()) {
            return true;
        }
        String name = s.getName() != null ? s.getName().toLowerCase() : "";
        String desc = s.getDescription() != null ? s.getDescription().toLowerCase() : "";
        return keywords.stream().anyMatch(k -> 
                name.contains(k.toLowerCase()) || desc.contains(k.toLowerCase()));
    }

    private boolean filterByVersion(SkillListing s, String version) {
        if (version == null || version.isEmpty()) {
            return true;
        }
        String skillVersion = s.getVersion();
        if (skillVersion == null) {
            return false;
        }
        if (version.startsWith(">=")) {
            return compareVersions(skillVersion, version.substring(2)) >= 0;
        } else if (version.startsWith(">")) {
            return compareVersions(skillVersion, version.substring(1)) > 0;
        } else if (version.startsWith("<=")) {
            return compareVersions(skillVersion, version.substring(2)) <= 0;
        } else if (version.startsWith("<")) {
            return compareVersions(skillVersion, version.substring(1)) < 0;
        } else if (version.startsWith("^")) {
            return isCompatibleVersion(skillVersion, version.substring(1));
        } else if (version.startsWith("~")) {
            return isSameMinorVersion(skillVersion, version.substring(1));
        }
        return skillVersion.equals(version);
    }

    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");
        int len = Math.max(parts1.length, parts2.length);
        for (int i = 0; i < len; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }

    private boolean isCompatibleVersion(String actual, String required) {
        String[] actualParts = actual.split("\\.");
        String[] requiredParts = required.split("\\.");
        if (actualParts.length < 1 || requiredParts.length < 1) {
            return false;
        }
        int majorActual = Integer.parseInt(actualParts[0]);
        int majorRequired = Integer.parseInt(requiredParts[0]);
        if (majorActual != majorRequired) {
            return false;
        }
        return compareVersions(actual, required) >= 0;
    }

    private boolean isSameMinorVersion(String actual, String required) {
        String[] actualParts = actual.split("\\.");
        String[] requiredParts = required.split("\\.");
        if (actualParts.length < 2 || requiredParts.length < 2) {
            return false;
        }
        int majorActual = Integer.parseInt(actualParts[0]);
        int majorRequired = Integer.parseInt(requiredParts[0]);
        int minorActual = Integer.parseInt(actualParts[1]);
        int minorRequired = Integer.parseInt(requiredParts[1]);
        return majorActual == majorRequired && minorActual == minorRequired && 
               compareVersions(actual, required) >= 0;
    }

    private SkillSearchResultDTO.SkillListItemDTO convertToListItem(SkillListing listing) {
        SkillSearchResultDTO.SkillListItemDTO item = new SkillSearchResultDTO.SkillListItemDTO();
        item.setId(listing.getSkillId());
        item.setName(listing.getName());
        item.setVersion(listing.getVersion());
        item.setType(listing.getType());
        item.setCapabilities(listing.getCapabilities());
        item.setScenes(listing.getScenes());
        item.setEndpoint(listing.getEndpoint());
        item.setDownloadUrl(listing.getDownloadUrl());
        item.setDescription(listing.getDescription());
        item.setAuthor(listing.getAuthor());
        item.setRating(listing.getRating());
        item.setDownloadCount(listing.getDownloadCount());
        return item;
    }

    private SkillManifestDTO convertToManifest(SkillListing listing) {
        SkillManifestDTO manifest = new SkillManifestDTO();
        
        SkillManifestDTO.Metadata metadata = new SkillManifestDTO.Metadata();
        metadata.setId(listing.getSkillId());
        metadata.setName(listing.getName());
        metadata.setVersion(listing.getVersion());
        metadata.setDescription(listing.getDescription());
        metadata.setAuthor(listing.getAuthor());
        metadata.setLicense(listing.getLicense());
        metadata.setHomepage(listing.getHomepage());
        metadata.setRepository(listing.getRepository());
        manifest.setMetadata(metadata);
        
        SkillManifestDTO.Spec spec = new SkillManifestDTO.Spec();
        spec.setType(listing.getType() != null ? listing.getType() : "tool-skill");
        
        if (listing.getCapabilities() != null && !listing.getCapabilities().isEmpty()) {
            List<SkillManifestDTO.Capability> capabilities = listing.getCapabilities().stream()
                    .map(capId -> {
                        SkillManifestDTO.Capability cap = new SkillManifestDTO.Capability();
                        cap.setId(capId);
                        cap.setName(capId);
                        cap.setDescription("Capability: " + capId);
                        return cap;
                    })
                    .collect(Collectors.toList());
            spec.setCapabilities(capabilities);
        }
        
        if (listing.getScenes() != null && !listing.getScenes().isEmpty()) {
            List<SkillManifestDTO.Scene> scenes = listing.getScenes().stream()
                    .map(sceneName -> {
                        SkillManifestDTO.Scene scene = new SkillManifestDTO.Scene();
                        scene.setName(sceneName);
                        scene.setDescription("Scene: " + sceneName);
                        scene.setCapabilities(listing.getCapabilities());
                        return scene;
                    })
                    .collect(Collectors.toList());
            spec.setScenes(scenes);
        }
        
        SkillManifestDTO.Runtime runtime = new SkillManifestDTO.Runtime();
        runtime.setLanguage("java");
        runtime.setJavaVersion("11");
        runtime.setFramework("spring-boot");
        spec.setRuntime(runtime);
        
        SkillManifestDTO.Deployment deployment = new SkillManifestDTO.Deployment();
        deployment.setModes(Arrays.asList("remote-hosted", "local-deployed"));
        deployment.setSingleton(false);
        deployment.setRequiresAuth(true);
        spec.setDeployment(deployment);
        
        manifest.setSpec(spec);
        
        return manifest;
    }
}
