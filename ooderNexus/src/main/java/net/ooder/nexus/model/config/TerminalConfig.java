package net.ooder.nexus.model.config;

import java.io.Serializable;

public class TerminalConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private int maxTerminals;
    private int terminalTimeout;
    private int terminalReconnectAttempts;
    private int terminalReconnectDelay;
    private boolean enableTerminalLogging;
    private boolean enableTerminalHistory;
    private int maxTerminalHistorySize;
    private long lastUpdated;

    public TerminalConfig() {
    }

    public TerminalConfig(int maxTerminals, int terminalTimeout, int terminalReconnectAttempts, int terminalReconnectDelay, boolean enableTerminalLogging, boolean enableTerminalHistory, int maxTerminalHistorySize) {
        this.maxTerminals = maxTerminals;
        this.terminalTimeout = terminalTimeout;
        this.terminalReconnectAttempts = terminalReconnectAttempts;
        this.terminalReconnectDelay = terminalReconnectDelay;
        this.enableTerminalLogging = enableTerminalLogging;
        this.enableTerminalHistory = enableTerminalHistory;
        this.maxTerminalHistorySize = maxTerminalHistorySize;
        this.lastUpdated = System.currentTimeMillis();
    }

    public TerminalConfig(int maxTerminals, int terminalTimeout, int terminalReconnectAttempts, int terminalReconnectDelay, boolean enableTerminalLogging, boolean enableTerminalHistory, int maxTerminalHistorySize, long lastUpdated) {
        this.maxTerminals = maxTerminals;
        this.terminalTimeout = terminalTimeout;
        this.terminalReconnectAttempts = terminalReconnectAttempts;
        this.terminalReconnectDelay = terminalReconnectDelay;
        this.enableTerminalLogging = enableTerminalLogging;
        this.enableTerminalHistory = enableTerminalHistory;
        this.maxTerminalHistorySize = maxTerminalHistorySize;
        this.lastUpdated = lastUpdated;
    }

    public int getMaxTerminals() {
        return maxTerminals;
    }

    public void setMaxTerminals(int maxTerminals) {
        this.maxTerminals = maxTerminals;
    }

    public int getTerminalTimeout() {
        return terminalTimeout;
    }

    public void setTerminalTimeout(int terminalTimeout) {
        this.terminalTimeout = terminalTimeout;
    }

    public int getTerminalReconnectAttempts() {
        return terminalReconnectAttempts;
    }

    public void setTerminalReconnectAttempts(int terminalReconnectAttempts) {
        this.terminalReconnectAttempts = terminalReconnectAttempts;
    }

    public int getTerminalReconnectDelay() {
        return terminalReconnectDelay;
    }

    public void setTerminalReconnectDelay(int terminalReconnectDelay) {
        this.terminalReconnectDelay = terminalReconnectDelay;
    }

    public boolean isEnableTerminalLogging() {
        return enableTerminalLogging;
    }

    public void setEnableTerminalLogging(boolean enableTerminalLogging) {
        this.enableTerminalLogging = enableTerminalLogging;
    }

    public boolean isEnableTerminalHistory() {
        return enableTerminalHistory;
    }

    public void setEnableTerminalHistory(boolean enableTerminalHistory) {
        this.enableTerminalHistory = enableTerminalHistory;
    }

    public int getMaxTerminalHistorySize() {
        return maxTerminalHistorySize;
    }

    public void setMaxTerminalHistorySize(int maxTerminalHistorySize) {
        this.maxTerminalHistorySize = maxTerminalHistorySize;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
