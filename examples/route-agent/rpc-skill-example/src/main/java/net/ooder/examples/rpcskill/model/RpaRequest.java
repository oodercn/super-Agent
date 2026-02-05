package net.ooder.examples.rpaskill.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * RPA Request Model
 * Contains parameters for RPA operation: token, url, script, jsonF
 * 作者：ooderAI agent team   V0.6.0
 */
public class RpaRequest {

    @JSONField(name = "token")
    private String token;

    @JSONField(name = "url")
    private String url;

    @JSONField(name = "script")
    private String script;

    @JSONField(name = "jsonF")
    private String jsonF;

    // Default constructor
    public RpaRequest() {
    }

    // Parameterized constructor
    public RpaRequest(String token, String url, String script, String jsonF) {
        this.token = token;
        this.url = url;
        this.script = script;
        this.jsonF = jsonF;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getJsonF() {
        return jsonF;
    }

    public void setJsonF(String jsonF) {
        this.jsonF = jsonF;
    }

    @Override
    public String toString() {
        return "RpaRequest{" +
                "token='" + token + '\'' +
                ", url='" + url + '\'' +
                ", script='" + script + '\'' +
                ", jsonF='" + jsonF + '\'' +
                '}';
    }
}
