package net.ooder.skillcenter.sdk.cloud;

import java.util.List;
import java.util.Map;

public class ContainerConfig {
    private String image;
    private List<String> command;
    private List<String> args;
    private Map<String, String> env;
    private List<PortConfig> ports;
    private List<VolumeMount> volumes;
    private ProbeConfig livenessProbe;
    private ProbeConfig readinessProbe;

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public List<String> getCommand() { return command; }
    public void setCommand(List<String> command) { this.command = command; }
    public List<String> getArgs() { return args; }
    public void setArgs(List<String> args) { this.args = args; }
    public Map<String, String> getEnv() { return env; }
    public void setEnv(Map<String, String> env) { this.env = env; }
    public List<PortConfig> getPorts() { return ports; }
    public void setPorts(List<PortConfig> ports) { this.ports = ports; }
    public List<VolumeMount> getVolumes() { return volumes; }
    public void setVolumes(List<VolumeMount> volumes) { this.volumes = volumes; }
    public ProbeConfig getLivenessProbe() { return livenessProbe; }
    public void setLivenessProbe(ProbeConfig livenessProbe) { this.livenessProbe = livenessProbe; }
    public ProbeConfig getReadinessProbe() { return readinessProbe; }
    public void setReadinessProbe(ProbeConfig readinessProbe) { this.readinessProbe = readinessProbe; }

    public static class PortConfig {
        private int port;
        private String protocol;
        private String name;

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getProtocol() { return protocol; }
        public void setProtocol(String protocol) { this.protocol = protocol; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class VolumeMount {
        private String name;
        private String mountPath;
        private boolean readOnly;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getMountPath() { return mountPath; }
        public void setMountPath(String mountPath) { this.mountPath = mountPath; }
        public boolean isReadOnly() { return readOnly; }
        public void setReadOnly(boolean readOnly) { this.readOnly = readOnly; }
    }

    public static class ProbeConfig {
        private String type;
        private String path;
        private int port;
        private int initialDelaySeconds;
        private int periodSeconds;
        private int timeoutSeconds;
        private int failureThreshold;

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public int getInitialDelaySeconds() { return initialDelaySeconds; }
        public void setInitialDelaySeconds(int initialDelaySeconds) { this.initialDelaySeconds = initialDelaySeconds; }
        public int getPeriodSeconds() { return periodSeconds; }
        public void setPeriodSeconds(int periodSeconds) { this.periodSeconds = periodSeconds; }
        public int getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
        public int getFailureThreshold() { return failureThreshold; }
        public void setFailureThreshold(int failureThreshold) { this.failureThreshold = failureThreshold; }
    }
}
