
package net.ooder.sdk.core.metadata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Timeline implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final int DEFAULT_MAX_HISTORY = 1000;
    private static final long DEFAULT_ONLINE_TIMEOUT = TimeUnit.MINUTES.toMillis(5);
    
    private final List<TimelinePoint> history;
    private Context currentContext;
    private int maxHistory;
    private long onlineTimeout;
    private long createTime;
    
    public Timeline() {
        this.history = new ArrayList<>();
        this.currentContext = Context.empty();
        this.maxHistory = DEFAULT_MAX_HISTORY;
        this.onlineTimeout = DEFAULT_ONLINE_TIMEOUT;
        this.createTime = System.currentTimeMillis();
    }
    
    public Timeline(int maxHistory) {
        this();
        this.maxHistory = maxHistory;
    }
    
    public List<TimelinePoint> getHistory() {
        return Collections.unmodifiableList(history);
    }
    
    public Context getCurrentContext() {
        return currentContext;
    }
    
    public void setCurrentContext(Context context) {
        this.currentContext = context;
    }
    
    public int getMaxHistory() { return maxHistory; }
    public void setMaxHistory(int maxHistory) { this.maxHistory = maxHistory; }
    
    public long getOnlineTimeout() { return onlineTimeout; }
    public void setOnlineTimeout(long onlineTimeout) { this.onlineTimeout = onlineTimeout; }
    
    public long getCreateTime() { return createTime; }
    
    public void record(String event) {
        record(event, currentContext);
    }
    
    public void record(String event, Context context) {
        TimelinePoint point = new TimelinePoint(context, event);
        
        if (history.size() >= maxHistory) {
            history.remove(0);
        }
        history.add(point);
        
        if (context != null && context.isValid()) {
            this.currentContext = context;
        }
    }
    
    public void record(String event, IdentityInfo identity, LocationInfo location, ResourceInfo resource) {
        Context context = new Context(identity, location, resource);
        record(event, context);
    }
    
    public void updateContext(IdentityInfo identity, LocationInfo location, ResourceInfo resource) {
        this.currentContext = new Context(identity, location, resource);
    }
    
    public TimelinePoint getLatest() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1);
    }
    
    public TimelinePoint getFirst() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(0);
    }
    
    public List<TimelinePoint> getByEventType(String eventType) {
        List<TimelinePoint> result = new ArrayList<>();
        for (TimelinePoint point : history) {
            if (eventType.equals(point.getEventType())) {
                result.add(point);
            }
        }
        return result;
    }
    
    public List<TimelinePoint> getByTimeRange(long startTime, long endTime) {
        List<TimelinePoint> result = new ArrayList<>();
        for (TimelinePoint point : history) {
            if (point.getTimestamp() >= startTime && point.getTimestamp() <= endTime) {
                result.add(point);
            }
        }
        return result;
    }
    
    public TimelinePoint findNearest(long timestamp) {
        if (history.isEmpty()) {
            return null;
        }
        
        TimelinePoint nearest = history.get(0);
        long minDiff = Math.abs(nearest.getTimestamp() - timestamp);
        
        for (TimelinePoint point : history) {
            long diff = Math.abs(point.getTimestamp() - timestamp);
            if (diff < minDiff) {
                minDiff = diff;
                nearest = point;
            }
        }
        
        return nearest;
    }
    
    public Context getContextAt(long timestamp) {
        TimelinePoint point = findNearest(timestamp);
        return point != null ? point.getContext() : null;
    }
    
    public boolean isOnline() {
        if (history.isEmpty()) {
            return false;
        }
        TimelinePoint latest = getLatest();
        if (latest == null) {
            return false;
        }
        return (System.currentTimeMillis() - latest.getTimestamp()) < onlineTimeout;
    }
    
    public boolean isExpired() {
        return false;
    }
    
    public long getAge() {
        return System.currentTimeMillis() - createTime;
    }
    
    public long getUptime() {
        return getAge();
    }
    
    public long getIdleTime() {
        if (history.isEmpty()) {
            return getAge();
        }
        TimelinePoint latest = getLatest();
        if (latest == null) {
            return getAge();
        }
        return System.currentTimeMillis() - latest.getTimestamp();
    }
    
    public long getTimeSinceLastEvent() {
        return getIdleTime();
    }
    
    public int getEventCount() {
        return history.size();
    }
    
    public void clear() {
        history.clear();
    }
    
    public String getAgeFormatted() {
        return formatDuration(getAge());
    }
    
    public String getIdleTimeFormatted() {
        return formatDuration(getIdleTime());
    }
    
    private String formatDuration(long millis) {
        long seconds = millis / 1000;
        if (seconds < 60) return seconds + "s";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + "m " + (seconds % 60) + "s";
        long hours = minutes / 60;
        if (hours < 24) return hours + "h " + (minutes % 60) + "m";
        long days = hours / 24;
        return days + "d " + (hours % 24) + "h";
    }
    
    public String getStatusSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(isOnline() ? "online" : "offline");
        sb.append(", age=").append(getAgeFormatted());
        sb.append(", idle=").append(getIdleTimeFormatted());
        sb.append(", events=").append(getEventCount());
        return sb.toString();
    }
    
    public static Timeline create() {
        return new Timeline();
    }
    
    public static Timeline withMaxHistory(int maxHistory) {
        return new Timeline(maxHistory);
    }
}
