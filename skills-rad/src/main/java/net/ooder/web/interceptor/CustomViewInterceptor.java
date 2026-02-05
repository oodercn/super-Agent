package net.ooder.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.web.RequestMethodBean;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomViewInterceptor extends BaseInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            this.getPanentMethodConfig(request);
            MethodConfig methodConfig = this.getCurrMethodConfig(request);
            if (methodConfig != null) {
                ApiClassConfig apiConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(methodConfig.getSourceClassName());
                if (apiConfig != null) {
                    methodConfig = apiConfig.getMethodByName(methodConfig.getMethodName());
                }
                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.MethodBeanKey, methodConfig);
                RequestMethodBean methodBean = findMethodBean(request.getRequestURI());
                if (methodBean == null) {
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        methodBean = new RequestMethodBean(handlerMethod.getMethod());
                    } else if (methodConfig.getMethod() != null) {
                        methodBean = new RequestMethodBean(methodConfig.getMethod());
                    }
                }
                if (methodBean != null) {
                    Object object = invokMethod(methodBean, request, response);


                    if (methodBean.getResponseBody() != null) {
                        this.sendJSON(response, JSONObject.toJSONString(object));
                    } else {
                        this.sendJSON(response, object.toString());
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}