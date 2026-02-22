package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/storage")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminStorageController {

    private static final Logger log = LoggerFactory.getLogger(AdminStorageController.class);

    @Autowired
    private AdminStorageService adminStorageService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getList() {
        log.info("Get storage list requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> items = adminStorageService.getAllStorage();
            result.setData(items);
            result.setSize(items.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting storage list", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStorage(@RequestBody Map<String, String> request) {
        log.info("Get storage requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> storage = adminStorageService.getStorageById(request.get("id"));
            if (storage == null) {
                result.setRequestStatus(404);
                result.setMessage("Storage not found");
            } else {
                result.setData(storage);
                result.setRequestStatus(200);
                result.setMessage("Success");
            }
        } catch (Exception e) {
            log.error("Error getting storage", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<Map<String, Object>> createStorage(@RequestBody Map<String, Object> request) {
        log.info("Create storage requested: {}", request.get("name"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> storage = adminStorageService.createStorage(request);
            result.setData(storage);
            result.setRequestStatus(200);
            result.setMessage("Created successfully");
        } catch (Exception e) {
            log.error("Error creating storage", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<Map<String, Object>> updateStorage(@RequestBody Map<String, Object> request) {
        log.info("Update storage requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            String id = (String) request.get("id");
            Map<String, Object> storage = adminStorageService.updateStorage(id, request);
            if (storage == null) {
                result.setRequestStatus(404);
                result.setMessage("Storage not found");
            } else {
                result.setData(storage);
                result.setRequestStatus(200);
                result.setMessage("Updated successfully");
            }
        } catch (Exception e) {
            log.error("Error updating storage", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteStorage(@RequestBody Map<String, String> request) {
        log.info("Delete storage requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminStorageService.deleteStorage(request.get("id"));
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Deleted successfully" : "Storage not found");
        } catch (Exception e) {
            log.error("Error deleting storage", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/settings/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getSettings() {
        log.info("Get storage settings requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> settings = adminStorageService.getStorageSettings();
            result.setData(settings);
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting settings", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/settings/update")
    @ResponseBody
    public ResultModel<Boolean> updateSettings(@RequestBody Map<String, Object> request) {
        log.info("Update storage settings requested");
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminStorageService.updateStorageSettings(request);
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Settings updated successfully");
        } catch (Exception e) {
            log.error("Error updating settings", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/backups/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getBackupList() {
        log.info("Get backup list requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> backups = adminStorageService.getBackupList();
            result.setData(backups);
            result.setSize(backups.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting backup list", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/backups/create")
    @ResponseBody
    public ResultModel<Boolean> createBackup() {
        log.info("Create backup requested");
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminStorageService.createBackup();
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Backup created successfully");
        } catch (Exception e) {
            log.error("Error creating backup", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/backups/restore")
    @ResponseBody
    public ResultModel<Boolean> restoreBackup(@RequestBody Map<String, String> request) {
        log.info("Restore backup requested: {}", request.get("backupId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminStorageService.restoreBackup(request.get("backupId"));
            result.setData(success);
            result.setRequestStatus(200);
            result.setMessage("Backup restored successfully");
        } catch (Exception e) {
            log.error("Error restoring backup", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/backups/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteBackup(@RequestBody Map<String, String> request) {
        log.info("Delete backup requested: {}", request.get("backupId"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminStorageService.deleteBackup(request.get("backupId"));
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Backup deleted successfully" : "Backup not found");
        } catch (Exception e) {
            log.error("Error deleting backup", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }
}
