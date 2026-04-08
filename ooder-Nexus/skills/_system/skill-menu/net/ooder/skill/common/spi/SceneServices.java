package net.ooder.skill.common.spi;

public interface SceneServices {
    
    UserService getUserService();
    
    StorageService getStorageService();
    
    ConfigService getConfigService();
    
    OrganizationService getOrganizationService();
    
    PermissionService getPermissionService();
    
    MessageService getMessageService();
    
    AuditService getAuditService();
    
    ImService getImService();
    
    OrgSyncService getOrgSyncService();
    
    PlatformBindService getPlatformBindService();
    
    TodoSyncService getTodoSyncService();
    
    CalendarService getCalendarService();
}
