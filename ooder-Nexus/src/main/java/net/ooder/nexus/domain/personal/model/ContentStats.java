package net.ooder.nexus.domain.personal.model;

public class ContentStats {
    private long read;
    private long like;
    private long comment;
    private long share;

    public ContentStats() {}

    public long getRead() {
        return read;
    }

    public void setRead(long read) {
        this.read = read;
    }

    public long getLike() {
        return like;
    }

    public void setLike(long like) {
        this.like = like;
    }

    public long getComment() {
        return comment;
    }

    public void setComment(long comment) {
        this.comment = comment;
    }

    public long getShare() {
        return share;
    }

    public void setShare(long share) {
        this.share = share;
    }
}
