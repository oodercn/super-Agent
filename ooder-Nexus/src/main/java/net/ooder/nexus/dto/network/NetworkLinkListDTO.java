package net.ooder.nexus.dto.network;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class NetworkLinkListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<NetworkLinkDTO> links;
    private Integer total;
    private Map<String, Integer> statusSummary;

    public List<NetworkLinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<NetworkLinkDTO> links) {
        this.links = links;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Map<String, Integer> getStatusSummary() {
        return statusSummary;
    }

    public void setStatusSummary(Map<String, Integer> statusSummary) {
        this.statusSummary = statusSummary;
    }
}
