package net.ooder.nexus.dto.personal;

import java.io.Serializable;

public class PlatformConfigResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String platformId;
    private String status;
    private AccountInfoDTO accountInfo;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccountInfoDTO getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(AccountInfoDTO accountInfo) {
        this.accountInfo = accountInfo;
    }

    public static class AccountInfoDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private String type;
        private Boolean verified;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }
    }
}
