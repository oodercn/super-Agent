package net.ooder.tools;

import net.ooder.cluster.ServerNode;
import net.ooder.cluster.ServerNodeList;
import net.ooder.common.ConfigCode;
import net.ooder.common.SystemStatus;
import net.ooder.config.CApplication;
import net.ooder.server.JDSServer;

import java.util.List;

public class OODApplication {
    private String userexpression;
    private String clusterManagerClass;
    private String code;
    private String nodeIds = "";
    private String nolineNodeIds = "";
    private String connectionHandle;
    private String jdsService;
    private String name;
    private String configPath;


    public OODApplication(CApplication application) {
        ServerNodeList node = JDSServer.getClusterClient().getServerNodeListByConfigCode(ConfigCode.fromType(application.getConfigCode()));
        this.code = application.getConfigCode();
        this.name = application.getName();
        List<ServerNode> servernodes = node.getServerNodeList();
        for (ServerNode subserverNode : servernodes) {
            String nodeId = subserverNode.getId();
            nodeIds = nodeIds + subserverNode.getId() + " ";
            if (JDSServer.getClusterClient().getSystemStatus(subserverNode.getId()).equals(SystemStatus.ONLINE)) {
                nolineNodeIds = nolineNodeIds + nodeId + " ";
            }
        }

        this.configPath = application.getConfigPath();


        this.connectionHandle = application.getConnectionHandle() != null ? application.getConnectionHandle().getImplementation() : "";
        this.jdsService = application.getJdsService() != null ? application.getJdsService().getImplementation() : "";
        this.userexpression = node.getUserexpression();
        this.clusterManagerClass = node.getClusterManagerClass();


    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }


    public String getNolineNodeIds() {
        return nolineNodeIds;
    }

    public void setNolineNodeIds(String nolineNodeIds) {
        this.nolineNodeIds = nolineNodeIds;
    }

    public String getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(String nodeIds) {
        this.nodeIds = nodeIds;
    }


    public String getUserexpression() {
        return userexpression;
    }

    public void setUserexpression(String userexpression) {
        this.userexpression = userexpression;
    }

    public String getClusterManagerClass() {
        return clusterManagerClass;
    }

    public void setClusterManagerClass(String clusterManagerClass) {
        this.clusterManagerClass = clusterManagerClass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getConnectionHandle() {
        return connectionHandle;
    }

    public void setConnectionHandle(String connectionHandle) {
        this.connectionHandle = connectionHandle;
    }

    public String getJdsService() {
        return jdsService;
    }

    public void setJdsService(String jdsService) {
        this.jdsService = jdsService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
