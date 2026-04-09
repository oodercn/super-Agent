package net.ooder.nexus.util;

import java.util.Map;

/**
 * HTTP 内容类型工具类，根据文件扩展名判断 Content-Type
 */
public final class ContentTypeUtils {

    private ContentTypeUtils() {}

    private static final Map<String, String> CONTENT_TYPE_MAP = Map.ofEntries(
        Map.entry("html", "text/html"),
        Map.entry("css", "text/css"),
        Map.entry("js", "application/javascript"),
        Map.entry("json", "application/json"),
        Map.entry("png", "image/png"),
        Map.entry("jpg", "image/jpeg"),
        Map.entry("jpeg", "image/jpeg"),
        Map.entry("gif", "image/gif"),
        Map.entry("svg", "image/svg+xml"),
        Map.entry("ico", "image/x-icon"),
        Map.entry("woff", "font/woff"),
        Map.entry("woff2", "font/woff2"),
        Map.entry("ttf", "font/ttf"),
        Map.entry("eot", "application/vnd.ms-fontobject"),
        Map.entry("map", "application/json"),
        Map.entry("webp", "image/webp")
    );

    /**
     * 根据文件路径获取 Content-Type
     */
    public static String getContentType(String path) {
        if (path == null) return "application/octet-stream";
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex < 0) return "application/octet-stream";
        String ext = path.substring(dotIndex + 1).toLowerCase();
        return CONTENT_TYPE_MAP.getOrDefault(ext, "application/octet-stream");
    }
}
