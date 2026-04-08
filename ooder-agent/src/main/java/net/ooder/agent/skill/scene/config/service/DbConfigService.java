package net.ooder.mvp.skill.scene.config.service;

import net.ooder.mvp.skill.scene.dto.db.DbConnectionDTO;
import net.ooder.mvp.skill.scene.dto.db.DbPoolConfigDTO;
import net.ooder.mvp.skill.scene.dto.db.DbTestRequestDTO;
import net.ooder.mvp.skill.scene.dto.db.DbMonitorDTO;

import java.util.List;

public interface DbConfigService {
    
    List<DbConnectionDTO> listConnections();
    
    DbConnectionDTO getConnection(String id);
    
    DbConnectionDTO saveConnection(DbConnectionDTO connection);
    
    void deleteConnection(String id);
    
    boolean testConnection(DbTestRequestDTO request);
    
    DbPoolConfigDTO getPoolConfig();
    
    void savePoolConfig(DbPoolConfigDTO config);
    
    DbMonitorDTO getMonitor();
}
