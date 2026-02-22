package net.ooder.nexus.skillcenter.dto.hosting;

import java.util.List;
import java.util.Map;

public class VolumeDTO {
    private String volumeId;
    private String name;
    private String type;
    private long size;
    private String status;
    private String accessMode;
    private String storageClass;
    private List<VolumeMount> mounts;
    private Map<String, Object> metadata;

    public String getVolumeId() { return volumeId; }
    public void setVolumeId(String volumeId) { this.volumeId = volumeId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getAccessMode() { return accessMode; }
    public void setAccessMode(String accessMode) { this.accessMode = accessMode; }
    public String getStorageClass() { return storageClass; }
    public void setStorageClass(String storageClass) { this.storageClass = storageClass; }
    public List<VolumeMount> getMounts() { return mounts; }
    public void setMounts(List<VolumeMount> mounts) { this.mounts = mounts; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    public static class VolumeMount {
        private String instanceId;
        private String mountPath;
        private boolean readOnly;

        public String getInstanceId() { return instanceId; }
        public void setInstanceId(String instanceId) { this.instanceId = instanceId; }
        public String getMountPath() { return mountPath; }
        public void setMountPath(String mountPath) { this.mountPath = mountPath; }
        public boolean isReadOnly() { return readOnly; }
        public void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }
    }
}
