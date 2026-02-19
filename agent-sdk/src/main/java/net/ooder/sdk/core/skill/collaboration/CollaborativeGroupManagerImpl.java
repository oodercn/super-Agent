
package net.ooder.sdk.core.skill.collaboration;

import net.ooder.sdk.api.skill.CollaborativeGroupManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class CollaborativeGroupManagerImpl implements CollaborativeGroupManager {
    
    private static final Logger log = LoggerFactory.getLogger(CollaborativeGroupManagerImpl.class);
    
    private final Map<String, Map<String, Object>> groups = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> links = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);
    
    @Override
    public String createCollaborativeGroup(String sceneId, String groupId, Map<String, Object> config) {
        String id = groupId != null ? groupId : "group-" + idGenerator.incrementAndGet();
        
        Map<String, Object> group = new HashMap<>();
        group.put("groupId", id);
        group.put("sceneId", sceneId);
        group.put("config", config);
        group.put("members", new ArrayList<Map<String, Object>>());
        group.put("createTime", System.currentTimeMillis());
        
        groups.put(id, group);
        log.info("Created collaborative group: {} for scene: {}", id, sceneId);
        
        return id;
    }
    
    @Override
    public boolean removeCollaborativeGroup(String groupId) {
        Map<String, Object> removed = groups.remove(groupId);
        if (removed != null) {
            log.info("Removed collaborative group: {}", groupId);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addMember(String groupId, String skillId, String role) {
        Map<String, Object> group = groups.get(groupId);
        if (group == null) {
            log.warn("Group not found: {}", groupId);
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> members = (List<Map<String, Object>>) group.get("members");
        if (members == null) {
            members = new ArrayList<>();
            group.put("members", members);
        }
        
        Map<String, Object> member = new HashMap<>();
        member.put("skillId", skillId);
        member.put("role", role);
        member.put("joinTime", System.currentTimeMillis());
        
        members.add(member);
        log.info("Added member {} to group {} with role {}", skillId, groupId, role);
        
        return true;
    }
    
    @Override
    public boolean removeMember(String groupId, String skillId) {
        Map<String, Object> group = groups.get(groupId);
        if (group == null) {
            return false;
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> members = (List<Map<String, Object>>) group.get("members");
        if (members == null) {
            return false;
        }
        
        boolean removed = members.removeIf(m -> skillId.equals(m.get("skillId")));
        if (removed) {
            log.info("Removed member {} from group {}", skillId, groupId);
        }
        
        return removed;
    }
    
    @Override
    public String createLink(String fromSkillId, String toSkillId, String linkType) {
        String linkId = "link-" + idGenerator.incrementAndGet();
        
        Map<String, Object> link = new HashMap<>();
        link.put("linkId", linkId);
        link.put("fromSkillId", fromSkillId);
        link.put("toSkillId", toSkillId);
        link.put("linkType", linkType);
        link.put("createTime", System.currentTimeMillis());
        
        links.put(linkId, link);
        log.info("Created link: {} from {} to {} ({})", linkId, fromSkillId, toSkillId, linkType);
        
        return linkId;
    }
    
    @Override
    public boolean removeLink(String linkId) {
        Map<String, Object> removed = links.remove(linkId);
        if (removed != null) {
            log.info("Removed link: {}", linkId);
            return true;
        }
        return false;
    }
    
    @Override
    public Map<String, Object> getCollaborativeGroupInfo(String groupId) {
        return groups.get(groupId);
    }
    
    @Override
    public Map<String, Object> getLinkInfo(String linkId) {
        return links.get(linkId);
    }
    
    @Override
    public void syncToMain(String mainServiceId) {
        log.info("Syncing collaborative info to main: {}", mainServiceId);
    }
    
    @Override
    public void receiveFromMain(Map<String, Object> collaborativeInfo) {
        if (collaborativeInfo == null) {
            return;
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> receivedGroups = 
            (List<Map<String, Object>>) collaborativeInfo.get("groups");
        if (receivedGroups != null) {
            for (Map<String, Object> group : receivedGroups) {
                String groupId = (String) group.get("groupId");
                if (groupId != null) {
                    groups.put(groupId, group);
                }
            }
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> receivedLinks = 
            (List<Map<String, Object>>) collaborativeInfo.get("links");
        if (receivedLinks != null) {
            for (Map<String, Object> link : receivedLinks) {
                String linkId = (String) link.get("linkId");
                if (linkId != null) {
                    links.put(linkId, link);
                }
            }
        }
        
        log.info("Received collaborative info from main: {} groups, {} links", 
            receivedGroups != null ? receivedGroups.size() : 0,
            receivedLinks != null ? receivedLinks.size() : 0);
    }
    
    @Override
    public List<Map<String, Object>> getAllCollaborativeGroups() {
        return new ArrayList<>(groups.values());
    }
    
    @Override
    public List<Map<String, Object>> getAllLinks() {
        return new ArrayList<>(links.values());
    }
    
    public int getGroupCount() {
        return groups.size();
    }
    
    public int getLinkCount() {
        return links.size();
    }
}
