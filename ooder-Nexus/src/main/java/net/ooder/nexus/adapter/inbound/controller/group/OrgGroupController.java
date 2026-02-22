package net.ooder.nexus.adapter.inbound.controller.group;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.domain.group.model.GroupExt;
import net.ooder.nexus.dto.group.GroupCreateDTO;
import net.ooder.nexus.service.group.OrgGroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组织群组控制器
 * 
 * <p>提供组织机构与群组的关联管理 API。</p>
 * 
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/org/group", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class OrgGroupController {

    private static final Logger log = LoggerFactory.getLogger(OrgGroupController.class);

    @Autowired
    private OrgGroupService orgGroupService;

    /**
     * 创建群组
     */
    @PostMapping("/create")
    @ResponseBody
    public ResultModel<GroupExt> createGroup(@RequestBody GroupCreateDTO dto) {
        log.info("Create group request: name={}, orgId={}", dto.getName(), dto.getOrgId());
        ResultModel<GroupExt> result = new ResultModel<>();
        try {
            GroupExt group = orgGroupService.createGroup(dto);
            result.setData(group);
            result.setRequestStatus(200);
            result.setMessage("群组创建成功");
        } catch (Exception e) {
            log.error("Error creating group", e);
            result.setRequestStatus(500);
            result.setMessage("创建群组失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取群组详情
     */
    @PostMapping("/get")
    @ResponseBody
    public ResultModel<GroupExt> getGroup(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        log.info("Get group request: groupId={}", groupId);
        ResultModel<GroupExt> result = new ResultModel<>();
        try {
            GroupExt group = orgGroupService.getGroup(groupId);
            if (group != null) {
                result.setData(group);
                result.setRequestStatus(200);
                result.setMessage("获取成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
            }
        } catch (Exception e) {
            log.error("Error getting group", e);
            result.setRequestStatus(500);
            result.setMessage("获取群组失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织下的群组列表
     */
    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<GroupExt>> getGroupsByOrg(@RequestBody Map<String, String> request) {
        String orgId = request.get("orgId");
        String departmentId = request.get("departmentId");
        String userId = request.get("userId");
        
        log.info("Get groups request: orgId={}, departmentId={}, userId={}", orgId, departmentId, userId);
        ListResultModel<List<GroupExt>> result = new ListResultModel<>();
        try {
            List<GroupExt> groups;
            if (userId != null) {
                groups = orgGroupService.getGroupsByUser(userId);
            } else if (departmentId != null) {
                groups = orgGroupService.getGroupsByDepartment(departmentId);
            } else if (orgId != null) {
                groups = orgGroupService.getGroupsByOrg(orgId);
            } else {
                groups = new java.util.ArrayList<>();
            }
            result.setData(groups);
            result.setSize(groups.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting groups", e);
            result.setRequestStatus(500);
            result.setMessage("获取群组列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 更新群组
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultModel<GroupExt> updateGroup(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        String name = request.get("name");
        String description = request.get("description");
        
        log.info("Update group request: groupId={}", groupId);
        ResultModel<GroupExt> result = new ResultModel<>();
        try {
            GroupExt group = orgGroupService.updateGroup(groupId, name, description);
            if (group != null) {
                result.setData(group);
                result.setRequestStatus(200);
                result.setMessage("更新成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
            }
        } catch (Exception e) {
            log.error("Error updating group", e);
            result.setRequestStatus(500);
            result.setMessage("更新群组失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 删除群组
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteGroup(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        String operatorId = request.get("operatorId");
        
        log.info("Delete group request: groupId={}, operatorId={}", groupId, operatorId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = orgGroupService.deleteGroup(groupId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "删除成功" : "无权删除此群组");
        } catch (Exception e) {
            log.error("Error deleting group", e);
            result.setRequestStatus(500);
            result.setMessage("删除群组失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取群组成员
     */
    @PostMapping("/members")
    @ResponseBody
    public ListResultModel<List<OrgGroupService.GroupMember>> getMembers(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        log.info("Get members request: groupId={}", groupId);
        ListResultModel<List<OrgGroupService.GroupMember>> result = new ListResultModel<>();
        try {
            List<OrgGroupService.GroupMember> members = orgGroupService.getMembers(groupId);
            result.setData(members);
            result.setSize(members.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            log.error("Error getting members", e);
            result.setRequestStatus(500);
            result.setMessage("获取成员列表失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 添加成员
     */
    @PostMapping("/members/add")
    @ResponseBody
    public ResultModel<Boolean> addMember(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        String userId = request.get("userId");
        String userName = request.get("userName");
        String role = request.get("role");
        
        log.info("Add member request: groupId={}, userId={}", groupId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = orgGroupService.addMember(groupId, userId, userName, role);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "添加成功" : "群组不存在");
        } catch (Exception e) {
            log.error("Error adding member", e);
            result.setRequestStatus(500);
            result.setMessage("添加成员失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 移除成员
     */
    @PostMapping("/members/remove")
    @ResponseBody
    public ResultModel<Boolean> removeMember(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        String userId = request.get("userId");
        String operatorId = request.get("operatorId");
        
        log.info("Remove member request: groupId={}, userId={}", groupId, userId);
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            boolean success = orgGroupService.removeMember(groupId, userId, operatorId);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 403);
            result.setMessage(success ? "移除成功" : "无权移除此成员");
        } catch (Exception e) {
            log.error("Error removing member", e);
            result.setRequestStatus(500);
            result.setMessage("移除成员失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 生成邀请码
     */
    @PostMapping("/invite/generate")
    @ResponseBody
    public ResultModel<Map<String, Object>> generateInviteCode(@RequestBody Map<String, String> request) {
        String groupId = request.get("groupId");
        log.info("Generate invite code request: groupId={}", groupId);
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String code = orgGroupService.generateInviteCode(groupId);
            Map<String, Object> data = new HashMap<>();
            data.put("inviteCode", code);
            data.put("groupId", groupId);
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("生成成功");
        } catch (Exception e) {
            log.error("Error generating invite code", e);
            result.setRequestStatus(500);
            result.setMessage("生成邀请码失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 通过邀请码加入群组
     */
    @PostMapping("/invite/join")
    @ResponseBody
    public ResultModel<GroupExt> joinByInviteCode(@RequestBody Map<String, String> request) {
        String inviteCode = request.get("inviteCode");
        String userId = request.get("userId");
        String userName = request.get("userName");
        
        log.info("Join by invite code request: code={}, userId={}", inviteCode, userId);
        ResultModel<GroupExt> result = new ResultModel<>();
        try {
            GroupExt group = orgGroupService.joinByInviteCode(inviteCode, userId, userName);
            if (group != null) {
                result.setData(group);
                result.setRequestStatus(200);
                result.setMessage("加入成功");
            } else {
                result.setRequestStatus(404);
                result.setMessage("邀请码无效或群组不存在");
            }
        } catch (Exception e) {
            log.error("Error joining group", e);
            result.setRequestStatus(500);
            result.setMessage("加入群组失败: " + e.getMessage());
        }
        return result;
    }
}
