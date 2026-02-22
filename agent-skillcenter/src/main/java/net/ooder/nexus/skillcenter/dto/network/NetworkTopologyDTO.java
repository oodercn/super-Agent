package net.ooder.nexus.skillcenter.dto.network;

import java.util.List;

public class NetworkTopologyDTO {

    private List<TopologyNodeDTO> nodes;
    private List<TopologyLinkDTO> links;
    private Long timestamp;

    public NetworkTopologyDTO() {}

    public List<TopologyNodeDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<TopologyNodeDTO> nodes) {
        this.nodes = nodes;
    }

    public List<TopologyLinkDTO> getLinks() {
        return links;
    }

    public void setLinks(List<TopologyLinkDTO> links) {
        this.links = links;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
