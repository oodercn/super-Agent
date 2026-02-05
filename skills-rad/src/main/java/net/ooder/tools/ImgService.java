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
import net.ooder.esd.manager.plugins.img.ImgFactory;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.manager.plugins.img.node.ImgNode;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = {"/admin/plugs/img/"})
@MethodChinaName(cname = "图片服务")
public class ImgService {

    @MethodChinaName(cname = "图片类配置")
    @RequestMapping(value = {"getSelImg"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<ImgConfig>> getSelImg(String projectName) {
        ListResultModel<List<ImgConfig>> result = new ListResultModel<List<ImgConfig>>();
        List<ImgConfig> imgConfigs = new ArrayList<ImgConfig>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            imgConfigs = projectVersion.getProject().getImgs();
            result.setData(imgConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<ImgConfig>> errorResult = new ErrorListResultModel<List<ImgConfig>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "添加图片")
    @RequestMapping(value = {"addImg"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> addImg(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            String[] ids = StringUtility.split(id, ";");
            ProjectConfig config = version.getProject().getConfig();

            List<String> imgIds = config.getImgs();
            for (String imgId : ids) {
                if (!imgIds.contains(imgId)) {
                    ImgConfig imgConfig = ImgFactory.getInstance(getClient().getSpace()).getImgConfigById(projectName, imgId);
                    Folder folder = CtVfsFactory.getCtVfsService().getFolderById(imgId);
                    if (folder != null) {
                        Folder imgFolder = (Folder) getClient().copy(projectName, folder.getPath(), "img/" + folder.getName());
                        imgIds.add(imgFolder.getID());
                    }
                }
            }
            config.setImgs(imgIds);
            version.updateConfig(config);
        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }


    @MethodChinaName(cname = "图片定义")
    @RequestMapping(value = {"getImgTreeProject"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<ImgNode>> getImgTreeProject() {
        ListResultModel<List<ImgNode>> result = new ListResultModel<List<ImgNode>>();
        List<ImgNode> fontNodes = new ArrayList<ImgNode>();
        try {
            List<Project> projects = getClient().getResourceAllProject(ProjectResourceType.img);
            for (Project project : projects) {
                fontNodes.add(new ImgNode(project));
            }
            result.setData(fontNodes);

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }


    @MethodChinaName(cname = "字体类配置")
    @RequestMapping(value = {"delImg"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<Boolean> delFont(String projectName, String id) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        List<String> fontConfigIds = new ArrayList<String>();
        try {
            Project project = getClient().getProjectByName(projectName);
            fontConfigIds = project.getConfig().getImgs();
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
    @RequestMapping(value = {"getProjectImgs"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<ImgConfig>> getProjectImgs(String projectName) {
        ListResultModel<List<ImgConfig>> result = new ListResultModel<List<ImgConfig>>();
        try {
            Project project = getClient().getProjectByName(projectName);
            List<ImgConfig> fontConfigs = project.getImgs();
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
