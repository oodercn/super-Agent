package net.ooder.nexus.dto.log;

import java.io.Serializable;

/**
 * Log export DTO
 * Used for exporting logs with filters
 */
public class LogExportDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Log level filter
     */
    private String level;

    /**
     * Log source filter
     */
    private String source;

    /**
     * Start timestamp for time range filter
     */
    private Long startTime;

    /**
     * End timestamp for time range filter
     */
    private Long endTime;

    /**
     * Export format: json, csv
     */
    private String format;

    /**
     * Output file path (optional)
     */
    private String outputPath;

    /**
     * Maximum number of logs to export
     */
    private Integer maxRecords;

    public LogExportDTO() {
        this.format = "json"; // default format
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public Integer getMaxRecords() {
        return maxRecords;
    }

    public void setMaxRecords(Integer maxRecords) {
        this.maxRecords = maxRecords;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LogExportDTO dto = new LogExportDTO();

        public Builder level(String level) {
            dto.setLevel(level);
            return this;
        }

        public Builder source(String source) {
            dto.setSource(source);
            return this;
        }

        public Builder startTime(Long startTime) {
            dto.setStartTime(startTime);
            return this;
        }

        public Builder endTime(Long endTime) {
            dto.setEndTime(endTime);
            return this;
        }

        public Builder format(String format) {
            dto.setFormat(format);
            return this;
        }

        public Builder outputPath(String outputPath) {
            dto.setOutputPath(outputPath);
            return this;
        }

        public Builder maxRecords(Integer maxRecords) {
            dto.setMaxRecords(maxRecords);
            return this;
        }

        public LogExportDTO build() {
            return dto;
        }
    }
}
