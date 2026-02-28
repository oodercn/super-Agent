package net.ooder.nexus.dto.p2p;

import java.io.Serializable;

public class LinkCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
