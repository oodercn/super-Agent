package net.ooder.editor.toolbox.file;

import net.ooder.annotation.Pid;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.editor.console.ddd.esdclass.DDDModuleViewService;
import net.ooder.editor.console.ddd.esdclass.DDDViewInfoService;
import net.ooder.editor.console.ddd.esdclass.EsdClassContextMenu;
import net.ooder.editor.toolbox.file.apiconfig.APIConfigTreeService;
import net.ooder.editor.toolbox.file.apiconfig.ApiConfigContextMenu;
import net.ooder.editor.toolbox.file.menu.*;
import net.ooder.editor.toolbox.file.service.*;
import net.ooder.esd.annotation.ChildTreeAnnotation;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.FileImgCssType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.web.APIConfig;

@TabsAnnotation(singleOpen = true)
@ToolBarMenu(autoIconColor = false, menuClasses = ProjectToolBar.class, showCaption = false, iconFontSize = "1.2em", hAlign = HAlignType.right)
@TreeAnnotation(caption = "工程树",
        size = 300, lazyLoad = true, heplBar = true)
public class OODFileTree extends TreeListItem {

    @Pid
    String projectName;

    @Pid
    String packageName;

    @Pid
    String sourceClassName;

    @Pid
    String domainId;

    @Pid
    String parentId;

    @Pid
    String path;

    @Pid
    String sourceMethodName;
    @Pid
    String currentClassName;
    @Pid
    String xpath;

    @Pid
    String childTabId;

    @RightContextMenu(menuClass = PageContextMenu.class)
    @ChildTreeAnnotation(bindClass = OODPageService.class, caption = "普通", imageClass = "ri-file-code-line", iconColor = IconColorEnum.YELLOW, initFold = true)
    public OODFileTree(EUModule module, String projectName, String domainId) {
        this.name = module.getName() + ".cls";
        this.path = module.getPath();
        this.projectName = projectName;

        String curProjectPath = module.getProjectVersion().getPath();
        if (curProjectPath != null && !curProjectPath.equals("") && path.startsWith(curProjectPath)) {
            path = path.substring(curProjectPath.length());
        }
        if (module.getComponent().getMethodAPIBean() != null) {
            MethodConfig methodConfig = module.getComponent().getMethodAPIBean();
            this.sourceMethodName = methodConfig.getMethodName();
            this.sourceClassName = methodConfig.getSourceClassName();

        }
        this.className = module.getClassName();
        this.currentClassName = className;
        this.id = module.getPath();
        this.caption = module.getName();
        String title = module.getComponent().getTitle();
        if (title != null && !title.equals("")) {
            this.caption = caption + "(" + title + ")";
        }
        try {
            if (domainId == null || domainId.equals("")) {
                domainId = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER).getDomainId();
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.domainId = domainId;
    }

    @RightContextMenu(menuClass = PackageContextMenu.class)
    @ChildTreeAnnotation(bindClass = OODPackageService.class, caption = "Package源码包", imageClass = "ri-folder-line", iconColor = IconColorEnum.DARKBLUE, lazyLoad = true, dynDestory = true, initFold = true)
    public OODFileTree(EUPackage euPackage, String domainId, String projectName) {
        this.projectName = projectName;
        try {
            if (domainId == null || domainId.equals("")) {
                domainId = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER).getDomainId();
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.domainId = domainId;
        this.id = euPackage.getPackageName();
        this.name = euPackage.getName();
        this.path = euPackage.getPath();
        this.className = euPackage.getPackageName();
        this.packageName = euPackage.getPackageName();
        this.imageClass = euPackage.getImageClass();
        this.caption = euPackage.getName();

        if (euPackage.getPackagePathType() != null) {
            this.iconColor = euPackage.getPackagePathType().getApiType().getIconColorEnum();
        }

        String curProjectPath = euPackage.getProjectVersion().getPath();
        if (curProjectPath != null && !curProjectPath.equals("") && path.startsWith(curProjectPath)) {
            path = path.substring(curProjectPath.length());
        }

        if (euPackage.getParent() != null) {
            this.parentId = euPackage.getParent().getId();
        } else {
            this.parentId = projectName;
        }


    }

    @RightContextMenu(menuClass = DomainContextMenu.class)
    @ChildTreeAnnotation(bindClass = OODDominService.class, caption = "领域模型", iconColor = IconColorEnum.DARKBLUE, lazyLoad = true, dynDestory = true, initFold = true, group = true)
    public OODFileTree(DomainInst domainInst, String projectName) {
        UserSpace userSpace = domainInst.getUserSpace();
        this.domainId = domainInst.getDomainId();
        String euPath = domainInst.getEuPackage().replace(".", "/") + "/";
        this.name = userSpace.getName() + "(" + userSpace.name() + ")";
        this.projectName = projectName;
        this.parentId = projectName;
        this.imageClass = userSpace.getImageClass();
        this.caption = name;
        this.path = euPath;
        this.packageName = domainInst.getSpace();
        this.group = true;
        this.id = domainInst.getSpace();
        if (domainInst.getUserSpace().equals(UserSpace.VIEW)) {
            this.initFold = false;
        } else {
            this.initFold = true;
        }
    }

    @RightContextMenu(menuClass = PackageContextMenu.class)
    @ChildTreeAnnotation(bindClass = OODFolderService.class, caption = "文件夹节点", iconColor = IconColorEnum.DARKBLUE, imageClass = "ri-folder-line", lazyLoad = true, initFold = true)
    public OODFileTree(Folder folder, String domainId, String projectName) {
        this.path = folder.getPath();
        this.id = folder.getID();
        this.parentId = folder.getParentId();
        this.caption = folder.getDescription() == null ? folder.getName() : folder.getDescription();
        this.tips = caption;
        this.domainId = domainId;
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            String subpath = StringUtility.replace(folder.getPath(), version.getPath(), "/");
            APIPaths apiPaths = APIFactory.getInstance().getAPIPaths(subpath);
            if (apiPaths != null) {
                for (APIConfig config : apiPaths.getApiConfigs()) {
                    if (config.getChinaName() != null) {
                        this.name = folder.getName() + "(" + config.getDesc() + ")";
                        this.imageClass = apiPaths.getImageClass();
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @RightContextMenu(menuClass = FileContextMenu.class)
    @ChildTreeAnnotation(bindClass = OODFileService.class, caption = "普通文件", iconColor = IconColorEnum.BABYBLUE, imageClass = "ri-javascript-line", lazyLoad = true, initFold = true)
    public OODFileTree(FileInfo fileInfo, String projectName) {
        path = fileInfo.getPath();
        parentId = fileInfo.getFolderId();
        this.caption = fileInfo.getDescription() == null ? fileInfo.getName() : fileInfo.getDescription();
        name = fileInfo.getName();
        this.id = path;
        String fileType = name.substring(name.indexOf(".") + 1).toLowerCase();
        this.imageClass = FileImgCssType.fromType(fileType).getImageClass();
        this.tips = caption;
    }

    @RightContextMenu(menuClass = EsdClassContextMenu.class)
    @ChildTreeAnnotation(bindClass = {DDDModuleViewService.class}, caption = "源码解析（ESDClass）", iconColor = IconColorEnum.GREEN, imageClass = "ri-file-text-line", dynDestory = true, initFold = true, lazyLoad = true)
    public OODFileTree(ESDClass esdClass, String packageName, String domainId, String projectName) {
        this.id = esdClass.getClassName();
        this.parentId = packageName;
        this.projectName = projectName;
        this.caption = esdClass.getDesc() + "(" + esdClass.getCtClass().getSimpleName() + ")";
        this.sourceClassName = esdClass.getClassName();
        try {
            if (domainId == null || domainId.equals("")) {
                domainId = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER).getDomainId();
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.domainId = domainId;

    }

    @RightContextMenu(menuClass = ApiConfigContextMenu.class)
    @ChildTreeAnnotation(bindClass = {APIConfigTreeService.class}, caption = "源码解析（APIConfig）", iconColor = IconColorEnum.PINK, imageClass = "ri-file-code-line", dynDestory = true, initFold = true, lazyLoad = true)
    public OODFileTree(APIConfig apiConfig, String packageName, String domainId, String projectName) {
        this.id = apiConfig.getClassName();
        this.parentId = packageName;
        this.projectName = projectName;

        this.caption = apiConfig.getDesc() + "(" + apiConfig.getName() + ")";
        this.imageClass = apiConfig.getImageClass() == null ? "ri-file-code-line" : apiConfig.getImageClass();
        this.sourceClassName = apiConfig.getClassName();
        try {
            if (domainId == null || domainId.equals("")) {
                domainId = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.USER).getDomainId();
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.domainId = domainId;

    }


    @ChildTreeAnnotation(bindClass = DDDViewInfoService.class, caption = "通用视图导航")
    public OODFileTree(CustomViewBean viewBean, String domainId, String sourceClassName, String childTabId, String xpath, String currentClassName) {
        this.imageClass = viewBean.getImageClass();
        this.sourceClassName = sourceClassName;
        this.caption = viewBean.getName();
        this.xpath = xpath;
        this.childTabId = childTabId;
        this.currentClassName = currentClassName;
        this.sourceMethodName = viewBean.getSourceMethodName();
        this.id = sourceClassName + "_" + viewBean.getMethodConfig().getEUClassName();
        this.domainId = domainId;
    }


    @RightContextMenu(menuClass = ModuleContextMenu.class)
    @ChildTreeAnnotation(bindClass = OODModuleService.class, caption = "用户自定义模块", imageClass = "ri-file-code-line", iconColor = IconColorEnum.YELLOW, initFold = true)
    public OODFileTree(MethodConfig methodConfig, String domainId, String projectName) {
        try {
            EUModule module = methodConfig.getModule(JDSActionContext.getActionContext().getContext(), projectName);
            this.name = module.getName() + ".cls";
            this.path = module.getPath();
            this.projectName = projectName;
            String curProjectPath = module.getProjectVersion().getPath();
            if (curProjectPath != null && !curProjectPath.equals("") && path.startsWith(curProjectPath)) {
                path = path.substring(curProjectPath.length());
            }

            this.className = module.getClassName();
            this.currentClassName = className;
            this.id = module.getPath();
            this.caption = module.getName();
            String title = module.getComponent().getTitle();
            if (title != null && !title.equals("")) {
                this.caption = caption + "(" + title + ")";
            }
            this.domainId = domainId;
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getChildTabId() {
        return childTabId;
    }

    public void setChildTabId(String childTabId) {
        this.childTabId = childTabId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
