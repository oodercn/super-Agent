package net.ooder.nexus.dto.scene;

import java.io.Serializable;
import java.util.List;

public class LocalSceneOverviewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer totalScenes;
    private Integer activeScenes;
    private Integer archivedScenes;
    private List<SceneSummaryDTO> recentScenes;

    public Integer getTotalScenes() {
        return totalScenes;
    }

    public void setTotalScenes(Integer totalScenes) {
        this.totalScenes = totalScenes;
    }

    public Integer getActiveScenes() {
        return activeScenes;
    }

    public void setActiveScenes(Integer activeScenes) {
        this.activeScenes = activeScenes;
    }

    public Integer getArchivedScenes() {
        return archivedScenes;
    }

    public void setArchivedScenes(Integer archivedScenes) {
        this.archivedScenes = archivedScenes;
    }

    public List<SceneSummaryDTO> getRecentScenes() {
        return recentScenes;
    }

    public void setRecentScenes(List<SceneSummaryDTO> recentScenes) {
        this.recentScenes = recentScenes;
    }

    public static class SceneSummaryDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        private String sceneId;
        private String name;
        private String status;
        private String updatedAt;

        public String getSceneId() {
            return sceneId;
        }

        public void setSceneId(String sceneId) {
            this.sceneId = sceneId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
