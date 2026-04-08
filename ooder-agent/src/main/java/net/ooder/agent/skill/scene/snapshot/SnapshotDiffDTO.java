package net.ooder.mvp.skill.scene.snapshot;

import java.util.List;
import java.util.Map;

public class SnapshotDiffDTO {
    private String versionId1;
    private String versionId2;
    private String snapshotId;
    private long compareTime;
    private boolean identical;
    private List<FieldDiff> fieldDiffs;
    private Map<String, Object> addedData;
    private Map<String, Object> removedData;
    private Map<String, Object> modifiedData;
    private int addedCount;
    private int removedCount;
    private int modifiedCount;
    
    public static class FieldDiff {
        private String fieldPath;
        private Object oldValue;
        private Object newValue;
        private String diffType;
        
        public String getFieldPath() { return fieldPath; }
        public void setFieldPath(String fieldPath) { this.fieldPath = fieldPath; }
        public Object getOldValue() { return oldValue; }
        public void setOldValue(Object oldValue) { this.oldValue = oldValue; }
        public Object getNewValue() { return newValue; }
        public void setNewValue(Object newValue) { this.newValue = newValue; }
        public String getDiffType() { return diffType; }
        public void setDiffType(String diffType) { this.diffType = diffType; }
    }
    
    public String getVersionId1() { return versionId1; }
    public void setVersionId1(String versionId1) { this.versionId1 = versionId1; }
    public String getVersionId2() { return versionId2; }
    public void setVersionId2(String versionId2) { this.versionId2 = versionId2; }
    public String getSnapshotId() { return snapshotId; }
    public void setSnapshotId(String snapshotId) { this.snapshotId = snapshotId; }
    public long getCompareTime() { return compareTime; }
    public void setCompareTime(long compareTime) { this.compareTime = compareTime; }
    public boolean isIdentical() { return identical; }
    public void setIdentical(boolean identical) { this.identical = identical; }
    public List<FieldDiff> getFieldDiffs() { return fieldDiffs; }
    public void setFieldDiffs(List<FieldDiff> fieldDiffs) { this.fieldDiffs = fieldDiffs; }
    public Map<String, Object> getAddedData() { return addedData; }
    public void setAddedData(Map<String, Object> addedData) { this.addedData = addedData; }
    public Map<String, Object> getRemovedData() { return removedData; }
    public void setRemovedData(Map<String, Object> removedData) { this.removedData = removedData; }
    public Map<String, Object> getModifiedData() { return modifiedData; }
    public void setModifiedData(Map<String, Object> modifiedData) { this.modifiedData = modifiedData; }
    public int getAddedCount() { return addedCount; }
    public void setAddedCount(int addedCount) { this.addedCount = addedCount; }
    public int getRemovedCount() { return removedCount; }
    public void setRemovedCount(int removedCount) { this.removedCount = removedCount; }
    public int getModifiedCount() { return modifiedCount; }
    public void setModifiedCount(int modifiedCount) { this.modifiedCount = modifiedCount; }
}
