package net.ooder.web.interceptor;

import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.vfs.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ViewJSAInterceptor extends BaseInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ViewJSAInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String url = request.getRequestURI();
            url = url.substring(0, url.length() - ".jsx".length());
            ProjectVersion projectVersion = ESDFacrory.getAdminESDClient().getProjectVersionByName(this.getProjectName(request));
            FileInfo fileInfo = ESDFacrory.getAdminESDClient().getFileByPath(url+".js", projectVersion.getVersionName());
            if (fileInfo != null) {
                String json = ESDFacrory.getAdminESDClient().readFileAsString(fileInfo.getPath(), projectVersion.getVersionName());
                sendJSON(response, json);
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}