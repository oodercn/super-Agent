package net.ooder.skillcenter.manager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 群组管理器，负责群组的管理
 */
public class GroupManager {
    // 单例实例
    private static GroupManager instance;
    
    // 群组映射，key为群组ID，value为群组实例
    private Map<String, Group> groupMap;

    /**
     * 私有构造方法
     */
    private GroupManager() {
        this.groupMap = new HashMap<>();
        this.loadSampleData();
    }

    /**
     * 获取实例
     * @return 群组管理器实例
     */
    public static synchronized GroupManager getInstance() {
        if (instance == null) {
            instance = new GroupManager();
        }
        return instance;
    }

    /**
     * 创建新的群组
     * @param group 群组实例
     * @return 创建的群组实例
     */
    public synchronized Group createGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }

        // 生成群组ID
        if (group.getId() == null || group.getId().isEmpty()) {
            group.setId("group-" + UUID.randomUUID().toString().substring(0, 8));
        }

        // 设置创建时间
        if (group.getCreatedAt() == null) {
            group.setCreatedAt(LocalDateTime.now());
        }

        // 初始化成员和技能列表
        if (group.getMembers() == null) {
            group.setMembers(new ArrayList<>());
        }
        if (group.getSkills() == null) {
            group.setSkills(new ArrayList<>());
        }

        // 更新成员数量
        group.setMemberCount(group.getMembers().size());

        // 添加到群组映射
        groupMap.put(group.getId(), group);
        return group;
    }

    /**
     * 获取指定ID的群组
     * @param id 群组ID
     * @return 群组实例，不存在则返回null
     */
    public Group getGroup(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }
        return groupMap.get(id);
    }

    /**
     * 获取所有群组
     * @return 群组列表
     */
    public List<Group> getAllGroups() {
        return new ArrayList<>(groupMap.values());
    }

    /**
     * 更新群组信息
     * @param group 群组实例
     * @return 更新后的群组实例
     */
    public synchronized Group updateGroup(Group group) {
        if (group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }

        if (group.getId() == null || group.getId().isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }

        if (!groupMap.containsKey(group.getId())) {
            throw new IllegalArgumentException("Group not found: " + group.getId());
        }

        // 更新成员数量
        if (group.getMembers() != null) {
            group.setMemberCount(group.getMembers().size());
        }

        // 更新群组
        groupMap.put(group.getId(), group);
        return group;
    }

    /**
     * 删除群组
     * @param id 群组ID
     * @return 删除是否成功
     */
    public synchronized boolean deleteGroup(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }

        return groupMap.remove(id) != null;
    }

    /**
     * 搜索群组
     * @param keyword 关键词
     * @return 匹配的群组列表
     */
    public List<Group> searchGroups(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return getAllGroups();
        }

        List<Group> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();

        for (Group group : groupMap.values()) {
            if (group.getName().toLowerCase().contains(lowerKeyword) || 
                group.getId().toLowerCase().contains(lowerKeyword) ||
                (group.getDescription() != null && group.getDescription().toLowerCase().contains(lowerKeyword))) {
                result.add(group);
            }
        }

        return result;
    }

    /**
     * 加载示例数据
     */
    private void loadSampleData() {
        // 创建示例群组数据
        Group group1 = new Group();
        group1.setId("group-001");
        group1.setName("Development Team");
        group1.setDescription("Development team for skill center");
        group1.setMembers(new ArrayList<String>() {{ add("User123"); add("User456"); add("User789"); add("User101"); add("User202"); }});
        group1.setSkills(new ArrayList<String>() {{ add("text-to-uppercase-skill"); add("code-generation-skill"); add("local-deployment-skill"); }});
        group1.setCreatedAt(LocalDateTime.now().minusDays(10));
        group1.setMemberCount(group1.getMembers().size());
        groupMap.put(group1.getId(), group1);

        Group group2 = new Group();
        group2.setId("group-002");
        group2.setName("Design Team");
        group2.setDescription("Design team for skill center");
        group2.setMembers(new ArrayList<String>() {{ add("User303"); add("User404"); add("User505"); }});
        group2.setSkills(new ArrayList<String>() {{ add("image-resizer-skill"); }});
        group2.setCreatedAt(LocalDateTime.now().minusDays(5));
        group2.setMemberCount(group2.getMembers().size());
        groupMap.put(group2.getId(), group2);

        Group group3 = new Group();
        group3.setId("group-003");
        group3.setName("Marketing Team");
        group3.setDescription("Marketing team for skill center");
        group3.setMembers(new ArrayList<String>() {{ add("User606"); add("User707"); add("User808"); add("User909"); }});
        group3.setSkills(new ArrayList<String>());
        group3.setCreatedAt(LocalDateTime.now().minusDays(3));
        group3.setMemberCount(group3.getMembers().size());
        groupMap.put(group3.getId(), group3);

        System.out.println("Loaded " + groupMap.size() + " sample groups");
    }

    /**
     * 群组类
     */
    public static class Group {
        private String id;
        private String name;
        private String description;
        private int memberCount;
        private LocalDateTime createdAt;
        private List<String> members;
        private List<String> skills;

        // getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public List<String> getMembers() {
            return members;
        }

        public void setMembers(List<String> members) {
            this.members = members;
        }

        public List<String> getSkills() {
            return skills;
        }

        public void setSkills(List<String> skills) {
            this.skills = skills;
        }
    }
}
