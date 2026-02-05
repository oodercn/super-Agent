package net.ooder.examples.skilla.model;

import java.io.Serializable;

public class InformationRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private String script;
    private String jsonF;

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

    @Override
    public String toString() {
        return "InformationRequest{" +
                "token='" + token + '\'' +
                ", script='" + script + '\'' +
                ", jsonF='" + jsonF + '\'' +
                '}';
    }
}
