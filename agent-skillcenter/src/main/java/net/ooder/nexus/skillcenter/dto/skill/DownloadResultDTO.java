package net.ooder.nexus.skillcenter.dto.skill;

/**
 * 下载结果DTO - 符合v0.7.0协议规范
 */
public class DownloadResultDTO {
    
    private boolean success;
    private String skillId;
    private String filename;
    private long size;
    private String message;

    public DownloadResultDTO() {}

    public static DownloadResultDTO success(String skillId, String filename, long size) {
        DownloadResultDTO dto = new DownloadResultDTO();
        dto.setSuccess(true);
        dto.setSkillId(skillId);
        dto.setFilename(filename);
        dto.setSize(size);
        return dto;
    }

    public static DownloadResultDTO failure(String message) {
        DownloadResultDTO dto = new DownloadResultDTO();
        dto.setSuccess(false);
        dto.setMessage(message);
        return dto;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getSkillId() { return skillId; }
    public void setSkillId(String skillId) { this.skillId = skillId; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
