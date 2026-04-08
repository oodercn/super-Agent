package net.ooder.mvp.skill.scene.service.impl;

import net.ooder.mvp.skill.scene.dto.PageResult;
import net.ooder.mvp.skill.scene.dto.scene.SceneTemplateDTO;
import net.ooder.mvp.skill.scene.dto.scene.RoleDefinitionDTO;
import net.ooder.mvp.skill.scene.dto.scene.CapabilityDefDTO;
import net.ooder.mvp.skill.scene.service.SceneTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SceneTemplateServiceImpl implements SceneTemplateService {

    private static final Logger log = LoggerFactory.getLogger(SceneTemplateServiceImpl.class);

    private final Map<String, SceneTemplateDTO> templates = new ConcurrentHashMap<>();
    private final Map<String, List<CapabilityDefDTO>> templateCapabilities = new ConcurrentHashMap<>();
    private final Map<String, List<RoleDefinitionDTO>> templateRoles = new ConcurrentHashMap<>();

    @Override
    public SceneTemplateDTO create(SceneTemplateDTO template) {
        log.info("[create] Creating template: {}", template.getName());
        String templateId = template.getTemplateId() != null ? template.getTemplateId() : "tpl-" + System.currentTimeMillis();
        template.setTemplateId(templateId);
        template.setCreateTime(System.currentTimeMillis());
        templates.put(templateId, template);
        return template;
    }

    @Override
    public SceneTemplateDTO get(String templateId) {
        return templates.get(templateId);
    }

    @Override
    public PageResult<SceneTemplateDTO> listAll(int pageNum, int pageSize) {
        List<SceneTemplateDTO> allTemplates = new ArrayList<>(templates.values());
        int total = allTemplates.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<SceneTemplateDTO> pagedList = fromIndex < total ?
            allTemplates.subList(fromIndex, toIndex) : new ArrayList<>();

        PageResult<SceneTemplateDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public PageResult<SceneTemplateDTO> listByCategory(String category, int pageNum, int pageSize) {
        List<SceneTemplateDTO> filtered = new ArrayList<>();
        for (SceneTemplateDTO tpl : templates.values()) {
            if (category.equals(tpl.getCategory())) {
                filtered.add(tpl);
            }
        }

        int total = filtered.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);

        List<SceneTemplateDTO> pagedList = fromIndex < total ?
            filtered.subList(fromIndex, toIndex) : new ArrayList<>();

        PageResult<SceneTemplateDTO> result = new PageResult<>();
        result.setList(pagedList);
        result.setTotal(total);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    public boolean delete(String templateId) {
        log.info("[delete] Deleting template: {}", templateId);
        templates.remove(templateId);
        templateCapabilities.remove(templateId);
        templateRoles.remove(templateId);
        return true;
    }

    @Override
    public boolean publish(String templateId) {
        log.info("[publish] Publishing template: {}", templateId);
        SceneTemplateDTO tpl = templates.get(templateId);
        if (tpl != null) {
            tpl.setStatus("PUBLISHED");
            return true;
        }
        return false;
    }

    @Override
    public boolean activate(String templateId) {
        log.info("[activate] Activating template: {}", templateId);
        SceneTemplateDTO tpl = templates.get(templateId);
        if (tpl != null) {
            tpl.setStatus("ACTIVE");
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivate(String templateId) {
        log.info("[deactivate] Deactivating template: {}", templateId);
        SceneTemplateDTO tpl = templates.get(templateId);
        if (tpl != null) {
            tpl.setStatus("INACTIVE");
            return true;
        }
        return false;
    }

    @Override
    public boolean addCapability(String templateId, CapabilityDefDTO capability) {
        log.info("[addCapability] Adding capability to template: {}", templateId);
        List<CapabilityDefDTO> caps = templateCapabilities.computeIfAbsent(templateId, k -> new ArrayList<>());
        caps.add(capability);
        return true;
    }

    @Override
    public boolean removeCapability(String templateId, String capId) {
        log.info("[removeCapability] Removing capability from template: {}", capId);
        List<CapabilityDefDTO> caps = templateCapabilities.get(templateId);
        if (caps != null) {
            caps.removeIf(c -> capId.equals(c.getCapId()));
            return true;
        }
        return false;
    }

    @Override
    public boolean addRole(String templateId, RoleDefinitionDTO role) {
        log.info("[addRole] Adding role to template: {}", templateId);
        List<RoleDefinitionDTO> roles = templateRoles.computeIfAbsent(templateId, k -> new ArrayList<>());
        roles.add(role);
        return true;
    }

    @Override
    public boolean removeRole(String templateId, String roleName) {
        log.info("[removeRole] Removing role from template: {}", roleName);
        List<RoleDefinitionDTO> roles = templateRoles.get(templateId);
        if (roles != null) {
            roles.removeIf(r -> roleName.equals(r.getName()));
            return true;
        }
        return false;
    }
}
