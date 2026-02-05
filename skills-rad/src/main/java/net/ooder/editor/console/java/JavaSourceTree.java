package net.ooder.editor.console.java;

import net.ooder.annotation.Pid;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.editor.console.java.page.JavaPagePackageMenu;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.ViewInst;

@TabsAnnotation(closeBtn = true)
@TreeAnnotation(caption = "视图编辑",
        customService = SourceDomainService.class,
        size = 300, lazyLoad = true, heplBar = true)
@LayoutAnnotation(layoutType = LayoutType.horizontal)
public class JavaSourceTree extends TreeListItem {

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
    PackageCatItem catItem;


    @Pid
    String packageName;

    @Pid
    String currentClassName;

    @Pid
    String parentId;

    @Pid
    String sourceClassName;

    @Pid
    String javaTempId;

    @ChildTreeAnnotation(imageClass = "ri-layout-line", caption = "工程信息", iconColor = IconColorEnum.DARKBLUE, bindClass = SourceDomainService.class, deepSearch = true, dynDestory = true)
    public JavaSourceTree(String projectVersionName) {
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectVersionName, UserSpace.VIEW);
            this.imageClass = domainInst.getUserSpace().getImageClass();
            this.caption = domainInst.getUserSpace().getName() + "(" + domainInst.getRootPackage().getPackageName() + ")";
            this.domainId = domainInst.getDomainId();
            this.id = domainInst.getDomainId() + "|" + domainInst.getPackageName();

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @RightContextMenu(menuClass = JavaSourcePackageMenu.class)
    @ChildTreeAnnotation(caption = "包分类", customItems = PackageCatItem.class, group = true, deepSearch = true, lazyLoad = true, dynDestory = true)
    public JavaSourceTree(PackageCatItem catItem, String currentClassName, String projectName) {
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
            this.parentId = domainInst.getDomainId() + "|" + domainInst.getPackageName();
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
    @ChildTreeAnnotation(imageClass = "ri-folder-line", caption = "页面源码", iconColor = IconColorEnum.YELLOW, deepSearch = true, bindClass = JavaSrcFileService.class, initFold = true, lazyLoad = true, dynDestory = true)
    public JavaSourceTree(JavaPackage javaPackage, String projectName, PackageCatItem catItem) {
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            this.catItem = catItem;
            String packageName = javaPackage.getPackageName();
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            switch (catItem) {
                case View:
                    this.caption = javaPackage.getPackageName();
                    break;
                case Repository:
                    String basePackage = domainInst.getRepositoryInst().getPackageName();
                    if (packageName.startsWith(basePackage)) {
                        this.caption = packageName.substring(basePackage.length() + 1);
                    }
                    break;
                case Aggregation:
                    domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
                    this.caption = javaPackage.getPackageName() + "(" + catItem.getName() + ")";
                    basePackage = domainInst.getPackageName();
                    if (packageName.startsWith(basePackage) && !packageName.equals(basePackage)) {
                        this.caption = packageName.substring(basePackage.length() + 1);
                    }
                    break;
                case User:
                    domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
                    this.caption = javaPackage.getPackageName() + "(" + catItem.getName() + ")";
                    basePackage = domainInst.getPackageName();
                    if (packageName.startsWith(basePackage) && !packageName.equals(basePackage)) {
                        this.caption = packageName.substring(basePackage.length() + 1);
                    }
                    break;

            }
            this.domainId = domainInst.getDomainId();
            this.id = domainId + "|" + javaPackage.getPackageName();
            this.filePath = javaPackage.getPackageFile().getPath();
            this.packageName = javaPackage.getPackageName();
            this.parentId = catItem.name();
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }


    @ChildTreeAnnotation(imageClass = "ri-code-line", caption = "java源码文件", iconColor = IconColorEnum.LIGHTPURPLE, bindClass = SourceFileService.class, closeBtn = true)
    public JavaSourceTree(JavaSrcBean javaFile) {
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

    public PackageCatItem getCatItem() {
        return catItem;
    }

    public void setCatItem(PackageCatItem catItem) {
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
