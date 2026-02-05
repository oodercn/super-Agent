package net.ooder.editor.console.java.task;

import net.ooder.annotation.Pid;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.editor.console.java.SourceFileService;
import net.ooder.editor.console.java.page.JavaPageBar;
import net.ooder.editor.console.java.page.PageDomainService;
import net.ooder.editor.console.java.page.PagePackageCatItem;
import net.ooder.esd.annotation.ChildTreeAnnotation;
import net.ooder.esd.annotation.LayoutAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.TreeAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.annotation.ui.LayoutType;
import net.ooder.esd.annotation.ui.PositionType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.temp.JavaTemp;

@TabsAnnotation(closeBtn = true)
@TreeAnnotation(caption = "VIEW视图编辑",
        customService = PageDomainService.class,
        size = 300, lazyLoad = true, heplBar = true)
@ToolBarMenu(menuClasses = JavaPageBar.class, dock = Dock.top, position = PositionType.top, autoIconColor = false, iconFontSize = "1em")
@LayoutAnnotation(layoutType = LayoutType.horizontal)
public class JavaTaskTree extends TreeListItem {

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

    @ChildTreeAnnotation(imageClass = "ri-layout-line", caption = "模块代码", iconColor = IconColorEnum.DARKBLUE, bindClass = PageDomainService.class, deepSearch = true, lazyLoad = true, dynDestory = true)
    public JavaTaskTree(GenJavaTask genTask, String projectName) {
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);

            this.domainId = domainInst.getDomainId();
            this.id = domainInst.getDomainId() + "|" + domainInst.getPackageName();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    @ChildTreeAnnotation(imageClass = "ri-layout-line", caption = "模块代码", iconColor = IconColorEnum.DARKBLUE, bindClass = PageDomainService.class, deepSearch = true, lazyLoad = true, dynDestory = true)
    public JavaTaskTree(AggRootBuild build, String projectName) {
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);

            this.domainId = domainInst.getDomainId();
            this.id = domainInst.getDomainId() + "|" + domainInst.getPackageName();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    @ChildTreeAnnotation(imageClass = "ri-code-line", caption = "java源码文件", iconColor = IconColorEnum.LIGHTPURPLE, bindClass = SourceFileService.class, closeBtn = true)
    public JavaTaskTree(JavaTemp javaTemp) {
        this.caption = javaTemp.getName();
        this.javaTempId = javaTemp.getJavaTempId();
        this.parentId = domainId + "|" + packageName;

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
