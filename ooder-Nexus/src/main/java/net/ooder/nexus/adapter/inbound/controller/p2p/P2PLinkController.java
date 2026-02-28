package net.ooder.nexus.adapter.inbound.controller.p2p;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.p2p.*;
import net.ooder.nexus.service.LinkService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/p2p/links")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class P2PLinkController {

    private static final Logger log = LoggerFactory.getLogger(P2PLinkController.class);

    @Autowired
    private LinkService linkService;

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<LinkDTO> createLink(@RequestBody LinkCreateDTO request) {
        String sourceId = request.getSourceId();
        String targetId = request.getTargetId();
        String type = request.getType() != null ? request.getType() : "DIRECT";

        log.info("Create link request: {} -> {} ({})", sourceId, targetId, type);

        try {
            Map<String, Object> link = linkService.createLink(sourceId, targetId, type);
            if (link.containsKey("error")) {
                return ResultModel.error("创建链路失败: " + link.get("error"), 500);
            }
            return ResultModel.success(convertToLinkDTO(link));
        } catch (Exception e) {
            log.error("Failed to create link", e);
            return ResultModel.error("创建链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<List<LinkDTO>> getAllLinks() {
        log.info("Get all links request");

        try {
            List<Map<String, Object>> links = linkService.getAllLinks();
            List<LinkDTO> result = new ArrayList<>();
            for (Map<String, Object> link : links) {
                result.add(convertToLinkDTO(link));
            }
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to get all links", e);
            return ResultModel.error("获取链路列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<LinkDTO> getLink(@RequestBody LinkQueryDTO request) {
        String linkId = request.getLinkId();

        log.info("Get link request: {}", linkId);

        try {
            Map<String, Object> link = linkService.getLink(linkId);
            if (link.containsKey("error")) {
                return ResultModel.error(link.get("error").toString(), 404);
            }
            return ResultModel.success(convertToLinkDTO(link));
        } catch (Exception e) {
            log.error("Failed to get link", e);
            return ResultModel.error("获取链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/between")
    @ResponseBody
    public ResultModel<LinkDTO> getLinkBetween(@RequestBody LinkQueryDTO request) {
        String sourceId = request.getSourceId();
        String targetId = request.getTargetId();

        log.info("Get link between request: {} -> {}", sourceId, targetId);

        try {
            Map<String, Object> link = linkService.getLinkBetween(sourceId, targetId);
            if (link.containsKey("error")) {
                return ResultModel.error(link.get("error").toString(), 404);
            }
            return ResultModel.success(convertToLinkDTO(link));
        } catch (Exception e) {
            log.error("Failed to get link between", e);
            return ResultModel.error("获取链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResultModel<Boolean> removeLink(@RequestBody LinkQueryDTO request) {
        String linkId = request.getLinkId();

        log.info("Remove link request: {}", linkId);

        try {
            boolean result = linkService.removeLink(linkId);
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to remove link", e);
            return ResultModel.error("移除链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/quality")
    @ResponseBody
    public ResultModel<LinkQualityDTO> getLinkQuality(@RequestBody LinkQueryDTO request) {
        String linkId = request.getLinkId();

        log.info("Get link quality request: {}", linkId);

        try {
            Map<String, Object> quality = linkService.getLinkQuality(linkId);
            if (quality.containsKey("error")) {
                return ResultModel.error(quality.get("error").toString(), 404);
            }
            return ResultModel.success(convertToLinkQualityDTO(quality));
        } catch (Exception e) {
            log.error("Failed to get link quality", e);
            return ResultModel.error("获取链路质量失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/quality/update")
    @ResponseBody
    public ResultModel<Boolean> updateLinkQuality(@RequestBody LinkQualityUpdateDTO request) {
        String linkId = request.getLinkId();
        int latency = request.getLatency() != null ? request.getLatency() : 0;
        double packetLoss = request.getPacketLoss() != null ? request.getPacketLoss() : 0.0;

        log.info("Update link quality request: {} latency={} packetLoss={}", linkId, latency, packetLoss);

        try {
            linkService.updateLinkQuality(linkId, latency, packetLoss);
            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("Failed to update link quality", e);
            return ResultModel.error("更新链路质量失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/path/optimal")
    @ResponseBody
    public ResultModel<List<LinkDTO>> findOptimalPath(@RequestBody PathQueryDTO request) {
        String sourceId = request.getSourceId();
        String targetId = request.getTargetId();

        log.info("Find optimal path request: {} -> {}", sourceId, targetId);

        try {
            List<Map<String, Object>> path = linkService.findOptimalPath(sourceId, targetId);
            List<LinkDTO> result = new ArrayList<>();
            for (Map<String, Object> link : path) {
                result.add(convertToLinkDTO(link));
            }
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to find optimal path", e);
            return ResultModel.error("查找最优路径失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/path/all")
    @ResponseBody
    public ResultModel<List<List<LinkDTO>>> findAllPaths(@RequestBody PathQueryDTO request) {
        String sourceId = request.getSourceId();
        String targetId = request.getTargetId();
        int maxPaths = request.getMaxPaths() != null ? request.getMaxPaths() : 5;

        log.info("Find all paths request: {} -> {} (max {})", sourceId, targetId, maxPaths);

        try {
            List<List<Map<String, Object>>> paths = linkService.findAllPaths(sourceId, targetId, maxPaths);
            List<List<LinkDTO>> result = new ArrayList<>();
            for (List<Map<String, Object>> path : paths) {
                List<LinkDTO> pathLinks = new ArrayList<>();
                for (Map<String, Object> link : path) {
                    pathLinks.add(convertToLinkDTO(link));
                }
                result.add(pathLinks);
            }
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to find all paths", e);
            return ResultModel.error("查找所有路径失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/from")
    @ResponseBody
    public ResultModel<List<LinkDTO>> getLinksFrom(@RequestBody LinkQueryDTO request) {
        String sourceId = request.getSourceId();

        log.info("Get links from request: {}", sourceId);

        try {
            List<Map<String, Object>> links = linkService.getLinksFrom(sourceId);
            List<LinkDTO> result = new ArrayList<>();
            for (Map<String, Object> link : links) {
                result.add(convertToLinkDTO(link));
            }
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to get links from", e);
            return ResultModel.error("获取链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/to")
    @ResponseBody
    public ResultModel<List<LinkDTO>> getLinksTo(@RequestBody LinkQueryDTO request) {
        String targetId = request.getTargetId();

        log.info("Get links to request: {}", targetId);

        try {
            List<Map<String, Object>> links = linkService.getLinksTo(targetId);
            List<LinkDTO> result = new ArrayList<>();
            for (Map<String, Object> link : links) {
                result.add(convertToLinkDTO(link));
            }
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to get links to", e);
            return ResultModel.error("获取链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/stats")
    @ResponseBody
    public ResultModel<NetworkStatsDTO> getNetworkStats() {
        log.info("Get network stats request");

        try {
            Map<String, Object> stats = linkService.getNetworkStats();
            return ResultModel.success(convertToNetworkStatsDTO(stats));
        } catch (Exception e) {
            log.error("Failed to get network stats", e);
            return ResultModel.error("获取网络统计失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/monitor/enable")
    @ResponseBody
    public ResultModel<Boolean> enableQualityMonitor(@RequestBody MonitorEnableDTO request) {
        long intervalMs = request.getIntervalMs() != null ? request.getIntervalMs() : 30000L;

        log.info("Enable quality monitor request: interval={}ms", intervalMs);

        try {
            linkService.enableQualityMonitor(intervalMs);
            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("Failed to enable quality monitor", e);
            return ResultModel.error("启用质量监控失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/monitor/disable")
    @ResponseBody
    public ResultModel<Boolean> disableQualityMonitor() {
        log.info("Disable quality monitor request");

        try {
            linkService.disableQualityMonitor();
            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("Failed to disable quality monitor", e);
            return ResultModel.error("禁用质量监控失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/monitor/status")
    @ResponseBody
    public ResultModel<MonitorStatusDTO> getMonitorStatus() {
        log.info("Get monitor status request");

        try {
            MonitorStatusDTO status = new MonitorStatusDTO();
            status.setEnabled(linkService.isQualityMonitorEnabled());
            status.setLinkCount(linkService.getLinkCount());
            return ResultModel.success(status);
        } catch (Exception e) {
            log.error("Failed to get monitor status", e);
            return ResultModel.error("获取监控状态失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/exists")
    @ResponseBody
    public ResultModel<Boolean> hasLink(@RequestBody LinkQueryDTO request) {
        String sourceId = request.getSourceId();
        String targetId = request.getTargetId();

        log.info("Check link exists request: {} -> {}", sourceId, targetId);

        try {
            boolean exists = linkService.hasLink(sourceId, targetId);
            return ResultModel.success(exists);
        } catch (Exception e) {
            log.error("Failed to check link exists", e);
            return ResultModel.error("检查链路失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/count")
    @ResponseBody
    public ResultModel<LinkCountDTO> getLinkCount() {
        log.info("Get link count request");

        try {
            LinkCountDTO result = new LinkCountDTO();
            result.setCount(linkService.getLinkCount());
            return ResultModel.success(result);
        } catch (Exception e) {
            log.error("Failed to get link count", e);
            return ResultModel.error("获取链路数量失败: " + e.getMessage(), 500);
        }
    }

    private LinkDTO convertToLinkDTO(Map<String, Object> map) {
        LinkDTO dto = new LinkDTO();
        dto.setLinkId((String) map.get("linkId"));
        dto.setSourceId((String) map.get("sourceId"));
        dto.setTargetId((String) map.get("targetId"));
        dto.setType((String) map.get("type"));
        dto.setStatus((String) map.get("status"));
        
        Object latency = map.get("avgLatency");
        if (latency instanceof Number) {
            dto.setLatency(((Number) latency).intValue());
        }
        
        Object packetLoss = map.get("packetLossRate");
        if (packetLoss instanceof Number) {
            dto.setPacketLoss(((Number) packetLoss).doubleValue());
        }
        
        Object createTime = map.get("createTime");
        if (createTime instanceof Number) {
            dto.setCreatedAt(((Number) createTime).longValue());
        }
        
        Object lastActiveTime = map.get("lastActiveTime");
        if (lastActiveTime instanceof Number) {
            dto.setUpdatedAt(((Number) lastActiveTime).longValue());
        }
        
        return dto;
    }

    private LinkQualityDTO convertToLinkQualityDTO(Map<String, Object> map) {
        LinkQualityDTO dto = new LinkQualityDTO();
        dto.setLinkId((String) map.get("linkId"));
        dto.setQualityLevel((String) map.get("qualityLevel"));
        
        Object latency = map.get("latency");
        if (latency instanceof Number) {
            dto.setLatency(((Number) latency).intValue());
        }
        
        Object packetLoss = map.get("packetLoss");
        if (packetLoss instanceof Number) {
            dto.setPacketLoss(((Number) packetLoss).doubleValue());
        }
        
        Object bandwidth = map.get("bandwidth");
        if (bandwidth instanceof Number) {
            dto.setBandwidth(((Number) bandwidth).longValue());
        }
        
        Object score = map.get("score");
        if (score instanceof Number) {
            dto.setScore(((Number) score).intValue());
        }
        
        return dto;
    }

    private NetworkStatsDTO convertToNetworkStatsDTO(Map<String, Object> map) {
        NetworkStatsDTO dto = new NetworkStatsDTO();
        
        Object totalLinks = map.get("totalLinks");
        if (totalLinks instanceof Number) {
            dto.setTotalLinks(((Number) totalLinks).intValue());
        }
        
        Object activeLinks = map.get("activeLinks");
        if (activeLinks instanceof Number) {
            dto.setActiveLinks(((Number) activeLinks).intValue());
        }
        
        Object degradedLinks = map.get("degradedLinks");
        if (degradedLinks instanceof Number) {
            dto.setDegradedLinks(((Number) degradedLinks).intValue());
        }
        
        Object failedLinks = map.get("failedLinks");
        if (failedLinks instanceof Number) {
            dto.setFailedLinks(((Number) failedLinks).intValue());
        }
        
        Object avgLatency = map.get("averageLatency");
        if (avgLatency instanceof Number) {
            dto.setAverageLatency(((Number) avgLatency).doubleValue());
        }
        
        Object avgPacketLoss = map.get("averagePacketLoss");
        if (avgPacketLoss instanceof Number) {
            dto.setAveragePacketLoss(((Number) avgPacketLoss).doubleValue());
        }
        
        return dto;
    }
}
