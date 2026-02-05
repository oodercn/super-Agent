package net.ooder.tools;


import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.event.ToolBarEventEnum;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.custom.component.CustomToolsBar;
import net.ooder.esd.custom.component.RADTopToolsBar;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Event;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
@RequestMapping(value = {"/action/spatoolbar/"})
@MethodChinaName(cname = "RAD菜单")
public class TopMenuAction {


    @ResponseBody
    @RequestMapping(value = {"getComponentBar"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ResultModel<RADTopToolsBar> getComponentBar(CustomMenuType menuType, String pattern, String domainId, String projectName) {
        ResultModel<RADTopToolsBar> resultModel = new ResultModel<RADTopToolsBar>();
        RADTopToolsBar toolsBar = new RADTopToolsBar("ComponentBar");
        try {
            List<Component> viewBars = PluginsFactory.getInstance().getAllViewBar(menuType, domainId);
            for (Component bar : viewBars) {
                if (bar instanceof CustomToolsBar) {
                    CustomToolsBar childBar = PluginsFactory.getInstance().getViewBarById(((CustomToolsBar) bar).getId(), null, true);
                    Set<Map.Entry<ToolBarEventEnum, Event>> entrySet = childBar.getEvents().entrySet();
                    for (Map.Entry<ToolBarEventEnum, Event> entry : entrySet) {
                        List<Action> actions = entry.getValue().getActions();
                        for (Action action : actions) {
                            toolsBar.addAction(action);
                        }
                    }
                    List<TreeListItem> items = childBar.getProperties().getItems();
                    for (TreeListItem item : items) {
                        toolsBar.getProperties().addItem(item);
                    }
                    List<APICallerComponent> apiCallerComponents = childBar.getApis();
                    for (APICallerComponent api : apiCallerComponents) {
                        toolsBar.getApis().add(api);
                    }
                }
            }
            resultModel.setData(toolsBar);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }


    @ResponseBody
    @RequestMapping(value = {"getPluginBar"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ResultModel<RADTopToolsBar> getPluginBar(CustomMenuType menuType, String pattern, String domainId, String projectName) {
        ResultModel<RADTopToolsBar> resultModel = new ResultModel<RADTopToolsBar>();
        RADTopToolsBar toolsBar = new RADTopToolsBar("TopBar");
        try {
            List<Component> viewBars = PluginsFactory.getInstance().getAllViewBar(menuType, domainId);
            for (Component bar : viewBars) {
                if (bar instanceof CustomToolsBar) {
                    CustomToolsBar childBar = PluginsFactory.getInstance().getViewBarById(((CustomToolsBar) bar).getId(), null, true);
                    Set<Map.Entry<ToolBarEventEnum, Event>> entrySet = childBar.getEvents().entrySet();
                    for (Map.Entry<ToolBarEventEnum, Event> entry : entrySet) {
                        List<Action> actions = entry.getValue().getActions();
                        for (Action action : actions) {
                            toolsBar.addAction(action);
                        }
                    }
                    List<TreeListItem> items = childBar.getProperties().getItems();
                    for (TreeListItem item : items) {
                        toolsBar.getProperties().addItem(item);
                    }
                    List<APICallerComponent> apiCallerComponents = childBar.getApis();
                    for (APICallerComponent api : apiCallerComponents) {
                        toolsBar.getApis().add(api);
                    }
                }
            }
            resultModel.setData(toolsBar);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }


}
