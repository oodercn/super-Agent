package net.ooder.nexus.service.south;

import java.util.concurrent.CompletableFuture;

/**
 * 南向网络服务接口
 *
 * <p>提供 HTTP/认证能力，用于与下层服务交互。</p>
 *
 * @author ooder Team
 * @version 2.0
 * @since SDK 0.7.2
 */
public interface SouthNetworkService {

    CompletableFuture<HttpResponse> sendHttpRequest(HttpRequest request);

    CompletableFuture<AuthenticationResult> authenticate(String username, String password);

    boolean checkPermission(String userId, String resource, String action);

    class HttpRequest {
        private String url;
        private String method;
        private String body;
        private java.util.Map<String, String> headers;

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        public String getMethod() { return method; }
        public void setMethod(String method) { this.method = method; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public java.util.Map<String, String> getHeaders() { return headers; }
        public void setHeaders(java.util.Map<String, String> headers) { this.headers = headers; }
    }

    class HttpResponse {
        private int statusCode;
        private String body;
        private java.util.Map<String, String> headers;
        private String errorMessage;

        public int getStatusCode() { return statusCode; }
        public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public java.util.Map<String, String> getHeaders() { return headers; }
        public void setHeaders(java.util.Map<String, String> headers) { this.headers = headers; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public boolean isSuccess() {
            return statusCode >= 200 && statusCode < 300;
        }
    }

    class AuthenticationResult {
        private boolean success;
        private String userId;
        private String token;
        private long expiresAt;
        private String errorMessage;

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public long getExpiresAt() { return expiresAt; }
        public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
}
