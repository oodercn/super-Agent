package net.ooder.web.interceptor;

import net.ooder.context.JDSActionContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CustomClsInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String euClassName = request.getParameter("tag");
            if (euClassName == null) {
                euClassName = request.getRequestURI();
            }
            String projectName = this.getProjectName(request);

            String clear = request.getParameter("clear");
            if (clear != null) {
                this.clear(euClassName, projectName);
            }



            List<Map> cssList = new ArrayList<Map>();
            Map<String, String> values = new HashMap<>();
            cssList.add(values);
            JDSActionContext.getActionContext().getContext().put("projectName", projectName);
            JDSActionContext.getActionContext().getContext().put("cssList", cssList);
            JDSActionContext.getActionContext().getContext().put("className", euClassName);
            this.sendFtl(response, "resources/templates/cls.ftl");
            return false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;


    }

}