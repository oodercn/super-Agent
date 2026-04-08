package net.ooder.agent.dto.capability;

import java.util.List;

public class CapabilityLogListDTO {
    private List<CapabilityLogDTO> list;
    private Integer total;
    private Integer pageNum;
    private Integer pageSize;

    public List<CapabilityLogDTO> getList() {
        return list;
    }

    public void setList(List<CapabilityLogDTO> list) {
        this.list = list;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
