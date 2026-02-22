package net.ooder.nexus.adapter.inbound.controller.personal.group;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/personal/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalGroupController {

    private final Map<String, Map<String, Object>> groups = new LinkedHashMap<>();

    public PersonalGroupController() {
        Map<String, Object> group1 = new HashMap<>();
        group1.put("id", "group-001");
        group1.put("name", "常用技能");
        group1.put("description", "经常使用的技能");
        group1.put("memberCount", 5);
        group1.put("createTime", System.currentTimeMillis());
        groups.put("group-001", group1);

        Map<String, Object> group2 = new HashMap<>();
        group2.put("id", "group-002");
        group2.put("name", "开发工具");
        group2.put("description", "开发相关的工具技能");
        group2.put("memberCount", 3);
        group2.put("createTime", System.currentTimeMillis());
        groups.put("group-002", group2);

        Map<String, Object> group3 = new HashMap<>();
        group3.put("id", "group-003");
        group3.put("name", "系统管理");
        group3.put("description", "系统管理相关技能");
        group3.put("memberCount", 2);
        group3.put("createTime", System.currentTimeMillis());
        groups.put("group-003", group3);
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getGroupList() {
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<Map<String, Object>> groupList = new ArrayList<>(groups.values());
            result.setData(groupList);
            result.setSize(groupList.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("获取群组列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getGroup(@RequestBody Map<String, String> request) {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            Map<String, Object> group = groups.get(groupId);
            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                return result;
            }
            result.setData(group);
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("获取群组详情失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createGroup(@RequestBody Map<String, Object> req) {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String name = (String) req.get("name");
            String description = (String) req.get("description");

            String groupId = "group-" + UUID.randomUUID().toString().substring(0, 8);
            Map<String, Object> group = new HashMap<>();
            group.put("id", groupId);
            group.put("name", name);
            group.put("description", description);
            group.put("memberCount", 0);
            group.put("createTime", System.currentTimeMillis());

            groups.put(groupId, group);

            result.setData(group);
            result.setRequestStatus(200);
            result.setMessage("创建成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("创建群组失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Boolean> updateGroup(@RequestBody Map<String, Object> req) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String groupId = (String) req.get("groupId");
            Map<String, Object> group = groups.get(groupId);
            if (group == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                result.setData(false);
                return result;
            }

            if (req.get("name") != null) {
                group.put("name", req.get("name"));
            }
            if (req.get("description") != null) {
                group.put("description", req.get("description"));
            }

            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("更新成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("更新群组失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteGroup(@RequestBody Map<String, String> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            Map<String, Object> removed = groups.remove(groupId);
            if (removed == null) {
                result.setRequestStatus(404);
                result.setMessage("群组不存在");
                result.setData(false);
                return result;
            }
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("删除成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("删除群组失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    @PostMapping("/skills")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getGroupSkills(@RequestBody Map<String, String> request) {
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            String groupId = request.get("groupId");

            List<Map<String, Object>> skillList = new ArrayList<>();

            Map<String, Object> skill1 = new HashMap<>();
            skill1.put("id", "skill-001");
            skill1.put("name", "示例技能1");
            skill1.put("description", "这是一个示例技能");
            skill1.put("version", "1.0.0");
            skill1.put("status", "active");
            skillList.add(skill1);

            Map<String, Object> skill2 = new HashMap<>();
            skill2.put("id", "skill-002");
            skill2.put("name", "示例技能2");
            skill2.put("description", "这是另一个示例技能");
            skill2.put("version", "1.0.0");
            skill2.put("status", "active");
            skillList.add(skill2);

            result.setData(skillList);
            result.setSize(skillList.size());
            result.setRequestStatus(200);
            result.setMessage("获取成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("获取群组技能失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/skills/add")
    @ResponseBody
    public ResultModel<Boolean> addSkillToGroup(@RequestBody Map<String, String> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            String skillId = request.get("skillId");
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("添加成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("添加技能到群组失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    @PostMapping("/skills/remove")
    @ResponseBody
    public ResultModel<Boolean> removeSkillFromGroup(@RequestBody Map<String, String> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String groupId = request.get("groupId");
            String skillId = request.get("skillId");
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("移除成功");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("从群组移除技能失败: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }
}
