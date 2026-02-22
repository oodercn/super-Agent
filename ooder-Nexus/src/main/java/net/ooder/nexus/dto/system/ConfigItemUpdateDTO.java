package net.ooder.nexus.dto.system;

import java.io.Serializable;

/**
 * Config item update request DTO
 */
public class ConfigItemUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Object value;

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
}
