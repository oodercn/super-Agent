package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminMarketService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/market")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminMarketController {

    private static final Logger log = LoggerFactory.getLogger(AdminMarketController.class);

    @Autowired
    private AdminMarketService adminMarketService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getList() {
        log.info("Get market items requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> items = adminMarketService.getAllMarketItems();
            result.setData(items);
            result.setSize(items.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting market items", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getItem(@RequestBody Map<String, String> request) {
        log.info("Get market item requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> item = adminMarketService.getMarketItemById(request.get("id"));
            if (item == null) {
                result.setRequestStatus(404);
                result.setMessage("Item not found");
            } else {
                result.setData(item);
                result.setRequestStatus(200);
                result.setMessage("Success");
            }
        } catch (Exception e) {
            log.error("Error getting market item", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/featured")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getFeatured() {
        log.info("Get featured items requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> items = adminMarketService.getFeaturedItems();
            result.setData(items);
            result.setSize(items.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting featured items", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/categories")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getCategories() {
        log.info("Get market categories requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> categories = adminMarketService.getCategories();
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

    @PostMapping("/submit")
    @ResponseBody
    public ResultModel<Boolean> submitToMarket(@RequestBody Map<String, Object> request) {
        log.info("Submit to market requested: {}", request.get("skillId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            String skillId = (String) request.get("skillId");
            boolean success = adminMarketService.submitToMarket(skillId, request);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Submitted successfully");
        } catch (Exception e) {
            log.error("Error submitting to market", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Boolean> updateItem(@RequestBody Map<String, Object> request) {
        log.info("Update market item requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            String itemId = (String) request.get("id");
            boolean success = adminMarketService.updateMarketItem(itemId, request);
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Updated successfully" : "Item not found");
        } catch (Exception e) {
            log.error("Error updating market item", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResultModel<Boolean> removeFromMarket(@RequestBody Map<String, String> request) {
        log.info("Remove from market requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminMarketService.removeFromMarket(request.get("id"));
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Removed successfully" : "Item not found");
        } catch (Exception e) {
            log.error("Error removing from market", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatistics() {
        log.info("Get market statistics requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> stats = adminMarketService.getMarketStatistics();
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
}
