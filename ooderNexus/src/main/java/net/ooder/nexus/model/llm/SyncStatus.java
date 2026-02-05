package net.ooder.nexus.model.llm;

import java.util.List;

public class SyncStatus {
    private String batchId;
    private SyncState state;
    private int totalSkills;
    private int completedSkills;
    private int failedSkills;
    private long startTime;
    private long endTime;
    private String errorMessage;
    private List<SyncTask> tasks;

    public SyncStatus() {
        this.startTime = System.currentTimeMillis();
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public SyncState getState() {
        return state;
    }

    public void setState(SyncState state) {
        this.state = state;
    }

    public int getTotalSkills() {
        return totalSkills;
    }

    public void setTotalSkills(int totalSkills) {
        this.totalSkills = totalSkills;
    }

    public int getCompletedSkills() {
        return completedSkills;
    }

    public void setCompletedSkills(int completedSkills) {
        this.completedSkills = completedSkills;
    }

    public int getFailedSkills() {
        return failedSkills;
    }

    public void setFailedSkills(int failedSkills) {
        this.failedSkills = failedSkills;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<SyncTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<SyncTask> tasks) {
        this.tasks = tasks;
    }

    public enum SyncState {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    public static class SyncTask {
        private String taskId;
        private String skillId;
        private String skillName;
        private TaskState state;
        private String errorMessage;
        private long startTime;
        private long endTime;

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getSkillId() {
            return skillId;
        }

        public void setSkillId(String skillId) {
            this.skillId = skillId;
        }

        public String getSkillName() {
            return skillName;
        }

        public void setSkillName(String skillName) {
            this.skillName = skillName;
        }

        public TaskState getState() {
            return state;
        }

        public void setState(TaskState state) {
            this.state = state;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public enum TaskState {
            PENDING,
            IN_PROGRESS,
            COMPLETED,
            FAILED,
            CANCELLED
        }
    }
}
