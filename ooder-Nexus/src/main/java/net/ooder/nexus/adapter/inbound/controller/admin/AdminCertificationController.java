package net.ooder.nexus.adapter.inbound.controller.admin;

import net.ooder.config.ResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.nexus.service.AdminCertificationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/certification")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminCertificationController {

    private static final Logger log = LoggerFactory.getLogger(AdminCertificationController.class);

    @Autowired
    private AdminCertificationService adminCertificationService;

    @PostMapping("/list")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getList() {
        log.info("Get certifications requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> certs = adminCertificationService.getAllCertifications();
            result.setData(certs);
            result.setSize(certs.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting certifications", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<Map<String, Object>> getCertification(@RequestBody Map<String, String> request) {
        log.info("Get certification requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> cert = adminCertificationService.getCertificationById(request.get("id"));
            if (cert == null) {
                result.setRequestStatus(404);
                result.setMessage("Certification not found");
            } else {
                result.setData(cert);
                result.setRequestStatus(200);
                result.setMessage("Success");
            }
        } catch (Exception e) {
            log.error("Error getting certification", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/pending")
    @ResponseBody
    public ListResultModel<List<Map<String, Object>>> getPending() {
        log.info("Get pending certifications requested");
        ListResultModel<List<Map<String, Object>>> result = new ListResultModel<List<Map<String, Object>>>();
        try {
            List<Map<String, Object>> certs = adminCertificationService.getPendingCertifications();
            result.setData(certs);
            result.setSize(certs.size());
            result.setRequestStatus(200);
            result.setMessage("Success");
        } catch (Exception e) {
            log.error("Error getting pending certifications", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/submit")
    @ResponseBody
    public ResultModel<Map<String, Object>> submitCertification(@RequestBody Map<String, Object> request) {
        log.info("Submit certification requested: {}", request.get("name"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> cert = adminCertificationService.submitCertification(request);
            result.setData(cert);
            result.setRequestStatus(200);
            result.setMessage("Certification submitted successfully");
        } catch (Exception e) {
            log.error("Error submitting certification", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/approve")
    @ResponseBody
    public ResultModel<Map<String, Object>> approveCertification(@RequestBody Map<String, Object> request) {
        log.info("Approve certification requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            String id = (String) request.get("id");
            Map<String, Object> cert = adminCertificationService.approveCertification(id, request);
            if (cert == null) {
                result.setRequestStatus(404);
                result.setMessage("Certification not found");
            } else {
                result.setData(cert);
                result.setRequestStatus(200);
                result.setMessage("Certification approved");
            }
        } catch (Exception e) {
            log.error("Error approving certification", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResultModel<Map<String, Object>> rejectCertification(@RequestBody Map<String, Object> request) {
        log.info("Reject certification requested: {}", request.get("id"));
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            String id = (String) request.get("id");
            Map<String, Object> cert = adminCertificationService.rejectCertification(id, request);
            if (cert == null) {
                result.setRequestStatus(404);
                result.setMessage("Certification not found");
            } else {
                result.setData(cert);
                result.setRequestStatus(200);
                result.setMessage("Certification rejected");
            }
        } catch (Exception e) {
            log.error("Error rejecting certification", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/revoke")
    @ResponseBody
    public ResultModel<Boolean> revokeCertification(@RequestBody Map<String, String> request) {
        log.info("Revoke certification requested: {}", request.get("id"));
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            boolean success = adminCertificationService.revokeCertification(request.get("id"));
            result.setData(success);
            result.setRequestStatus(success ? 200 : 404);
            result.setMessage(success ? "Certification revoked" : "Certification not found");
        } catch (Exception e) {
            log.error("Error revoking certification", e);
            result.setRequestStatus(500);
            result.setMessage("Error: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatistics() {
        log.info("Get certification statistics requested");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            Map<String, Object> stats = adminCertificationService.getCertificationStatistics();
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
