package net.ooder.editor.console.ddd.esdclass;


import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.dsm.aggregation.api.method.APIMethodBaseGridView;
import net.ooder.dsm.aggregation.config.entity.info.AggEntityConfigView;
import net.ooder.dsm.aggregation.config.entity.info.AggEntityRefGridView;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.UIAnnotation;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/rad/ddd/esdclass/ref/")
@Controller
@ButtonViewsAnnotation()
@MethodChinaName(cname = "实体信息", imageClass = "ri-cursor-line")
public class DddEntityMetaView {
    @CustomAnnotation(uid = true, hidden = true)
    private String sourceClassName;
    @CustomAnnotation(pid = true, hidden = true)
    private String projectId;
    @CustomAnnotation(pid = true, hidden = true)
    private String domainId;

    @CustomAnnotation(pid = true, hidden = true)
    private String sourceMethodName;


    @RequestMapping(method = RequestMethod.POST, value = "AggEntityConfig")
    @ModuleAnnotation(caption = "视图", imageClass = "ri-layout-line")
    @UIAnnotation(dock = Dock.fill)
    @CustomAnnotation(index = 1)
    @GridViewAnnotation
    @ResponseBody
    public ListResultModel<List<AggEntityConfigView>> getAggEntity(String sourceClassName, String projectName, String sourceMethodName, String domainId) {
        ListResultModel<List<AggEntityConfigView>> result = new ListResultModel<List<AggEntityConfigView>>();
        List<Object> customViews = new ArrayList<>();
        try {
            ApiClassConfig customESDClassAPIBean = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            List<MethodConfig> customMethods = customESDClassAPIBean.getAllViewMethods();
            for (MethodConfig methodAPIBean : customMethods) {
                if (methodAPIBean.getView() != null) {
                    customViews.add(methodAPIBean.getView());
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        result = PageUtil.getDefaultPageList(customViews, AggEntityConfigView.class);
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "AttMethod")
    @ModuleAnnotation(caption = "领域事件", imageClass = "ri-layout-line")
    @CustomAnnotation(index = 2)
    @UIAnnotation(dock = Dock.fill)
    @GridViewAnnotation
    @ResponseBody
    public ListResultModel<List<APIMethodBaseGridView>> getAttMethod(String sourceClassName, String projectName, String sourceMethodName, String domainId) {
        ListResultModel<List<APIMethodBaseGridView>> result = new ListResultModel<List<APIMethodBaseGridView>>();
        List<MethodConfig> attMethods = new ArrayList<>();
        try {
            AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(sourceClassName, false);
            for (MethodConfig apiBean : aggEntityConfig.getAllMethods()) {
                if (!apiBean.isModule() && apiBean.getApi() != null) {
                    attMethods.add(apiBean);
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        result = PageUtil.getDefaultPageList(attMethods, APIMethodBaseGridView.class);

        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "EsdClassRefs")
    @GridViewAnnotation
    @ModuleAnnotation(imageClass = "ri-cursor-line", caption = "实体关系")
    @CustomAnnotation(index = 3)
    @ResponseBody
    public ListResultModel<List<AggEntityRefGridView>> getEsdClassRefs(String sourceClassName, String projectName, String sourceMethodName, String domainId) {
        ListResultModel<List<AggEntityRefGridView>> tables = new ListResultModel();
        try {
            DSMFactory factory = DSMFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace());
            List<AggEntityRef> dsmRefs = factory.getAggregationManager().getEntityRefByName(sourceClassName, domainId, projectName);
            tables = PageUtil.getDefaultPageList(dsmRefs, AggEntityRefGridView.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return tables;
    }


    public String getSourceMethodName() {
        return sourceMethodName;
    }


    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

}
