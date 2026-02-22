package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

/**
 * Admin Group Service Interface
 * Provides group management functions for admin module
 *
 * @author ooder Team
 * @version 0.7.0
 */
public interface AdminGroupService {

    List<Map<String, Object>> getAllGroups();

    Map<String, Object> getGroupById(String groupId);

    Map<String, Object> createGroup(Map<String, Object> groupData);

    Map<String, Object> updateGroup(String groupId, Map<String, Object> groupData);

    boolean deleteGroup(String groupId);

    List<Map<String, Object>> getGroupMembers(String groupId);

    boolean addMember(String groupId, String userId);

    boolean removeMember(String groupId, String userId);

    GroupStatistics getStatistics();

    public static class GroupStatistics {
        private int totalGroups;
        private int totalMembers;
        private int activeGroups;
        private int inactiveGroups;

        public int getTotalGroups() { return totalGroups; }
        public void setTotalGroups(int totalGroups) { this.totalGroups = totalGroups; }
        public int getTotalMembers() { return totalMembers; }
        public void setTotalMembers(int totalMembers) { this.totalMembers = totalMembers; }
        public int getActiveGroups() { return activeGroups; }
        public void setActiveGroups(int activeGroups) { this.activeGroups = activeGroups; }
        public int getInactiveGroups() { return inactiveGroups; }
        public void setInactiveGroups(int inactiveGroups) { this.inactiveGroups = inactiveGroups; }
    }
}
