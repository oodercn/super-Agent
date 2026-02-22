package net.ooder.nexus.dto.skill;

import java.io.Serializable;
import java.util.List;

/**
 * 技能运行请求DTO
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
public class SkillRunDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String input;
    private List<String> args;
    private String mode;
    private boolean debug;

    public SkillRunDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
