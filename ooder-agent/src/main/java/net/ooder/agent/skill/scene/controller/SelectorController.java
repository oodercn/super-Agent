package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.capability.model.Capability;
import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.SceneGroupService;
import net.ooder.mvp.skill.scene.service.SceneTemplateService;
import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.selector.CapabilityItemDTO;
import net.ooder.mvp.skill.scene.dto.selector.CapabilityTypeDTO;
import net.ooder.mvp.skill.scene.dto.selector.OrgNodeDTO;
import net.ooder.mvp.skill.scene.dto.selector.ProviderItemDTO;
import net.ooder.mvp.skill.scene.dto.selector.SceneGroupItemDTO;
import net.ooder.mvp.skill.scene.dto.selector.TemplateItemDTO;
import net.ooder.mvp.skill.scene.dto.selector.UserNodeDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/selectors")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SelectorController {

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private CapabilityBindingService bindingService;

    @Autowired
    private SceneGroupService sceneGroupService;

    @Autowired
    private SceneTemplateService templateService;

    @GetMapping("/org-tree")
    public ResultModel<List<OrgNodeDTO>> getOrgTree() {
        List<OrgNodeDTO> tree = new ArrayList<OrgNodeDTO>();

        OrgNodeDTO rd = new OrgNodeDTO("dept-rd", "研发部", "department");
        List<OrgNodeDTO> rdChildren = new ArrayList<OrgNodeDTO>();
        rdChildren.add(createUserNode("user-manager-001", "张经理", "manager"));
        rdChildren.add(createUserNode("user-employee-001", "李员工", "employee"));
        rdChildren.add(createUserNode("user-employee-002", "王员工", "employee"));
        rdChildren.add(createUserNode("user-employee-003", "赵员工", "employee"));
        rd.setChildren(rdChildren);
        tree.add(rd);

        OrgNodeDTO hr = new OrgNodeDTO("dept-hr", "人力资源部", "department");
        List<OrgNodeDTO> hrChildren = new ArrayList<OrgNodeDTO>();
        hrChildren.add(createUserNode("user-hr-001", "刘HR", "hr"));
        hr.setChildren(hrChildren);
        tree.add(hr);

        return ResultModel.success(tree);
    }

    private OrgNodeDTO createUserNode(String id, String name, String role) {
        OrgNodeDTO user = new OrgNodeDTO(id, name, "user");
        user.setRole(role);
        return user;
    }

    @GetMapping("/users")
    public ResultModel<List<UserNodeDTO>> getUsers() {
        List<UserNodeDTO> users = new ArrayList<UserNodeDTO>();

        users.add(new UserNodeDTO("user-manager-001", "张经理", "manager", "dept-rd"));
        users.add(new UserNodeDTO("user-employee-001", "李员工", "employee", "dept-rd"));
        users.add(new UserNodeDTO("user-employee-002", "王员工", "employee", "dept-rd"));
        users.add(new UserNodeDTO("user-employee-003", "赵员工", "employee", "dept-rd"));
        users.add(new UserNodeDTO("user-hr-001", "刘HR", "hr", "dept-hr"));

        return ResultModel.success(users);
    }

    @GetMapping("/capabilities")
    public ResultModel<List<CapabilityItemDTO>> getCapabilities(
            @RequestParam(required = false) String type) {
        
        List<Capability> capabilities;
        if (type != null && !type.isEmpty()) {
            capabilities = capabilityService.findByType(
                net.ooder.mvp.skill.scene.capability.model.CapabilityType.valueOf(type));
        } else {
            capabilities = capabilityService.findAll();
        }

        List<CapabilityItemDTO> result = new ArrayList<CapabilityItemDTO>();
        for (Capability cap : capabilities) {
            CapabilityItemDTO item = new CapabilityItemDTO(
                cap.getCapabilityId(),
                cap.getName(),
                cap.getDescription(),
                cap.getCapabilityType() != null ? cap.getCapabilityType().name() : "CUSTOM",
                cap.getStatus() != null ? cap.getStatus().name() : "ENABLED"
            );
            result.add(item);
        }

        return ResultModel.success(result);
    }

    @GetMapping("/capability-types")
    public ResultModel<List<CapabilityTypeDTO>> getCapabilityTypes() {
        List<CapabilityTypeDTO> types = new ArrayList<CapabilityTypeDTO>();

        Map<String, Integer> typeCounts = new HashMap<String, Integer>();
        List<Capability> allCapabilities = capabilityService.findAll();
        for (Capability cap : allCapabilities) {
            String typeName = cap.getCapabilityType() != null ? cap.getCapabilityType().name() : "CUSTOM";
            typeCounts.put(typeName, typeCounts.getOrDefault(typeName, 0) + 1);
        }

        for (net.ooder.mvp.skill.scene.capability.model.CapabilityType type : 
                net.ooder.mvp.skill.scene.capability.model.CapabilityType.values()) {
            CapabilityTypeDTO typeInfo = new CapabilityTypeDTO(
                type.name(),
                type.getName(),
                type.getDescription(),
                typeCounts.getOrDefault(type.name(), 0)
            );
            types.add(typeInfo);
        }

        return ResultModel.success(types);
    }

    @GetMapping("/scene-groups")
    public ResultModel<List<SceneGroupItemDTO>> getSceneGroups() {
        List<SceneGroupItemDTO> result = new ArrayList<SceneGroupItemDTO>();

        try {
            PageResult<?> pageResult = sceneGroupService.listAll(1, 100);
            List<?> groups = pageResult.getList();
            for (Object obj : groups) {
                SceneGroupItemDTO item;
                if (obj instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) obj;
                    item = new SceneGroupItemDTO(
                        (String) map.get("sceneGroupId"),
                        (String) map.get("name"),
                        (String) map.get("description"),
                        map.get("status") != null ? map.get("status").toString() : null
                    );
                } else if (obj instanceof net.ooder.mvp.skill.scene.dto.scene.SceneGroupDTO) {
                    net.ooder.mvp.skill.scene.dto.scene.SceneGroupDTO dto = 
                        (net.ooder.mvp.skill.scene.dto.scene.SceneGroupDTO) obj;
                    item = new SceneGroupItemDTO(
                        dto.getSceneGroupId(),
                        dto.getName(),
                        dto.getDescription(),
                        dto.getStatus() != null ? dto.getStatus().name() : null
                    );
                } else {
                    item = new SceneGroupItemDTO(obj.toString(), obj.toString());
                }
                result.add(item);
            }
        } catch (Exception e) {
            result.add(new SceneGroupItemDTO("sg-daily-report", "日志汇报组"));
            result.add(new SceneGroupItemDTO("sg-project-alpha", "项目Alpha组"));
        }

        return ResultModel.success(result);
    }

    @GetMapping("/templates")
    public ResultModel<List<TemplateItemDTO>> getTemplates() {
        List<TemplateItemDTO> result = new ArrayList<TemplateItemDTO>();

        try {
            PageResult<?> pageResult = templateService.listAll(1, 100);
            List<?> templates = pageResult.getList();
            for (Object obj : templates) {
                TemplateItemDTO item;
                if (obj instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) obj;
                    item = new TemplateItemDTO(
                        (String) map.get("templateId"),
                        (String) map.get("name"),
                        (String) map.get("description")
                    );
                } else if (obj instanceof net.ooder.mvp.skill.scene.dto.scene.SceneTemplateDTO) {
                    net.ooder.mvp.skill.scene.dto.scene.SceneTemplateDTO dto = 
                        (net.ooder.mvp.skill.scene.dto.scene.SceneTemplateDTO) obj;
                    item = new TemplateItemDTO(dto.getTemplateId(), dto.getName(), dto.getDescription());
                } else {
                    item = new TemplateItemDTO(obj.toString(), obj.toString());
                }
                result.add(item);
            }
        } catch (Exception e) {
            result.add(new TemplateItemDTO("tpl-daily-report", "日志汇报模板"));
            result.add(new TemplateItemDTO("tpl-meeting", "会议模板"));
        }

        return ResultModel.success(result);
    }

    @GetMapping("/providers")
    public ResultModel<List<ProviderItemDTO>> getProviders(
            @RequestParam(required = false) String type) {
        
        List<ProviderItemDTO> result = new ArrayList<ProviderItemDTO>();

        if ("SKILL".equals(type) || type == null) {
            result.add(new ProviderItemDTO("skill-daily-report", "日志汇报技能", "SKILL"));
            result.add(new ProviderItemDTO("skill-notification", "通知技能", "SKILL"));
        }
        if ("AGENT".equals(type) || type == null) {
            result.add(new ProviderItemDTO("agent-llm-001", "LLM分析助手", "AGENT"));
            result.add(new ProviderItemDTO("agent-coordinator-001", "协调Agent", "AGENT"));
        }
        if ("SUPER_AGENT".equals(type) || type == null) {
            result.add(new ProviderItemDTO("super-agent-001", "超级Agent", "SUPER_AGENT"));
        }
        if ("DEVICE".equals(type) || type == null) {
            result.add(new ProviderItemDTO("device-sensor-001", "传感器设备", "DEVICE"));
        }

        return ResultModel.success(result);
    }
}
