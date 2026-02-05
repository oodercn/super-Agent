package net.ooder.web.interceptor;

import net.ooder.context.JDSActionContext;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.Project;
import net.ooder.vfs.FileInfo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectConfigInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String projectName = this.getProjectName(request);
            JDSActionContext.getActionContext().getContext().put("projectName", projectName);
            JDSActionContext.getActionContext().getContext().put("domainId", projectName);
            List<FileInfo> cssFiles = new ArrayList<FileInfo>();
            JDSActionContext.getActionContext().getContext().put("cssList", cssFiles);
            Project project = ESDFacrory.getAdminESDClient().getProjectByName(projectName);
            this.sendFtl(response, "resources/templates/projectManager.ftl");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;


    }

}