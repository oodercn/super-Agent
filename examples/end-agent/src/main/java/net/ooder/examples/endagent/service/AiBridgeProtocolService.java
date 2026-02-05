package net.ooder.examples.endagent.service;

import com.alibaba.fastjson.JSON;
import net.ooder.examples.endagent.model.AiBridgeMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AiBridgeProtocolService {
    // FastJSON用于JSON处理
    private final NetworkService networkService;
    private final SkillManager skillManager;
    private final SecurityService securityService;
    private final AuthorizationService authorizationService;
    private final CacheService cacheService;

    public AiBridgeProtocolService(NetworkService networkService, SkillManager skillManager, SecurityService securityService, AuthorizationService authorizationService, CacheService cacheService) {
        this.networkService = networkService;
        this.skillManager = skillManager;
        this.securityService = securityService;
        this.authorizationService = authorizationService;
        this.cacheService = cacheService;
    }

    // 创建新消息
    public AiBridgeMessage createMessage(String command, String source, String target, Object params) {
        AiBridgeMessage message = new AiBridgeMessage();
        message.setMessageId(UUID.randomUUID().toString());
        message.setTimestamp(System.currentTimeMillis());
        message.setCommand(command);
        message.setSource(source);
        message.setTarget(target);
        if (params != null) {
            message.setParams(JSON.parseObject(JSON.toJSONString(params), java.util.Map.class));
        }
        return message;
    }

    // 发送消息
    public void sendMessage(AiBridgeMessage message) throws Exception {
        String json = JSON.toJSONString(message);
        networkService.send(json, message.getTarget());
    }

    // 处理接收到的消息
    public AiBridgeMessage handleMessage(String json) throws Exception {
        AiBridgeMessage message = JSON.parseObject(json, AiBridgeMessage.class);
        AiBridgeMessage response = null;

        switch (message.getCommand()) {
            case "skill.discover":
                response = handleSkillDiscover(message);
                break;
            case "skill.invoke":
                response = handleSkillInvoke(message);
                break;
            case "agent.register":
                response = handleAgentRegister(message);
                break;
            case "agent.unregister":
                response = handleAgentUnregister(message);
                break;
            case "resource.list":
                response = handleResourceList(message);
                break;
            case "resource.get":
                response = handleResourceGet(message);
                break;
            case "skill.call": // 兼容旧格式
                response = handleSkillInvoke(message);
                break;
            case "skill.register":
                response = handleSkillRegister(message);
                break;
            case "scene.join":
                response = handleSceneJoin(message);
                break;
            case "scene.leave":
                response = handleSceneLeave(message);
                break;
            case "scene.declare":
                response = handleSceneDeclare(message);
                break;
            case "scene.declare.cancel":
                response = handleSceneDeclareCancel(message);
                break;
            case "scene.query":
                response = handleSceneQuery(message);
                break;
            case "cap.declare":
                response = handleCapDeclare(message);
                break;
            case "cap.update":
                response = handleCapUpdate(message);
                break;
            case "cap.query":
                response = handleCapQuery(message);
                break;
            case "cap.remove":
                response = handleCapRemove(message);
                break;
            case "group.member.add":
                response = handleGroupMemberAdd(message);
                break;
            case "group.member.remove":
                response = handleGroupMemberRemove(message);
                break;
            case "group.link.add":
                response = handleGroupLinkAdd(message);
                break;
            case "group.link.remove":
                response = handleGroupLinkRemove(message);
                break;
            case "group.data.set":
                response = handleGroupDataSet(message);
                break;
            case "group.data.get":
                response = handleGroupDataGet(message);
                break;
            case "cap.vfs.sync":
                response = handleCapVfsSync(message);
                break;
            case "cap.vfs.sync.status":
                response = handleCapVfsSyncStatus(message);
                break;
            case "cap.vfs.recover":
                response = handleCapVfsRecover(message);
                break;
            case "batch.execute":
                response = handleBatchExecute(message);
                break;
            default:
                response = createErrorResponse(message, 400, "Unknown command", "Command not supported: " + message.getCommand());
                break;
        }

        return response;
    }

    // 处理技能发现请求
    private AiBridgeMessage handleSkillDiscover(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 构建缓存键
            String spaceId = (String) (request.getParams() != null ? request.getParams().get("space_id") : null);
            String capability = (String) (request.getParams() != null ? request.getParams().get("capability") : null);
            String cacheKey = "skill_discover:" + (spaceId != null ? spaceId : "") + ":" + (capability != null ? capability : "");

            // 尝试从缓存获取
            Object cachedResult = cacheService.getSkillMetadata(cacheKey);
            if (cachedResult != null) {
                response.setParams((java.util.Map<String, Object>) cachedResult);
                response.setStatus("success");
                return response;
            }

            // 缓存未命中，执行实际查询
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("skills", skillManager.getAllSkills());
            response.setParams(params);
            response.setStatus("success");

            // 存入缓存
            cacheService.putSkillMetadata(cacheKey, params);
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理技能调用请求
    private AiBridgeMessage handleSkillInvoke(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            String skillId = (String) request.getParams().get("skill_id");
            String capabilityId = (String) request.getParams().get("capability_id");
            java.util.Map<String, Object> callParams = (java.util.Map<String, Object>) request.getParams().get("params");

            Object result = skillManager.callCapability(skillId, capabilityId, callParams);
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("result", result);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理技能注册请求
    private AiBridgeMessage handleSkillRegister(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 这里简化处理，实际应该验证和注册技能
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理场景加入请求
    private AiBridgeMessage handleSceneJoin(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 这里简化处理，实际应该验证并加入场景
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理场景离开请求
    private AiBridgeMessage handleSceneLeave(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 这里简化处理，实际应该验证并离开场景
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理智能体注册请求
    private AiBridgeMessage handleAgentRegister(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null || request.getParams().get("agent_id") == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "agent_id is required");
            }

            String agentId = (String) request.getParams().get("agent_id");
            String agentType = (String) request.getParams().get("type");
            java.util.List<String> endpoints = (java.util.List<String>) request.getParams().get("endpoints");

            // 这里简化处理，实际应该将智能体信息存储到数据库或缓存中
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理智能体注销请求
    private AiBridgeMessage handleAgentUnregister(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null || request.getParams().get("agent_id") == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "agent_id is required");
            }

            String agentId = (String) request.getParams().get("agent_id");

            // 这里简化处理，实际应该从数据库或缓存中移除智能体信息
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理资源列表请求
    private AiBridgeMessage handleResourceList(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            String spaceId = (String) request.getParams().get("space_id");
            String resourceType = (String) request.getParams().get("type");

            // 这里简化处理，实际应该根据space_id和type查询资源列表
            java.util.List<Object> resources = new java.util.ArrayList<>();
            java.util.Map<String, Object> resource1 = new java.util.HashMap<>();
            resource1.put("resource_id", "resource_001");
            resource1.put("name", "Resource 1");
            resource1.put("type", "device");
            resources.add(resource1);

            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("resources", resources);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理资源详情请求
    private AiBridgeMessage handleResourceGet(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null || request.getParams().get("resource_id") == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "resource_id is required");
            }

            String resourceId = (String) request.getParams().get("resource_id");

            // 这里简化处理，实际应该根据resource_id查询资源详情
            java.util.Map<String, Object> resource = new java.util.HashMap<>();
            resource.put("resource_id", resourceId);
            resource.put("name", "Resource " + resourceId);
            resource.put("type", "device");
            resource.put("status", "active");

            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("resource", resource);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理场景声明请求
    private AiBridgeMessage handleSceneDeclare(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String sceneType = (String) request.getParams().get("scene_type");
            String skillId = (String) request.getParams().get("skill_id");
            String skillRole = (String) request.getParams().get("skill_role");
            Boolean isOwnerDeclaration = (Boolean) request.getParams().get("is_owner_declaration");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (sceneType == null || skillId == null || skillRole == null || 
                isOwnerDeclaration == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "scene_type, skill_id, skill_role, is_owner_declaration, and cap_id are required");
            }

            // 验证场景声明权限
            if (!authorizationService.hasSceneDeclarationPermission(skillId, sceneType)) {
                return createErrorResponse(request, 403, "Forbidden", 
                    "Skill does not have permission to declare this scene");
            }

            // 对于RouteAgent，验证证书SN和授权状态
            if ("agent_route".equals(skillRole)) {
                String certSn = (String) request.getParams().get("cert_sn");
                if (!securityService.validateCertificateSn(certSn)) {
                    return createErrorResponse(request, 400, "Invalid parameters", 
                        "cert_sn is required for RouteAgent and must be 16 hexadecimal characters");
                }
                // 验证RouteAgent是否经过授权
                if (!authorizationService.isAuthorizedRouteAgent(skillId, certSn)) {
                    return createErrorResponse(request, 403, "Forbidden", 
                        "RouteAgent is not authorized");
                }
            }

            // 这里简化处理，实际应该存储场景声明信息
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("scene_type", sceneType);
            params.put("skill_id", skillId);
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理取消场景声明请求
    private AiBridgeMessage handleSceneDeclareCancel(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String sceneType = (String) request.getParams().get("scene_type");
            String skillId = (String) request.getParams().get("skill_id");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (sceneType == null || skillId == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "scene_type, skill_id, and cap_id are required");
            }

            // 这里简化处理，实际应该从存储中移除场景声明
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理场景查询请求
    private AiBridgeMessage handleSceneQuery(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            String sceneType = (String) request.getParams().get("scene_type");

            // 这里简化处理，实际应该根据scene_type查询场景信息
            java.util.List<java.util.Map<String, Object>> scenes = new java.util.ArrayList<>();
            java.util.Map<String, Object> scene1 = new java.util.HashMap<>();
            scene1.put("scene_type", sceneType != null ? sceneType : "SMART_HOME");
            scene1.put("skill_id", "smart_home_controller_001");
            scene1.put("skill_role", "agent_route");
            scene1.put("cap_id", "smart_home_channel_001");
            scenes.add(scene1);

            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("scenes", scenes);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理Cap声明请求
    private AiBridgeMessage handleCapDeclare(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String capId = (String) request.getParams().get("cap_id");
            String capName = (String) request.getParams().get("cap_name");
            String skillId = (String) request.getParams().get("skill_id");
            String capType = (String) request.getParams().get("cap_type");
            Integer maxMembers = (Integer) request.getParams().get("max_members");
            String dataStorageType = (String) request.getParams().get("data_storage_type");
            String linkType = (String) request.getParams().get("link_type");
            String diskPath = (String) request.getParams().get("disk_path");
            String groupFilePath = (String) request.getParams().get("group_file_path");
            String linksFilePath = (String) request.getParams().get("links_file_path");
            Boolean syncEnabled = (Boolean) request.getParams().get("sync_enabled");
            Integer syncInterval = (Integer) request.getParams().get("sync_interval");

            // 验证必填参数
            if (capId == null || capName == null || skillId == null || capType == null || 
                maxMembers == null || dataStorageType == null || linkType == null || 
                diskPath == null || groupFilePath == null || linksFilePath == null || 
                syncEnabled == null || syncInterval == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "Required parameters are missing");
            }

            // 验证参数约束
            if (maxMembers < 1 || maxMembers > 1000) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "max_members must be between 1 and 1000");
            }

            if (syncInterval < 1 || syncInterval > 3600) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "sync_interval must be between 1 and 3600 seconds");
            }

            // 这里简化处理，实际应该存储Cap声明信息
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("cap_id", capId);
            params.put("cap_name", capName);
            params.put("skill_id", skillId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理Cap更新请求
    private AiBridgeMessage handleCapUpdate(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String capId = (String) request.getParams().get("cap_id");
            java.util.Map<String, Object> capInfo = (java.util.Map<String, Object>) request.getParams().get("cap_info");

            // 验证必填参数
            if (capId == null || capInfo == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "cap_id and cap_info are required");
            }

            // 这里简化处理，实际应该更新Cap信息
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("cap_id", capId);
            params.put("updated_fields", capInfo.keySet());
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理Cap查询请求
    private AiBridgeMessage handleCapQuery(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            String skillId = (String) request.getParams().get("skill_id");
            String capId = (String) request.getParams().get("cap_id");

            // 构建缓存键
            String cacheKey = "cap_query:" + (skillId != null ? skillId : "") + ":" + (capId != null ? capId : "");

            // 尝试从缓存获取
            Object cachedResult = cacheService.getCap(cacheKey);
            if (cachedResult != null) {
                response.setParams((java.util.Map<String, Object>) cachedResult);
                response.setStatus("success");
                return response;
            }

            // 缓存未命中，执行实际查询
            java.util.List<java.util.Map<String, Object>> caps = new java.util.ArrayList<>();
            java.util.Map<String, Object> cap1 = new java.util.HashMap<>();
            cap1.put("cap_id", capId != null ? capId : "device_control_cap_001");
            cap1.put("cap_name", "设备控制能力");
            cap1.put("skill_id", skillId != null ? skillId : "smart_home_controller_001");
            cap1.put("cap_type", "device_control");
            caps.add(cap1);

            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("caps", caps);
            response.setParams(params);
            response.setStatus("success");

            // 存入缓存
            cacheService.putCap(cacheKey, params);
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理Cap移除请求
    private AiBridgeMessage handleCapRemove(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null || request.getParams().get("cap_id") == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "cap_id is required");
            }

            String capId = (String) request.getParams().get("cap_id");

            // 这里简化处理，实际应该移除Cap信息
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理添加频道成员请求
    private AiBridgeMessage handleGroupMemberAdd(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String groupId = (String) request.getParams().get("group_id");
            String skillId = (String) request.getParams().get("skill_id");
            String role = (String) request.getParams().get("role");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (groupId == null || skillId == null || role == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "group_id, skill_id, role, and cap_id are required");
            }

            // 这里简化处理，实际应该将成员添加到频道
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("group_id", groupId);
            params.put("skill_id", skillId);
            params.put("role", role);
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理移除频道成员请求
    private AiBridgeMessage handleGroupMemberRemove(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String groupId = (String) request.getParams().get("group_id");
            String skillId = (String) request.getParams().get("skill_id");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (groupId == null || skillId == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "group_id, skill_id, and cap_id are required");
            }

            // 这里简化处理，实际应该将成员从频道移除
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("group_id", groupId);
            params.put("skill_id", skillId);
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理添加链路关系请求
    private AiBridgeMessage handleGroupLinkAdd(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String groupId = (String) request.getParams().get("group_id");
            String sourceSkillId = (String) request.getParams().get("source_skill_id");
            String targetSkillId = (String) request.getParams().get("target_skill_id");
            String linkType = (String) request.getParams().get("link_type");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (groupId == null || sourceSkillId == null || targetSkillId == null || 
                linkType == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "group_id, source_skill_id, target_skill_id, link_type, and cap_id are required");
            }

            // 这里简化处理，实际应该添加链路关系
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("group_id", groupId);
            params.put("source_skill_id", sourceSkillId);
            params.put("target_skill_id", targetSkillId);
            params.put("link_type", linkType);
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理移除链路关系请求
    private AiBridgeMessage handleGroupLinkRemove(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String groupId = (String) request.getParams().get("group_id");
            String sourceSkillId = (String) request.getParams().get("source_skill_id");
            String targetSkillId = (String) request.getParams().get("target_skill_id");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (groupId == null || sourceSkillId == null || targetSkillId == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "group_id, source_skill_id, target_skill_id, and cap_id are required");
            }

            // 这里简化处理，实际应该移除链路关系
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("group_id", groupId);
            params.put("source_skill_id", sourceSkillId);
            params.put("target_skill_id", targetSkillId);
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理设置频道数据请求
    private AiBridgeMessage handleGroupDataSet(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String groupId = (String) request.getParams().get("group_id");
            String dataKey = (String) request.getParams().get("data_key");
            Object dataValue = request.getParams().get("data_value");
            String dataType = (String) request.getParams().get("data_type");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (groupId == null || dataKey == null || dataValue == null || 
                dataType == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "group_id, data_key, data_value, data_type, and cap_id are required");
            }

            // 这里简化处理，实际应该设置频道数据
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("group_id", groupId);
            params.put("data_key", dataKey);
            params.put("data_value", dataValue);
            params.put("data_type", dataType);
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理获取频道数据请求
    private AiBridgeMessage handleGroupDataGet(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String groupId = (String) request.getParams().get("group_id");
            String dataKey = (String) request.getParams().get("data_key");
            String capId = (String) request.getParams().get("cap_id");

            // 验证必填参数
            if (groupId == null || dataKey == null || capId == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "group_id, data_key, and cap_id are required");
            }

            // 这里简化处理，实际应该获取频道数据
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("group_id", groupId);
            params.put("data_key", dataKey);
            params.put("data_value", "sample_data_value");
            params.put("data_type", "string");
            params.put("cap_id", capId);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理VFS同步请求
    private AiBridgeMessage handleCapVfsSync(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            String capId = (String) request.getParams().get("cap_id");
            String vfsPath = (String) request.getParams().get("vfs_path");

            // 验证必填参数
            if (capId == null || vfsPath == null) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "cap_id and vfs_path are required");
            }

            // 验证参数约束
            if (capId.length() < 1 || capId.length() > 64) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "cap_id must be between 1 and 64 characters");
            }

            if (vfsPath.length() < 1 || vfsPath.length() > 256) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "vfs_path must be between 1 and 256 characters");
            }

            // 这里简化处理，实际应该执行VFS同步操作
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("cap_id", capId);
            params.put("vfs_path", vfsPath);
            params.put("sync_status", "in_progress");
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理VFS同步状态查询请求
    private AiBridgeMessage handleCapVfsSyncStatus(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null || request.getParams().get("cap_id") == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "cap_id is required");
            }

            String capId = (String) request.getParams().get("cap_id");

            // 验证参数约束
            if (capId.length() < 1 || capId.length() > 64) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "cap_id must be between 1 and 64 characters");
            }

            // 这里简化处理，实际应该查询VFS同步状态
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("cap_id", capId);
            params.put("sync_status", "completed");
            params.put("last_sync_time", System.currentTimeMillis());
            params.put("sync_count", 5);
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理VFS数据恢复请求
    private AiBridgeMessage handleCapVfsRecover(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null || request.getParams().get("cap_id") == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "cap_id is required");
            }

            String capId = (String) request.getParams().get("cap_id");

            // 验证参数约束
            if (capId.length() < 1 || capId.length() > 64) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "cap_id must be between 1 and 64 characters");
            }

            // 这里简化处理，实际应该执行VFS数据恢复操作
            java.util.Map<String, Object> params = new java.util.HashMap<>();
            params.put("cap_id", capId);
            params.put("recovery_status", "in_progress");
            params.put("recovery_time", System.currentTimeMillis());
            response.setParams(params);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 处理批量命令执行请求
    private AiBridgeMessage handleBatchExecute(AiBridgeMessage request) {
        AiBridgeMessage response = createResponse(request);
        try {
            // 验证参数
            if (request.getParams() == null) {
                return createErrorResponse(request, 400, "Invalid parameters", "Parameters are required");
            }

            java.util.List<java.util.Map<String, Object>> commands = (java.util.List<java.util.Map<String, Object>>) request.getParams().get("commands");

            // 验证必填参数
            if (commands == null || commands.isEmpty()) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "commands list is required and cannot be empty");
            }

            // 验证命令数量限制
            if (commands.size() > 100) {
                return createErrorResponse(request, 400, "Invalid parameters", 
                    "commands list cannot contain more than 100 commands");
            }

            // 执行每个命令
            java.util.List<java.util.Map<String, Object>> results = new java.util.ArrayList<>();
            for (java.util.Map<String, Object> cmd : commands) {
                String commandName = (String) cmd.get("command");
                java.util.Map<String, Object> cmdParams = (java.util.Map<String, Object>) cmd.get("params");

                // 创建临时消息对象
                AiBridgeMessage tempMessage = new AiBridgeMessage();
                tempMessage.setCommand(commandName);
                tempMessage.setParams(cmdParams);
                tempMessage.setSource(request.getSource());
                tempMessage.setTarget(request.getTarget());

                // 执行命令
                AiBridgeMessage cmdResponse = new AiBridgeMessage();
                switch (commandName) {
                    case "skill.discover":
                        cmdResponse = handleSkillDiscover(tempMessage);
                        break;
                    case "skill.invoke":
                        cmdResponse = handleSkillInvoke(tempMessage);
                        break;
                    case "agent.register":
                        cmdResponse = handleAgentRegister(tempMessage);
                        break;
                    case "agent.unregister":
                        cmdResponse = handleAgentUnregister(tempMessage);
                        break;
                    case "resource.list":
                        cmdResponse = handleResourceList(tempMessage);
                        break;
                    case "resource.get":
                        cmdResponse = handleResourceGet(tempMessage);
                        break;
                    case "skill.call": // 兼容旧格式
                        cmdResponse = handleSkillInvoke(tempMessage);
                        break;
                    case "skill.register":
                        cmdResponse = handleSkillRegister(tempMessage);
                        break;
                    case "scene.join":
                        cmdResponse = handleSceneJoin(tempMessage);
                        break;
                    case "scene.leave":
                        cmdResponse = handleSceneLeave(tempMessage);
                        break;
                    case "scene.declare":
                        cmdResponse = handleSceneDeclare(tempMessage);
                        break;
                    case "scene.declare.cancel":
                        cmdResponse = handleSceneDeclareCancel(tempMessage);
                        break;
                    case "scene.query":
                        cmdResponse = handleSceneQuery(tempMessage);
                        break;
                    case "cap.declare":
                        cmdResponse = handleCapDeclare(tempMessage);
                        break;
                    case "cap.update":
                        cmdResponse = handleCapUpdate(tempMessage);
                        break;
                    case "cap.query":
                        cmdResponse = handleCapQuery(tempMessage);
                        break;
                    case "cap.remove":
                        cmdResponse = handleCapRemove(tempMessage);
                        break;
                    case "group.member.add":
                        cmdResponse = handleGroupMemberAdd(tempMessage);
                        break;
                    case "group.member.remove":
                        cmdResponse = handleGroupMemberRemove(tempMessage);
                        break;
                    case "group.link.add":
                        cmdResponse = handleGroupLinkAdd(tempMessage);
                        break;
                    case "group.link.remove":
                        cmdResponse = handleGroupLinkRemove(tempMessage);
                        break;
                    case "group.data.set":
                        cmdResponse = handleGroupDataSet(tempMessage);
                        break;
                    case "group.data.get":
                        cmdResponse = handleGroupDataGet(tempMessage);
                        break;
                    case "cap.vfs.sync":
                        cmdResponse = handleCapVfsSync(tempMessage);
                        break;
                    case "cap.vfs.sync.status":
                        cmdResponse = handleCapVfsSyncStatus(tempMessage);
                        break;
                    case "cap.vfs.recover":
                        cmdResponse = handleCapVfsRecover(tempMessage);
                        break;
                    default:
                        cmdResponse = createErrorResponse(tempMessage, 400, "Unknown command", "Command not supported: " + commandName);
                        break;
                }

                // 收集结果
                java.util.Map<String, Object> resultEntry = new java.util.HashMap<>();
                resultEntry.put("command", commandName);
                resultEntry.put("status", cmdResponse.getStatus());
                resultEntry.put("result", cmdResponse.getResult());
                resultEntry.put("error", cmdResponse.getError());
                results.add(resultEntry);
            }

            // 设置响应参数
            java.util.Map<String, Object> responseParams = new java.util.HashMap<>();
            responseParams.put("results", results);
            response.setParams(responseParams);
            response.setStatus("success");
        } catch (Exception e) {
            response = createErrorResponse(request, 500, "Internal error", e.getMessage());
        }
        return response;
    }

    // 创建响应消息
    private AiBridgeMessage createResponse(AiBridgeMessage request) {
        AiBridgeMessage response = new AiBridgeMessage();
        response.setMessageId(UUID.randomUUID().toString());
        response.setTimestamp(System.currentTimeMillis());
        response.setResponseTo(request.getMessageId());
        response.setSource(request.getTarget());
        response.setTarget(request.getSource());
        return response;
    }

    // 创建错误响应消息
    private AiBridgeMessage createErrorResponse(AiBridgeMessage request, int code, String message, String details) {
        AiBridgeMessage response = createResponse(request);
        response.setStatus("error");

        AiBridgeMessage.ErrorInfo error = new AiBridgeMessage.ErrorInfo();
        error.setCode(code);
        error.setMessage(message);
        error.setDetails(details);
        response.setError(error);

        return response;
    }


}
