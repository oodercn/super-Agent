package net.ooder.sdk.api.llm;

/**
 * Token Usage Statistics
 *
 * @author ooder Team
 * @since 0.7.1
 */
public class TokenUsage {

    private int promptTokens;
    private int completionTokens;
    private int totalTokens;
    private long requestCount;

    public TokenUsage() {
        this.promptTokens = 0;
        this.completionTokens = 0;
        this.totalTokens = 0;
        this.requestCount = 0;
    }

    public void addUsage(int promptTokens, int completionTokens) {
        this.promptTokens += promptTokens;
        this.completionTokens += completionTokens;
        this.totalTokens += promptTokens + completionTokens;
        this.requestCount++;
    }

    public int getPromptTokens() { return promptTokens; }
    public void setPromptTokens(int promptTokens) { this.promptTokens = promptTokens; }

    public int getCompletionTokens() { return completionTokens; }
    public void setCompletionTokens(int completionTokens) { this.completionTokens = completionTokens; }

    public int getTotalTokens() { return totalTokens; }
    public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }

    public long getRequestCount() { return requestCount; }
    public void setRequestCount(long requestCount) { this.requestCount = requestCount; }

    public void reset() {
        this.promptTokens = 0;
        this.completionTokens = 0;
        this.totalTokens = 0;
        this.requestCount = 0;
    }

    @Override
    public String toString() {
        return "TokenUsage{" +
                "promptTokens=" + promptTokens +
                ", completionTokens=" + completionTokens +
                ", totalTokens=" + totalTokens +
                ", requestCount=" + requestCount +
                '}';
    }
}
