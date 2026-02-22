package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.AuthenticationRequestDTO;
import net.ooder.skillcenter.service.AuthenticationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class AuthenticationServiceSdkImpl implements AuthenticationService {

    private final Map<String, AuthenticationRequestDTO> requestStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
    }

    @Override
    public PageResult<AuthenticationRequestDTO> getAllRequests(int pageNum, int pageSize) {
        List<AuthenticationRequestDTO> all = new ArrayList<>(requestStore.values());
        all.sort(Comparator.comparing(AuthenticationRequestDTO::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        return paginate(all, pageNum, pageSize);
    }

    @Override
    public AuthenticationRequestDTO getRequestById(String requestId) {
        return requestStore.get(requestId);
    }

    @Override
    public boolean updateRequestStatus(String requestId, String status, String comments) {
        AuthenticationRequestDTO request = requestStore.get(requestId);
        if (request != null) {
            request.setStatus(status);
            request.setComments(comments);
            request.setUpdatedAt(new Date());
            return true;
        }
        return false;
    }

    private <T> PageResult<T> paginate(List<T> list, int pageNum, int pageSize) {
        int total = list.size();
        int start = (pageNum - 1) * pageSize;
        int end = Math.min(start + pageSize, total);

        if (start >= total) {
            return PageResult.empty();
        }

        List<T> pageList = list.subList(start, end);
        return PageResult.of(pageList, total, pageNum, pageSize);
    }
}
