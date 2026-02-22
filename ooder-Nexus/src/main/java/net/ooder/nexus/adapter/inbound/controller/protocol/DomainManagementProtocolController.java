package net.ooder.nexus.adapter.inbound.controller.protocol;

import net.ooder.config.ResultModel;
import net.ooder.sdk.northbound.protocol.DomainManagementProtocol;
import net.ooder.sdk.northbound.protocol.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/protocol/domain")
public class DomainManagementProtocolController {

    private static final Logger log = LoggerFactory.getLogger(DomainManagementProtocolController.class);

    private final DomainManagementProtocol domainManagementProtocol;

    @Autowired
    public DomainManagementProtocolController(@Autowired(required = false) DomainManagementProtocol domainManagementProtocol) {
        this.domainManagementProtocol = domainManagementProtocol;
        log.info("DomainManagementProtocolController initialized: {}", 
            domainManagementProtocol != null ? "SDK protocol available" : "using mock");
    }

    @PostMapping("/create")
    @ResponseBody
    public ResultModel<DomainInfo> createDomain(@RequestBody Map<String, Object> params) {
        log.info("Create domain requested: {}", params);
        ResultModel<DomainInfo> result = new ResultModel<>();

        try {
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CreateDomainRequest request = new CreateDomainRequest();
                request.setDomainName((String) params.get("domainName"));
                request.setDomainType((String) params.get("domainType"));
                request.setOwnerId((String) params.get("ownerId"));
                
                CompletableFuture<DomainInfo> future = domainManagementProtocol.createDomain(request);
                DomainInfo domainInfo = future.get();
                
                result.setData(domainInfo);
                result.setRequestStatus(200);
                result.setMessage("Domain created successfully");
            }
        } catch (Exception e) {
            log.error("Create domain failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to create domain: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/get")
    @ResponseBody
    public ResultModel<DomainInfo> getDomain(@RequestBody Map<String, Object> params) {
        log.info("Get domain requested: {}", params);
        ResultModel<DomainInfo> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<DomainInfo> future = domainManagementProtocol.getDomain(domainId);
                DomainInfo domainInfo = future.get();
                result.setData(domainInfo);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get domain failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get domain: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/update")
    @ResponseBody
    public ResultModel<DomainInfo> updateDomain(@RequestBody Map<String, Object> params) {
        log.info("Update domain requested: {}", params);
        ResultModel<DomainInfo> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                UpdateDomainRequest request = new UpdateDomainRequest();
                request.setDomainName((String) params.get("domainName"));
                
                CompletableFuture<DomainInfo> future = domainManagementProtocol.updateDomain(domainId, request);
                DomainInfo domainInfo = future.get();
                result.setData(domainInfo);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Update domain failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to update domain: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel<Void> deleteDomain(@RequestBody Map<String, Object> params) {
        log.info("Delete domain requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol != null) {
                domainManagementProtocol.deleteDomain(domainId);
            }
            result.setRequestStatus(200);
            result.setMessage("Domain deleted successfully");
        } catch (Exception e) {
            log.error("Delete domain failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to delete domain: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/list")
    @ResponseBody
    public ResultModel<List<DomainInfo>> listDomains(@RequestBody(required = false) Map<String, Object> params) {
        log.info("List domains requested: {}", params);
        ResultModel<List<DomainInfo>> result = new ResultModel<>();

        try {
            if (domainManagementProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                DomainQuery query = new DomainQuery();
                query.setDomainType((String) params.get("domainType"));
                query.setOwnerId((String) params.get("ownerId"));
                
                CompletableFuture<List<DomainInfo>> future = domainManagementProtocol.listDomains(query);
                List<DomainInfo> domains = future.get();
                result.setData(domains);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("List domains failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to list domains: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/member/add")
    @ResponseBody
    public ResultModel<Void> addMember(@RequestBody Map<String, Object> params) {
        log.info("Add member requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                AddMemberRequest request = new AddMemberRequest();
                request.setMemberId((String) params.get("memberId"));
                request.setMemberName((String) params.get("memberName"));
                request.setDomainRole((String) params.get("domainRole"));
                
                domainManagementProtocol.addMember(domainId, request);
                result.setRequestStatus(200);
                result.setMessage("Member added successfully");
            }
        } catch (Exception e) {
            log.error("Add member failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to add member: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/member/remove")
    @ResponseBody
    public ResultModel<Void> removeMember(@RequestBody Map<String, Object> params) {
        log.info("Remove member requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            String memberId = (String) params.get("memberId");
            
            if (domainManagementProtocol != null) {
                domainManagementProtocol.removeMember(domainId, memberId);
            }
            result.setRequestStatus(200);
            result.setMessage("Member removed successfully");
        } catch (Exception e) {
            log.error("Remove member failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to remove member: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/member/list")
    @ResponseBody
    public ResultModel<List<DomainMember>> listMembers(@RequestBody Map<String, Object> params) {
        log.info("List members requested: {}", params);
        ResultModel<List<DomainMember>> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<DomainMember>> future = domainManagementProtocol.listMembers(domainId);
                List<DomainMember> members = future.get();
                result.setData(members);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("List members failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to list members: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/member/update-role")
    @ResponseBody
    public ResultModel<Void> updateMemberRole(@RequestBody Map<String, Object> params) {
        log.info("Update member role requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            String memberId = (String) params.get("memberId");
            String newRole = (String) params.get("newRole");
            
            if (domainManagementProtocol != null) {
                domainManagementProtocol.updateMemberRole(domainId, memberId, newRole);
            }
            result.setRequestStatus(200);
            result.setMessage("Member role updated successfully");
        } catch (Exception e) {
            log.error("Update member role failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to update member role: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/policy/set")
    @ResponseBody
    public ResultModel<Void> setDomainPolicy(@RequestBody Map<String, Object> params) {
        log.info("Set domain policy requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                DomainPolicyConfig policy = new DomainPolicyConfig();
                policy.setDomainId(domainId);
                policy.setAllowedSkills((List<String>) params.get("allowedSkills"));
                policy.setRequiredSkills((List<String>) params.get("requiredSkills"));
                
                domainManagementProtocol.setDomainPolicy(domainId, policy);
                result.setRequestStatus(200);
                result.setMessage("Domain policy set successfully");
            }
        } catch (Exception e) {
            log.error("Set domain policy failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to set domain policy: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/policy/get")
    @ResponseBody
    public ResultModel<DomainPolicyConfig> getDomainPolicy(@RequestBody Map<String, Object> params) {
        log.info("Get domain policy requested: {}", params);
        ResultModel<DomainPolicyConfig> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                CompletableFuture<DomainPolicyConfig> future = domainManagementProtocol.getDomainPolicy(domainId);
                DomainPolicyConfig policy = future.get();
                result.setData(policy);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Get domain policy failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to get domain policy: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/create")
    @ResponseBody
    public ResultModel<DomainInvitation> createInvitation(@RequestBody Map<String, Object> params) {
        log.info("Create invitation requested: {}", params);
        ResultModel<DomainInvitation> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setRequestStatus(503);
                result.setMessage("SDK not available");
            } else {
                InvitationRequest request = new InvitationRequest();
                request.setTargetId((String) params.get("targetId"));
                request.setTargetName((String) params.get("targetName"));
                request.setMessage((String) params.get("message"));
                
                CompletableFuture<DomainInvitation> future = domainManagementProtocol.createInvitation(domainId, request);
                DomainInvitation invitation = future.get();
                result.setData(invitation);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("Create invitation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to create invitation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/accept")
    @ResponseBody
    public ResultModel<Void> acceptInvitation(@RequestBody Map<String, Object> params) {
        log.info("Accept invitation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String invitationId = (String) params.get("invitationId");
            
            if (domainManagementProtocol != null) {
                domainManagementProtocol.acceptInvitation(invitationId);
            }
            result.setRequestStatus(200);
            result.setMessage("Invitation accepted");
        } catch (Exception e) {
            log.error("Accept invitation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to accept invitation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/reject")
    @ResponseBody
    public ResultModel<Void> rejectInvitation(@RequestBody Map<String, Object> params) {
        log.info("Reject invitation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String invitationId = (String) params.get("invitationId");
            
            if (domainManagementProtocol != null) {
                domainManagementProtocol.rejectInvitation(invitationId);
            }
            result.setRequestStatus(200);
            result.setMessage("Invitation rejected");
        } catch (Exception e) {
            log.error("Reject invitation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to reject invitation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/revoke")
    @ResponseBody
    public ResultModel<Void> revokeInvitation(@RequestBody Map<String, Object> params) {
        log.info("Revoke invitation requested: {}", params);
        ResultModel<Void> result = new ResultModel<>();

        try {
            String invitationId = (String) params.get("invitationId");
            
            if (domainManagementProtocol != null) {
                domainManagementProtocol.revokeInvitation(invitationId);
            }
            result.setRequestStatus(200);
            result.setMessage("Invitation revoked");
        } catch (Exception e) {
            log.error("Revoke invitation failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to revoke invitation: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/list-pending")
    @ResponseBody
    public ResultModel<List<DomainInvitation>> listPendingInvitations(@RequestBody Map<String, Object> params) {
        log.info("List pending invitations requested: {}", params);
        ResultModel<List<DomainInvitation>> result = new ResultModel<>();

        try {
            String domainId = (String) params.get("domainId");
            
            if (domainManagementProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<DomainInvitation>> future = domainManagementProtocol.listPendingInvitations(domainId);
                List<DomainInvitation> invitations = future.get();
                result.setData(invitations);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("List pending invitations failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to list pending invitations: " + e.getMessage());
        }

        return result;
    }

    @PostMapping("/invitation/list-my")
    @ResponseBody
    public ResultModel<List<DomainInvitation>> listMyInvitations() {
        log.info("List my invitations requested");
        ResultModel<List<DomainInvitation>> result = new ResultModel<>();

        try {
            if (domainManagementProtocol == null) {
                result.setData(new ArrayList<>());
                result.setRequestStatus(200);
            } else {
                CompletableFuture<List<DomainInvitation>> future = domainManagementProtocol.listMyInvitations();
                List<DomainInvitation> invitations = future.get();
                result.setData(invitations);
                result.setRequestStatus(200);
            }
        } catch (Exception e) {
            log.error("List my invitations failed", e);
            result.setRequestStatus(500);
            result.setMessage("Failed to list my invitations: " + e.getMessage());
        }

        return result;
    }

    @GetMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getStatus() {
        ResultModel<Map<String, Object>> result = new ResultModel<>();
        
        Map<String, Object> status = new HashMap<>();
        status.put("sdkAvailable", domainManagementProtocol != null);
        status.put("protocolType", "DomainManagementProtocol");
        
        result.setData(status);
        result.setRequestStatus(200);
        return result;
    }
}
