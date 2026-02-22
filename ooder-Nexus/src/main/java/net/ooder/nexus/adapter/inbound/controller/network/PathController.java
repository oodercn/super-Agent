package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.config.ResultModel;
import net.ooder.nexus.service.NexusNetworkService;
import net.ooder.sdk.api.network.LinkInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/network/path")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class PathController {

    private static final Logger log = LoggerFactory.getLogger(PathController.class);

    @Autowired
    private NexusNetworkService networkService;

    @PostMapping("/optimal")
    public ResultModel<Map<String, Object>> findOptimalPath(@RequestBody Map<String, String> params) {
        String sourceId = params.get("sourceId");
        String targetId = params.get("targetId");
        
        log.info("Finding optimal path: {} -> {}", sourceId, targetId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            List<LinkInfo> path = networkService.findOptimalPath(sourceId, targetId);
            
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("sourceId", sourceId);
            data.put("targetId", targetId);
            data.put("found", !path.isEmpty());
            
            if (!path.isEmpty()) {
                List<Map<String, Object>> pathLinks = new ArrayList<Map<String, Object>>();
                
                for (LinkInfo link : path) {
                    Map<String, Object> linkData = new HashMap<String, Object>();
                    linkData.put("linkId", link.getLinkId());
                    linkData.put("sourceId", link.getSourceId());
                    linkData.put("targetId", link.getTargetId());
                    linkData.put("linkType", link.getType() != null ? link.getType().name() : "UNKNOWN");
                    pathLinks.add(linkData);
                }
                
                data.put("path", pathLinks);
                data.put("hopCount", path.size());
            }
            
            result.setData(data);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to find optimal path", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @PostMapping("/all")
    public ResultModel<List<Map<String, Object>>> findAllPaths(@RequestBody Map<String, Object> params) {
        String sourceId = (String) params.get("sourceId");
        String targetId = (String) params.get("targetId");
        int maxPaths = params.containsKey("maxPaths") ? ((Number) params.get("maxPaths")).intValue() : 5;
        
        log.info("Finding all paths: {} -> {} (max: {})", sourceId, targetId, maxPaths);
        ResultModel<List<Map<String, Object>>> result = new ResultModel<List<Map<String, Object>>>();
        
        try {
            List<List<LinkInfo>> paths = networkService.findAllPaths(sourceId, targetId, maxPaths);
            List<Map<String, Object>> pathList = new ArrayList<Map<String, Object>>();
            
            for (List<LinkInfo> path : paths) {
                Map<String, Object> pathData = new HashMap<String, Object>();
                
                List<Map<String, Object>> pathLinks = new ArrayList<Map<String, Object>>();
                for (LinkInfo link : path) {
                    Map<String, Object> linkData = new HashMap<String, Object>();
                    linkData.put("linkId", link.getLinkId());
                    linkData.put("sourceId", link.getSourceId());
                    linkData.put("targetId", link.getTargetId());
                    pathLinks.add(linkData);
                }
                
                pathData.put("path", pathLinks);
                pathData.put("hopCount", path.size());
                pathList.add(pathData);
            }
            
            result.setData(pathList);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to find all paths", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/topology")
    public ResultModel<Map<String, Object>> getNetworkTopology() {
        log.info("Getting network topology");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        
        try {
            List<LinkInfo> links = networkService.getAllLinks();
            
            Set<String> nodes = new HashSet<String>();
            List<Map<String, Object>> edges = new ArrayList<Map<String, Object>>();
            
            for (LinkInfo link : links) {
                nodes.add(link.getSourceId());
                nodes.add(link.getTargetId());
                
                Map<String, Object> edge = new HashMap<String, Object>();
                edge.put("id", link.getLinkId());
                edge.put("source", link.getSourceId());
                edge.put("target", link.getTargetId());
                edge.put("type", link.getType() != null ? link.getType().name() : "UNKNOWN");
                edges.add(edge);
            }
            
            Map<String, Object> topology = new HashMap<String, Object>();
            topology.put("nodes", new ArrayList<String>(nodes));
            topology.put("edges", edges);
            topology.put("nodeCount", nodes.size());
            topology.put("edgeCount", edges.size());
            
            result.setData(topology);
            result.setRequestStatus(200);
        } catch (Exception e) {
            log.error("Failed to get network topology", e);
            result.setRequestStatus(500);
            result.setMessage(e.getMessage());
        }
        
        return result;
    }
}
