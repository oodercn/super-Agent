package net.ooder.editor.toolbox.file.menu;

import net.ooder.annotation.*;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.util.OODUtil;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;


@Controller
@RequestMapping(value = {"/rad/file/action/"})
@Aggregation(type = AggregationType.MENU, rootClass = OODProjectMenu.class, userSpace = UserSpace.SYS)
public class OODProjectMenu extends FileContextMenu {


    @MethodChinaName(cname = "刷新文件")
    @RequestMapping(method = RequestMethod.POST, value = "reLoad")
    @CustomAnnotation(imageClass = "ri-refresh-line", index = 1, tips = "刷新文件")
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<OODFileTree>> reLoad(String id, String projectName, String parentId, String packageName, String path) {
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<List<OODFileTree>>();
        try {
            if (id == null) {
                id = "view";
            }
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ProjectVersion projectVersion = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            if (packageName != null) {
                EUPackage euPackage = projectVersion.getEUPackage(packageName);
                CtVfsFactory.getCtVfsService().clearCache(euPackage.getPath());
            } else if (path != null) {
                Folder folder = ESDFacrory.getAdminESDClient().getFolderByPath(path, packageName);
                CtVfsFactory.getCtVfsService().clearCache(folder.getPath());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        result.setIds(Arrays.asList(id));
        return result;
    }


    @Split
    @CustomAnnotation(index = 10)
    @ResponseBody
    public ResultModel<Boolean> split10() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "newClass")
    @CustomAnnotation(imageClass = "ri-add-line", index = 15, caption = "新建页面")
    @APIEventAnnotation(
            bindAction = @CustomAction(name = "newClass", script = "SPA.newClass()", params = {"args[0]", "args[1]"})
    )
    public @ResponseBody
    ResultModel<OODFileTree> newClass(String fileName, String className, String packageName, String domainId, String projectName) {
        ResultModel<OODFileTree> result = new ResultModel<OODFileTree>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ProjectVersion version = getClient().getProjectVersionByName(projectName);

            if (className != null) {
                if (className.endsWith(".cls")) {
                    className = className.substring(0, className.length() - ".cls".length());
                }
            } else {
                String simClassName = OODUtil.formatJavaName(fileName, true);
                className = packageName + "." + simClassName;
            }
            EUModule module = getClient().getModule(className, version.getVersionName(), true);
            if (module == null) {
                module = version.createModule(className);
                BlockComponent blockComponent = new BlockComponent(Dock.fill, module.getName() + ModuleComponent.DefaultTopBoxfix);
                module.getComponent().addChildren(blockComponent);
                module.getComponent().setCurrComponent(blockComponent);
                getClient().saveModule(module, false);
            } else {
                throw new JDSException("页面已存在！");
            }
            OODFileTree oodFileTree = new OODFileTree(module, projectName, domainId);
            oodFileTree.setImageClass("ri-file-code-line");
            oodFileTree.setIconColor(IconColorEnum.YELLOW);
            result.setData(oodFileTree);
        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "newFolder")
    @CustomAnnotation(imageClass = "ri-folder-line", index = 16, caption = "新建文件夹")
    @APIEventAnnotation(
            bindAction = @CustomAction(name = "newFolder", script = "SPA.newFolder()", params = {"args[0]", "args[1]"})
    )
    @ResponseBody
    ResultModel<OODFileTree> newFolder(String path, String fileName, String projectName, String domainId) {
        ResultModel<OODFileTree> result = new ResultModel<OODFileTree>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            Folder pfolder = version.createFolder(path);
            Folder folder = pfolder.createChildFolder(fileName, fileName);
            String cPath = folder.getPath();
            cPath = cPath.substring(version.getPath().length());
            String packageName = StringUtility.replace(cPath, "/", ".");
            EUPackage euPackage = version.getEUPackage(packageName);
            result.setData(new OODFileTree(euPackage, domainId, projectName));
        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());

        }
        return result;
    }


}
