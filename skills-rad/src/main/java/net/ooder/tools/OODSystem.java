package net.ooder.tools;

import net.ooder.cluster.ServerNode;
import net.ooder.cluster.service.ServerEventFactory;
import net.ooder.common.*;
import net.ooder.esb.config.manager.ExpressionTempBean;
import net.ooder.esb.config.manager.ServiceBean;
import net.ooder.org.OrgManager;
import net.ooder.org.Person;
import net.ooder.org.PersonNotFoundException;
import net.ooder.server.JDSServer;
import net.ooder.server.OrgManagerFactory;
import net.ooder.server.SubSystem;

import java.util.List;

public class OODSystem {

    String personname;

    String name;

    String sysid;

    SystemStatus status;

    String personid;

    String url;

    String orgname;

    String repeatEventKey = "";


    private String enname;

    private SystemType type;

    private ConfigCode configname;

    private TokenType tokenType;

    private String icon;

    private String vfsUrl;


    public OODSystem(ServerNode node) throws JDSException {
        SubSystem subSystem = JDSServer.getInstance().getClusterClient().getSystem(node.getId());
        if (subSystem == null) {
            throw new JDSException("subSystem is null systemId is[" + node.getId() + "] systemName is[" + node.getName() + "]");
        }

        String systemId = subSystem.getSysId();

        OrgManager orgManager = OrgManagerFactory.getOrgManager(subSystem.getConfigname());


        status = JDSServer.getClusterClient().getSystemStatus(node.getId());

        this.name = subSystem.getName();
        this.enname = subSystem.getEnname();
        this.configname = subSystem.getConfigname();
        this.icon = subSystem.getIcon();
        this.vfsUrl = subSystem.getUrl();
        this.tokenType = subSystem.getTokenType();
        this.personid = subSystem.getAdminId();

        this.type = subSystem.getType();
        if (orgManager.getTopOrgs(systemId).size() > 0) {
            this.orgname = orgManager.getTopOrgs(systemId).get(0).getName();
        }
        try {
            if (subSystem.getAdminId() != null && orgManager.getPersonByID(subSystem.getAdminId()) != null) {
                Person person = orgManager.getPersonByID(subSystem.getAdminId());
                this.personname = person.getName();
            }
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }

        ServerEventFactory factory = ServerEventFactory.getInstance();
        List<ExpressionTempBean> serviceBeans = factory.getRegisterEventByCode(node.getId());

        for (ServiceBean serviceBean : serviceBeans) {
            repeatEventKey = repeatEventKey + serviceBean.getName() + ",";
        }
        if (repeatEventKey.endsWith(",")) {
            repeatEventKey = repeatEventKey.substring(0, repeatEventKey.length() - 1);
        }

        this.url = subSystem.getUrl();

        this.sysid = subSystem.getSysId();

    }

    public String getRepeatEventKey() {
        return repeatEventKey;
    }

    public void setRepeatEventKey(String repeatEventKey) {
        this.repeatEventKey = repeatEventKey;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getOrgname() {
        return orgname;
    }

    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getSysid() {
        return sysid;
    }

    public void setSysid(String sysid) {
        this.sysid = sysid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SystemStatus getStatus() {
        return status;
    }

    public void setStatus(SystemStatus status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public ConfigCode getConfigname() {
        return configname;
    }

    public void setConfigname(ConfigCode configname) {
        this.configname = configname;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getVfsUrl() {
        return vfsUrl;
    }

    public void setVfsUrl(String vfsUrl) {
        this.vfsUrl = vfsUrl;
    }

    public SystemType getType() {

        return type;
    }

    public void setType(SystemType type) {
        this.type = type;
    }
}
