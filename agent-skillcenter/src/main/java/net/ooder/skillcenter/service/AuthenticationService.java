package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.AuthenticationRequestDTO;

public interface AuthenticationService {
    
    PageResult<AuthenticationRequestDTO> getAllRequests(int pageNum, int pageSize);
    
    AuthenticationRequestDTO getRequestById(String requestId);
    
    boolean updateRequestStatus(String requestId, String status, String comments);
}
