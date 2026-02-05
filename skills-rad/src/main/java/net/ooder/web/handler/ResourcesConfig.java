package net.ooder.web.handler;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import net.ooder.web.interceptor.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ResourcesConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RADInterceptor()).addPathPatterns("/OOD");
        registry.addInterceptor(new DebugInterceptor()).addPathPatterns("/debug");
        registry.addInterceptor(new DesignerInterceptor()).addPathPatterns("/RAD/Designer");
        registry.addInterceptor(new RADJSXInterceptor()).addPathPatterns("/RAD/**/*.jsx");
        registry.addInterceptor(new OODJSXInterceptor()).addPathPatterns("/ood/**/*.jsx");
        registry.addInterceptor(new ViewJSAInterceptor()).addPathPatterns("/view/**/*.jsx");
        registry.addInterceptor(new CustomDynInterceptor()).addPathPatterns("/**/*.dyn");
        registry.addInterceptor(new CustomClsInterceptor()).addPathPatterns("/**/*.cls");
        registry.addInterceptor(new ModuleInterceptor()).addPathPatterns("/**/*.jsx");
        registry.addInterceptor(new CustomViewInterceptor()).addPathPatterns("/view/**");
        registry.addInterceptor(new CustomDataInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteDateUseDateFormat
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(0, fastConverter);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        super.addResourceHandlers(registry);
    }

}