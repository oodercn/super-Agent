package net.ooder.web.interceptor;

import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ModuleInterceptor extends BaseInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ModuleInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {

            String projectName = this.getProjectName(request);
            String euClassName = request.getParameter("tag");


            if (euClassName == null) {
                euClassName = request.getRequestURI();
            }
            String realClassName = euClassName;
            if (euClassName.indexOf(CustomViewFactory.INMODULE__) > -1) {
                String[] nameArr = euClassName.split(CustomViewFactory.INMODULE__);
                realClassName = nameArr[0];
            }

            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, projectName);
            MethodConfig methodConfig = this.getCurrMethodConfig(request);

            if (euModule == null) {

                if (methodConfig != null) {
                    if (!euClassName.equals(realClassName)) {
                        EUModule proxyModule = ESDFacrory.getAdminESDClient().createModule(euClassName, projectName);
                        ModuleComponent cloneModuleComponent = proxyModule.getComponent();
                        EUModule realModule = ESDFacrory.getAdminESDClient().getModule(realClassName, projectName);
                        if (realModule == null) {
                            realModule = ESDFacrory.getAdminESDClient().getCustomModule(realClassName, projectName, new HashMap<>());
                        }
                        ModuleComponent moduleComponent = realModule.getComponent();
                        List<net.ooder.esd.tool.component.Component> components = new ArrayList<>();
                        components.addAll(moduleComponent.getChildren());
                        for (net.ooder.esd.tool.component.Component component : components) {
                            if (component.equals(realModule.getComponent().getMainBoxComponent())) {
                                for (net.ooder.esd.tool.component.Component uiCom : component.getChildren()) {
                                    cloneModuleComponent.getMainBoxComponent().addChildren(uiCom.clone());
                                }
                            } else {
                                cloneModuleComponent.addChildren(component.clone());
                            }
                        }
                        net.ooder.esd.tool.component.Component currComponent = cloneModuleComponent.findComponentByAlias(realModule.getComponent().getCurrComponentAlias());

                        if (currComponent != null) {
                            cloneModuleComponent.setCurrComponent(currComponent);
                        }

                        proxyModule.update(false);
                        euModule = proxyModule;
                    } else {
                        euModule = ESDFacrory.getAdminESDClient().getModule(realClassName, projectName);
                        if (euModule == null) {
                            euModule = ESDFacrory.getAdminESDClient().getCustomModule(realClassName, projectName, new HashMap<>());
                        }
                    }
                }

            }

            if (methodConfig != null) {
                if (methodConfig.getApi().getAutoRun() == null || !methodConfig.getApi().getAutoRun()) {
                    net.ooder.esd.tool.component.Component component = euModule.getComponent().getCurrComponent();
                    if (component instanceof DataComponent) {
                        Map context = new HashMap<>();
                        context.putAll(euModule.getComponent().getValueMap());
                        context.putAll(JDSActionContext.getActionContext().getContext());
                        ResultModel resultModel = (ResultModel) methodConfig.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), context);
                        if (resultModel != null && resultModel.getData() != null) {
                            ((DataComponent) component).setData(resultModel.getData());
                        }
                    }
                }
            }

            if (euModule == null) {
                euModule = ESDFacrory.getAdminESDClient().getModule(realClassName, projectName);
                if (euModule == null) {
                    euModule = ESDFacrory.getAdminESDClient().getCustomModule(realClassName, projectName, new HashMap<>());
                }
            }


            StringBuffer json = ESDFacrory.getAdminESDClient().genJSON(euModule, null, true);
            sendJSON(response, json.toString());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}