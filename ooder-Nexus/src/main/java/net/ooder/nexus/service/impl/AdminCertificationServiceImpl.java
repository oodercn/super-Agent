package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminCertificationService;
import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.storage.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminCertificationServiceImpl implements AdminCertificationService {

    private static final Logger log = LoggerFactory.getLogger(AdminCertificationServiceImpl.class);
    private static final String CERT_KEY = "admin/certifications";

    private final StorageService storageService;

    @Autowired
    public AdminCertificationServiceImpl(StorageService storageService) {
        this.storageService = storageService;
        log.info("AdminCertificationServiceImpl initialized with StorageService (SDK 0.7.1)");
    }

    @Override
    public List<Map<String, Object>> getAllCertifications() {
        log.info("Getting all certifications");
        Optional<List<Map<String, Object>>> certsOpt = storageService.load(CERT_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return certsOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public Map<String, Object> getCertificationById(String certId) {
        log.info("Getting certification by id: {}", certId);
        List<Map<String, Object>> certs = getAllCertifications();
        for (Map<String, Object> cert : certs) {
            if (certId.equals(cert.get("id"))) {
                return cert;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getPendingCertifications() {
        log.info("Getting pending certifications");
        List<Map<String, Object>> certs = getAllCertifications();
        List<Map<String, Object>> pending = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> cert : certs) {
            String status = (String) cert.get("status");
            if ("pending".equals(status)) {
                pending.add(cert);
            }
        }
        return pending;
    }

    @Override
    public Map<String, Object> submitCertification(Map<String, Object> certData) {
        log.info("Submitting certification: {}", certData.get("name"));
        List<Map<String, Object>> certs = getAllCertifications();
        String id = "cert-" + System.currentTimeMillis();
        Map<String, Object> cert = new HashMap<String, Object>(certData);
        cert.put("id", id);
        cert.put("status", "pending");
        cert.put("submitTime", System.currentTimeMillis());
        certs.add(cert);
        storageService.save(CERT_KEY, certs);
        return cert;
    }

    @Override
    public Map<String, Object> approveCertification(String certId, Map<String, Object> approvalData) {
        log.info("Approving certification: {}", certId);
        List<Map<String, Object>> certs = getAllCertifications();
        for (int i = 0; i < certs.size(); i++) {
            if (certId.equals(certs.get(i).get("id"))) {
                Map<String, Object> cert = certs.get(i);
                cert.put("status", "approved");
                cert.put("approvedBy", approvalData.get("approvedBy"));
                cert.put("approvedTime", System.currentTimeMillis());
                cert.put("approvalNote", approvalData.get("note"));
                certs.set(i, cert);
                storageService.save(CERT_KEY, certs);
                return cert;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> rejectCertification(String certId, Map<String, Object> rejectionData) {
        log.info("Rejecting certification: {}", certId);
        List<Map<String, Object>> certs = getAllCertifications();
        for (int i = 0; i < certs.size(); i++) {
            if (certId.equals(certs.get(i).get("id"))) {
                Map<String, Object> cert = certs.get(i);
                cert.put("status", "rejected");
                cert.put("rejectedBy", rejectionData.get("rejectedBy"));
                cert.put("rejectedTime", System.currentTimeMillis());
                cert.put("rejectReason", rejectionData.get("reason"));
                certs.set(i, cert);
                storageService.save(CERT_KEY, certs);
                return cert;
            }
        }
        return null;
    }

    @Override
    public boolean revokeCertification(String certId) {
        log.info("Revoking certification: {}", certId);
        List<Map<String, Object>> certs = getAllCertifications();
        for (int i = 0; i < certs.size(); i++) {
            if (certId.equals(certs.get(i).get("id"))) {
                Map<String, Object> cert = certs.get(i);
                cert.put("status", "revoked");
                cert.put("revokedTime", System.currentTimeMillis());
                certs.set(i, cert);
                storageService.save(CERT_KEY, certs);
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> getCertificationStatistics() {
        log.info("Getting certification statistics");
        Map<String, Object> stats = new HashMap<String, Object>();
        List<Map<String, Object>> certs = getAllCertifications();
        
        int total = certs.size();
        int pending = 0;
        int approved = 0;
        int rejected = 0;
        int revoked = 0;
        
        for (Map<String, Object> cert : certs) {
            String status = (String) cert.get("status");
            if ("pending".equals(status)) pending++;
            else if ("approved".equals(status)) approved++;
            else if ("rejected".equals(status)) rejected++;
            else if ("revoked".equals(status)) revoked++;
        }
        
        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("approved", approved);
        stats.put("rejected", rejected);
        stats.put("revoked", revoked);
        
        return stats;
    }
}
