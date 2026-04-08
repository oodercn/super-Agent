package net.ooder.skill.config.dto;

public class CliQrCodeDTO {
    
    private boolean success;
    private String cliId;
    private String qrcodeUrl;
    private long expireTime;
    private String scanTip;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getCliId() { return cliId; }
    public void setCliId(String cliId) { this.cliId = cliId; }
    public String getQrcodeUrl() { return qrcodeUrl; }
    public void setQrcodeUrl(String qrcodeUrl) { this.qrcodeUrl = qrcodeUrl; }
    public long getExpireTime() { return expireTime; }
    public void setExpireTime(long expireTime) { this.expireTime = expireTime; }
    public String getScanTip() { return scanTip; }
    public void setScanTip(String scanTip) { this.scanTip = scanTip; }
}
