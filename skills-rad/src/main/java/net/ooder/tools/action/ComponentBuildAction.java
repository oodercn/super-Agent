package net.ooder.tools.action;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.Split;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.WidgetBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ComponentList;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.web.util.JSONGenUtil;
import org.mvel2.templates.TemplateRuntime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


@Controller

@RequestMapping(value = {"/action/component/build/"})
@MenuBarMenu(menuType = CustomMenuType.TOP, caption = "组件编译", index = 6, imageClass = "ri-settings-3-line")
@Aggregation(type = AggregationType.MENU, userSpace = UserSpace.SYS)
public class ComponentBuildAction {
    public ComponentBuildAction() {

    }


    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @RequestMapping(method = RequestMethod.POST, value = "reBuildModule")
    @CustomAnnotation(index = 0, caption = "更新组件", imageClass = "ri-refresh-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.SPA_CLASSNAME, RequestPathEnum.RAD_JSON, RequestPathEnum.RAD_SELECTITEMS})
    @ResponseBody
    public ResultModel<Boolean> rePackage(String currentClassName, String projectName, String[] json, String[] selectItems) {
        ResultModel result = new ResultModel<>();
        try {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
            if (methodConfig == null) {
                methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
            }
            Map<String, Object> ctxMap = new HashMap<>();
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            for (String currCom : selectItems) {
                Component component = euModule.getComponent().findComponentByAlias(currCom);
                if (component != null) {
                    CustomViewBean customViewBean = null;
                    ComponentBean componentBean = methodConfig.getView().findComByPath(component.getPath());
                    if (componentBean instanceof CustomViewBean) {
                        customViewBean = (CustomViewBean) componentBean;
                    } else if (componentBean instanceof WidgetBean) {
                        customViewBean = ((WidgetBean) componentBean).getViewBean();
                    } else if (componentBean instanceof CustomModuleRefFieldBean) {
                        CustomModuleRefFieldBean refFieldBean = (CustomModuleRefFieldBean) componentBean;
                        customViewBean = refFieldBean.getModuleBean().getMethodConfig().getView();
                    }

                    EUModule module = customViewBean.getMethodConfig().getModule(JDSActionContext.getActionContext().getContext(), projectName);
                    if (module != null) {
                        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, module.getClassName(), projectName);
                        if (aggRootBuild != null) {
                            aggRootBuild.reBuildModule();
                        }
                    }
                }
            }

            result.setCtx(ctxMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "clear")
    @CustomAnnotation(index = 0, caption = "重新编译", imageClass = "ri-refresh-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME, RequestPathEnum.SPA_CLASSNAME, RequestPathEnum.RAD_JSON, RequestPathEnum.RAD_SELECTITEMS}, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    @ResponseBody
    public ResultModel<Boolean> clear(String currentClassName, String projectName, String[] json, String[] selectItems) {
        ResultModel result = new ResultModel<>();
        try {
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            MethodConfig methodConfig = null;
            try {
                methodConfig = euModule.getComponent().getMethodAPIBean();
                if (methodConfig == null) {
                    methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            DomainInst domainInst = DSMFactory.getInstance().getDomainInstById(methodConfig.getDomainId());
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            ComponentList components = new ComponentList();
            for (String objjson : json) {
                JSONObject obj = JSONObject.parseObject(objjson);
                if (obj != null && obj.getString("key") != null) {
                    String classType = obj.getString("key");
                    Component component = JSONObject.parseObject(obj.toJSONString(), (Class<Component>) ComponentType.fromType(classType).getClazz());
                    JSONObject property = obj.getJSONObject("properties");
                    //兼容版本，后期会移除
                    String tagter = obj.getString("tagter");
                    if (tagter != null) {
                        component.setTarget(tagter);
                    }
                    if (property != null) {
                        String propertyJson = obj.getJSONObject("properties").toJSONString();
                        Map context = JDSActionContext.getActionContext().getContext();
                        propertyJson = (String) TemplateRuntime.eval(propertyJson, context);
                        Class clazz = ComponentType.fromType(classType).getClazz();
                        Type realType = JSONGenUtil.getRealType(clazz, Properties.class);
                        if (realType != null) {
                            Properties properties = JSONObject.parseObject(propertyJson, realType);
                            component.setProperties(properties);
                        }
                    }
                    components.add(component);
                }

            }


            for (String currCom : selectItems) {
                Component component = euModule.getComponent().findComponentByAlias(currCom);
                if (component != null) {
                    CustomViewBean customViewBean = null;
                    ComponentBean componentBean = methodConfig.getView().findComByPath(component.getPath());

                    if (componentBean != null) {
                        componentBean = methodConfig.getView().findComByAlias(currCom);
                    }

                    if (componentBean instanceof CustomViewBean) {
                        customViewBean = (CustomViewBean) componentBean;
                    } else if (componentBean instanceof WidgetBean) {
                        customViewBean = ((WidgetBean) componentBean).getViewBean();
                    } else if (componentBean instanceof CustomModuleRefFieldBean) {
                        CustomModuleRefFieldBean refFieldBean = (CustomModuleRefFieldBean) componentBean;
                        customViewBean = refFieldBean.getModuleBean().getMethodConfig().getView();
                    }

                    EUModule module = customViewBean.getMethodConfig().getModule(JDSActionContext.getActionContext().getContext(), projectName);
                    if (module != null) {
                        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, module.getClassName(), projectName);
                        if (aggRootBuild != null) {
                            aggRootBuild.reBuildModule();
                        }
                    }
                }
            }
            result.setCtx(ctxMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }
}
