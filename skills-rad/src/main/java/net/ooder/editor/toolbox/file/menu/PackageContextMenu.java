package net.ooder.editor.toolbox.file.menu;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.Split;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.debug.bean.ConfigModuleTree;
import net.ooder.editor.toolbox.file.UPLoadOODFile;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.tools.OODFile;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Controller
@RequestMapping(value = "/rad/file/package/context/")
@Aggregation(type = AggregationType.MENU, rootClass = PackageContextMenu.class)
@MenuBarMenu(menuType = CustomMenuType.CONTEXTMENU, caption = "包结构右键菜单")
public class PackageContextMenu extends OODProjectMenu {


    @RequestMapping(method = RequestMethod.POST, value = "copyFolder")
    @CustomAnnotation(imageClass = "ri-file-copy-line", index = 3, caption = "复制文件夹")
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW)
    public @ResponseBody
    ResultModel<Boolean> copyFolder(String path, String packageName) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        result.addCtx("sfilePath", path);
        result.addCtx("packageName", packageName);
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "paste")
    @CustomAnnotation(imageClass = "ri-clipboard-line", index = 4, caption = "粘贴文件夹")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.TREEVIEW, RequestPathEnum.STAGVAR, RequestPathEnum.CTX}, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<ConfigModuleTree>> paste(String parentId, String sfilePath, String path, String domainId, String projectName) {
        TreeListResultModel<List<ConfigModuleTree>> result = new TreeListResultModel<List<ConfigModuleTree>>();
        try {

            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            FileInfo fileInfo = ESDFacrory.getAdminESDClient().getFileByPath(sfilePath, projectName);
            if (fileInfo != null) {
                Folder folder = ESDFacrory.getAdminESDClient().getFolderByPath(path, projectName);
                CtVfsFactory.getCtVfsService().copyFile(fileInfo, folder);
            } else {
                CtVfsFactory.getCtVfsService().copyFolder(sfilePath, path);
            }
            result.setIds(Arrays.asList(new String[]{parentId}));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @MethodChinaName(cname = "添加普通文件")
    @RequestMapping(value = {"newTextFile"}, method = {RequestMethod.POST})
    @CustomAnnotation(imageClass = "ri-folder-line", index = 11, caption = "添加普通文件")
    @APIEventAnnotation(
            bindAction = @CustomAction(name = "newTextFile", script = "SPA.newTextFile()", params = {"args[0]", "args[1]"})
    )
    @ResponseBody
    public ResultModel<OODFile> newTextFile(String parentPath, String fileName, String fullPath, String projectName) {
        ResultModel<OODFile> result = new ResultModel<OODFile>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            Folder folder = version.createFolder(parentPath);
            FileInfo fileInfo = folder.createFile(fileName, null);
            fileInfo = getClient().saveFile(new StringBuffer("{}"), fileInfo.getPath(), projectName);
            result.setData(new OODFile(fileInfo, version));
        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }

    @Split
    @CustomAnnotation(index = 7)
    @ResponseBody
    public ResultModel<Boolean> split7() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }


    @MethodChinaName(cname = "编译")
    @RequestMapping(method = RequestMethod.POST, value = "dsmBuild")
    @CustomAnnotation(imageClass = "ri-refresh-line", index = 8)
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    public @ResponseBody
    TreeListResultModel<List<ConfigModuleTree>> dsmBuild(String id, String projectName, String domainId, String packageName, String parentId) {
        TreeListResultModel<List<ConfigModuleTree>> result = new TreeListResultModel<List<ConfigModuleTree>>();
        try {
            Map<String, ?> valueMap = JDSActionContext.getActionContext().getContext();
            result.setIds(Arrays.asList(new String[]{parentId}));
            ESDFacrory.getAdminESDClient().buildPackage(projectName, packageName, valueMap, this.getCurrChromeDriver());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = {"buildView"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.CTX, RequestPathEnum.TREEVIEW}, callback = CustomCallBack.TREERELOADNODE)
    @CustomAnnotation(index = 9, caption = "同步视图", imageClass = "ri-exchange-line")
    @ResponseBody
    public ResultModel<Boolean> buildView(String domainId, String packageName, String projectName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            Map<String, Object> contextMap = new HashMap<>();
            Map<String, ?> valueMap = JDSActionContext.getActionContext().getContext();
            contextMap.putAll(valueMap);
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            Set<EUModule> modules = ESDFacrory.getAdminESDClient().buildPackage(projectName, packageName, valueMap, this.getCurrChromeDriver());
            for (EUModule module : modules) {
                List<ModuleComponent> moduleComponents = module.getComponent().findComponents(ComponentType.MODULE, null);
                for (ModuleComponent moduleComponent : moduleComponents) {
                    if (moduleComponent.getEuModule() != null) {
                        ESDFacrory.getAdminESDClient().delModule(moduleComponent.getEuModule());
                    }
                }
                if (module.getComponent().getMethodAPIBean() != null) {
                    CustomViewFactory.getInstance().buildView(module.getComponent().getMethodAPIBean(), projectName, contextMap, false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    @RequestMapping(value = "UploadFile")
    @APIEventAnnotation(autoRun = true)
    @DialogAnnotation(width = "450", height = "380", caption = "导入文件")
    @ModuleAnnotation
    @FormViewAnnotation
    @CustomAnnotation(caption = "导入文件", index = 31, imageClass = "ri-file-line", tips = "上传")
    public @ResponseBody
    ResultModel<UPLoadOODFile> uploadFile(String domainId, String packageName) {
        ResultModel<UPLoadOODFile> resultModel = new ResultModel<UPLoadOODFile>();
        try {
            UPLoadOODFile upLoadFile = new UPLoadOODFile(domainId, packageName);
            resultModel.setData(upLoadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }


}
