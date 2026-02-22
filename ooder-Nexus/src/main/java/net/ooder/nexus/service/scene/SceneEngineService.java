package net.ooder.nexus.service.scene;

import net.ooder.scene.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class SceneEngineService {

    private static final Logger log = LoggerFactory.getLogger(SceneEngineService.class);

    private final SceneClient sceneClient;
    private final AdminClient adminClient;
    private final String localUserId;

    @Autowired
    public SceneEngineService(@Autowired(required = false) SceneClient sceneClient,
                              @Autowired(required = false) AdminClient adminClient) {
        this.sceneClient = sceneClient;
        this.adminClient = adminClient;
        this.localUserId = "nexus-" + UUID.randomUUID().toString().substring(0, 8);
        
        log.info("SceneEngineService initialized - SceneClient: {}, AdminClient: {}",
            sceneClient != null ? "available" : "not available",
            adminClient != null ? "available" : "not available");
    }

    public boolean isAvailable() {
        return sceneClient != null;
    }

    public CompletableFuture<SceneGroupInfo> createSceneGroup(String sceneId, String groupName, int maxMembers) {
        log.info("Creating scene group for scene: {}, name: {}", sceneId, groupName);

        if (adminClient == null) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            SceneGroupInfo groupInfo = new SceneGroupInfo();
            groupInfo.setSceneId(sceneId);
            groupInfo.setName(groupName);
            
            return CompletableFuture.supplyAsync(() -> {
                try {
                    SceneGroupInfo created = adminClient.createSceneGroup(groupInfo);
                    log.info("Scene group created: {}", created.getGroupId());
                    return created;
                } catch (Exception e) {
                    log.error("Failed to create scene group: {}", e.getMessage(), e);
                    return null;
                }
            });
        } catch (Exception e) {
            log.error("Error creating scene group: {}", e.getMessage(), e);
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<SceneGroupInfo> getSceneGroup(String groupId) {
        log.info("Getting scene group: {}", groupId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return sceneClient.getSceneGroup(groupId);
            } catch (Exception e) {
                log.error("Failed to get scene group: {}", e.getMessage(), e);
                return null;
            }
        });
    }

    public CompletableFuture<List<SceneGroupInfo>> listMySceneGroups() {
        log.info("Listing my scene groups");

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(new ArrayList<SceneGroupInfo>());
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return sceneClient.listMySceneGroups();
            } catch (Exception e) {
                log.error("Failed to list scene groups: {}", e.getMessage(), e);
                return new ArrayList<SceneGroupInfo>();
            }
        });
    }

    public CompletableFuture<Boolean> joinSceneGroup(String groupId, String inviteCode) {
        log.info("Joining scene group: {}", groupId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                sceneClient.joinSceneGroup(groupId, inviteCode);
                log.info("Successfully joined scene group: {}", groupId);
                return true;
            } catch (Exception e) {
                log.error("Failed to join scene group: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> leaveSceneGroup(String groupId) {
        log.info("Leaving scene group: {}", groupId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                sceneClient.leaveSceneGroup(groupId);
                log.info("Successfully left scene group: {}", groupId);
                return true;
            } catch (Exception e) {
                log.error("Failed to leave scene group: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> deleteSceneGroup(String groupId) {
        log.info("Deleting scene group: {}", groupId);

        if (adminClient == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                adminClient.deleteSceneGroup(groupId);
                log.info("Successfully deleted scene group: {}", groupId);
                return true;
            } catch (Exception e) {
                log.error("Failed to delete scene group: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<List<SceneMemberInfo>> listSceneGroupMembers(String groupId) {
        log.info("Listing members of scene group: {}", groupId);

        if (adminClient == null) {
            return CompletableFuture.completedFuture(new ArrayList<SceneMemberInfo>());
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return adminClient.listSceneGroupMembers(groupId);
            } catch (Exception e) {
                log.error("Failed to list scene group members: {}", e.getMessage(), e);
                return new ArrayList<SceneMemberInfo>();
            }
        });
    }

    public CompletableFuture<Boolean> removeSceneGroupMember(String groupId, String memberId) {
        log.info("Removing member {} from scene group: {}", memberId, groupId);

        if (adminClient == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                adminClient.removeSceneGroupMember(groupId, memberId);
                log.info("Successfully removed member {} from scene group: {}", memberId, groupId);
                return true;
            } catch (Exception e) {
                log.error("Failed to remove scene group member: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<HeartbeatResult> startHeartbeat(String groupId) {
        log.info("Starting heartbeat for scene group: {}", groupId);

        if (sceneClient == null) {
            HeartbeatResult result = new HeartbeatResult();
            result.setGroupId(groupId);
            result.setSuccess(false);
            result.setActiveMembers(new ArrayList<String>());
            result.setInactiveMembers(new ArrayList<String>());
            return CompletableFuture.completedFuture(result);
        }

        return sceneClient.startHeartbeat(groupId);
    }

    public void stopHeartbeat(String groupId) {
        log.info("Stopping heartbeat for scene group: {}", groupId);

        if (sceneClient != null) {
            try {
                sceneClient.stopHeartbeat(groupId);
                log.info("Heartbeat stopped for scene group: {}", groupId);
            } catch (Exception e) {
                log.error("Failed to stop heartbeat: {}", e.getMessage(), e);
            }
        }
    }

    public HeartbeatStatus getHeartbeatStatus(String groupId) {
        log.info("Getting heartbeat status for scene group: {}", groupId);

        if (sceneClient == null) {
            return null;
        }

        try {
            return sceneClient.getHeartbeatStatus(groupId);
        } catch (Exception e) {
            log.error("Failed to get heartbeat status: {}", e.getMessage(), e);
            return null;
        }
    }

    public CompletableFuture<SkillInfo> findSkill(String skillId) {
        log.info("Finding skill: {}", skillId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return sceneClient.findSkill(skillId);
            } catch (Exception e) {
                log.error("Failed to find skill: {}", e.getMessage(), e);
                return null;
            }
        });
    }

    public CompletableFuture<List<SkillInfo>> searchSkills(String keyword, String category) {
        log.info("Searching skills with keyword: {}, category: {}", keyword, category);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(new ArrayList<SkillInfo>());
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                SkillQuery query = new SkillQuery();
                query.setKeyword(keyword);
                query.setCategory(category);
                return sceneClient.searchSkills(query);
            } catch (Exception e) {
                log.error("Failed to search skills: {}", e.getMessage(), e);
                return new ArrayList<SkillInfo>();
            }
        });
    }

    public CompletableFuture<Boolean> installSkill(String skillId, String groupId) {
        log.info("Installing skill {} to group: {}", skillId, groupId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                Map<String, Object> config = new HashMap<String, Object>();
                config.put("groupId", groupId);
                sceneClient.installSkill(skillId, config);
                log.info("Skill {} installed to group: {}", skillId, groupId);
                return true;
            } catch (Exception e) {
                log.error("Failed to install skill: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<Boolean> uninstallSkill(String skillId) {
        log.info("Uninstalling skill: {}", skillId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(false);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                sceneClient.uninstallSkill(skillId);
                log.info("Skill {} uninstalled", skillId);
                return true;
            } catch (Exception e) {
                log.error("Failed to uninstall skill: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<Object> invokeCapability(String skillId, String capabilityId, Map<String, Object> params) {
        log.info("Invoking capability {} of skill: {}", capabilityId, skillId);

        if (sceneClient == null) {
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> {
            try {
                return sceneClient.invokeCapability(skillId, capabilityId, params);
            } catch (Exception e) {
                log.error("Failed to invoke capability: {}", e.getMessage(), e);
                return null;
            }
        });
    }

    public String getLocalUserId() {
        return localUserId;
    }
}
