package net.ooder.examples.skillc.service;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.ooder.examples.skillc.model.Agent;

@Service
public class CoordinationService {
    private static final Logger logger = LoggerFactory.getLogger(CoordinationService.class);
    private final SceneService sceneService;
    private final WebClient.Builder webClientBuilder;
    private final Map<String, Map<String, Object>> agentDataCache = new ConcurrentHashMap<>();

    @Autowired
    public CoordinationService(SceneService sceneService, WebClient.Builder webClientBuilder) {
        this.sceneService = sceneService;
        this.webClientBuilder = webClientBuilder;
    }

    public boolean storeAgentData(String agentId, Map<String, Object> data) {
        agentDataCache.put(agentId, data);
        logger.info("Stored data from agent {}", agentId);
        return true;
    }

    public Map<String, Object> getAgentData(String agentId) {
        return agentDataCache.get(agentId);
    }

    public boolean clearAgentData(String agentId) {
        agentDataCache.remove(agentId);
        logger.info("Cleared data for agent {}", agentId);
        return true;
    }

    public Mono<Map<String, Object>> retrieveDataFromSkillA(String sceneId) {
        List<Agent> participants = sceneService.getSceneParticipants(sceneId);
        Agent skillAAgent = participants.stream()
                .filter(agent -> "skill-a".equals(agent.getType()) && "JOINED".equals(agent.getStatus()))
                .findFirst()
                .orElse(null);

        if (skillAAgent == null) {
            logger.error("Skill A agent not found in scene {}", sceneId);
            return Mono.error(new RuntimeException("Skill A agent not available"));
        }

        logger.info("Retrieving data from Skill A at {}", skillAAgent.getEndpoint());
        return webClientBuilder.baseUrl(skillAAgent.getEndpoint())
                .build()
                .get()
                .uri("/api/v1/skill-a/retrieve")
                .retrieve()
                .bodyToMono(Map.class)
                .doOnSuccess(data -> {
                    logger.info("Successfully retrieved data from Skill A: {}", JSON.toJSONString(data));
                    storeAgentData(skillAAgent.getAgentId(), data);
                })
                .doOnError(error -> logger.error("Failed to retrieve data from Skill A: {}", ((Throwable) error).getMessage()));
    }

    public Mono<Map<String, Object>> submitDataToSkillB(String sceneId, Map<String, Object> data) {
        List<Agent> participants = sceneService.getSceneParticipants(sceneId);
        Agent skillBAgent = participants.stream()
                .filter(agent -> "skill-b".equals(agent.getType()) && "JOINED".equals(agent.getStatus()))
                .findFirst()
                .orElse(null);

        if (skillBAgent == null) {
            logger.error("Skill B agent not found in scene {}", sceneId);
            return Mono.error(new RuntimeException("Skill B agent not available"));
        }

        logger.info("Submitting data to Skill B at {}", skillBAgent.getEndpoint());
        return webClientBuilder.baseUrl(skillBAgent.getEndpoint())
                .build()
                .post()
                .uri("/api/v1/skill-b/submit")
                .bodyValue(data)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnSuccess(result -> {
                    logger.info("Successfully submitted data to Skill B: {}", JSON.toJSONString(result));
                    storeAgentData(skillBAgent.getAgentId(), result);
                })
                .doOnError(error -> logger.error("Failed to submit data to Skill B: {}", ((Throwable) error).getMessage()));
    }

    public Mono<Map<String, Object>> coordinateDataFlow(String sceneId) {
        return retrieveDataFromSkillA(sceneId)
                .flatMap(data -> submitDataToSkillB(sceneId, data))
                .doOnSuccess(result -> logger.info("Successfully coordinated data flow for scene {}", sceneId));
    }

    public Map<String, Map<String, Object>> getAllAgentData() {
        return new ConcurrentHashMap<>(agentDataCache);
    }
}