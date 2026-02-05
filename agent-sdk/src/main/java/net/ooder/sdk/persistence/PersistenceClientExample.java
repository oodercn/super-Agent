package net.ooder.sdk.persistence;

import java.util.List;

/**
 * 持久化客户端使用示例
 */
public class PersistenceClientExample {
    public static void main(String[] args) {
        // 获取持久化客户端实例
        PersistenceClient client = PersistenceClient.getInstance();
        
        // 示例1: 使用MCP客户端查询所有路由代理及其技能
        System.out.println("=== Example 1: MCP Client ===");
        McpPersistenceClient mcpClient = client.asMcpClient();
        
        // 获取所有路由代理及其技能
        System.out.println("Getting all route agents and their skills...");
        List<RouteAgentTree> routeAgentTrees = mcpClient.getRouteAgentsAndSkills();
        routeAgentTrees.forEach(routeAgent -> {
            System.out.println("Route Agent: " + routeAgent.getAgentName() + " (ID: " + routeAgent.getAgentId() + ")");
            routeAgent.getSkills().forEach(skill -> {
                System.out.println("  - Skill: " + skill.getSkillName() + " (ID: " + skill.getSkillId() + ")");
            });
        });
        
        // 获取特定路由代理的技能树
        System.out.println("\nGetting skill tree for route agent-1...");
        SkillTreeNode skillTree = mcpClient.getSkillTree("route-1");
        printSkillTree(skillTree, 0);
        
        // 示例2: 使用路由客户端管理路由代理资源
        System.out.println("\n=== Example 2: Route Client ===");
        RoutePersistenceClient routeClient = client.asRouteClient("route-1");
        
        // 获取路由代理的技能树
        System.out.println("Getting skill tree for route-1...");
        SkillTreeNode routeSkillTree = routeClient.getSkillTree();
        printSkillTree(routeSkillTree, 0);
        
        // 获取所有技能状态
        System.out.println("\nGetting all skill statuses...");
        List<SkillStatusInfo> skillStatuses = routeClient.getAllSkillStatuses();
        skillStatuses.forEach(status -> {
            System.out.println("Skill: " + status.getSkillId() + " - Status: " + status.getStatus());
        });
        
        // 示例3: 使用终端客户端管理终端代理资源
        System.out.println("\n=== Example 3: End Client ===");
        EndPersistenceClient endClient = client.asEndClient("end-1");
        
        // 保存技能状态
        System.out.println("Saving skill status...");
        boolean saved = endClient.saveSkillStatus("skill-1", net.ooder.sdk.skill.SkillStatus.READY);
        System.out.println("Skill status saved: " + saved);
        
        // 加载技能状态
        System.out.println("Loading skill status...");
        net.ooder.sdk.skill.SkillStatus loadedStatus = endClient.loadSkillStatus("skill-1");
        System.out.println("Skill status loaded: " + loadedStatus);
    }
    
    /**
     * 打印技能树
     * @param node 技能树节点
     * @param indent 缩进级别
     */
    private static void printSkillTree(SkillTreeNode node, int indent) {
        String prefix = "";
        for (int i = 0; i < indent; i++) {
            prefix += "  ";
        }
        System.out.println(prefix + "- Skill: " + node.getSkillName() + " (ID: " + node.getSkillId() + ")");
        
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            node.getChildren().forEach(child -> printSkillTree(child, indent + 1));
        }
    }
}
