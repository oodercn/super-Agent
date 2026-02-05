package net.ooder.examples.skillb.model;

import java.io.Serializable;
import java.util.Map;

public class DataSubmissionRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private String script;
    private String jsonF;
    private Map<String, Object> data;

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataSubmissionRequest{" +
                "token='" + token + '\'' +
                ", script='" + script + '\'' +
                ", jsonF='" + jsonF + '\'' +
                ", data=" + data +
                '}';
    }
}
