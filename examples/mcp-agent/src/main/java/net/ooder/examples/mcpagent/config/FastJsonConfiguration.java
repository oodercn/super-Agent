package net.ooder.examples.mcpagent.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * FastJSON配置类
 * 使Spring Boot使用FastJSON作为默认JSON序列化器
 */
@Configuration
public class FastJsonConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 清除默认的消息转换器
        converters.clear();
        
        // 创建FastJSON消息转换器
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        
        // 配置FastJSON
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 设置序列化特性
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,  // 输出空值字段
                SerializerFeature.WriteNullStringAsEmpty,  // 字符串为null时输出""
                SerializerFeature.WriteNullNumberAsZero,  // 数值为null时输出0
                SerializerFeature.WriteNullBooleanAsFalse,  // 布尔值为null时输出false
                SerializerFeature.WriteNullListAsEmpty,  // 列表为null时输出[]
                SerializerFeature.DisableCircularReferenceDetect,  // 禁用循环引用检测
                SerializerFeature.PrettyFormat  // 格式化输出
        );
        
        // 设置支持的媒体类型
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
        
        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        
        // 添加到消息转换器列表
        converters.add(fastJsonHttpMessageConverter);
    }
}
