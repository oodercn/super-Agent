package net.ooder.skillcenter.service;

import net.ooder.skillcenter.dto.PageResult;
import net.ooder.nexus.skillcenter.dto.network.NetworkLinkDTO;
import net.ooder.nexus.skillcenter.dto.network.NetworkRouteDTO;
import net.ooder.nexus.skillcenter.dto.network.NetworkTopologyDTO;
import net.ooder.nexus.skillcenter.dto.network.NetworkQualityDTO;

import java.util.Map;

public interface NetworkService {

    Map<String, Object> getNetworkStatus();

    Map<String, Object> getNetworkStats();

    PageResult<NetworkLinkDTO> getLinks(int pageNum, int pageSize);

    NetworkLinkDTO getLinkById(String linkId);

    boolean disconnectLink(String linkId);

    boolean reconnectLink(String linkId);

    PageResult<NetworkRouteDTO> getRoutes(int pageNum, int pageSize);

    NetworkRouteDTO getRouteById(String routeId);

    NetworkRouteDTO findRoute(String sourceNode, String targetNode);

    NetworkTopologyDTO getTopology();

    NetworkQualityDTO getQuality();
}
