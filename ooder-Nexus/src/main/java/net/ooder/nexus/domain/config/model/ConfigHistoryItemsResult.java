package net.ooder.nexus.domain.config.model;

import java.io.Serializable;
import java.util.List;

public class ConfigHistoryItemsResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ConfigHistoryItem> history;
    private int count;

    public ConfigHistoryItemsResult() {
    }

    public ConfigHistoryItemsResult(List<ConfigHistoryItem> history, int count) {
        this.history = history;
        this.count = count;
    }

    public List<ConfigHistoryItem> getHistory() {
        return history;
    }

    public void setHistory(List<ConfigHistoryItem> history) {
        this.history = history;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
