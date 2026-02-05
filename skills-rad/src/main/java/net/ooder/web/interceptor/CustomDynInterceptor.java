package net.ooder.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import net.ooder.web.RequestMethodBean;
import ognl.OgnlException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomDynInterceptor extends BaseInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws OgnlException, ClassNotFoundException, IOException {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RequestMethodBean methodBean = findMethodBean(request.getRequestURI());
        if (methodBean == null) {
            methodBean = new RequestMethodBean(handlerMethod.getMethod());
        }
        Object object = invokMethod(methodBean, request, response);
        if (methodBean.getResponseBody() != null) {
            this.sendJSON(response, JSONObject.toJSONString(object));
        } else {
            this.sendJSON(response, object.toString());
        }
        return false;
    }


}

