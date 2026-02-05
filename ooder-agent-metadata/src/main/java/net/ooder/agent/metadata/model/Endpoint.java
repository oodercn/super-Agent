package net.ooder.agent.metadata.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint模型
 * 服务的网络访问点
 */
public class Endpoint implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String url;
    private String protocol;
    private String method;
    private Map<String, String> headers;
    private Map<String, String> params;
    private Map<String, String> metadata;

    public Endpoint() {
        this.headers = new HashMap<>();
        this.params = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    public Endpoint(String id, String url, String protocol) {
        this();
        this.id = id;
        this.url = url;
        this.protocol = protocol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = new HashMap<>(headers);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Map<String, String> getParams() {
        return new HashMap<>(params);
    }

    public void setParams(Map<String, String> params) {
        this.params = new HashMap<>(params);
    }

    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

    public Map<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = new HashMap<>(metadata);
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
