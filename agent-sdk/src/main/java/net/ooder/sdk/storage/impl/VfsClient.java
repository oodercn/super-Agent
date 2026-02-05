package net.ooder.sdk.storage.impl;

import com.alibaba.fastjson.JSON;
import net.ooder.sdk.storage.VfsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * VFS客户端类，封装与VFS服务器的HTTP通信逻辑
 */
public class VfsClient {
    private static final Logger log = LoggerFactory.getLogger(VfsClient.class);
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CHARSET_UTF8 = StandardCharsets.UTF_8.name();
    private static final int DEFAULT_RETRY_DELAY = 1000; // 1秒

    private final VfsConfig config;

    public VfsClient(VfsConfig config) {
        this.config = config;
    }
    
    /**
     * 读取输入流内容
     * @param in 输入流
     * @return 字节数组
     * @throws IOException 读取异常
     */
    private byte[] readInputStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        return out.toByteArray();
    }

    /**
     * 检查VFS服务器是否可用
     * @return VFS服务器是否可用
     */
    public boolean isVfsAvailable() {
        if (!config.isEnableVfs()) {
            return false;
        }

        int retryCount = 0;
        while (retryCount <= config.getVfsRetryCount()) {
            try {
                URL url = new URL(config.getVfsServerUrl() + "/health");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout((int) config.getVfsConnectionTimeout());
                conn.setReadTimeout((int) config.getVfsReadTimeout());
                
                int responseCode = conn.getResponseCode();
                conn.disconnect();
                
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true;
                }
            } catch (IOException e) {
                log.warn("VFS server check failed, retry {}/{}: {}", retryCount, config.getVfsRetryCount(), e.getMessage());
            }
            
            retryCount++;
            if (retryCount <= config.getVfsRetryCount()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(DEFAULT_RETRY_DELAY * retryCount);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        log.error("VFS server is not available after {} retries", config.getVfsRetryCount());
        return false;
    }

    /**
     * 向VFS服务器发送GET请求
     * @param endpoint 请求端点
     * @return 响应内容
     * @throws IOException 通信异常
     */
    public String get(String endpoint) throws IOException {
        URL url = new URL(config.getVfsServerUrl() + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout((int) config.getVfsConnectionTimeout());
        conn.setReadTimeout((int) config.getVfsReadTimeout());
        
        try (InputStream in = conn.getInputStream()) {
            return new String(readInputStream(in), CHARSET_UTF8);
        } finally {
            conn.disconnect();
        }
    }

    /**
     * 向VFS服务器发送POST请求
     * @param endpoint 请求端点
     * @param data 请求数据
     * @return 响应内容
     * @throws IOException 通信异常
     */
    public String post(String endpoint, Object data) throws IOException {
        URL url = new URL(config.getVfsServerUrl() + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout((int) config.getVfsConnectionTimeout());
        conn.setReadTimeout((int) config.getVfsReadTimeout());
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", CONTENT_TYPE_JSON + "; charset=" + CHARSET_UTF8);
        
        try (OutputStream out = conn.getOutputStream()) {
            String jsonData = JSON.toJSONString(data);
            out.write(jsonData.getBytes(CHARSET_UTF8));
            out.flush();
        }
        
        try (InputStream in = conn.getInputStream()) {
            return new String(readInputStream(in), CHARSET_UTF8);
        } finally {
            conn.disconnect();
        }
    }

    /**
     * 向VFS服务器发送PUT请求
     * @param endpoint 请求端点
     * @param data 请求数据
     * @return 响应内容
     * @throws IOException 通信异常
     */
    public String put(String endpoint, Object data) throws IOException {
        URL url = new URL(config.getVfsServerUrl() + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setConnectTimeout((int) config.getVfsConnectionTimeout());
        conn.setReadTimeout((int) config.getVfsReadTimeout());
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", CONTENT_TYPE_JSON + "; charset=" + CHARSET_UTF8);
        
        try (OutputStream out = conn.getOutputStream()) {
            String jsonData = JSON.toJSONString(data);
            out.write(jsonData.getBytes(CHARSET_UTF8));
            out.flush();
        }
        
        try (InputStream in = conn.getInputStream()) {
            return new String(readInputStream(in), CHARSET_UTF8);
        } finally {
            conn.disconnect();
        }
    }

    /**
     * 向VFS服务器发送DELETE请求
     * @param endpoint 请求端点
     * @return 响应内容
     * @throws IOException 通信异常
     */
    public String delete(String endpoint) throws IOException {
        URL url = new URL(config.getVfsServerUrl() + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setConnectTimeout((int) config.getVfsConnectionTimeout());
        conn.setReadTimeout((int) config.getVfsReadTimeout());
        
        try (InputStream in = conn.getInputStream()) {
            return new String(readInputStream(in), CHARSET_UTF8);
        } finally {
            conn.disconnect();
        }
    }

    /**
     * 获取组相关的VFS端点路径
     * @param path 文件路径
     * @return 完整的组相关VFS端点
     */
    public String getGroupEndpoint(String path) {
        return "/group/" + config.getGroupName() + "/files" + (path.startsWith("/") ? "" : "/") + path;
    }
}