package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.config.ResultModel;
import net.ooder.nexus.service.NexusNetworkService;
import net.ooder.sdk.api.network.LinkInfo;
import net.ooder.sdk.api.network.LinkQualityInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/network/quality")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class LinkQualityController {

    private static final Logger log = LoggerFactory.getLogger(LinkQualityController.class);

    @Autowired
    private NexusNetworkService networkService;

    @GetMapping("/link/{linkId}")
    public ResultModel<Map<String, Object>> getLinkQuality(@PathVariable String linkId) {
        log.info("Getting quality for link: {}", linkId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            LinkQualityInfo quality = networkService.getLinkQuality(linkId);
            if (quality != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("linkId", linkId);
                data.put("latency", quality.getLatency());
                data.put("packetLoss", quality.getPacketLoss());
                data.put("bandwidth", quality.getBandwidth());
                data.put("qualityLevel", quality.getQualityLevel() != null ? quality.getQualityLevel().name() : "UNKNOWN");
                data.put("score", quality.getScore());
                result.setData(data);
                result.setRequestStatus(200);
            } else {
                result.setRequestStatus(404);
                result.setMessage("Link not found: " + linkId);
            }
        } catch (Exception e) {
            log.error("Failed to get link quality", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/all")
    public ResultModel<List<Map<String, Object>>> getAllLinkQuality() {
        log.info("Getting quality for all links");
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();
        
        try {
            List<LinkInfo> links = networkService.getAllLinks();
            List<Map<String, Object>> qualityList = new ArrayList<Map<String, Object>>();
            
            for (LinkInfo link : links) {
                LinkQualityInfo quality = networkService.getLinkQuality(link.getLinkId());
                if (quality != null) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("linkId", link.getLinkId());
                    data.put("sourceId", link.getSourceId());
                    data.put("targetId", link.getTargetId());
                    data.put("latency", quality.getLatency());
                    data.put("packetLoss", quality.getPacketLoss());
                    data.put("bandwidth", quality.getBandwidth());
                    data.put("qualityLevel", quality.getQualityLevel() != null ? quality.getQualityLevel().name() : "UNKNOWN");
                    data.put("score", quality.getScore());
                    qualityList.add(data);
                }
            }
            
            result.setData(qualityList);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to get all link quality", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/monitor/enable")
    public ResultModel<Void> enableQualityMonitor(@RequestBody Map<String, Object> params) {
        log.info("Enabling quality monitor");
        ResultModel<Void> result = new ResultModel<Void>();
        
        try {
            long intervalMs = params.containsKey("intervalMs") ? 
                ((Number) params.get("intervalMs")).longValue() : 5000L;
            
            networkService.enableQualityMonitor(intervalMs);
            result.setRequestStatus(200);
            result.setMessage("Quality monitor enabled with interval: " + intervalMs + "ms");
        } catch (Exception e) {
            log.error("Failed to enable quality monitor", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/monitor/disable")
    public ResultModel<Void> disableQualityMonitor() {
        log.info("Disabling quality monitor");
        ResultModel<Void> result = new ResultModel<Void>();
        
        try {
            networkService.disableQualityMonitor();
            result.setRequestStatus(200);
            result.setMessage("Quality monitor disabled");
        } catch (Exception e) {
            log.error("Failed to disable quality monitor", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/monitor/status")
    public ResultModel<Map<String, Object>> getMonitorStatus() {
        log.info("Getting monitor status");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("enabled", networkService.isQualityMonitorEnabled());
            result.setData(data);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to get monitor status", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/stats")
    public ResultModel<Map<String, Object>> getNetworkStats() {
        log.info("Getting network stats");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            Map<String, Object> stats = networkService.getNetworkStats();
            result.setData(stats);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to get network stats", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }
}
