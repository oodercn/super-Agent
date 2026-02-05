package net.ooder.web.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import freemarker.template.TemplateException;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.RequestType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.config.*;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.CustomDynDataBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.PageBarComponent;
import net.ooder.esd.util.page.GridPageUtil;
import net.ooder.esd.util.page.TabPageUtil;
import net.ooder.esd.util.page.TreePageUtil;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.server.httpproxy.core.Request;
import net.ooder.server.httpproxy.core.Response;
import net.ooder.template.JDSFreemarkerResult;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.BaseParamsEnums;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.JSONGenUtil;
import net.ooder.web.util.MethodUtil;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);

    public static final String CUSSCLASSNAME = "_currClassName_";

    public static final String ProjectName = "projectName";

    public static final String ProjectVersionName = "projectVersionName";

    public static final String VVVERSION = "VVVERSION";

    public static final String[] CKEY = new String[]{"preview", "public", "custom", "debug", "RAD", "root"};


    public MethodConfig getPanentMethodConfig(HttpServletRequest request) throws JDSException {
        MethodConfig topMethodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.TopMethodBeanKey);
        String projectName = this.getProjectName(request);
        Object obj = request.getParameter(CUSSCLASSNAME);
        if (obj != null && !obj.equals("") && !obj.equals("RAD")) {
            String currClassName = obj.toString();
            if (currClassName.indexOf(CustomViewFactory.INMODULE__) > -1) {
                currClassName = currClassName.split(CustomViewFactory.INMODULE__)[0];
            }
            topMethodConfig = CustomViewFactory.getInstance().getMethodAPIBean(currClassName, projectName);
            if (topMethodConfig != null) {
                ApiClassConfig apiConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(topMethodConfig.getSourceClassName());
                if (apiConfig != null) {
                    topMethodConfig = apiConfig.getMethodByName(topMethodConfig.getMethodName());
                }
                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.TopMethodBeanKey, topMethodConfig);
            }
        }

        return topMethodConfig;
    }

    public String getRealClassName(HttpServletRequest request) {
        return getCustomClassName(request)[0];
    }

    ;

    public String[] getCustomClassName(HttpServletRequest request) {
        String euClassName = request.getParameter("tag");
        if (euClassName == null) {
            euClassName = request.getRequestURI();
        }
        String[] nameArr = new String[]{euClassName};
        if (euClassName.indexOf(CustomViewFactory.INMODULE__) > -1) {
            nameArr = euClassName.split(CustomViewFactory.INMODULE__);
        }
        return nameArr;
    }

    public MethodConfig getCurrMethodConfig(HttpServletRequest request) {
        MethodConfig methodConfig = (MethodConfig) JDSActionContext.getActionContext().getContext().get(CustomViewFactory.MethodBeanKey);
        try {
            String[] nameArr = getCustomClassName(request);
            String euClassName = nameArr[0];
            methodConfig = CustomViewFactory.getInstance().getMethodAPIBean(euClassName, this.getProjectName(request));
            if (nameArr.length > 1) {
                String value = nameArr[1];
                if (methodConfig != null && value != null && methodConfig.getRequestMethodBean().getParamSet().size() > 0) {
                    Set<RequestParamBean> paramBeanSet = methodConfig.getParamSet();
                    for (RequestParamBean paramBean : paramBeanSet) {
                        if (paramBean.getParamClass().isEnum()) {
                            Object obj = TypeUtils.castToJavaBean(value, paramBean.getParamClass());
                            if (obj != null) {
                                JDSActionContext.getActionContext().getContext().put(paramBean.getParamName(), obj);
                                break;
                            }
                        }
                    }
                }
            }

            JDSActionContext.getActionContext().getContext().put(CustomViewFactory.MethodBeanKey, methodConfig);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return methodConfig;
    }


    public Object invokMethod(RequestMethodBean methodBean, HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, OgnlException {
        Object object = null;
        Object service = this.getService(methodBean, request);
        if (service == null) {
            return null;
        }
        RequestMethodBean realMethodBean = null;
        if (!service.getClass().getName().equals(methodBean.getSourceMethod().getDeclaringClass().getName())) {
            realMethodBean = new RequestMethodBean(MethodUtil.getEqualMethod(service.getClass(), methodBean.getSourceMethod()), methodBean.getMappingBean(), methodBean.getDomainId());
        }
        Map<String, Object> allParamsMap = this.getAllParamMap(request);
        JDSActionContext.getActionContext().getContext().put(CustomViewFactory.CurrModuleKey, getEUModule(request, allParamsMap));
        Map<String, String> paramsMap = methodBean.getParamsMap();
        Set<RequestParamBean> keySet = methodBean.getParamSet();
        List<Object> objects = new ArrayList<>();
        List<Class> objectTyps = new ArrayList<>();

        DynLoadAnnotation dynAnnotation = AnnotationUtil.getMethodAnnotation(methodBean.getSourceMethod(), DynLoadAnnotation.class);
        ModuleComponent moduleComponent = null;
        for (RequestParamBean paramBean : keySet) {
            Class paramClass = paramBean.getParamClass();
            if (realMethodBean != null) {
                paramClass = realMethodBean.getRealParams(paramBean.getParamName()).getParamClass();
            }

            Class ctClass = ClassUtility.loadClass(paramsMap.get(paramBean.getParamName()));
            String iClassName = ctClass.getName();
            Class iClass = ClassUtility.loadClass(iClassName);
            String key = paramBean.getParamName();
            Object value = null;
            try {
                RequestBody requestBody = null;
                for (Annotation annotation : paramBean.getAnnotations()) {
                    if (annotation.annotationType().equals(RequestBody.class)) {
                        requestBody = (RequestBody) annotation;
                    }
                }
                if (requestBody != null) {
                    if (methodBean.getRequestType().equals(RequestType.JSON)) {
                        for (BaseParamsEnums baseParams : BaseParamsEnums.values()) {
                            if (request.getParameterMap().containsKey(baseParams.name())) {
                                JDSActionContext.getActionContext().getContext().put(baseParams.name(), request.getParameterMap().get(baseParams.name()));
                            }
                        }
                        String json = request.getReader().lines().collect(Collectors.joining());
                        value = JSONObject.parseObject(json, paramClass);
                    }
                } else if (Request.class.isAssignableFrom(iClass)) {
                    value = request;
                } else if (ModuleComponent.class.isAssignableFrom(iClass)) {
                    if (moduleComponent == null) {
                        moduleComponent = getEUModule(request, allParamsMap).getComponent().getRealModuleComponent();
                    }
                    value = moduleComponent;
                } else if (EUModule.class.isAssignableFrom(iClass)) {
                    value = getEUModule(request, allParamsMap);
                } else if (Response.class.isAssignableFrom(iClass)) {
                    value = response;
                } else {
                    switch (methodBean.getRequestType()) {
                        case FORM:
                            if (iClass.isArray()) {
                                if (JSONObject.parseArray(allParamsMap.get(key).toString()).size() > 0) {
                                    value = JSONObject.parseArray(allParamsMap.get(key).toString(), iClass.getComponentType()).toArray();
                                } else {
                                    value = Array.newInstance(iClass.getComponentType(), 0);
                                }
                            } else {
                                if (paramBean.getJsonData()) {
                                    if (iClass.isArray() || Collection.class.isAssignableFrom(iClass)) {
                                        value = JSONArray.parseObject(allParamsMap.get(key).toString(), paramBean.getParamType());
                                    } else {
                                        value = JSONObject.parseObject(allParamsMap.get(key).toString(), paramBean.getParamType());
                                    }

                                } else {
                                    value = TypeUtils.castToJavaBean(allParamsMap.get(key), iClass);
                                }
                            }
                            break;
                        case JSON:
                            String json = request.getReader().lines().collect(Collectors.joining());
                            JSONObject jsonObject = JSON.parseObject(json);
                            if (jsonObject != null) {
                                for (BaseParamsEnums baseParams : BaseParamsEnums.values()) {
                                    if (jsonObject.containsKey(baseParams.name())) {
                                        JDSActionContext.getActionContext().getContext().put(baseParams.name(), jsonObject.get(baseParams.name()));
                                    }
                                }

                                String paramName = paramBean.getParamName();
                                Object obj = jsonObject.get(paramName);
                                if (obj != null) {
                                    value = TypeUtils.castToJavaBean(obj, iClass);
                                    JDSActionContext.getActionContext().getContext().put(paramName, value);
                                }

                            }
                            break;

                        default:
                            value = TypeUtils.cast(allParamsMap.get(paramBean.getParamName()), iClass, null);
                            break;
                    }

                }
            } catch (Throwable e) {
                e.printStackTrace();
                logger.error("参数转换错误：  [" + paramBean.getParamName() + "] convertValue err " + e.getMessage());
            }
            objectTyps.add(iClass);
            objects.add(value);
        }

        if (service != null) {
            if (dynAnnotation != null) {
                object = OgnlRuntime.callMethod(JDSActionContext.getActionContext().getOgnlContext(), service, methodBean.getMethodName(), objects.toArray());
                Map ctx = ((ResultModel) object).getCtx();
                if (ctx != null) {
                    allParamsMap.putAll(ctx);
                }

                if (JSONGenUtil.getInnerReturnType(methodBean).isAssignableFrom(EUModule.class)) {
                    EUModule otherModule = ((ResultModel<EUModule>) object).getData();
                    try {
                        ((ResultModel) object).setData(otherModule.getRealComponents(true));
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                } else if (!JSONGenUtil.getInnerReturnType(methodBean).isAssignableFrom(net.ooder.esd.tool.component.Component.class) || !dynAnnotation.refClassName().equals("")) {
                    if (moduleComponent == null) {
                        try {
                            moduleComponent = getEUModule(request, allParamsMap).getComponent().getRealModuleComponent().clone();
                        } catch (JDSException e) {
                            e.printStackTrace();
                        }
                    }

                    if (moduleComponent != null) {
                        ResultModel resultModel = (ResultModel) object;
                        boolean syncLoad = false;
                        if (moduleComponent instanceof DataComponent) {
                            List<APICallerComponent> componentList = moduleComponent.findComponents(ComponentType.APICALLER, "Reload");
                            for (APICallerComponent api : componentList) {
                                if (api.getAlias().equals("Reload") && api.getProperties().getAutoRun()) {
                                    syncLoad = true;
                                    // api.getProperties().setAutoRun(false);
                                }
                            }
                            if (syncLoad) {
                                ((DataComponent) moduleComponent).setData(resultModel.getData());
                                if (resultModel instanceof TreeListResultModel) {
                                } else if (resultModel instanceof ListResultModel) {
                                    ListResultModel listResultModel = (ListResultModel) object;
                                    List<PageBarComponent> pageBarComponents = moduleComponent.findComponents(ComponentType.PAGEBAR, null);
                                    if (pageBarComponents.size() > 0) {
                                        pageBarComponents.get(0).setData(Integer.valueOf(listResultModel.getSize()));
                                    }
                                }
                            }
                        }
                    }

                    ((ResultModel) object).setData(moduleComponent.getTopComponents(true));
                }
            } else {
                object = OgnlRuntime.callMethod(JDSActionContext.getActionContext().getOgnlContext(), service, methodBean.getMethodName(), objects.toArray());
            }
        } else {
            object = OgnlRuntime.callMethod(JDSActionContext.getActionContext().getOgnlContext(), service, methodBean.getMethodName(), objects.toArray());
        }


        return object;
    }

    public ResultModel dataProxy(Object object, MethodConfig methodConfig) throws ClassNotFoundException {
        ResultModel resultModel = new ResultModel();
        ModuleViewType moduleViewType = methodConfig.getModuleViewType();
        Class iClass = JSONGenUtil.getInnerReturnType(methodConfig.getViewClass().getCtClass());
        if (moduleViewType != null && !(object instanceof ResultModel)) {
            switch (moduleViewType) {
                case GALLERYCONFIG:
                    if (Collection.class.isAssignableFrom(object.getClass())) {
                        resultModel = GridPageUtil.getDefaultPageList(Arrays.asList(((Collection) object).toArray()), iClass);
                    }
                    break;
                case TREECONFIG:
                    if (Collection.class.isAssignableFrom(object.getClass())) {
                        resultModel = TreePageUtil.getDefaultTreeList(Arrays.asList(((Collection) object).toArray()), iClass);
                    }
                    break;
                case NAVTABSCONFIG:
                    if (Collection.class.isAssignableFrom(object.getClass())) {
                        resultModel = TabPageUtil.getTabList(Arrays.asList(((Collection) object).toArray()), iClass);
                    }
                    break;
                case POPTREECONFIG:
                    if (Collection.class.isAssignableFrom(object.getClass())) {
                        resultModel = TreePageUtil.getDefaultTreeList(Arrays.asList(((Collection) object).toArray()), iClass);
                    }
                    break;
                case NAVGALLERYCONFIG:
                    break;
                case GRIDCONFIG:
                    if (Collection.class.isAssignableFrom(object.getClass())) {
                        object = GridPageUtil.getDefaultPageList(Arrays.asList(((Collection) object).toArray()), iClass);
                    }
                    break;
                case NAVBUTTONVIEWSCONFIG:
                    break;
                case FORMCONFIG:
                    CustomFormViewBean formViewBean = (CustomFormViewBean) methodConfig.getView();
                    List<FieldFormConfig> fieldFormConfigs = formViewBean.getAllFields();
                    for (FieldFormConfig fieldFormConfig : fieldFormConfigs) {
                        if (fieldFormConfig.getCustomBean() != null
                                && fieldFormConfig.getFieldBean().getExpression() != null
                                && !fieldFormConfig.getFieldBean().getExpression().equals("")) {
                            Map<String, Object> objectMap = JDSActionContext.getActionContext().getContext();
                            String expression = fieldFormConfig.getFieldBean().getExpression();
                            Object obj = EsbUtil.parExpression(expression, objectMap, object, fieldFormConfig.getEsdField().getReturnType());
                            OgnlUtil.setProperty(fieldFormConfig.getEsdField().getName(), obj, object, objectMap);
                        }
                    }

                    resultModel.setData(object);

                    break;
                case NAVMENUBARCONFIG:
                    break;
            }
        }

        return resultModel;

    }

    public Object getService(RequestMethodBean methodBean, HttpServletRequest request) throws ClassNotFoundException, OgnlException {
        Map<String, Object> allParamsMap = this.getAllParamMap(request);
        Class clazz = ClassUtility.loadClass(methodBean.getClassName());
        Object service = getRealService(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            if (allParamsMap.get(field.getName()) != null) {
                try {
                    OgnlRuntime.setProperty(JDSActionContext.getActionContext().getOgnlContext(), service, field.getName(), TypeUtils.castToJavaBean(allParamsMap.get(field.getName()), field.getType()));
                } catch (OgnlException e) {
                }
            }
        }
        return service;
    }

    public Map<String, Object> getAllParamMap(HttpServletRequest request) {
        Map<String, Object> contextMap = new HashMap();
        Map<String, String[]> paramsMap = request.getParameterMap();
        Set<String> keySet = paramsMap.keySet();
        for (String key : keySet) {
            String[] obj = paramsMap.get(key);
            if (obj != null && obj.length == 1) {
                contextMap.put(key, obj[0]);
            } else {
                contextMap.put(key, obj);
            }
        }
        return contextMap;
    }

    public String formatPath(String urlStr, String projectName) throws MalformedURLException {
        String path = urlStr;
        if (urlStr.startsWith("http")) {
            URL url = new URL(urlStr);
            path = url.getPath();
        }
        if (path.startsWith("/")) {
            path = path.substring(1, path.length());
        }
        for (String ckey : CKEY) {
            String key = ckey + "/";
            if (path.startsWith(key)) {
                path = path.substring(key.length());
            }
        }
        if (projectName != null) {
            if (projectName.indexOf(VVVERSION) > -1) {
                projectName = projectName.split(VVVERSION)[0];
            }
            if ((path.startsWith(projectName + "/") || path.startsWith(projectName + VVVERSION))) {
                path = path.substring(path.indexOf("/") + 1);
            }
        }


        return path;
    }

    Object getRealService(Class clazz) throws OgnlException {
        Object service = null;
        if (clazz.getInterfaces().length > 0) {
            service = EsbUtil.parExpression(clazz.getInterfaces()[0]);
        } else {
            service = EsbUtil.parExpression(clazz);
        }

        if (service == null) {
            if (clazz.isInterface()) {
                Aggregation aggregation = (Aggregation) clazz.getAnnotation(Aggregation.class);
                if (aggregation != null && !aggregation.rootClass().equals(Void.class) && !aggregation.rootClass().equals(clazz)) {
                    clazz = aggregation.rootClass();
                    service = getRealService(clazz);
                }
            } else {
                service = OgnlRuntime.callConstructor(JDSActionContext.getActionContext().getOgnlContext(), clazz.getName(), new Object[]{});
            }

        }

        return service;
    }

    public EUModule getEUModule(HttpServletRequest request, Map<String, Object> allParamsMap) {
        EUModule module = null;
        try {
            String projectName = this.getProjectName(request);
            MethodConfig currMethodConfig = this.getCurrMethodConfig(request);
            CustomDynDataBean customDynDataBean = currMethodConfig.getDynDataBean();
            CustomModuleBean moduleAnnotation = currMethodConfig.getModuleBean();
            if (customDynDataBean != null && moduleAnnotation != null) {
                PackagePathType packagePathType = PackagePathType.startPath(StringUtility.replace(customDynDataBean.getRefClassName(), ".", "/"));
                if (!customDynDataBean.getRefClassName().equals("")) {
                    if (projectName == null || projectName.equals("")) {
                        if (packagePathType != null) {
                            packagePathType.getApiType().getDefaultProjectName();
                        }
                    }
                    ProjectVersion projectVersion = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
                    module = projectVersion.getModule(customDynDataBean.getRefClassName());
                    if (module == null) {
                        module = ESDFacrory.getAdminESDClient().rebuildCustomModule(customDynDataBean.getRefClassName(), projectName, allParamsMap);
                    }
                    if (module != null) {
                        module.getComponent().getRealModuleComponent().fillFormValues(allParamsMap, true);
                    }
                } else {
                    Class componentClass = JSONGenUtil.getInnerReturnType(currMethodConfig.getMethod());
                    if (ModuleComponent.class.isAssignableFrom(componentClass)) {
                        module = ESDFacrory.getAdminESDClient().buildDynCustomModule(componentClass, allParamsMap, true);
                    } else {
                        String currClassName = currMethodConfig.getEUClassName();
                        packagePathType = PackagePathType.startPath(StringUtility.replace(currClassName, ".", "/"));
                        if (packagePathType != null && packagePathType.getApiType() != null && packagePathType.getApiType().getDefaultProjectName() != null) {
                            module = CustomViewFactory.getInstance().createRealView(currMethodConfig, null, packagePathType.getApiType().getDefaultProjectName(), allParamsMap, true);
                        } else {
                            module = CustomViewFactory.getInstance().createRealView(currMethodConfig, null, projectName, allParamsMap, true);
                        }
                    }
                }
            } else {
                MethodConfig methodConfig = this.getPanentMethodConfig(request);
                if (methodConfig != null) {
                    module = methodConfig.getModule(allParamsMap, projectName);
                    if (module != null) {
                        module.getComponent().getRealModuleComponent().fillFormValues(allParamsMap, true);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return module;
    }


    public ResultModel clear(String currentClassName, String projectName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW);
            Map<String, Object> ctxMap = new HashMap<>();
            ctxMap.put("domainId", domainInst.getDomainId());
            JDSActionContext.getActionContext().getContext().putAll(ctxMap);
            EUModule euModule = ESDFacrory.getAdminESDClient().getModule(currentClassName, projectName);
            if (euModule != null) {
                ModuleComponent moduleComponent = euModule.getComponent();
                MethodConfig methodConfig = null;
                try {
                    methodConfig = moduleComponent.getMethodAPIBean();
                    if (methodConfig == null) {
                        methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euModule.getPath(), projectName);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                Set<String> esdClassNames = new HashSet<>();
                esdClassNames.add(methodConfig.getSourceClassName());
                Set<Class> classes = methodConfig.getOtherClass();
                for (Class clazz : classes) {
                    if (clazz != null) {
                        esdClassNames.add(clazz.getName());
                    }
                }

                DSMFactory.getInstance().getAggregationManager().delAggEntity(domainInst.getDomainId(), esdClassNames, projectName, false);

                CustomViewFactory.getInstance().reLoad();
                ESDFacrory.getAdminESDClient().delModule(euModule);
            }

        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    protected RequestMethodBean findMethodBean(String path) {
        RequestMethodBean methodBean = APIConfigFactory.getInstance().getRequestMappingBean(path);
        if (methodBean == null) {
            methodBean = APIConfigFactory.getInstance().findMethodBean(path);
        }
        return methodBean;
    }


    public boolean sendJSON(HttpServletResponse response, String json) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.addHeader("content-type", "application/javascript");
        response.getWriter().write(json.toString());

        return true;
    }


    public boolean sendFtl(HttpServletResponse response, String resource) throws IOException {
        String responseStr = "";
        try {
            JDSFreemarkerResult result = new JDSFreemarkerResult();
            StringWriter stringWriter = (StringWriter) result.doExecute(resource, null);
            responseStr = stringWriter.toString();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        response.setCharacterEncoding("UTF-8");
        response.addHeader("content-type", "text/html");
        response.getWriter().write(responseStr);
        return true;
    }

    public String getProjectName(HttpServletRequest request) {
        String projectName = request.getParameter(ProjectVersionName);
        if (projectName == null || projectName.equals("") || (projectName.equals("projectManager") && request.getParameter(ProjectName) != null)) {
            projectName = request.getParameter(ProjectName);
        }
        if (projectName == null || projectName.equals("")) {
            projectName = JDSConfig.getValue("projectName");
        }
        JDSActionContext.getActionContext().getContext().put("projectName", projectName);
        return projectName;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }

}

