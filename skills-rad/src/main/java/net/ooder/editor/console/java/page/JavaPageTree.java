package net.ooder.editor.console.java.page;

import net.ooder.annotation.Pid;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.editor.console.java.SourceFileService;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.PositionType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.engine.EUModule;

@TabsAnnotation(closeBtn = true)
@TreeAnnotation(caption = "VIEW视图编辑",
        customService = PageDomainService.class,
        size = 300, lazyLoad = true, heplBar = true)
@ToolBarMenu(menuClasses = JavaPageBar.class, dock = Dock.top, position = PositionType.top, autoIconColor = false, iconFontSize = "1em")
@LayoutAnnotation(layoutType = LayoutType.horizontal)
public class JavaPageTree extends TreeListItem {

    @Pid
    String projectVersionName;

    @Pid
    String domainId;

    @Pid
    String dsmId;

    @Pid
    String filePath;

    @Pid
    String sfilePath;

    @Pid
    String packageName;

    @Pid
    PagePackageCatItem catItem;

    @Pid
    String currentClassName;

    @Pid
    String parentId;

    @Pid
    String sourceClassName;

    @Pid
    String javaTempId;

    @ChildTreeAnnotation(imageClass = "ri-layout-line", caption = "模块代码", iconColor = IconColorEnum.DARKBLUE, bindClass = PageDomainService.class, deepSearch = true,lazyLoad = true,dynDestory = true)
    public JavaPageTree(EUModule euModule, String projectName) {
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            this.caption = euModule.getName();
            this.domainId = domainInst.getDomainId();
            this.id = domainInst.getDomainId() + "|" + domainInst.getPackageName();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @ChildTreeAnnotation(caption = "包分类", customItems = PagePackageCatItem.class, group = true, deepSearch = true, lazyLoad = true, dynDestory = true)
    public JavaPageTree(PagePackageCatItem catItem, String currentClassName, String projectName) {
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            this.currentClassName = currentClassName;
            this.imageClass = catItem.getImageClass();
            this.domainId = domainInst.getDomainId();
            this.catItem = catItem;
            this.id = catItem.name();
            this.parentId = DSMType.AGGREGATION.getType() + domainInst.getProjectVersionName();
            switch (catItem) {
                case View:
                    ViewInst viewInst = domainInst.getViewInst();
                    String name = viewInst.getPackageName().equals("") ? DSMType.VIEW.name().toLowerCase() : viewInst.getPackageName();
                    this.caption = catItem.getName() + "(" + name + ")";
                    break;
                case Repository:
                    this.caption = catItem.getName() + "(" + domainInst.getRepositoryInst().getPackageName() + ")";

                    break;
                case Aggregation:
                    this.caption = catItem.getName() + "(" + domainInst.getPackageName() + ")";
                    break;
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    @RightContextMenu(menuClass = JavaPagePackageMenu.class)
    @ChildTreeAnnotation(imageClass = "ri-folder-line", caption = "页面源码", iconColor = IconColorEnum.YELLOW, deepSearch = true, bindClass = JavaPageSrcListService.class, initFold = true, lazyLoad = true, dynDestory = true)
    public JavaPageTree(JavaPackage javaPackage, String currentClassName, String domainId, PagePackageCatItem catItem) {
        this.domainId = domainId;
        this.packageName = javaPackage.getPackageName();
        this.filePath = javaPackage.getPackageFile().getPath();
        this.parentId = catItem.name();
        this.currentClassName = currentClassName;
        this.id = domainId + "|" + packageName;

        try {
            DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(domainId);
            String basePackage = domainInst.getPackageName();
            switch (catItem) {
                case View:
                    basePackage = domainInst.getViewInst().getPackageName();
                    break;
                case Repository:
                    basePackage = domainInst.getRepositoryInst().getPackageName();
                    break;
                case Aggregation:
                    basePackage = domainInst.getPackageName();
                    break;
            }
            if (packageName.startsWith(basePackage) && !packageName.equals(basePackage)) {
                this.caption = packageName.substring(basePackage.length() + 1);
            } else {
                this.caption = packageName;
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    @ChildTreeAnnotation(imageClass = "ri-code-line", caption = "java源码文件", iconColor = IconColorEnum.LIGHTPURPLE, bindClass = SourceFileService.class, closeBtn = true)
    public JavaPageTree(JavaSrcBean javaFile) {
        this.caption = javaFile.getName();
        this.javaTempId = javaFile.getJavaTempId();
        this.packageName = javaFile.getPackageName();
        this.domainId = javaFile.getDsmId();
        this.sourceClassName = javaFile.getClassName();
        this.id = javaFile.getClassName();
        this.dsmId = javaFile.getDsmId();
        this.parentId = domainId + "|" + packageName;
        this.filePath = javaFile.getFile().getAbsolutePath();

    }

    public PagePackageCatItem getCatItem() {
        return catItem;
    }

    public void setCatItem(PagePackageCatItem catItem) {
        this.catItem = catItem;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public String getSfilePath() {
        return sfilePath;
    }

    public void setSfilePath(String sfilePath) {
        this.sfilePath = sfilePath;
    }

    public String getDsmId() {
        return dsmId;
    }

    public void setDsmId(String dsmId) {
        this.dsmId = dsmId;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }
}
