package net.ooder.tools;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.engine.*;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.vfs.Folder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = {"/admin/plugs/component/"})
@MethodChinaName(cname = "扩展组件服务")
public class ComponentPluginService {

    @MethodChinaName(cname = "获取扩展组件服务")
    @RequestMapping(value = {"getExtCom"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<Set<EUModule>> getExtCom( String projectName) {
        ListResultModel<Set<EUModule>> result = new ListResultModel<Set<EUModule>>();
        Set<EUModule> modulesConfigs = new HashSet<>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            modulesConfigs = projectVersion.getAllModule();
            result.setData(modulesConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<Set<EUModule>> errorResult = new ErrorListResultModel<Set<EUModule>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "添加组件")
    @RequestMapping(value = {"addExtCom"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> addComponent(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            String[] ids = StringUtility.split(id, ";");

            ProjectConfig config = version.getProject().getConfig();
            List<String> extcom= config.getExtcoms();
            for (String apiId : ids) {
                if (!extcom.contains(apiId)) {
                    extcom.add(apiId);
                }
            }

            config.setExtcoms(extcom);
            version.updateConfig(config);

        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }


    @MethodChinaName(cname = "获取组件工程")
    @RequestMapping(value = {"getExtComTreeProject"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getExtComTreeProject() {
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        List<OODFile> oodFiles = new ArrayList<OODFile>();
        List<Folder> folders = new ArrayList<Folder>();
        try {

            List<Project> projects = this.getClient().getAllProject(ProjectDefAccess.ExtCom);
            for (Project project : projects) {
                Set<EUModule> modules = null;
                ProjectVersion projectVersion = project.getActiveProjectVersion();
                try {
                    modules = projectVersion.getAllModule();
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (modules != null && modules.size() > 0) {
                    oodFiles.add(new OODFile(projectVersion));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }
        result.setData(oodFiles);
        return result;
    }

    @MethodChinaName(cname = "获取通用组件工程")
    @RequestMapping(value = {"getComTreeProject"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getComTreeProject() {
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        List<OODFile> oodFiles = new ArrayList<OODFile>();
        List<Folder> folders = new ArrayList<Folder>();
        try {

            List<Project> projects = this.getClient().getAllProject(ProjectDefAccess.Component);
            for (Project project : projects) {
                Set<EUModule> modules = null;
                ProjectVersion projectVersion = project.getActiveProjectVersion();
                try {
                    modules = projectVersion.getAllModule();
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (modules != null && modules.size() > 0) {
                    oodFiles.add(new OODFile(projectVersion));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }
        result.setData(oodFiles);
        return result;
    }

    @MethodChinaName(cname = "删除组件")
    @RequestMapping(value = {"delComponent"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> delComponent(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<String> fontConfigIds = new ArrayList<String>();
        try {
            Project project = getClient().getProjectByName(projectName);
            fontConfigIds = project.getConfig().getFonts();
            fontConfigIds.remove(id);
            this.getClient().updateProjectConfig(project.getId(), project.getConfig());
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<Boolean> errorResult = new ErrorResultModel<Boolean>();
            errorResult.setErrdes(e.getMessage());
            errorResult.setErrcode(e.getErrorCode());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "获取组件")
    @RequestMapping(value = {"getProjectExtComs"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<EUModule>> getProjectComponents(String projectName) {
        ListResultModel<List<EUModule>> result = new ListResultModel<List<EUModule>>();

        try {
            Project project = getClient().getProjectByName(projectName);
            List<EUModule> fontConfigs = project.getComponents();
            result.setData(fontConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }

}
