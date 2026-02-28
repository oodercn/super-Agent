package net.ooder.nexus.dto.personal;

import java.io.Serializable;
import java.util.List;

public class MediaStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String period;
    private MediaStatsOverviewDTO overview;
    private List<PlatformStatsDTO> byPlatform;
    private List<MediaTrendDTO> trend;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public MediaStatsOverviewDTO getOverview() {
        return overview;
    }

    public void setOverview(MediaStatsOverviewDTO overview) {
        this.overview = overview;
    }

    public List<PlatformStatsDTO> getByPlatform() {
        return byPlatform;
    }

    public void setByPlatform(List<PlatformStatsDTO> byPlatform) {
        this.byPlatform = byPlatform;
    }

    public List<MediaTrendDTO> getTrend() {
        return trend;
    }

    public void setTrend(List<MediaTrendDTO> trend) {
        this.trend = trend;
    }

    public static class MediaStatsOverviewDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private Long totalRead;
        private Long totalLike;
        private Long totalComment;
        private Long totalShare;

        public Long getTotalRead() {
            return totalRead;
        }

        public void setTotalRead(Long totalRead) {
            this.totalRead = totalRead;
        }

        public Long getTotalLike() {
            return totalLike;
        }

        public void setTotalLike(Long totalLike) {
            this.totalLike = totalLike;
        }

        public Long getTotalComment() {
            return totalComment;
        }

        public void setTotalComment(Long totalComment) {
            this.totalComment = totalComment;
        }

        public Long getTotalShare() {
            return totalShare;
        }

        public void setTotalShare(Long totalShare) {
            this.totalShare = totalShare;
        }
    }

    public static class PlatformStatsDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String platformId;
        private String name;
        private Long read;
        private Long like;
        private Long comment;
        private Long share;

        public String getPlatformId() {
            return platformId;
        }

        public void setPlatformId(String platformId) {
            this.platformId = platformId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getRead() {
            return read;
        }

        public void setRead(Long read) {
            this.read = read;
        }

        public Long getLike() {
            return like;
        }

        public void setLike(Long like) {
            this.like = like;
        }

        public Long getComment() {
            return comment;
        }

        public void setComment(Long comment) {
            this.comment = comment;
        }

        public Long getShare() {
            return share;
        }

        public void setShare(Long share) {
            this.share = share;
        }
    }

    public static class MediaTrendDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String date;
        private Integer read;
        private Integer like;
        private Integer comment;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getRead() {
            return read;
        }

        public void setRead(Integer read) {
            this.read = read;
        }

        public Integer getLike() {
            return like;
        }

        public void setLike(Integer like) {
            this.like = like;
        }

        public Integer getComment() {
            return comment;
        }

        public void setComment(Integer comment) {
            this.comment = comment;
        }
    }
}
