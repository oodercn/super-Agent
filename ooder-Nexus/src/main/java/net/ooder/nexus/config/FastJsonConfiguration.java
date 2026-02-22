package net.ooder.nexus.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * FastJSON 配置类
 *
 * <p>配置 Spring Boot 使用 FastJSON 作为默认的 JSON 序列化/反序列化工具</p>
 *
 * <p><strong>配置说明：</strong></p>
 * <ul>
 *   <li>禁用 Jackson，启用 FastJSON</li>
 *   <li>统一日期格式</li>
 *   <li>禁用循环引用检测（使用 $ref）</li>
 *   <li>格式化输出（开发环境）</li>
 * </ul>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@Configuration
public class FastJsonConfiguration implements WebMvcConfigurer {

    /**
     * 配置消息转换器
     *
     * <p>使用 FastJSON 替换默认的 Jackson 转换器</p>
     *
     * @param converters 转换器列表
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 移除默认的 Jackson 转换器
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);

        // 创建 FastJSON 转换器
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();

        // 配置 FastJSON
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);

        // 序列化特性
        fastJsonConfig.setSerializerFeatures(
            // 输出格式化（开发环境建议开启，生产环境建议关闭）
            SerializerFeature.PrettyFormat,
            // 禁用循环引用检测（避免 $ref 出现在输出中）
            SerializerFeature.DisableCircularReferenceDetect,
            // 输出空值字段
            SerializerFeature.WriteMapNullValue,
            // 空字符串输出为 ""
            SerializerFeature.WriteNullStringAsEmpty,
            // 空 List 输出为 []
            SerializerFeature.WriteNullListAsEmpty,
            // 使用 ISO8601 日期格式
            SerializerFeature.UseISO8601DateFormat,
            // 全局日期格式
            SerializerFeature.WriteDateUseDateFormat
        );

        // 设置日期格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

        // 应用配置
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);

        // 设置支持的 MediaType
        List<MediaType> supportedMediaTypes = new ArrayList<>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastJsonConverter.setSupportedMediaTypes(supportedMediaTypes);

        // 添加到转换器列表（添加到最前面，优先使用）
        converters.add(0, fastJsonConverter);
    }
}
