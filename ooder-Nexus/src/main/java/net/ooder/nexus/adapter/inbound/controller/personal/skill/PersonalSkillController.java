package net.ooder.nexus.adapter.inbound.controller.personal.skill;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.infrastructure.management.NexusSkillManager;
import net.ooder.nexus.model.Skill;
import net.ooder.nexus.model.SkillResult;
import net.ooder.nexus.model.SkillContext;
import net.ooder.nexus.model.SkillException;
import net.ooder.nexus.model.SkillParam;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/personal/skills")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalSkillController {

    private final NexusSkillManager skillManager;
    private final Map<String, SimpleSkill> personalSkills = new LinkedHashMap<>();

    public PersonalSkillController(NexusSkillManager nexusSkillManager) {
        this.skillManager = nexusSkillManager;
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getList() {
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<>();
        try {
            List<Map<String, Object>> skillList = new ArrayList<>();
            for (SimpleSkill skill : personalSkills.values()) {
                Map<String, Object> skillMap = new HashMap<>();
                skillMap.put("id", skill.getId());
                skillMap.put("name", skill.getName());
                skillMap.put("description", skill.getDescription());
                skillMap.put("category", skill.getCategory());
                skillMap.put("code", skill.getCode());
                skillMap.put("status", "active");
                skillMap.put("createTime", skill.getCreateTime());
                skillList.add(skillMap);
            }
            result.setData(skillList);
            result.setSize(skillList.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("Failed to get skill list: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> create(@RequestBody Map<String, Object> request) {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            String category = (String) request.get("category");
            String code = (String) request.get("code");

            if (name == null || name.isEmpty()) {
                result.setRequestStatus(400);
                result.setMessage("Skill name cannot be empty");
                return result;
            }

            String skillId = "skill-" + System.currentTimeMillis();
            SimpleSkill skill = new SimpleSkill(skillId, name, description, category, code);
            
            personalSkills.put(skillId, skill);

            Map<String, Object> data = new HashMap<>();
            data.put("id", skillId);
            data.put("name", name);
            data.put("status", "active");

            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Created successfully");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("Failed to create skill: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSkill(@RequestBody Map<String, String> request) {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        try {
            String id = request.get("id");
            SimpleSkill skill = personalSkills.get(id);
            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
                return result;
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("id", skill.getId());
            data.put("name", skill.getName());
            data.put("description", skill.getDescription());
            data.put("category", skill.getCategory());
            data.put("code", skill.getCode());
            data.put("status", "active");
            
            result.setData(data);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("Failed to get skill: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Boolean> update(@RequestBody Map<String, Object> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String id = (String) request.get("id");
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            String category = (String) request.get("category");
            String code = (String) request.get("code");

            SimpleSkill skill = personalSkills.get(id);
            if (skill == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
                result.setData(false);
                return result;
            }

            skill.update(name, description, category, code);
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Updated successfully");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("Failed to update skill: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> delete(@RequestBody Map<String, String> request) {
        ResultModel<Boolean> result = new ResultModel<>();
        try {
            String id = request.get("id");
            SimpleSkill removed = personalSkills.remove(id);
            if (removed == null) {
                result.setRequestStatus(404);
                result.setMessage("Skill not found");
                result.setData(false);
                return result;
            }
            result.setData(true);
            result.setRequestStatus(200);
            result.setMessage("Deleted successfully");
        } catch (Exception e) {
            result.setRequestStatus(500);
            result.setMessage("Failed to delete skill: " + e.getMessage());
            result.setData(false);
        }
        return result;
    }

    private static class SimpleSkill implements Skill {
        private final String id;
        private String name;
        private String description;
        private String category;
        private String code;
        private final long createTime;

        public SimpleSkill(String id, String name, String description, String category, String code) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.code = code;
            this.createTime = System.currentTimeMillis();
        }

        public void update(String name, String description, String category, String code) {
            if (name != null) this.name = name;
            if (description != null) this.description = description;
            if (category != null) this.category = category;
            if (code != null) this.code = code;
        }

        public String getCategory() { return category; }
        public String getCode() { return code; }
        public long getCreateTime() { return createTime; }

        @Override
        public String getId() { return id; }
        @Override
        public String getName() { return name; }
        @Override
        public String getDescription() { return description; }
        @Override
        public boolean isAvailable() { return true; }
        @Override
        public Map<String, SkillParam> getParams() { return new HashMap<String, SkillParam>(); }
        
        @Override
        public SkillResult execute(SkillContext context) throws SkillException {
            SkillResult result = new SkillResult();
            result.addData("message", "Skill executed: " + name);
            return result;
        }
    }
}
