package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.RadarService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class RadarServiceImpl implements RadarService {

    private static final Logger log = LoggerFactory.getLogger(RadarServiceImpl.class);

    private boolean radarModeActive = false;
    private final List<RadarListener> listeners = new CopyOnWriteArrayList<RadarListener>();

    public RadarServiceImpl() {
        log.info("RadarServiceImpl initialized with SDK 0.7.2");
    }

    @Override
    public CompletableFuture<Void> startRadarMode() {
        log.info("Starting radar mode");
        
        return CompletableFuture.runAsync(() -> {
            radarModeActive = true;
            notifyProgress(0, "雷达模式启动中");
            
            notifyProgress(50, "扫描公共资源");
            
            notifyProgress(100, "雷达模式已启动");
            log.info("Radar mode started successfully");
        });
    }

    @Override
    public CompletableFuture<Void> stopRadarMode() {
        log.info("Stopping radar mode");
        
        return CompletableFuture.runAsync(() -> {
            radarModeActive = false;
            log.info("Radar mode stopped");
        });
    }

    @Override
    public boolean isRadarModeActive() {
        return radarModeActive;
    }

    @Override
    public CompletableFuture<List<PublicSkill>> scanPublicSkills(String category) {
        log.info("Scanning public skills, category: {}", category);
        
        return CompletableFuture.supplyAsync(() -> {
            List<PublicSkill> skills = new ArrayList<PublicSkill>();
            
            PublicSkill skill1 = new PublicSkill();
            skill1.setSkillId("skill-data-analysis");
            skill1.setSkillName("数据分析工具");
            skill1.setDescription("提供数据清洗、统计分析、可视化等能力");
            skill1.setCategory("数据分析");
            skill1.setVersion("1.2.0");
            skill1.setAuthor("ooder Team");
            skill1.setRating(4.8);
            skill1.setDownloadCount(15000);
            skills.add(skill1);
            
            PublicSkill skill2 = new PublicSkill();
            skill2.setSkillId("skill-file-converter");
            skill2.setSkillName("文件格式转换");
            skill2.setDescription("支持PDF、Word、Excel等格式互转");
            skill2.setCategory("文件处理");
            skill2.setVersion("2.0.1");
            skill2.setAuthor("Community");
            skill2.setRating(4.5);
            skill2.setDownloadCount(8500);
            skills.add(skill2);
            
            PublicSkill skill3 = new PublicSkill();
            skill3.setSkillId("skill-smart-assistant");
            skill3.setSkillName("智能助手");
            skill3.setDescription("AI驱动的智能对话助手");
            skill3.setCategory("AI助手");
            skill3.setVersion("3.1.0");
            skill3.setAuthor("ooder AI");
            skill3.setRating(4.9);
            skill3.setDownloadCount(25000);
            skills.add(skill3);
            
            return skills;
        });
    }

    @Override
    public CompletableFuture<List<PublicResource>> scanPublicResources(String resourceType) {
        log.info("Scanning public resources, type: {}", resourceType);
        
        return CompletableFuture.supplyAsync(() -> {
            List<PublicResource> resources = new ArrayList<PublicResource>();
            
            PublicResource res1 = new PublicResource();
            res1.setResourceId("res-dataset-sales");
            res1.setResourceName("销售数据集");
            res1.setResourceType("DATASET");
            res1.setDescription("2025年全球销售数据样本");
            res1.setProvider("ooder Data");
            res1.setAccessUrl("/api/public/resources/res-dataset-sales");
            res1.setSize(1024 * 1024 * 50);
            resources.add(res1);
            
            PublicResource res2 = new PublicResource();
            res2.setResourceId("res-model-nlp");
            res2.setResourceName("NLP基础模型");
            res2.setResourceType("MODEL");
            res2.setDescription("多语言自然语言处理基础模型");
            res2.setProvider("ooder AI");
            res2.setAccessUrl("/api/public/resources/res-model-nlp");
            res2.setSize(1024 * 1024 * 500);
            resources.add(res2);
            
            return resources;
        });
    }

    @Override
    public CompletableFuture<List<DiscoveredNode>> discoverNearbyNodes() {
        log.info("Discovering nearby nodes");
        
        return CompletableFuture.supplyAsync(() -> {
            List<DiscoveredNode> nodes = new ArrayList<DiscoveredNode>();
            
            DiscoveredNode node1 = new DiscoveredNode();
            node1.setNodeId("mcp-local-001");
            node1.setNodeName("本地MCP节点");
            node1.setNodeType("MCP");
            node1.setAddress("192.168.1.100");
            node1.setPort(9876);
            node1.setStatus("ONLINE");
            node1.setLastSeen(System.currentTimeMillis());
            nodes.add(node1);
            notifyNodeDiscovered(node1);
            
            DiscoveredNode node2 = new DiscoveredNode();
            node2.setNodeId("route-local-001");
            node2.setNodeName("路由节点A");
            node2.setNodeType("ROUTE");
            node2.setAddress("192.168.1.101");
            node2.setPort(9876);
            node2.setStatus("ONLINE");
            node2.setLastSeen(System.currentTimeMillis() - 5000);
            nodes.add(node2);
            notifyNodeDiscovered(node2);
            
            return nodes;
        });
    }

    @Override
    public CompletableFuture<List<SearchResult>> searchResources(String keyword) {
        log.info("Searching resources with keyword: {}", keyword);
        
        return CompletableFuture.supplyAsync(() -> {
            List<SearchResult> results = new ArrayList<SearchResult>();
            
            SearchResult result1 = new SearchResult();
            result1.setResultId("skill-data-analysis");
            result1.setResultType("SKILL");
            result1.setTitle("数据分析工具");
            result1.setDescription("强大的数据分析技能");
            result1.setRelevance(0.95);
            results.add(result1);
            
            SearchResult result2 = new SearchResult();
            result2.setResultId("res-dataset-sales");
            result2.setResultType("DATASET");
            result2.setTitle("销售数据集");
            result2.setDescription("2025年销售数据");
            result2.setRelevance(0.85);
            results.add(result2);
            
            return results;
        });
    }

    @Override
    public CompletableFuture<List<RecommendedResource>> getRecommendedResources(int limit) {
        log.info("Getting recommended resources, limit: {}", limit);
        
        return CompletableFuture.supplyAsync(() -> {
            List<RecommendedResource> recommendations = new ArrayList<RecommendedResource>();
            
            RecommendedResource rec1 = new RecommendedResource();
            rec1.setResourceId("skill-smart-assistant");
            rec1.setResourceName("智能助手");
            rec1.setResourceType("SKILL");
            rec1.setReason("热门推荐");
            rec1.setScore(4.9);
            recommendations.add(rec1);
            
            RecommendedResource rec2 = new RecommendedResource();
            rec2.setResourceId("skill-data-analysis");
            rec2.setResourceName("数据分析工具");
            rec2.setResourceType("SKILL");
            rec2.setReason("基于您的使用习惯");
            rec2.setScore(4.8);
            recommendations.add(rec2);
            
            return recommendations.subList(0, Math.min(limit, recommendations.size()));
        });
    }

    @Override
    public void addRadarListener(RadarListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeRadarListener(RadarListener listener) {
        listeners.remove(listener);
    }

    private void notifyNodeDiscovered(DiscoveredNode node) {
        for (RadarListener listener : listeners) {
            try {
                listener.onNodeDiscovered(node);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }

    private void notifyProgress(int progress, String stage) {
        for (RadarListener listener : listeners) {
            try {
                listener.onScanProgress(progress, stage);
            } catch (Exception e) {
                log.warn("Listener error: {}", e.getMessage());
            }
        }
    }
}
