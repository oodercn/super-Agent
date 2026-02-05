package net.ooder.editor.toolbox.file.menu;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.Split;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.*;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = "/rad/file/custom/context/")
@Aggregation(type = AggregationType.MENU, rootClass = FileContextMenu.class)
public class FileContextMenu {


    @Split
    @CustomAnnotation(index = 20)
    @ResponseBody
    public ResultModel<Boolean> split20() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @MethodChinaName(cname = "重命名")
    @RequestMapping(method = RequestMethod.POST, value = "rename")
    @CustomAnnotation(imageClass = "ri-edit-line", index = 25, tips = "重命名")
    @APIEventAnnotation(
            bindAction = @CustomAction(script = "SPA.reName()", params = "{args[0]}")
    )
    public @ResponseBody
    ResultModel<OODFileTree> reName(String newName, String path, String projectName, String domainId) {
        ResultModel<OODFileTree> result = new ResultModel<OODFileTree>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            Object obj = getClient().reName(projectName, path, newName);
            if (obj instanceof EUModule) {
                result.setData(new OODFileTree((EUModule) obj, projectName, domainId));
            } else if (obj instanceof FileInfo) {
                result.setData(new OODFileTree((FileInfo) obj, projectName));
            } else if (obj instanceof Folder) {
                result.setData(new OODFileTree((Folder) obj, domainId, projectName));
            }

        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }


    @MethodChinaName(cname = "删除文档或目录")
    @RequestMapping(method = RequestMethod.POST, value = "delete")
    @CustomAnnotation(imageClass = "ri-subtract-line", index = 26)
    @APIEventAnnotation(beforeInvoke = CustomBeforInvoke.WARN, customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<OODFileTree>> delete(String parentId, String path, String projectName) {
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<List<OODFileTree>>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            Folder pfolder = null;
            ProjectVersion version = getClient().getProjectVersionByName(projectName);

            if (parentId == null) {

                Folder folder = getClient().getFolderByPath(path, projectName);
                if (folder == null) {
                    FileInfo fileInfo = getClient().getFileByPath(path, projectName);
                    pfolder = fileInfo.getFolder();
                } else {
                    pfolder = folder.getParent();
                }
                EUPackage euPackage = ESDFacrory.getAdminESDClient().getPackageByPath(projectName, pfolder.getPath());
                parentId = euPackage.getPackageName();
            }

            version.delFile(Arrays.asList(path));
            result.setIds(Arrays.asList(parentId));
        } catch (JDSException e) {
            result = new ErrorListResultModel();
            ((ErrorListResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorListResultModel) result).setErrdes(e.getMessage());
        }
        return result;

    }


    @JSONField(serialize = false)
    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }

    @JSONField(serialize = false)
    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }
}
