package net.ooder.editor.console.java.esd;

import net.ooder.annotation.Pid;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.editor.console.java.JavaSourcePackageMenu;
import net.ooder.editor.console.java.SourceDomainService;
import net.ooder.editor.console.java.SourceFileService;
import net.ooder.editor.console.java.SourcePackage;
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

@TabsAnnotation(closeBtn = true)
@TreeAnnotation(caption = "视图编辑",
        customService = EsdDomainService.class,
        size = 300, lazyLoad = true, heplBar = true)
@ToolBarMenu(menuClasses = JavaToolBar.class, dock = Dock.top, position = PositionType.top, autoIconColor = false, iconFontSize = "1em")
@LayoutAnnotation(layoutType = LayoutType.horizontal)
public class JavaEsdTree extends TreeListItem {

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
    String currentClassName;

    @Pid
    String parentId;

    @Pid
    String sourceClassName;

    @Pid
    String javaTempId;

    @ChildTreeAnnotation(imageClass = "ri-layout-line", caption = "工程信息", iconColor = IconColorEnum.DARKBLUE, bindClass = SourceDomainService.class, deepSearch = true, lazyLoad = true, dynDestory = true)
    public JavaEsdTree(String projectVersionName) {
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectVersionName, UserSpace.USER);
            this.imageClass = domainInst.getUserSpace().getImageClass();
            this.caption = domainInst.getUserSpace().getName() + "(" + domainInst.getRootPackage().getPackageName() + ")";
            this.domainId = domainInst.getDomainId();
            this.id = domainInst.getDomainId() + "|" + domainInst.getPackageName();
            this.parentId = DSMType.AGGREGATION.getType() + domainInst.getProjectVersionName();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @RightContextMenu(menuClass = JavaSourcePackageMenu.class)
    @ChildTreeAnnotation(imageClass = "ri-folder-line", caption = "用户指定源码树", iconColor = IconColorEnum.YELLOW, deepSearch = true, bindClass = JavaModuleSrcListService.class, initFold = false, lazyLoad = true, dynDestory = true)
    public JavaEsdTree(SourcePackage sourcePackage) {
        JavaPackage javaPackage = sourcePackage.getJavaPackage();
        this.caption = javaPackage.getPackageName();
        this.currentClassName = sourcePackage.getCurrentClassName();
        this.domainId = sourcePackage.getDomainId();
        this.id = domainId + "|" + javaPackage.getPackageName();
        this.filePath = javaPackage.getPackageFile().getPath();
        this.packageName = javaPackage.getPackageName();
        this.parentId = domainId;

    }


    @ChildTreeAnnotation(imageClass = "ri-code-line", caption = "java源码文件", iconColor = IconColorEnum.LIGHTPURPLE, bindClass = SourceFileService.class, closeBtn = true)
    public JavaEsdTree(JavaSrcBean javaFile) {
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
