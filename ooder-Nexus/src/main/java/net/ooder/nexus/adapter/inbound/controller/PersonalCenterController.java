package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.personal.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PersonalCenterController {

    @GetMapping("/installed-skills")
    public ResultModel<List<InstalledSkillDTO>> getInstalledSkills() {
        List<InstalledSkillDTO> skills = new ArrayList<>();
        
        skills.add(createMockSkill("1", "数据抽取技能", "1.0.0", "RUNNING"));
        skills.add(createMockSkill("2", "文件同步技能", "1.2.0", "RUNNING"));
        skills.add(createMockSkill("3", "消息推送技能", "0.9.0", "STOPPED"));
        skills.add(createMockSkill("4", "日志分析技能", "2.0.0", "RUNNING"));
        skills.add(createMockSkill("5", "网络监控技能", "1.5.0", "RUNNING"));
        
        return ResultModel.success("获取成功", skills);
    }

    @GetMapping("/groups")
    public ResultModel<List<GroupDTO>> getGroups() {
        List<GroupDTO> groups = new ArrayList<>();
        
        groups.add(createMockGroup("1", "研发团队", 8, "active"));
        groups.add(createMockGroup("2", "运维团队", 5, "active"));
        
        return ResultModel.success("获取成功", groups);
    }

    @GetMapping("/executions")
    public ResultModel<List<ExecutionDTO>> getExecutions() {
        List<ExecutionDTO> executions = new ArrayList<>();
        
        executions.add(createMockExecution("1", "数据抽取任务", "completed", "2026-02-28 10:30:00"));
        executions.add(createMockExecution("2", "文件同步任务", "running", "2026-02-28 11:00:00"));
        executions.add(createMockExecution("3", "日志分析任务", "completed", "2026-02-28 09:15:00"));
        
        return ResultModel.success("获取成功", executions);
    }

    @GetMapping("/shared-skills")
    public ResultModel<List<SharedSkillDTO>> getSharedSkills() {
        List<SharedSkillDTO> sharedSkills = new ArrayList<>();
        
        sharedSkills.add(createMockSharedSkill("1", "数据抽取技能", "张三", "2026-02-27"));
        sharedSkills.add(createMockSharedSkill("2", "日志分析技能", "李四", "2026-02-26"));
        
        return ResultModel.success("获取成功", sharedSkills);
    }

    private InstalledSkillDTO createMockSkill(String id, String name, String version, String status) {
        InstalledSkillDTO skill = new InstalledSkillDTO();
        skill.setId(id);
        skill.setName(name);
        skill.setVersion(version);
        skill.setStatus(status);
        skill.setInstallTime("2026-02-20 10:00:00");
        return skill;
    }

    private GroupDTO createMockGroup(String id, String name, int memberCount, String status) {
        GroupDTO group = new GroupDTO();
        group.setId(id);
        group.setName(name);
        group.setMemberCount(memberCount);
        group.setStatus(status);
        return group;
    }

    private ExecutionDTO createMockExecution(String id, String name, String status, String time) {
        ExecutionDTO execution = new ExecutionDTO();
        execution.setId(id);
        execution.setName(name);
        execution.setStatus(status);
        execution.setExecuteTime(time);
        return execution;
    }

    private SharedSkillDTO createMockSharedSkill(String id, String name, String sharedBy, String sharedTime) {
        SharedSkillDTO skill = new SharedSkillDTO();
        skill.setId(id);
        skill.setName(name);
        skill.setSharedBy(sharedBy);
        skill.setSharedTime(sharedTime);
        return skill;
    }
}
