package net.ooder.tools;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.font.node.FontNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/admin/plugs/font/"})
@MethodChinaName(cname = "图标字体服务")
public class FontService {

    @MethodChinaName(cname = "字体类配置")
    @RequestMapping(value = {"getSelFont"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<FontConfig>> getSelFont(String projectName) {
        ListResultModel<List<FontConfig>> result = new ListResultModel<List<FontConfig>>();
        List<FontConfig> fontConfigs = new ArrayList<FontConfig>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            fontConfigs = projectVersion.getProject().getFonts();
            result.setData(fontConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<FontConfig>> errorResult = new ErrorListResultModel<List<FontConfig>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "添加字体定义")
    @RequestMapping(value = {"addFont"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> addFont(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            String[] ids = StringUtility.split(id, ";");
            ProjectConfig config = version.getProject().getConfig();
            List<String> fontIds = config.getFonts();
            for (String apiId : ids) {
                if (!fontIds.contains(apiId)) {
                    fontIds.add(apiId);
                }
            }
            config.setFonts(fontIds);
            version.updateConfig(config);

        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }


    @MethodChinaName(cname = "获取字体文件定义")
    @RequestMapping(value = {"getFontTreeProject"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<FontNode>> getFontTreeProject() {
        ListResultModel<List<FontNode>> result = new ListResultModel<List<FontNode>>();
        List<FontNode> fontNodes = new ArrayList<FontNode>();
        try {
            List<Project> projects = getClient().getResourceAllProject(ProjectResourceType.font);
            for (Project project : projects) {
                fontNodes.add(new FontNode(project));
            }
            result.setData(fontNodes);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }


    @MethodChinaName(cname = "字体类配置")
    @RequestMapping(value = {"delFont"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> delFont(String projectName, String id) {
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


    @MethodChinaName(cname = "字体类配置")
    @RequestMapping(value = {"getProjectFonts"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<FontConfig>> getProjectFonts(String projectName) {
        ListResultModel<List<FontConfig>> result = new ListResultModel<List<FontConfig>>();
        try {
            Project project = getClient().getProjectByName(projectName);
            List<FontConfig> fontConfigs = project.getFonts();
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
