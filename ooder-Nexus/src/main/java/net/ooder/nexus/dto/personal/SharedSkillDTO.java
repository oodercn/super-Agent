package net.ooder.nexus.dto.personal;

import java.io.Serializable;

public class SharedSkillDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String sharedBy;
    private String sharedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSharedBy() {
        return sharedBy;
    }

    public void setSharedBy(String sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getSharedTime() {
        return sharedTime;
    }

    public void setSharedTime(String sharedTime) {
        this.sharedTime = sharedTime;
    }
}
