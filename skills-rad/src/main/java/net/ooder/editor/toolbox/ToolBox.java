package net.ooder.editor.toolbox;

import net.ooder.annotation.Pid;
import net.ooder.editor.toolbox.file.service.OODProjectService;
import net.ooder.esd.annotation.BlockAnnotation;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.TabItemAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.BarLocationType;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.SideBarStatusType;
import net.ooder.esd.custom.properties.NavTabListItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rad/toolbox/")
@BlockAnnotation(borderType = BorderType.none, dock = Dock.fill)
@ButtonViewsAnnotation(barLocation = BarLocationType.left, autoIconColor = false,
        bindService = ToolBoxService.class, barSize = "2.5em",
        sideBarStatus = SideBarStatusType.expand)
@TabsAnnotation(lazyAppend = false)
public class ToolBox extends NavTabListItem {

    @Pid
    String projectName;
    @Pid
    ToolBoxEnum toolBox;

    public ToolBox() {

    }

    @TabItemAnnotation(customItems = ToolBoxEnum.class)
    public ToolBox(ToolBoxEnum toolBox, String projectName) {
        this.projectName = projectName;
        this.toolBox = toolBox;
        this.bindClass = toolBox.getBindClass();
        this.tips = toolBox.getName();
        this.id = toolBox.getType();
        this.caption = "";
        this.imageClass = toolBox.getImageClass();
    }


    public ToolBoxEnum getToolBox() {
        return toolBox;
    }

    public void setToolBox(ToolBoxEnum toolBox) {
        this.toolBox = toolBox;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * 组件配置枚举类
     * 用于统一管理RAD组件配置
     */
    public enum ToolBoxEnum implements TabItem {


        PROJECT("工程列表", "ri-folder-line", OODProjectService.class, true, true, true),
        LAYOUT_CONTAINER("布局容器", "ri-layout-column-line", WidgetService.class, false, true, true, WidgetEnums.CONTAINER, WidgetEnums.BUTTON_VIEWS, WidgetEnums.TOOLBAR),
        FORM("表单", "ri-file-line", WidgetService.class, false, true, true, WidgetEnums.BASEFORM, WidgetEnums.ADVFORM),
        AGGREGATE_APP("高级组件", "ri-cloud-line", WidgetService.class, false, true, true, WidgetEnums.COMPONENTS, WidgetEnums.STYLE_COMPONENTS, WidgetEnums.API_COMPONENTS),
        SVG("图形库", "ri-artboard-line", WidgetService.class, true, true, true, WidgetEnums.SVGPAPER, WidgetEnums.SVGBASE, WidgetEnums.SVGCOMB),
        MOBILE("移动组件", "ri-smartphone-line", WidgetService.class, false, true, true, WidgetEnums.MBASE, WidgetEnums.MLAYOUT, WidgetEnums.MNAV, WidgetEnums.MFDBACK);
        private final String name;
        private final String caption;
        private final String tips;
        private final String imageClass;
        private final Class bindClass;
        private final boolean initFold;
        private final boolean dynLoad;
        private final boolean dynDestory;
        WidgetEnums[] widgetEnums;

        ToolBoxEnum(String name, String imageClass, Class bindClass, boolean initFold, boolean dynLoad, boolean dynDestory, WidgetEnums... widgetEnums) {
            this.name = name;
            this.tips = name;
            this.caption = name;
            this.dynLoad = dynLoad;
            this.imageClass = imageClass;
            this.bindClass = bindClass;
            this.initFold = initFold;
            this.dynDestory = dynDestory;
            this.widgetEnums = widgetEnums;

        }

        public WidgetEnums[] getWidgetEnums() {
            return widgetEnums;
        }

        public void setWidgetEnums(WidgetEnums[] widgetEnums) {
            this.widgetEnums = widgetEnums;
        }


        @Override
        public String getCaption() {
            return caption;
        }

        @Override
        public boolean isDynLoad() {
            return dynLoad;
        }

        @Override
        public String getTips() {
            return tips;
        }

        @Override
        public boolean isInitFold() {
            return initFold;
        }

        @Override
        public boolean isDynDestory() {
            return dynDestory;
        }


        public Class[] getBindClass() {
            return new Class[]{bindClass};
        }

        @Override
        public String toString() {
            return name();
        }

        @Override
        public String getType() {
            return name();
        }

        @Override
        public String getName() {
            return name;
        }

        public String getImageClass() {
            return imageClass;
        }
    }
}
