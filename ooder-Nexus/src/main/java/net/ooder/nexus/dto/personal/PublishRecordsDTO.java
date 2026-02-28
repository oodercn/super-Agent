package net.ooder.nexus.dto.personal;

import net.ooder.nexus.domain.personal.model.MediaPublishTask;

import java.io.Serializable;
import java.util.List;

public class PublishRecordsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer total;
    private List<MediaPublishTask> records;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<MediaPublishTask> getRecords() {
        return records;
    }

    public void setRecords(List<MediaPublishTask> records) {
        this.records = records;
    }
}
