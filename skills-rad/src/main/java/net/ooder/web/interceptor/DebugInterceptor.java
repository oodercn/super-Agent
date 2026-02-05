package net.ooder.web.interceptor;

import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class DebugInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {

            String path = request.getParameter("tag");
            if (path == null) {
                path = request.getRequestURI();
            }
            String projectName = this.getProjectName(request);


            try {
                ProjectVersion projectVersion = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
                String className = StringUtility.replace(path, projectVersion.getPath(), "");
                if (className.endsWith(".js")) {
                    className = className.substring(0, className.length() - 3);
                }
                className = StringUtility.replace(className, "/", ".");
              //  className = className + ".jsx";
                JDSActionContext.getActionContext().getContext().put("className", className);
            } catch (JDSException e) {
                e.printStackTrace();
            }
            this.sendFtl(response, "resources/templates/jsa.ftl");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;


    }
}