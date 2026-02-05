package net.ooder.examples.rpaskill.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RPA Configuration
 * Reads default external RPA information from application.yml
 * 作者：ooderAI agent team   V0.6.0
 */
@Configuration
@ConfigurationProperties(prefix = "rpa.default")
public class RpaConfig {

    private String token;
    private String url;
    private String script;
    private String jsonF;

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
}
