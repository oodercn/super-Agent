package net.ooder.skillcenter.southbound;

public class HeartbeatConfig {
    private int interval = 30000;
    private int timeout = 90000;
    private int maxMissed = 3;
    private boolean autoFailover = true;

    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }

    public int getTimeout() { return timeout; }
    public void setTimeout(int timeout) { this.timeout = timeout; }

    public int getMaxMissed() { return maxMissed; }
    public void setMaxMissed(int maxMissed) { this.maxMissed = maxMissed; }

    public boolean isAutoFailover() { return autoFailover; }
    public void setAutoFailover(boolean autoFailover) { this.autoFailover = autoFailover; }
}
