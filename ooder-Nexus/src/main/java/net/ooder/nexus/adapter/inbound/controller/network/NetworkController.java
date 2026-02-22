package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.NexusNetworkService;
import net.ooder.sdk.api.network.LinkInfo;
import net.ooder.sdk.api.network.LinkQualityInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/network")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class NetworkController {

    private static final Logger log = LoggerFactory.getLogger(NetworkController.class);

    @Autowired
    private NexusNetworkService networkService;

    @PostMapping("/links/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createLink(@RequestBody Map<String, String> request) {
        log.info("Create link requested: {} -> {}", request.get("sourceId"), request.get("targetId"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String sourceId = request.get("sourceId");
            String targetId = request.get("targetId");
            String linkType = request.getOrDefault("linkType", "DIRECT");

            LinkInfo link = networkService.createLink(sourceId, targetId, linkType);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("linkId", link.getLinkId());
            data.put("sourceId", link.getSourceId());
            data.put("targetId", link.getTargetId());
            data.put("type", link.getType());
            data.put("status", link.getStatus());

            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Link created successfully");
        } catch (Exception e) {
            log.error("Failed to create link", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to create link: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/links/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getAllLinks() {
        log.info("Get all links requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            List<LinkInfo> links = networkService.getAllLinks();
            List<Map<String, Object>> linkList = new ArrayList<Map<String, Object>>();

            for (LinkInfo link : links) {
                Map<String, Object> linkMap = new HashMap<String, Object>();
                linkMap.put("linkId", link.getLinkId());
                linkMap.put("sourceId", link.getSourceId());
                linkMap.put("targetId", link.getTargetId());
                linkMap.put("type", link.getType() != null ? link.getType().name() : "UNKNOWN");
                linkMap.put("status", link.getStatus() != null ? link.getStatus().name() : "UNKNOWN");
                linkList.add(linkMap);
            }

            result.setData(linkList);
            result.setSize(linkList.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Failed to get links", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get links: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/links/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getLink(@RequestBody Map<String, String> request) {
        log.info("Get link requested: {}", request.get("linkId"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String linkId = request.get("linkId");
            Optional<LinkInfo> linkOpt = networkService.getLink(linkId);

            if (linkOpt.isPresent()) {
                LinkInfo link = linkOpt.get();
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("linkId", link.getLinkId());
                data.put("sourceId", link.getSourceId());
                data.put("targetId", link.getTargetId());
                data.put("type", link.getType() != null ? link.getType().name() : "UNKNOWN");
                data.put("status", link.getStatus() != null ? link.getStatus().name() : "UNKNOWN");
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("Success");
            } else {
                result.setRequestStatus(404);
                result.setMessage("Link not found");
            }
        } catch (Exception e) {
            log.error("Failed to get link", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get link: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/links/remove")
    @ResponseBody
    public ResultModel<Boolean> removeLink(@RequestBody Map<String, String> request) {
        log.info("Remove link requested: {}", request.get("linkId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String linkId = request.get("linkId");
            networkService.removeLink(linkId);
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Link removed successfully");
        } catch (Exception e) {
            log.error("Failed to remove link", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to remove link: " + e.getMessage());
            result.setData(false);
        }

        return result;
    }

    @PostMapping("/links/quality")
    @ResponseBody
    public ResultModel<Map<String, Object>> getLinkQuality(@RequestBody Map<String, String> request) {
        log.info("Get link quality requested: {}", request.get("linkId"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();

        try {
            String linkId = request.get("linkId");
            LinkQualityInfo quality = networkService.getLinkQuality(linkId);

            Map<String, Object> data = new HashMap<String, Object>();
            if (quality != null) {
                data.put("latency", quality.getLatency());
                data.put("packetLoss", quality.getPacketLoss());
                data.put("bandwidth", quality.getBandwidth());
                data.put("qualityLevel", quality.getQualityLevel() != null ? quality.getQualityLevel().name() : "UNKNOWN");
            }
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Failed to get link quality", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get link quality: " + e.getMessage());
        }

        return result;
    }
}
