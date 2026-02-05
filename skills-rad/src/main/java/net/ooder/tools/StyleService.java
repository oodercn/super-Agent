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
import net.ooder.esd.manager.plugins.style.node.StyleConfig;
import net.ooder.esd.manager.plugins.style.node.StyleNode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/admin/plugs/style/"})
@MethodChinaName(cname = "样式服务")
public class StyleService {

    @MethodChinaName(cname = "样式配置")
    @RequestMapping(value = {"getSelStyle"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<StyleConfig>> getSelStyle(String projectName) {
        ListResultModel<List<StyleConfig>> result = new ListResultModel<List<StyleConfig>>();
        List<StyleConfig> styleConfigs = new ArrayList<StyleConfig>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            styleConfigs = projectVersion.getProject().getStyles();
            result.setData(styleConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<StyleConfig>> errorResult = new ErrorListResultModel<List<StyleConfig>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "添加样式")
    @RequestMapping(value = {"addStyle"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> addStyle(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            String[] ids = StringUtility.split(id, ";");

            ProjectConfig config = version.getProject().getConfig();
            List<String> styleIds = config.getStyles();
            for (String apiId : ids) {
                if (!styleIds.contains(apiId)) {
                    styleIds.add(apiId);
                }
            }

            config.setImgs(styleIds);
            version.updateConfig(config);

        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }


    @MethodChinaName(cname = "获取样式定义树")
    @RequestMapping(value = {"getStyleTreeProject"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<StyleNode>> getStyleTreeProject() {
        ListResultModel<List<StyleNode>> result = new ListResultModel<List<StyleNode>>();
        List<StyleNode> styleNodes = new ArrayList<StyleNode>();
        try {
            List<Project> projects = getClient().getResourceAllProject(ProjectResourceType.css);
            for (Project project : projects) {

                styleNodes.add(new StyleNode(project));
            }
            result.setData(styleNodes);

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }


    @MethodChinaName(cname = "删除样式")
    @RequestMapping(value = {"delStyle"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> delStyle(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<String> styleConfigIds = new ArrayList<String>();
        try {
            Project project = getClient().getProjectByName(projectName);
            styleConfigIds = project.getConfig().getStyles();
            styleConfigIds.remove(id);
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


    @MethodChinaName(cname = "获取工程样式")
    @RequestMapping(value = {"getProjectStyles"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<StyleConfig>> getProjectImgs(String projectName) {
        ListResultModel<List<StyleConfig>> result = new ListResultModel<List<StyleConfig>>();

        try {
            Project project = getClient().getProjectByName(projectName);
            List<StyleConfig> fontConfigs = project.getStyles();
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
