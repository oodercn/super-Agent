package net.ooder.web.interceptor;

import com.alibaba.fastjson.JSONArray;
import net.ooder.annotation.UserSpace;
import net.ooder.context.JDSActionContext;
import net.ooder.editor.toolbox.ToolBox;
import net.ooder.editor.toolbox.widgets.WidgetCatBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.vfs.FileInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class RADInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {

            String projectName = this.getProjectName(request);
            JDSActionContext.getActionContext().getContext().put("domainId", DSMFactory.getInstance().getDefaultDomain(projectName, UserSpace.VIEW).getDomainId());
            List<FileInfo> cssFiles = new ArrayList<FileInfo>();
            JDSActionContext.getActionContext().getContext().put("cssList", cssFiles);

            List<WidgetCatBean> widgetConfigBeans = new ArrayList<>();
            for (ToolBox.ToolBoxEnum toolBoxEnum : ToolBox.ToolBoxEnum.values()) {
                widgetConfigBeans.add(new WidgetCatBean(toolBoxEnum));
            }
            JDSActionContext.getActionContext().getContext().put("widgets", JSONArray.toJSONString(widgetConfigBeans, true));

            this.sendFtl(response, "resources/templates/RAD.ftl");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;


    }

}