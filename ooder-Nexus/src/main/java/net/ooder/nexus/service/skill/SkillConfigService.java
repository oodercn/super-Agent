package net.ooder.nexus.service.skill;

import net.ooder.nexus.domain.skill.model.DatabaseConnection;
import net.ooder.nexus.domain.skill.model.SkillConfig;

import java.util.List;
import java.util.Map;

public interface SkillConfigService {

    Map<String, Object> getConfigOverview();

    SkillConfig getSkillConfig(String skillId);

    SkillConfig updateSkillConfig(String skillId, Map<String, Object> config, boolean testConnection);

    Map<String, Object> testSkillConnection(String skillId, Map<String, Object> config);

    List<DatabaseConnection> getDatabaseConnections();

    DatabaseConnection getDatabaseConnection(String connectionId);

    DatabaseConnection createDatabaseConnection(DatabaseConnection connection);

    DatabaseConnection updateDatabaseConnection(DatabaseConnection connection);

    boolean deleteDatabaseConnection(String connectionId);

    Map<String, Object> testDatabaseConnection(DatabaseConnection connection);

    boolean enableSkill(String skillId);

    boolean disableSkill(String skillId);
}
