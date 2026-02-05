package net.ooder.tools;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.common.SystemStatus;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.engine.ConnectInfo;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.config.DevUserConfig;
import net.ooder.esd.engine.config.LocalServer;
import net.ooder.esd.engine.config.RemoteServer;
import net.ooder.esd.manager.esdserver.ESDServerUtil;
import net.ooder.server.ServerProjectConfig;
import net.ooder.server.httpproxy.ServerProxyFactory;
import net.ooder.server.httpproxy.core.ProxyHost;
import net.ooder.server.httpproxy.core.UUID;
import net.sf.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/admin/proxyhost/"})
@MethodChinaName(cname = "代理服务配置")

public class DeployHostService {

    @MethodChinaName(cname = "获取部署配置文件")
    @RequestMapping(value = {"getServerProjectConfig"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<ServerProjectConfig> getServerProjectConfig(String projectName) {
        ResultModel<ServerProjectConfig> result = new ResultModel<ServerProjectConfig>();
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            ServerProjectConfig config = new ServerProjectConfig();
            config.setHost(version.getProject().getConfig().getPublicServerUrl());
            config.setProjectName(version.getProject().getProjectName());
            config.setVersionName(version.getVersionName());
            config.setIndexPage("App.index");
            ConnectInfo connectInfo = ESDFacrory.getAdminESDClient().getConnectInfo();
            config.setUserId(connectInfo.getUserID());
            config.setUserName(connectInfo.getLoginName());
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<ServerProjectConfig> errorResult = new ErrorResultModel<ServerProjectConfig>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "获取本地用户代理")
    @RequestMapping(value = {"getProxyHosts"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<ProxyHost>> getProxyHosts() {
        ListResultModel<List<ProxyHost>> result = new ListResultModel<List<ProxyHost>>();
        List<ProxyHost> hosts = new ArrayList<ProxyHost>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            hosts = userConfig.getHosts();
            result.setData(hosts);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<ProxyHost>> errorResult = new ErrorListResultModel<List<ProxyHost>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "清空配置")
    @RequestMapping(value = {"clearHosts"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> clearHosts() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<ProxyHost> imgConfigs = new ArrayList<ProxyHost>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            ServerProxyFactory.getInstance().clear();
            userConfig.setHosts(new ArrayList<>());
            getClient().updateUserConfig(userConfig);
            result.setData(true);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "更新代理服务")
    @RequestMapping(value = {"updateProxyHosts"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<ProxyHost> updateProxyHosts(@RequestBody ProxyHost host) {
        ResultModel<ProxyHost> result = new ResultModel<ProxyHost>();

        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            String proxyId = host.getProxyId();
            if (proxyId == null || proxyId.equals("")) {
                proxyId = UUID.createUUID().toString();
                host.setProxyId(proxyId);
                userConfig.getHosts().add(host);
            } else {
                ProxyHost proxyHost = userConfig.getProxyHostById(proxyId);

                //更新运行时
                ProxyHost runtimeHost = ServerProxyFactory.serverProxyMap.get(proxyHost.getHost());
                if (runtimeHost != null) {
                    runtimeHost.setProxyUrl(proxyHost.getProxyUrl());
                }

                if (proxyHost != null) {
                    proxyHost.fill(host);
                } else {
                    userConfig.getHosts().add(host);
                }

            }

            getClient().updateUserConfig(userConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<ProxyHost> errorResult = new ErrorResultModel<ProxyHost>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "获取本地服务器配置")
    @RequestMapping(value = {"getServerList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<LocalServer>> getServerList() {
        ListResultModel<List<LocalServer>> result = new ListResultModel<List<LocalServer>>();
        List<LocalServer> hosts = new ArrayList<LocalServer>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            hosts = userConfig.getServers();
            result.setData(hosts);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<LocalServer>> errorResult = new ErrorListResultModel<List<LocalServer>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "删除远程服务器配置")
    @RequestMapping(value = {"deleteHost"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> deleteHost(String proxyId) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            DevUserConfig userConfig = getClient().getUserConfig();

            if (proxyId != null) {
                ProxyHost proxyHost = userConfig.getProxyHostById(proxyId);
                if (proxyHost != null) {
                    userConfig.getHosts().remove(proxyHost);
                }
                //更新运行时
                ProxyHost runtimeHost = ServerProxyFactory.serverProxyMap.get(proxyHost.getHost());
                if (runtimeHost != null) {
                    runtimeHost.setProxyUrl(null);
                }
            }

            getClient().updateUserConfig(userConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "删除远程服务器配置")
    @RequestMapping(value = {"deleteRemoteServer"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> deleteRemoteServer(String serverId) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            DevUserConfig userConfig = getClient().getUserConfig();

            if (serverId != null) {
                RemoteServer proxyHost = userConfig.getRemoteServerById(serverId);
                if (proxyHost != null) {
                    userConfig.getRemoteServers().remove(proxyHost);
                }

            }

            getClient().updateUserConfig(userConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "删除本地服务器配置")
    @RequestMapping(value = {"deleteLocalServer"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> deleteLocalServer(String serverId) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            if (serverId != null) {
                LocalServer proxyHost = userConfig.getServerById(serverId);
                if (proxyHost != null) {
                    userConfig.getServers().remove(proxyHost);
                }
            }

            getClient().updateUserConfig(userConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "更新本地服务器配置")
    @RequestMapping(value = {"updateLocalServer"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<LocalServer> updateLocalServer(@RequestBody LocalServer server) {
        ResultModel<LocalServer> result = new ResultModel<LocalServer>();

        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            String serverId = server.getServerId();
            if (serverId == null || serverId.equals("")) {
                serverId = UUID.createUUID().toString();
                server.setServerId(serverId);
                userConfig.getServers().add(server);
            } else {
                LocalServer proxyHost = userConfig.getServerById(serverId);
                if (proxyHost != null) {
                    BeanMap.create(proxyHost).putAll(BeanMap.create(server));
                } else {
                    userConfig.getServers().add(server);
                }

            }

            getClient().updateUserConfig(userConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<LocalServer> errorResult = new ErrorResultModel<LocalServer>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "清空本地服务器配置")
    @RequestMapping(value = {"clearServers"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> clearServers() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<ProxyHost> imgConfigs = new ArrayList<ProxyHost>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            userConfig.setServers(new ArrayList<>());
            getClient().updateUserConfig(userConfig);
            result.setData(true);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "启动本地服务器")
    @RequestMapping(value = {"startLocalServer"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> startLocalServer(String serverId, String projectName) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<ProxyHost> imgConfigs = new ArrayList<ProxyHost>();
        try {
            Project project = ESDFacrory.getAdminESDClient().getProjectByName(projectName);
            DevUserConfig userConfig = getClient().getUserConfig();
            LocalServer server = userConfig.getServerById(serverId);
            if (server != null) {
                ESDServerUtil.startESDServer(project, server);
                server.setStatus(SystemStatus.ONLINE);
            }

            result.setData(true);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "关闭服务器")
    @RequestMapping(value = {"stopLocalServer"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> stopLocalServer(String serverId) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<ProxyHost> imgConfigs = new ArrayList<ProxyHost>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            LocalServer server = userConfig.getServerById(serverId);
            if (server != null) {
                ESDServerUtil.stopESDServer(server);
                server.setStatus(SystemStatus.OFFLINE);
            }
            result.setData(true);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "获取远程服务器配置")
    @RequestMapping(value = {"getRemoteServerList"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<RemoteServer>> getRemoteServerList() {
        ListResultModel<List<RemoteServer>> result = new ListResultModel<List<RemoteServer>>();
        List<RemoteServer> hosts = new ArrayList<RemoteServer>();
        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            hosts = userConfig.getRemoteServers();
            result.setData(hosts);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<RemoteServer>> errorResult = new ErrorListResultModel<List<RemoteServer>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "更新远程服务器配置")
    @RequestMapping(value = {"updateRemoteServer"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<RemoteServer> updateRemoteServer(@RequestBody RemoteServer server) {
        ResultModel<RemoteServer> result = new ResultModel<RemoteServer>();

        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            String serverId = server.getServerId();
            if (serverId == null || serverId.equals("")) {
                serverId = UUID.createUUID().toString();
                server.setServerId(serverId);
                userConfig.getRemoteServers().add(server);
            } else {
                RemoteServer proxyHost = userConfig.getRemoteServerById(serverId);
                if (proxyHost != null) {
                    BeanMap.create(proxyHost).putAll(BeanMap.create(server));
                } else {
                    userConfig.getRemoteServers().add(server);
                }
            }

            getClient().updateUserConfig(userConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<RemoteServer> errorResult = new ErrorResultModel<RemoteServer>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "清空远程服务器配置")
    @RequestMapping(value = {"clearRemoteServers"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> clearRemoteServers() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        try {
            DevUserConfig userConfig = getClient().getUserConfig();
            userConfig.setRemoteServers(new ArrayList<>());
            getClient().updateUserConfig(userConfig);
            result.setData(true);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    public ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }

}
