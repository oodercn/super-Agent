package net.ooder.skillcenter.sdk.protocol;

import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO;
import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO.AutoLoginConfigDTO;
import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO.CredentialDTO;
import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO.DomainPolicyDTO;
import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO.LoginRequestDTO;
import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO.LoginResultDTO;
import net.ooder.nexus.skillcenter.dto.protocol.LoginDTO.SessionDTO;

import java.util.concurrent.CompletableFuture;

public interface LoginProtocolAdapter {

    CompletableFuture<LoginResultDTO> login(LoginRequestDTO request);

    CompletableFuture<Void> logout(String sessionId);

    CompletableFuture<LoginResultDTO> autoLogin(AutoLoginConfigDTO config);

    CompletableFuture<SessionDTO> getSession(String sessionId);

    CompletableFuture<SessionDTO> validateSession(String sessionId);

    CompletableFuture<Void> refreshSession(String sessionId);

    CompletableFuture<DomainPolicyDTO> getDomainPolicy(String userId);

    CompletableFuture<Void> saveCredential(CredentialDTO credential);

    CompletableFuture<CredentialDTO> loadCredential(String userId);

    CompletableFuture<Void> clearCredential(String userId);

    void addLoginListener(LoginEventListener listener);

    void removeLoginListener(LoginEventListener listener);

    boolean isAvailable();

    interface LoginEventListener {
        void onLoginSuccess(LoginResultDTO result);
        void onLoginFailure(String errorCode, String errorMessage);
        void onLogout(String sessionId);
        void onSessionExpired(String sessionId);
        void onPolicyApplied(String domainId, DomainPolicyDTO policy);
    }
}
