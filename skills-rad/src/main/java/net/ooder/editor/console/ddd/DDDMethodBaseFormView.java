package net.ooder.editor.console.ddd;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Hidden;
import net.ooder.annotation.Pid;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.field.ComboNumberAnnotation;
import net.ooder.esd.annotation.field.ListAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ItemRow;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.CustomAPICallBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.util.json.EnumSetDeserializer;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@FormAnnotation(col = 4)
public class DDDMethodBaseFormView {

    @CustomAnnotation(hidden = true, pid = true)
    String domainId;

    @CustomAnnotation(hidden = true, uid = true)
    String sourceClassName;

    @FieldAnnotation(colSpan = 2, manualHeight = 30)
    @CustomAnnotation(caption = "访问地址")
    String url;

    @CustomAnnotation(caption = "自动显示")
    Boolean autoDisplay;

    @Pid
    String sourceMethodName;
    @Pid
    String currentClassName;
    @Pid
    String xpath;

    @ComboNumberAnnotation
    @ComboInputAnnotation(inputType = ComboInputType.spin)
    @FieldAnnotation(componentType = ComponentType.COMBOINPUT)
    @CustomAnnotation(caption = "显示顺序")
    @Hidden
    public Integer index;


    @CustomAnnotation(caption = "JSON格式")
    Boolean responseBody;

    @CustomAnnotation(caption = "异步调用")
    public Boolean queryAsync;

    @CustomAnnotation(caption = "自动运行")
    public Boolean autoRun;

    @CustomAnnotation(caption = "级联处理")
    public Boolean allform;


    @JSONField(deserializeUsing = EnumSetDeserializer.class)
    @ListAnnotation(selMode = SelModeType.multibycheckbox, itemRow = ItemRow.cell)
    @FieldAnnotation(colSpan = -2, manualHeight = 60, componentType = ComponentType.LIST)
    @CustomAnnotation(caption = "HttpMethod")
    Set<RequestMethod> method = new LinkedHashSet<>();


    public DDDMethodBaseFormView() {

    }


    public DDDMethodBaseFormView(MethodConfig methodAPICallBean, String xpath, String currentClassName) {
        this.autoRun = methodAPICallBean.getApi().getApiCallerProperties().getAutoRun();
        this.sourceMethodName = methodAPICallBean.getMethodName();
        this.currentClassName = currentClassName;
        this.xpath = xpath;
        this.domainId = methodAPICallBean.getDomainId();

        this.url = methodAPICallBean.getRequestMapping().getFristUrl();
        this.method = methodAPICallBean.getRequestMapping().getMethod();
        if (method == null) {
            method = new HashSet<>();
        }
        if (method.isEmpty()) {
            method.add(RequestMethod.POST);
        }
        this.responseBody = methodAPICallBean.getResponseBody();
        this.sourceClassName = methodAPICallBean.getSourceClassName();


        CustomAPICallBean apiCallBean = methodAPICallBean.getApi();
        this.index = apiCallBean.getIndex();
        this.queryAsync = apiCallBean.getApiCallerProperties().getQueryAsync();
        this.autoDisplay = apiCallBean.getAutoDisplay();
        this.allform = apiCallBean.getApiCallerProperties().getIsAllform();

    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Set<RequestMethod> getMethod() {
        return method;
    }

    public void setMethod(Set<RequestMethod> method) {
        this.method = method;
    }

    public Boolean getAutoDisplay() {
        return autoDisplay;
    }

    public void setAutoDisplay(Boolean autoDisplay) {
        this.autoDisplay = autoDisplay;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public Boolean getQueryAsync() {
        return queryAsync;
    }

    public void setQueryAsync(Boolean queryAsync) {
        this.queryAsync = queryAsync;
    }

    public Boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(Boolean autoRun) {
        this.autoRun = autoRun;
    }

    public Boolean getAllform() {
        return allform;
    }

    public void setAllform(Boolean allform) {
        allform = allform;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Boolean getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Boolean responseBody) {
        this.responseBody = responseBody;
    }


    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }
}
