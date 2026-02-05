package net.ooder.web.interceptor;

import net.ooder.common.util.IOUtility;
import net.ooder.config.JDSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;

@Component
public class RADJSXInterceptor extends BaseInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RADJSXInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String url = request.getRequestURI();
            url = url.substring(0, url.length() - ".jsx".length());
            File file = new File(JDSUtil.getJdsRealPath() + "static" + File.separator + url + ".js");
            if (file.exists() && file.length() > 0) {
                String json = IOUtility.toString(new FileInputStream(file));
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