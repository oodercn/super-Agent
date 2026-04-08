package net.ooder.agent.dict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DictService {

    private static final Logger log = LoggerFactory.getLogger(DictService.class);
    private final Map<String, DictDTO> dictCache = new HashMap<>();

    public DictService() {
        initDefaultDicts();
    }

    private void initDefaultDicts() {
        initStatusDict();
        initPriorityDict();
        initSkillTypeDict();
        initCapabilityTypeDict();
        initParticipantTypeDict();
        initParticipantRoleDict();
        initParticipantStatusDict();
        initSceneGroupStatusDict();
        initSceneTypeDict();
        initConnectorTypeDict();
        initCapabilityProviderTypeDict();
        initCapabilityBindingStatusDict();
        initTemplateStatusDict();
        initTemplateCategoryDict();
        initKeyTypeDict();
        initKeyStatusDict();
        initAuditEventTypeDict();
        initAuditResultTypeDict();
        initSkillFormDict();
        initCapabilityCategoryDict();

        log.info("DictService initialized with {} dicts", dictCache.size());
    }

    private void initStatusDict() {
        DictDTO dict = new DictDTO("status", "状态字典", "通用状态字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("active", "启用", "1", 1));
        items.add(new DictItemDTO("inactive", "禁用", "0", 2));
        dict.setItems(items);
        dictCache.put("status", dict);
    }

    private void initPriorityDict() {
        DictDTO dict = new DictDTO("priority", "优先级字典", "任务优先级字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("high", "高", "3", 1));
        items.add(new DictItemDTO("medium", "中", "2", 2));
        items.add(new DictItemDTO("low", "低", "1", 3));
        dict.setItems(items);
        dictCache.put("priority", dict);
    }

    private void initSkillTypeDict() {
        DictDTO dict = new DictDTO("skill_type", "技能类型字典", "技能分类字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("system", "系统技能", "system", 1));
        items.add(new DictItemDTO("driver", "驱动技能", "driver", 2));
        items.add(new DictItemDTO("capability", "能力技能", "capability", 3));
        items.add(new DictItemDTO("scene", "场景技能", "scene", 4));
        dict.setItems(items);
        dictCache.put("skill_type", dict);
    }

    private void initCapabilityTypeDict() {
        DictDTO dict = new DictDTO("capability_type", "能力类型字典", "能力分类字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("api", "API能力", "api", 1));
        items.add(new DictItemDTO("service", "服务能力", "service", 2));
        items.add(new DictItemDTO("data", "数据能力", "data", 3));
        dict.setItems(items);
        dictCache.put("capability_type", dict);
    }

    private void initParticipantTypeDict() {
        DictDTO dict = new DictDTO("participant_type", "参与者类型字典", "场景参与者类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("user", "用户", "user", 1));
        items.add(new DictItemDTO("agent", "Agent", "agent", 2));
        items.add(new DictItemDTO("llm", "LLM", "llm", 3));
        items.add(new DictItemDTO("system", "系统", "system", 4));
        dict.setItems(items);
        dictCache.put("participant_type", dict);
    }

    private void initParticipantRoleDict() {
        DictDTO dict = new DictDTO("participant_role", "参与者角色字典", "场景参与者角色字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("manager", "管理者", "manager", 1));
        items.add(new DictItemDTO("employee", "员工", "employee", 2));
        items.add(new DictItemDTO("hr", "HR", "hr", 3));
        items.add(new DictItemDTO("llm-assistant", "LLM助手", "llm-assistant", 4));
        items.add(new DictItemDTO("coordinator", "协调Agent", "coordinator", 5));
        items.add(new DictItemDTO("super-agent", "超级Agent", "super-agent", 6));
        dict.setItems(items);
        dictCache.put("participant_role", dict);
    }

    private void initParticipantStatusDict() {
        DictDTO dict = new DictDTO("participant_status", "参与者状态字典", "场景参与者状态字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("pending", "待接受", "pending", 1));
        items.add(new DictItemDTO("active", "已激活", "active", 2));
        items.add(new DictItemDTO("inactive", "已停用", "inactive", 3));
        items.add(new DictItemDTO("rejected", "已拒绝", "rejected", 4));
        items.add(new DictItemDTO("removed", "已移除", "removed", 5));
        dict.setItems(items);
        dictCache.put("participant_status", dict);
    }

    private void initSceneGroupStatusDict() {
        DictDTO dict = new DictDTO("scene_group_status", "场景组状态字典", "场景组状态字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("draft", "草稿", "draft", 1));
        items.add(new DictItemDTO("active", "运行中", "active", 2));
        items.add(new DictItemDTO("paused", "已暂停", "paused", 3));
        items.add(new DictItemDTO("completed", "已完成", "completed", 4));
        items.add(new DictItemDTO("archived", "已归档", "archived", 5));
        dict.setItems(items);
        dictCache.put("scene_group_status", dict);
    }

    private void initSceneTypeDict() {
        DictDTO dict = new DictDTO("scene_type", "场景类型字典", "场景类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("chat", "对话场景", "chat", 1));
        items.add(new DictItemDTO("workflow", "工作流场景", "workflow", 2));
        items.add(new DictItemDTO("approval", "审批场景", "approval", 3));
        items.add(new DictItemDTO("notification", "通知场景", "notification", 4));
        dict.setItems(items);
        dictCache.put("scene_type", dict);
    }

    private void initConnectorTypeDict() {
        DictDTO dict = new DictDTO("connector_type", "连接器类型字典", "连接器类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("http", "HTTP", "http", 1));
        items.add(new DictItemDTO("websocket", "WebSocket", "websocket", 2));
        items.add(new DictItemDTO("grpc", "gRPC", "grpc", 3));
        items.add(new DictItemDTO("database", "数据库", "database", 4));
        items.add(new DictItemDTO("message_queue", "消息队列", "message_queue", 5));
        dict.setItems(items);
        dictCache.put("connector_type", dict);
    }

    private void initCapabilityProviderTypeDict() {
        DictDTO dict = new DictDTO("capability_provider_type", "能力提供者类型字典", "能力提供者类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("skill", "技能", "skill", 1));
        items.add(new DictItemDTO("external", "外部服务", "external", 2));
        items.add(new DictItemDTO("system", "系统", "system", 3));
        dict.setItems(items);
        dictCache.put("capability_provider_type", dict);
    }

    private void initCapabilityBindingStatusDict() {
        DictDTO dict = new DictDTO("capability_binding_status", "能力绑定状态字典", "能力绑定状态字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("pending", "待激活", "pending", 1));
        items.add(new DictItemDTO("active", "已激活", "active", 2));
        items.add(new DictItemDTO("inactive", "已停用", "inactive", 3));
        items.add(new DictItemDTO("expired", "已过期", "expired", 4));
        dict.setItems(items);
        dictCache.put("capability_binding_status", dict);
    }

    private void initTemplateStatusDict() {
        DictDTO dict = new DictDTO("template_status", "模板状态字典", "模板状态字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("draft", "草稿", "draft", 1));
        items.add(new DictItemDTO("published", "已发布", "published", 2));
        items.add(new DictItemDTO("deprecated", "已弃用", "deprecated", 3));
        dict.setItems(items);
        dictCache.put("template_status", dict);
    }

    private void initTemplateCategoryDict() {
        DictDTO dict = new DictDTO("template_category", "模板分类字典", "模板分类字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("hr", "人力资源", "hr", 1));
        items.add(new DictItemDTO("finance", "财务", "finance", 2));
        items.add(new DictItemDTO("it", "IT", "it", 3));
        items.add(new DictItemDTO("sales", "销售", "sales", 4));
        items.add(new DictItemDTO("general", "通用", "general", 5));
        dict.setItems(items);
        dictCache.put("template_category", dict);
    }

    private void initKeyTypeDict() {
        DictDTO dict = new DictDTO("key_type", "密钥类型字典", "密钥类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("api_key", "API密钥", "api_key", 1));
        items.add(new DictItemDTO("secret", "密钥", "secret", 2));
        items.add(new DictItemDTO("certificate", "证书", "certificate", 3));
        dict.setItems(items);
        dictCache.put("key_type", dict);
    }

    private void initKeyStatusDict() {
        DictDTO dict = new DictDTO("key_status", "密钥状态字典", "密钥状态字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("active", "有效", "active", 1));
        items.add(new DictItemDTO("expired", "已过期", "expired", 2));
        items.add(new DictItemDTO("revoked", "已吊销", "revoked", 3));
        dict.setItems(items);
        dictCache.put("key_status", dict);
    }

    private void initAuditEventTypeDict() {
        DictDTO dict = new DictDTO("audit_event_type", "审计事件类型字典", "审计事件类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("login", "登录", "login", 1));
        items.add(new DictItemDTO("logout", "登出", "logout", 2));
        items.add(new DictItemDTO("create", "创建", "create", 3));
        items.add(new DictItemDTO("update", "更新", "update", 4));
        items.add(new DictItemDTO("delete", "删除", "delete", 5));
        items.add(new DictItemDTO("access", "访问", "access", 6));
        dict.setItems(items);
        dictCache.put("audit_event_type", dict);
    }

    private void initAuditResultTypeDict() {
        DictDTO dict = new DictDTO("audit_result_type", "审计结果类型字典", "审计结果类型字典");
        List<DictItemDTO> items = new ArrayList<>();
        items.add(new DictItemDTO("success", "成功", "success", 1));
        items.add(new DictItemDTO("failure", "失败", "failure", 2));
        items.add(new DictItemDTO("denied", "拒绝", "denied", 3));
        dict.setItems(items);
        dictCache.put("audit_result_type", dict);
    }

    private void initSkillFormDict() {
        DictDTO dict = new DictDTO("skill_form", "能力形态字典", "能力形态分类字典");
        List<DictItemDTO> items = new ArrayList<>();
        for (SkillFormEnum form : SkillFormEnum.values()) {
            items.add(new DictItemDTO(form.getCode(), form.getName(), form.getValue(), form.getSort()));
        }
        dict.setItems(items);
        dictCache.put("skill_form", dict);
    }

    private void initCapabilityCategoryDict() {
        DictDTO dict = new DictDTO("capability_category", "能力分类字典", "能力业务分类字典");
        List<DictItemDTO> items = new ArrayList<>();
        for (CapabilityCategoryEnum cat : CapabilityCategoryEnum.values()) {
            items.add(new DictItemDTO(cat.getCode(), cat.getName(), cat.getValue(), cat.getSort()));
        }
        dict.setItems(items);
        dictCache.put("capability_category", dict);
    }

    public List<DictDTO> getAllDicts() {
        return new ArrayList<>(dictCache.values());
    }

    public DictDTO getDict(String code) {
        return dictCache.get(code);
    }

    public List<DictItemDTO> getDictItems(String code) {
        DictDTO dict = dictCache.get(code);
        return dict != null ? dict.getItems() : new ArrayList<>();
    }

    public DictItemDTO getDictItem(String code, String itemCode) {
        DictDTO dict = dictCache.get(code);
        if (dict != null && dict.getItems() != null) {
            return dict.getItems().stream()
                .filter(item -> itemCode.equals(item.getCode()))
                .findFirst()
                .orElse(null);
        }
        return null;
    }

    public String getDictItemName(String code, String itemCode) {
        DictItemDTO item = getDictItem(code, itemCode);
        return item != null ? item.getName() : itemCode;
    }

    public void refreshCache() {
        dictCache.clear();
        initDefaultDicts();
        log.info("Dict cache refreshed");
    }

    public String getSkillFormName(String code) {
        SkillFormEnum form = SkillFormEnum.fromCode(code);
        return form.getName();
    }

    public String getCapabilityCategoryName(String code) {
        CapabilityCategoryEnum cat = CapabilityCategoryEnum.fromCode(code);
        return cat.getName();
    }
}
