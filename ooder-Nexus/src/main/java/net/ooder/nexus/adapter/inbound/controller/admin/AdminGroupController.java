package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminGroupService;
import net.ooder.nexus.dto.admin.AdminGroupDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminGroupController {

    private static final Logger log = LoggerFactory.getLogger(AdminGroupController.class);

    @Autowired
    private AdminGroupService adminGroupService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<AdminGroupDTO>> getList() {
        log.info("Get admin group list requested");
        ListResultModel<List<AdminGroupDTO>> result = new ListResultModel<List<AdminGroupDTO>>();

        try {
            List<Map<String, Object>> groups = adminGroupService.getAllGroups();
            List<AdminGroupDTO> dtoList = new ArrayList<AdminGroupDTO>();

            for (Map<String, Object> group : groups) {
                dtoList.add(convertToDTO(group));
            }

            result.setData(dtoList);
            result.setSize(dtoList.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting group list", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<AdminGroupDTO> getGroup(@RequestBody Map<String, String> request) {
        log.info("Get group detail requested: {}", request.get("id"));
        ResultModel<AdminGroupDTO> result = new ResultModel<AdminGroupDTO>();

        try {
            String groupId = request.get("id");
            Map<String, Object> group = adminGroupService.getGroupById(groupId);

            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("Group not found");
                return result;
            }

            result.setData(convertToDTO(group));
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting group detail", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<AdminGroupDTO> createGroup(@RequestBody Map<String, Object> request) {
        log.info("Create group requested: {}", request.get("name"));
        ResultModel<AdminGroupDTO> result = new ResultModel<AdminGroupDTO>();

        try {
            Map<String, Object> group = adminGroupService.createGroup(request);
            result.setData(convertToDTO(group));
            result.setRequestStatus(200);
            result.setMessage("Group created successfully");
        } catch (Exception e) {
            log.error("Error creating group", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<AdminGroupDTO> updateGroup(@RequestBody Map<String, Object> request) {
        log.info("Update group requested: {}", request.get("id"));
        ResultModel<AdminGroupDTO> result = new ResultModel<AdminGroupDTO>();

        try {
            String groupId = (String) request.get("id");
            Map<String, Object> group = adminGroupService.updateGroup(groupId, request);

            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("Group not found");
                return result;
            }

            result.setData(convertToDTO(group));
            result.setRequestStatus(200);
            result.setMessage("Group updated successfully");
        } catch (Exception e) {
            log.error("Error updating group", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteGroup(@RequestBody Map<String, String> request) {
        log.info("Delete group requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String groupId = request.get("id");
            boolean success = adminGroupService.deleteGroup(groupId);

            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Group deleted successfully" : "Group not found");
        } catch (Exception e) {
            log.error("Error deleting group", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/members/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getMembers(@RequestBody Map<String, String> request) {
        log.info("Get group members requested: {}", request.get("groupId"));
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();

        try {
            String groupId = request.get("groupId");
            List<Map<String, Object>> members = adminGroupService.getGroupMembers(groupId);

            result.setData(members);
            result.setSize(members.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting group members", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/members/add")
    @ResponseBody
    public ResultModel<Boolean> addMember(@RequestBody Map<String, String> request) {
        log.info("Add member requested: groupId={}, userId={}", request.get("groupId"), request.get("userId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String groupId = request.get("groupId");
            String userId = request.get("userId");
            boolean success = adminGroupService.addMember(groupId, userId);

            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Member added successfully" : "Group not found");
        } catch (Exception e) {
            log.error("Error adding member", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/members/remove")
    @ResponseBody
    public ResultModel<Boolean> removeMember(@RequestBody Map<String, String> request) {
        log.info("Remove member requested: groupId={}, userId={}", request.get("groupId"), request.get("userId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            String groupId = request.get("groupId");
            String userId = request.get("userId");
            boolean success = adminGroupService.removeMember(groupId, userId);

            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Member removed successfully" : "Member not found");
        } catch (Exception e) {
            log.error("Error removing member", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<AdminGroupService.GroupStatistics> getStatistics() {
        log.info("Get group statistics requested");
        ResultModel<AdminGroupService.GroupStatistics> result = new ResultModel<AdminGroupService.GroupStatistics>();

        try {
            AdminGroupService.GroupStatistics stats = adminGroupService.getStatistics();
            result.setData(stats);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting statistics", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }

        return result;
    }

    private AdminGroupDTO convertToDTO(Map<String, Object> group) {
        AdminGroupDTO dto = new AdminGroupDTO();
        dto.setId((String) group.get("id"));
        dto.setName((String) group.get("name"));
        dto.setCode((String) group.get("code"));
        dto.setDescription((String) group.get("description"));
        dto.setStatus((String) group.get("status"));
        dto.setMemberCount((Integer) group.get("memberCount"));
        dto.setCreateTime((Long) group.get("createTime"));
        dto.setUpdateTime((Long) group.get("updateTime"));
        return dto;
    }
}
