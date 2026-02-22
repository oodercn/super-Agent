package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

public interface AdminCertificationService {

    List<Map<String, Object>> getAllCertifications();

    Map<String, Object> getCertificationById(String certId);

    List<Map<String, Object>> getPendingCertifications();

    Map<String, Object> submitCertification(Map<String, Object> certData);

    Map<String, Object> approveCertification(String certId, Map<String, Object> approvalData);

    Map<String, Object> rejectCertification(String certId, Map<String, Object> rejectionData);

    boolean revokeCertification(String certId);

    Map<String, Object> getCertificationStatistics();
}
