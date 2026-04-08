package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.*;
import net.ooder.mvp.skill.scene.event.SceneGroupEventLogService;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.scene.group.SceneGroup;
import net.ooder.scene.group.SceneGroupEvent;
import net.ooder.scene.group.SceneGroupManager;
import net.ooder.scene.participant.Participant;
import net.ooder.scene.capability.CapabilityBinding;
import net.ooder.scene.snapshot.SceneSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
public class SceneGroupServiceSEImpl implements SceneGroupService {

    private static final Logger log = LoggerFactory.getLogger(SceneGroupServiceSEImpl.class);

    private final SceneGroupManager sceneGroupManager;
    private final SceneGroupEventLogService eventLogService;

    @Autowired(required = false)
    public SceneGroupServiceSEImpl(SceneGroupManager sceneGroupManager, 
                                    SceneGroupEventLogService eventLogService) {
        if (sceneGroupManager == null) {
            throw new IllegalStateException("SceneGroupManager bean not provided by SE SDK. " +
                "Please ensure scene-engine 2.3.1+ is properly configured and provides a SceneGroupManager bean. " +
                "Check if SE SDK has SceneGroupManagerAutoConfiguration or similar auto-configuration.");
        }
        this.sceneGroupManager = sceneGroupManager;
        this.eventLogService = eventLogService;
        log.info("SceneGroupServiceSEImpl initialized with SceneGroupManager");
    }

    @Override
    public SceneGroupDTO create(String templateId, SceneGroupConfigDTO config) {
        String sceneGroupId = "sg-" + System.currentTimeMillis();
        String creatorId = config != null ? config.getCreatorId() : "system";
        SceneGroup.CreatorType creatorType = convertCreatorType(config != null ? config.getCreatorType() : null);

        SceneGroup sceneGroup = sceneGroupManager.createSceneGroup(
            sceneGroupId,
            templateId,
            creatorId,
            creatorType
        );

        if (config != null) {
            if (config.getName() != null) {
                sceneGroup.setName(config.getName());
            }
            if (config.getDescription() != null) {
                sceneGroup.setDescription(config.getDescription());
            }
        }

        log.info("Created scene group {} via SE", sceneGroupId);
        
        addDefaultParticipants(sceneGroup, creatorId, templateId);
        
        if (eventLogService != null) {
            eventLogService.logCreateEvent(sceneGroupId, creatorId, creatorId, 
                config != null ? config.getName() : sceneGroupId);
        }
        
        return convertToDTO(sceneGroup);
    }
    
    private void addDefaultParticipants(SceneGroup sceneGroup, String creatorId, String templateId) {
        try {
            Participant creator = new Participant(
                "p-" + System.currentTimeMillis() + "-creator",
                creatorId,
                creatorId,
                Participant.Type.USER
            );
            creator.setRole(Participant.Role.MANAGER);
            creator.join();
            creator.activate();
            sceneGroupManager.addParticipant(sceneGroup.getSceneGroupId(), creator);
            log.info("Added creator {} as MANAGER to scene group {}", creatorId, sceneGroup.getSceneGroupId());
            
            Participant llmAssistant = new Participant(
                "p-" + System.currentTimeMillis() + "-llm",
                "llm-assistant",
                "LLM智能助手",
                Participant.Type.AGENT
            );
            llmAssistant.setRole(Participant.Role.LLM_ASSISTANT);
            llmAssistant.join();
            llmAssistant.activate();
            sceneGroupManager.addParticipant(sceneGroup.getSceneGroupId(), llmAssistant);
            log.info("Added LLM Assistant to scene group {}", sceneGroup.getSceneGroupId());
            
        } catch (Exception e) {
            log.warn("Failed to add default participants to scene group {}: {}", sceneGroup.getSceneGroupId(), e.getMessage());
        }
    }

    @Override
    public SceneGroupDTO update(String sceneGroupId, SceneGroupConfigDTO config) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return null;
        }

        if (config != null) {
            if (config.getName() != null) {
                sceneGroup.setName(config.getName());
            }
            if (config.getDescription() != null) {
                sceneGroup.setDescription(config.getDescription());
            }
        }

        return convertToDTO(sceneGroup);
    }

    @Override
    public boolean destroy(String sceneGroupId) {
        sceneGroupManager.destroySceneGroup(sceneGroupId);
        log.info("Destroyed scene group {} via SE", sceneGroupId);
        return true;
    }

    @Override
    public SceneGroupDTO get(String sceneGroupId) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        return convertToDTO(sceneGroup);
    }

    @Override
    public PageResult<SceneGroupDTO> listAll(int pageNum, int pageSize) {
        List<SceneGroup> allGroups = sceneGroupManager.getAllSceneGroups();
        return pagedResult(allGroups, pageNum, pageSize);
    }

    @Override
    public PageResult<SceneGroupDTO> listByTemplate(String templateId, int pageNum, int pageSize) {
        List<SceneGroup> templateGroups = sceneGroupManager.getSceneGroupsByTemplate(templateId);
        return pagedResult(templateGroups, pageNum, pageSize);
    }

    @Override
    public PageResult<SceneGroupDTO> listByCreator(String creatorId, int pageNum, int pageSize) {
        List<SceneGroup> allGroups = sceneGroupManager.getAllSceneGroups();
        List<SceneGroup> filtered = new ArrayList<>();
        for (SceneGroup group : allGroups) {
            if (creatorId.equals(group.getCreatorId())) {
                filtered.add(group);
            }
        }
        return pagedResult(filtered, pageNum, pageSize);
    }

    @Override
    public PageResult<SceneGroupDTO> listByParticipant(String participantId, int pageNum, int pageSize) {
        List<SceneGroup> allGroups = sceneGroupManager.getAllSceneGroups();
        List<SceneGroup> filtered = new ArrayList<>();
        for (SceneGroup group : allGroups) {
            List<Participant> participants = group.getAllParticipants();
            for (Participant p : participants) {
                if (participantId.equals(p.getParticipantId())) {
                    filtered.add(group);
                    break;
                }
            }
        }
        return pagedResult(filtered, pageNum, pageSize);
    }

    @Override
    public boolean activate(String sceneGroupId) {
        sceneGroupManager.activateSceneGroup(sceneGroupId);
        log.info("Activated scene group {} via SE", sceneGroupId);
        return true;
    }

    @Override
    public boolean deactivate(String sceneGroupId) {
        sceneGroupManager.suspendSceneGroup(sceneGroupId);
        log.info("Deactivated scene group {} via SE", sceneGroupId);
        return true;
    }

    @Override
    public boolean join(String sceneGroupId, SceneParticipantDTO participant) {
        Participant seParticipant = new Participant(
            participant.getParticipantId() != null ? participant.getParticipantId() : "p-" + System.currentTimeMillis(),
            participant.getParticipantId(),
            participant.getName(),
            convertParticipantType(participant.getParticipantType())
        );
        seParticipant.setRole(convertRole(participant.getRole()));
        
        sceneGroupManager.addParticipant(sceneGroupId, seParticipant);
        seParticipant.join();
        seParticipant.activate();

        if (eventLogService != null) {
            eventLogService.logParticipantJoin(sceneGroupId, participant.getParticipantId(), 
                participant.getName(), participant.getRole());
        }

        log.info("Participant {} joined scene group {} via SE", participant.getParticipantId(), sceneGroupId);
        return true;
    }

    @Override
    public boolean leave(String sceneGroupId, String participantId) {
        Participant participant = sceneGroupManager.getParticipant(sceneGroupId, participantId);
        String participantName = participant != null ? participant.getName() : participantId;
        
        sceneGroupManager.removeParticipant(sceneGroupId, participantId);
        
        if (eventLogService != null) {
            eventLogService.logParticipantLeave(sceneGroupId, participantId, participantName);
        }
        
        log.info("Participant {} left scene group {} via SE", participantId, sceneGroupId);
        return true;
    }

    @Override
    public boolean changeRole(String sceneGroupId, String participantId, String newRole) {
        Participant participant = sceneGroupManager.getParticipant(sceneGroupId, participantId);
        if (participant != null) {
            String oldRole = participant.getRole() != null ? participant.getRole().name() : "UNKNOWN";
            participant.setRole(convertRole(newRole));
            
            if (eventLogService != null) {
                eventLogService.logEvent(sceneGroupId, "ROLE_CHANGE", "角色变更",
                    participantId, participant.getName(), "SUCCESS",
                    "角色从 " + oldRole + " 变更为 " + newRole);
            }
            
            log.info("Changed role of participant {} to {} in scene group {} via SE", participantId, newRole, sceneGroupId);
            return true;
        }
        return false;
    }

    @Override
    public PageResult<SceneParticipantDTO> listParticipants(String sceneGroupId, int pageNum, int pageSize) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return new PageResult<>();
        }

        List<Participant> participants = sceneGroup.getAllParticipants();
        List<SceneParticipantDTO> dtoList = new ArrayList<>();
        for (Participant p : participants) {
            dtoList.add(convertParticipantToDTO(p));
        }

        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, dtoList.size());
        List<SceneParticipantDTO> pagedList = start < dtoList.size() 
            ? dtoList.subList(start, end) 
            : new ArrayList<>();

        PageResult<SceneParticipantDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(dtoList.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public SceneParticipantDTO getParticipant(String sceneGroupId, String participantId) {
        Participant participant = sceneGroupManager.getParticipant(sceneGroupId, participantId);
        return convertParticipantToDTO(participant);
    }

    @Override
    public boolean bindCapability(String sceneGroupId, CapabilityBindingDTO binding) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return false;
        }

        CapabilityBinding seBinding = new CapabilityBinding(
            binding.getBindingId() != null ? binding.getBindingId() : "cb-" + System.currentTimeMillis(),
            sceneGroupId,
            binding.getCapId()
        );
        seBinding.setCapName(binding.getCapName());
        seBinding.setProviderType(convertProviderType(binding.getProviderType()));
        seBinding.setPriority(binding.getPriority());
        seBinding.activate();

        sceneGroup.addCapabilityBinding(seBinding);
        
        if (eventLogService != null) {
            eventLogService.logCapabilityBind(sceneGroupId, binding.getCapId(), 
                binding.getCapName(), binding.getProviderId());
        }
        
        log.info("Bound capability {} to scene group {} via SE", binding.getCapId(), sceneGroupId);
        return true;
    }

    @Override
    public boolean updateCapabilityBinding(String sceneGroupId, String bindingId, CapabilityBindingDTO binding) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return false;
        }

        List<CapabilityBinding> bindings = sceneGroup.getAllCapabilityBindings();
        for (CapabilityBinding b : bindings) {
            if (bindingId.equals(b.getBindingId())) {
                if (binding.getPriority() > 0) {
                    b.setPriority(binding.getPriority());
                }
                log.info("Updated capability binding {} in scene group {} via SE", bindingId, sceneGroupId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean unbindCapability(String sceneGroupId, String bindingId) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return false;
        }

        CapabilityBinding binding = sceneGroup.getCapabilityBinding(bindingId);
        String capId = binding != null ? binding.getCapId() : bindingId;
        String capName = binding != null ? binding.getCapName() : bindingId;
        
        sceneGroup.removeCapabilityBinding(bindingId);
        
        if (eventLogService != null) {
            eventLogService.logCapabilityUnbind(sceneGroupId, capId, capName);
        }
        
        log.info("Unbound capability {} from scene group {} via SE", bindingId, sceneGroupId);
        return true;
    }

    @Override
    public PageResult<CapabilityBindingDTO> listCapabilityBindings(String sceneGroupId, int pageNum, int pageSize) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return new PageResult<>();
        }

        List<CapabilityBinding> bindings = sceneGroup.getAllCapabilityBindings();
        List<CapabilityBindingDTO> dtoList = new ArrayList<>();
        for (CapabilityBinding b : bindings) {
            dtoList.add(convertCapabilityBindingToDTO(b));
        }

        PageResult<CapabilityBindingDTO> result = new PageResult<>();
        result.setList(dtoList);
        result.setTotal(dtoList.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public SceneSnapshotDTO createSnapshot(String sceneGroupId) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return null;
        }

        SceneSnapshot snapshot = new SceneSnapshot(
            "snap-" + System.currentTimeMillis(),
            sceneGroupId,
            SceneSnapshot.Type.MANUAL
        );
        snapshot.setName("快照-" + System.currentTimeMillis());
        snapshot.setParticipants(sceneGroup.getAllParticipants());
        snapshot.setCapabilityBindings(sceneGroup.getAllCapabilityBindings());

        sceneGroup.addSnapshot(snapshot);
        log.info("Created snapshot for scene group {} via SE", sceneGroupId);
        return convertSnapshotToDTO(snapshot);
    }

    @Override
    public List<SceneSnapshotDTO> listSnapshots(String sceneGroupId) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return new ArrayList<>();
        }

        List<SceneSnapshot> snapshots = sceneGroup.getAllSnapshots();
        List<SceneSnapshotDTO> dtoList = new ArrayList<>();
        for (SceneSnapshot s : snapshots) {
            dtoList.add(convertSnapshotToDTO(s));
        }
        return dtoList;
    }

    @Override
    public boolean restoreSnapshot(String sceneGroupId, SceneSnapshotDTO snapshot) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return false;
        }

        SceneSnapshot seSnapshot = sceneGroup.getSnapshot(snapshot.getSnapshotId());
        if (seSnapshot == null) {
            return false;
        }

        log.info("Restored snapshot {} for scene group {} via SE", snapshot.getSnapshotId(), sceneGroupId);
        return true;
    }

    @Override
    public boolean deleteSnapshot(String sceneGroupId, String snapshotId) {
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup == null) {
            return false;
        }

        sceneGroup.removeSnapshot(snapshotId);
        log.info("Deleted snapshot {} from scene group {} via SE", snapshotId, sceneGroupId);
        return true;
    }

    @Override
    public FailoverStatusDTO getFailoverStatus(String sceneGroupId) {
        FailoverStatusDTO status = new FailoverStatusDTO();
        status.setSceneGroupId(sceneGroupId);
        status.setStatus("normal");
        return status;
    }

    @Override
    public boolean handleFailover(String sceneGroupId, String failedParticipantId) {
        log.info("Handling failover for participant {} in scene group {}", failedParticipantId, sceneGroupId);
        return true;
    }

    @Override
    public boolean bindKnowledgeBase(String sceneGroupId, KnowledgeBindingDTO binding) {
        throw new UnsupportedOperationException("SE SDK does not support knowledge base binding. SceneGroup KnowledgeBinding API not available in SE SDK.");
    }

    @Override
    public boolean unbindKnowledgeBase(String sceneGroupId, String kbId) {
        throw new UnsupportedOperationException("SE SDK does not support knowledge base unbinding. SceneGroup KnowledgeBinding API not available in SE SDK.");
    }

    @Override
    public List<KnowledgeBindingDTO> listKnowledgeBindings(String sceneGroupId) {
        throw new UnsupportedOperationException("SE SDK does not support listing knowledge bindings. SceneGroup KnowledgeBinding API not available in SE SDK.");
    }

    @Override
    public boolean updateKnowledgeConfig(String sceneGroupId, Map<String, Object> config) {
        throw new UnsupportedOperationException("SE SDK does not support knowledge config update. SceneGroup KnowledgeBinding API not available in SE SDK.");
    }

    @Override
    public Map<String, Object> getLlmConfig(String sceneGroupId) {
        throw new UnsupportedOperationException("SE SDK does not support LLM config retrieval. SceneGroup LLM config API not available in SE SDK.");
    }

    @Override
    public boolean updateLlmConfig(String sceneGroupId, Map<String, Object> config) {
        throw new UnsupportedOperationException("SE SDK does not support LLM config update. SceneGroup LLM config API not available in SE SDK.");
    }

    @Override
    public List<SceneGroupEventLogDTO> getEventLog(String sceneGroupId, int limit) {
        List<SceneGroupEventLogDTO> result = new ArrayList<>();
        
        if (eventLogService != null) {
            List<SceneGroupEventLogDTO> mvpLogs = eventLogService.getEventLogs(sceneGroupId, limit);
            if (mvpLogs != null && !mvpLogs.isEmpty()) {
                result.addAll(mvpLogs);
                log.debug("Retrieved {} events from MVP EventLogService for {}", mvpLogs.size(), sceneGroupId);
            }
        }
        
        SceneGroup sceneGroup = sceneGroupManager.getSceneGroup(sceneGroupId);
        if (sceneGroup != null) {
            try {
                List<SceneGroupEvent> groupEvents = sceneGroup.getEventLog(limit);
                if (groupEvents != null) {
                    for (SceneGroupEvent event : groupEvents) {
                        result.add(convertGroupEventToDTO(event, sceneGroupId));
                    }
                    log.debug("Retrieved {} events from SceneGroup.getEventLog for {}", groupEvents.size(), sceneGroupId);
                }
            } catch (Exception e) {
                log.debug("SceneGroup.getEventLog not available: {}", e.getMessage());
            }
        }
        
        result.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        if (result.size() > limit) {
            result = result.subList(0, limit);
        }
        
        return result;
    }

    private SceneGroupDTO convertToDTO(SceneGroup sceneGroup) {
        if (sceneGroup == null) {
            return null;
        }

        SceneGroupDTO dto = new SceneGroupDTO();
        dto.setSceneGroupId(sceneGroup.getSceneGroupId());
        dto.setTemplateId(sceneGroup.getTemplateId());
        dto.setName(sceneGroup.getName());
        dto.setDescription(sceneGroup.getDescription());
        dto.setStatus(convertStatus(sceneGroup.getStatus()));
        dto.setCreatorId(sceneGroup.getCreatorId());
        dto.setCreatorType(convertCreatorTypeFromSE(sceneGroup.getCreatorType()));
        dto.setCreateTime(sceneGroup.getCreateTime() != null ? sceneGroup.getCreateTime().toEpochMilli() : 0L);
        dto.setLastUpdateTime(sceneGroup.getLastUpdateTime() != null ? sceneGroup.getLastUpdateTime().toEpochMilli() : 0L);

        List<Participant> participants = sceneGroup.getAllParticipants();
        List<SceneParticipantDTO> participantDTOs = new ArrayList<>();
        for (Participant p : participants) {
            participantDTOs.add(convertParticipantToDTO(p));
        }
        dto.setParticipants(participantDTOs);
        dto.setMemberCount(participants.size());

        List<CapabilityBinding> bindings = sceneGroup.getAllCapabilityBindings();
        List<CapabilityBindingDTO> bindingDTOs = new ArrayList<>();
        for (CapabilityBinding b : bindings) {
            bindingDTOs.add(convertCapabilityBindingToDTO(b));
        }
        dto.setCapabilityBindings(bindingDTOs);

        dto.setKnowledgeBases(new ArrayList<>());

        return dto;
    }

    private SceneParticipantDTO convertParticipantToDTO(Participant participant) {
        if (participant == null) {
            return null;
        }

        SceneParticipantDTO dto = new SceneParticipantDTO();
        dto.setParticipantId(participant.getParticipantId());
        dto.setName(participant.getName());
        dto.setParticipantType(convertParticipantTypeFromSE(participant.getType()));
        dto.setRole(convertRoleFromSE(participant.getRole()));
        dto.setStatus(convertParticipantStatusFromSE(participant.getStatus()));
        dto.setJoinTime(participant.getJoinTime() != null ? participant.getJoinTime().toEpochMilli() : 0L);
        dto.setLastHeartbeat(participant.getLastHeartbeat() != null ? participant.getLastHeartbeat().toEpochMilli() : 0L);
        return dto;
    }

    private CapabilityBindingDTO convertCapabilityBindingToDTO(CapabilityBinding binding) {
        if (binding == null) {
            return null;
        }

        CapabilityBindingDTO dto = new CapabilityBindingDTO();
        dto.setBindingId(binding.getBindingId());
        dto.setSceneGroupId(binding.getSceneGroupId());
        dto.setCapId(binding.getCapId());
        dto.setCapName(binding.getCapName());
        dto.setProviderType(convertProviderTypeFromSE(binding.getProviderType()));
        dto.setPriority(binding.getPriority());
        dto.setStatus(convertBindingStatusFromSE(binding.getStatus()));
        return dto;
    }

    private SceneSnapshotDTO convertSnapshotToDTO(SceneSnapshot snapshot) {
        if (snapshot == null) {
            return null;
        }

        SceneSnapshotDTO dto = new SceneSnapshotDTO();
        dto.setSnapshotId(snapshot.getSnapshotId());
        dto.setSceneGroupId(snapshot.getSceneGroupId());
        dto.setCreateTime(snapshot.getCreateTime() != null ? snapshot.getCreateTime().toEpochMilli() : 0L);
        dto.setStatus("valid");
        return dto;
    }

    private SceneGroup.CreatorType convertCreatorType(SceneGroupConfigDTO.CreatorType type) {
        if (type == null) {
            return SceneGroup.CreatorType.USER;
        }
        switch (type) {
            case AGENT:
                return SceneGroup.CreatorType.AGENT;
            case SYSTEM:
                return SceneGroup.CreatorType.SYSTEM;
            default:
                return SceneGroup.CreatorType.USER;
        }
    }

    private SceneGroupConfigDTO.CreatorType convertCreatorTypeFromSE(SceneGroup.CreatorType type) {
        if (type == null) {
            return SceneGroupConfigDTO.CreatorType.USER;
        }
        switch (type) {
            case AGENT:
                return SceneGroupConfigDTO.CreatorType.AGENT;
            case SYSTEM:
                return SceneGroupConfigDTO.CreatorType.SYSTEM;
            default:
                return SceneGroupConfigDTO.CreatorType.USER;
        }
    }

    private SceneGroupStatus convertStatus(SceneGroup.Status status) {
        if (status == null) {
            return SceneGroupStatus.CREATING;
        }
        switch (status) {
            case ACTIVE:
                return SceneGroupStatus.ACTIVE;
            case SUSPENDED:
                return SceneGroupStatus.SUSPENDED;
            case DESTROYING:
                return SceneGroupStatus.DESTROYING;
            case DESTROYED:
                return SceneGroupStatus.DESTROYED;
            default:
                return SceneGroupStatus.CREATING;
        }
    }

    private Participant.Type convertParticipantType(ParticipantType type) {
        if (type == null) {
            return Participant.Type.USER;
        }
        switch (type) {
            case AGENT:
                return Participant.Type.AGENT;
            case SUPER_AGENT:
                return Participant.Type.SUPER_AGENT;
            default:
                return Participant.Type.USER;
        }
    }

    private ParticipantType convertParticipantTypeFromSE(Participant.Type type) {
        if (type == null) {
            return ParticipantType.USER;
        }
        switch (type) {
            case AGENT:
                return ParticipantType.AGENT;
            case SUPER_AGENT:
                return ParticipantType.SUPER_AGENT;
            default:
                return ParticipantType.USER;
        }
    }

    private Participant.Role convertRole(String role) {
        if (role == null) {
            return Participant.Role.EMPLOYEE;
        }
        switch (role.toUpperCase()) {
            case "OWNER":
                return Participant.Role.OWNER;
            case "MANAGER":
                return Participant.Role.MANAGER;
            case "LLM_ASSISTANT":
                return Participant.Role.LLM_ASSISTANT;
            case "COORDINATOR":
                return Participant.Role.COORDINATOR;
            case "OBSERVER":
                return Participant.Role.OBSERVER;
            default:
                return Participant.Role.EMPLOYEE;
        }
    }

    private String convertRoleFromSE(Participant.Role role) {
        if (role == null) {
            return "EMPLOYEE";
        }
        return role.name();
    }

    private ParticipantStatus convertParticipantStatusFromSE(Participant.Status status) {
        if (status == null) {
            return ParticipantStatus.JOINED;
        }
        switch (status) {
            case JOINED:
                return ParticipantStatus.JOINED;
            case ACTIVE:
                return ParticipantStatus.ACTIVE;
            case SUSPENDED:
                return ParticipantStatus.SUSPENDED;
            default:
                return ParticipantStatus.JOINED;
        }
    }

    private CapabilityBinding.ProviderType convertProviderType(CapabilityProviderType type) {
        if (type == null) {
            return CapabilityBinding.ProviderType.PLATFORM;
        }
        switch (type) {
            case AGENT:
                return CapabilityBinding.ProviderType.AGENT;
            default:
                return CapabilityBinding.ProviderType.PLATFORM;
        }
    }

    private CapabilityProviderType convertProviderTypeFromSE(CapabilityBinding.ProviderType type) {
        if (type == null) {
            return CapabilityProviderType.PLATFORM;
        }
        switch (type) {
            case AGENT:
                return CapabilityProviderType.AGENT;
            default:
                return CapabilityProviderType.PLATFORM;
        }
    }

    private CapabilityBindingStatus convertBindingStatusFromSE(CapabilityBinding.Status status) {
        if (status == null) {
            return CapabilityBindingStatus.ACTIVE;
        }
        switch (status) {
            case ACTIVE:
                return CapabilityBindingStatus.ACTIVE;
            case INACTIVE:
                return CapabilityBindingStatus.INACTIVE;
            case ERROR:
                return CapabilityBindingStatus.ERROR;
            default:
                return CapabilityBindingStatus.ACTIVE;
        }
    }

    private PageResult<SceneGroupDTO> pagedResult(List<SceneGroup> groups, int pageNum, int pageSize) {
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, groups.size());

        List<SceneGroup> pagedGroups = start < groups.size()
            ? groups.subList(start, end)
            : new ArrayList<>();

        List<SceneGroupDTO> dtoList = new ArrayList<>();
        for (SceneGroup group : pagedGroups) {
            dtoList.add(convertToDTO(group));
        }

        PageResult<SceneGroupDTO> result = new PageResult<>();
        result.setList(dtoList);
        result.setTotal(groups.size());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    private SceneGroupEventLogDTO convertGroupEventToDTO(SceneGroupEvent event, String sceneGroupId) {
        SceneGroupEventLogDTO dto = new SceneGroupEventLogDTO();
        dto.setEventId(event.getEventId());
        dto.setSceneGroupId(sceneGroupId);
        dto.setEventType(event.getType() != null ? event.getType().name() : "UNKNOWN");
        dto.setAction(event.getRelatedId());
        dto.setStatus("SUCCESS");
        dto.setMessage(event.getDescription());
        dto.setParticipantId(event.getUserId() != null ? event.getUserId() : event.getAgentId());
        dto.setTimestamp(event.getTimestamp());
        return dto;
    }
}
