package net.ooder.tools;

import net.ooder.annotation.JsonData;
import net.ooder.annotation.MethodChinaName;
import net.ooder.config.ListResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.ui.ResponsePathEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.tools.component.SPAClassTree;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping(path = "/demo/component/")
@MethodChinaName(cname = "实体管理", imageClass = "ri-image-line")
public class SPACompoentService {

    @CustomAnnotation(pid = true, hidden = true)
    String dsmId;
    @CustomAnnotation(pid = true, hidden = true)
    String projectId;


    @RequestMapping(method = RequestMethod.POST, value = "loadChild")
    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD, customRequestData = {RequestPathEnum.SPA_CLASSNAME})
    @ResponseBody
    public TreeListResultModel<List<SPAClassTree>> loadChild(String projectName, String id, String dsmId, @JsonData Map<String, String> tagVar) {
        TreeListResultModel<List<SPAClassTree>> result = new TreeListResultModel<>();
        List<SPAClassTree> menuTrees = new ArrayList<>();
        try {
            String serviceName = tagVar.get("serviceName");
            ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(serviceName,  true);
            List<CustomMethodInfo> esdMethods = new ArrayList<>();
            esdMethods.addAll(esdClass.getMethodsList());
            esdMethods.addAll(esdClass.getOtherMethodsList());
            Collections.sort(esdMethods);
            APIConfig config = APIConfigFactory.getInstance().getAPIConfig(esdClass.getClassName());
            for (CustomMethodInfo field : esdMethods) {
                RequestMethodBean methodBean = config.getMethodByName(field.getInnerMethod().getName());
                projectName = JDSActionContext.getActionContext().getParams("projectName").toString();
                if (methodBean != null) {
                    APICallerComponent component = new APICallerComponent(methodBean);
                    APICallerProperties properties = component.getProperties();
                    properties.setExpression(field.getExpression());
                    properties.setImageClass(field.getImageClass());
                    Set<CustomMenuItem> bindMenus = properties.getBindMenu();
                    ComponentType[] bindTypes = new ComponentType[]{};
                    MethodConfig methodAPIBean = CustomViewFactory.getInstance().getMethodAPIBean(methodBean.getUrl(), projectName);
                    EUModule module = CustomViewFactory.getInstance().getViewByMethod(methodAPIBean, projectName, new HashMap<>());
                    if (bindMenus != null && bindMenus.size() > 0) {
                        for (CustomMenuItem bindMenu : bindMenus) {
                            // if (checkComponentType(bindTypes, bindMenu.getBindTypes())) {
                            if (bindMenu.getMenu() != null && bindMenu.getMenu().actions().length > 0 && module != null) {
                                menuTrees.add(new SPAClassTree(module, methodBean, bindMenu, dsmId));
                            }

                            //  }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(menuTrees);

        return result;
    }

    public boolean checkComponentType(ComponentType[] currComTypes, ComponentType[] componentTypes) {
        if (componentTypes == null || componentTypes.length == 0) {
            return true;
        }
        for (ComponentType currComType : currComTypes) {
            for (ComponentType type : componentTypes) {
                if (type.equals(currComType)) {
                    return true;
                }
            }
        }
        return false;
    }


    @MethodChinaName(cname = "保存实体关系")
    @RequestMapping(value = {"save"}, method = {RequestMethod.POST})
    @APIEventAnnotation(isAllform = true, callback = {CustomCallBack.CLOSE}, bindMenu = CustomMenuItem.TREESAVE
            , customRequestData = {RequestPathEnum.SPA_CLASSNAME, RequestPathEnum.RAD_SELECTITEMS}
            , customResponseData = {ResponsePathEnum.SPACOMPONENT})
    public @ResponseBody
    ListResultModel<List<Component>> save(String ESDClassTree, String className, String projectName, String[] selectItems) {
        ListResultModel<List<Component>> result = new ListResultModel<List<Component>>();
        List<Component> components = new ArrayList<>();
        try {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(className, projectName);
            if (selectItems != null && selectItems.length > 0) {
                String selectItem = selectItems[0];
                Component component = euModule.getComponent().findComponentByAlias(selectItem);
                EUModule tempModule = ESDFacrory.getAdminESDClient().getModule(ESDClassTree, projectName);
//                if (tempModule == null) {
//                    PackagePathType packagePathType = PackagePathType.startPath(path);
//                    tempModule = ESDFacrory.getESDClient().getModule(ESDClassTree,projectName);
//                }
                List<Component> componentList = tempModule.getTopComponents(true);


                if (!(component.getProperties() instanceof PanelProperties)) {
                    Component parentcomponent = component.getParent();
                    parentcomponent.removeChildren(component);
                    for (Component child : componentList) {
                        parentcomponent.addChildren(child);
                    }
                    components.add(parentcomponent);
                } else {
                    for (Component child : componentList) {
                        component.addChildren(child);
                    }
                    components.add(component);
                }


            }
            result.setData(components);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return result;

    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDsmId() {
        return dsmId;
    }

    public void setDsmId(String dsmId) {
        this.dsmId = dsmId;
    }
}
