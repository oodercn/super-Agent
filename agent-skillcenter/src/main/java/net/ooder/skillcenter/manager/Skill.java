package net.ooder.skillcenter.manager;

import java.util.Map;

/**
 * Skill interface for defining skill behavior
 */
public interface Skill {

    /**
     * Get the id of the skill
     * @return the skill id
     */
    String getSkillId();

    /**
     * Get the name of the skill
     * @return the skill name
     */
    String getSkillName();

    /**
     * Get the description of the skill
     * @return the skill description
     */
    String getSkillDescription();

    /**
     * Execute the skill with the given parameters
     * @param params the parameters for the skill execution
     * @return the result of the skill execution
     */
    Map<String, Object> execute(Map<String, Object> params);

    /**
     * Get the parameters supported by the skill
     * @return a map of parameter names to their descriptions
     */
    Map<String, String> getSupportedParameters();
}
