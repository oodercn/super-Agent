package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminCategoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/categories")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminCategoryController {

    private static final Logger log = LoggerFactory.getLogger(AdminCategoryController.class);

    @Autowired
    private AdminCategoryService adminCategoryService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getCategoryList() {
        log.info("Get categories requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> categories = adminCategoryService.getAllCategories();
            result.setData(categories);
            result.setSize(categories.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting categories", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getCategory(@RequestBody Map<String, String> request) {
        log.info("Get category requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> category = adminCategoryService.getCategoryById(request.get("id"));
            if (category == null) {
                result.setRequestStatus(404);
                result.setMessage("Category not found");
            } else {
                result.setData(category);
                result.setRequestStatus(200);
                result.setMessage("Success");
            }
        } catch (Exception e) {
            log.error("Error getting category", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createCategory(@RequestBody Map<String, Object> request) {
        log.info("Create category requested: {}", request.get("name"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> category = adminCategoryService.createCategory(request);
            result.setData(category);
            result.setRequestStatus(200);
            result.setMessage("Created successfully");
        } catch (Exception e) {
            log.error("Error creating category", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateCategory(@RequestBody Map<String, Object> request) {
        log.info("Update category requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            String id = (String) request.get("id");
            Map<String, Object> category = adminCategoryService.updateCategory(id, request);
            if (category == null) {
                result.setRequestStatus(404);
                result.setMessage("Category not found");
            } else {
                result.setData(category);
                result.setRequestStatus(200);
                result.setMessage("Updated successfully");
            }
        } catch (Exception e) {
            log.error("Error updating category", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteCategory(@RequestBody Map<String, String> request) {
        log.info("Delete category requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminCategoryService.deleteCategory(request.get("id"));
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Deleted successfully" : "Category not found");
        } catch (Exception e) {
            log.error("Error deleting category", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/skills/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getCategorySkills(@RequestBody Map<String, String> request) {
        log.info("Get category skills requested: {}", request.get("categoryId"));
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> skills = adminCategoryService.getCategorySkills(request.get("categoryId"));
            result.setData(skills);
            result.setSize(skills.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting category skills", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/skills/add")
    @ResponseBody
    public ResultModel<Boolean> addSkillToCategory(@RequestBody Map<String, String> request) {
        log.info("Add skill to category: skill={}, category={}", request.get("skillId"), request.get("categoryId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminCategoryService.addSkillToCategory(request.get("categoryId"), request.get("skillId"));
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Skill added to category");
        } catch (Exception e) {
            log.error("Error adding skill to category", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/skills/remove")
    @ResponseBody
    public ResultModel<Boolean> removeSkillFromCategory(@RequestBody Map<String, String> request) {
        log.info("Remove skill from category: skill={}, category={}", request.get("skillId"), request.get("categoryId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminCategoryService.removeSkillFromCategory(request.get("categoryId"), request.get("skillId"));
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Skill removed from category");
        } catch (Exception e) {
            log.error("Error removing skill from category", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/count")
    @ResponseBody
    public ResultModel<Integer> getCategoryCount() {
        log.info("Get category count requested");
        ResultModel<Integer> result = new ResultModel<Integer>();
        try {
            int count = adminCategoryService.getCategoryCount();
            result.setData(count);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting category count", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }
}
