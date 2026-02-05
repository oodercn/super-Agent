package net.ooder.editor.console.ddd;

import net.ooder.annotation.MethodChinaName;
import net.ooder.config.ListResultModel;
import net.ooder.dsm.aggregation.api.method.parameter.custom.CustomRequestParameterGridView;
import net.ooder.dsm.aggregation.api.method.parameter.custom.CustomResponseParameterGridView;
import net.ooder.dsm.aggregation.api.method.parameter.method.MethodParameterGridView;
import net.ooder.dsm.aggregation.api.method.parameter.request.RequestParameterGridView;
import net.ooder.dsm.aggregation.api.method.parameter.response.ResponseParameterGridView;
import net.ooder.dsm.view.config.base.BaseViewService;
import net.ooder.esd.annotation.ChildTreeAnnotation;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.ui.ResponsePathEnum;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RequestPathBean;
import net.ooder.esd.bean.ResponsePathBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(path = "/rad/ddd/api/")
@ToolBarMenu(menuClasses = DDDToolBar.class, dock = Dock.top, autoIconColor = false, iconFontSize = "1em")
@TabsAnnotation(barSize = "1.5em")
public class DDDParamsMetaGroup extends BaseViewService {
    private String methodName;
    @CustomAnnotation(pid = true, hidden = true)
    private String sourceClassName;

    @CustomAnnotation(pid = true, hidden = true)
    private String domainId;

    @CustomAnnotation(hidden = true)
    private String url;


    public DDDParamsMetaGroup() {

    }

    public DDDParamsMetaGroup(String methodName) {
        this.methodName = methodName;
    }

    @MethodChinaName(cname = "方法参数")
    @RequestMapping(method = RequestMethod.POST, value = "MethodParameters")
    @GridViewAnnotation
    @ModuleAnnotation()
    @ChildTreeAnnotation(index = 1, caption = "接口参数")
    @APIEventAnnotation(autoRun = true)
    @ResponseBody
    public ListResultModel<List<MethodParameterGridView>> getMethodParameters(String sourceClassName, String xpath, String sourceMethodName, String projectName, String currentClassName, String domainId) {
        ListResultModel<List<MethodParameterGridView>> resultModel = new ListResultModel<>();
        MethodConfig apiCallBean = this.getTopMethod(sourceClassName, xpath, sourceMethodName, currentClassName, projectName, domainId);
        List<MethodParameterGridView> parameterGridViews = new ArrayList<>();
        Set<RequestParamBean> paramBeans = apiCallBean.getParamSet();
        if (apiCallBean.getApi() != null) {
            for (RequestParamBean item : paramBeans) {
                parameterGridViews.add(new MethodParameterGridView(item, apiCallBean, currentClassName, xpath));
            }
        }
        resultModel = PageUtil.getDefaultPageList(parameterGridViews);

        return resultModel;
    }


    @MethodChinaName(cname = "常用请求")
    @RequestMapping(method = RequestMethod.POST, value = "CustomRequestParameters")
    @GridViewAnnotation
    @ModuleAnnotation()
    @ChildTreeAnnotation(caption = "常用请求", index = 2)
    @APIEventAnnotation(autoRun = true)
    @ResponseBody
    public ListResultModel<List<CustomRequestParameterGridView>> getCustomRequestParameters(String sourceClassName, String xpath, String sourceMethodName, String projectName, String currentClassName, String domainId) {
        ListResultModel<List<CustomRequestParameterGridView>> resultModel = new ListResultModel<>();

        MethodConfig apiCallBean = this.getTopMethod(sourceClassName, xpath, sourceMethodName, currentClassName, projectName, domainId);
        Set<RequestPathEnum> requestDataSource = apiCallBean.getApi().getCustomRequestData();
        List<CustomRequestParameterGridView> parameterGridViews = new ArrayList<>();
        if (apiCallBean.getApi() != null) {
            for (RequestPathEnum item : requestDataSource) {
                parameterGridViews.add(new CustomRequestParameterGridView(item, apiCallBean, currentClassName, xpath));
            }
        }
        resultModel = PageUtil.getDefaultPageList(parameterGridViews);


        return resultModel;
    }

    @MethodChinaName(cname = "常用映射")
    @RequestMapping(method = RequestMethod.POST, value = "CustomResponseParameter")
    @GridViewAnnotation
    @ModuleAnnotation(caption = "常用映射")
    @CustomAnnotation(index = 0)
    @APIEventAnnotation(autoRun = true)
    @ResponseBody
    public ListResultModel<List<CustomResponseParameterGridView>> getCustomResponseParameter(String sourceClassName, String xpath, String sourceMethodName, String projectName, String currentClassName, String domainId) {
        ListResultModel<List<CustomResponseParameterGridView>> resultModel = new ListResultModel<>();

        MethodConfig apiCallBean = this.getTopMethod(sourceClassName, xpath, sourceMethodName, currentClassName, projectName, domainId);
        Set<ResponsePathEnum> responseDataTarget = apiCallBean.getApi().getCustomResponseData();
        List<CustomResponseParameterGridView> parameterGridViews = new ArrayList<>();
        if (apiCallBean.getApi() != null) {
            for (ResponsePathEnum item : responseDataTarget) {
                parameterGridViews.add(new CustomResponseParameterGridView(item, apiCallBean, currentClassName, xpath));
            }
        }
        resultModel = PageUtil.getDefaultPageList(parameterGridViews);

        return resultModel;
    }


    @MethodChinaName(cname = "自定义请求")
    @RequestMapping(method = RequestMethod.POST, value = "RequestParameters")
    @GridViewAnnotation
    @ModuleAnnotation(caption = "自定义请求")
    @CustomAnnotation(index = 1)
    @APIEventAnnotation(autoRun = true)
    @ResponseBody
    public ListResultModel<List<RequestParameterGridView>> getRequestParameters(String sourceClassName, String xpath, String sourceMethodName, String projectName, String currentClassName, String domainId) {
        ListResultModel<List<RequestParameterGridView>> resultModel = new ListResultModel<>();

        MethodConfig apiCallBean = this.getTopMethod(sourceClassName, xpath, sourceMethodName, currentClassName, projectName, domainId);
        Set<RequestPathBean> requestDataSource = apiCallBean.getApi().getRequestDataSource();
        List<RequestParameterGridView> parameterGridViews = new ArrayList<>();
        if (apiCallBean.getApi() != null) {
            for (RequestPathBean item : requestDataSource) {
                parameterGridViews.add(new RequestParameterGridView(item, apiCallBean, currentClassName, xpath));
            }
        }
        resultModel = PageUtil.getDefaultPageList(parameterGridViews);

        return resultModel;
    }

    @MethodChinaName(cname = "自定义映射")
    @RequestMapping(method = RequestMethod.POST, value = "BindViews")
    @GridViewAnnotation
    @ModuleAnnotation(caption = "自定义映射")
    @CustomAnnotation(index = 0)
    @APIEventAnnotation(autoRun = true)
    @ResponseBody
    public ListResultModel<List<ResponseParameterGridView>> getBindViews(String sourceClassName, String xpath, String sourceMethodName, String projectName, String currentClassName, String domainId) {
        ListResultModel<List<ResponseParameterGridView>> resultModel = new ListResultModel<>();

        MethodConfig apiCallBean = this.getTopMethod(sourceClassName, xpath, sourceMethodName, currentClassName, projectName, domainId);
        Set<ResponsePathBean> responseDataTarget = apiCallBean.getApi().getResponseDataTarget();
        List<ResponseParameterGridView> parameterGridViews = new ArrayList<>();
        if (apiCallBean.getApi() != null) {
            for (ResponsePathBean item : responseDataTarget) {
                parameterGridViews.add(new ResponseParameterGridView(item, apiCallBean, currentClassName, xpath));
            }
        }
        resultModel = PageUtil.getDefaultPageList(parameterGridViews);

        return resultModel;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassNamee(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
}
