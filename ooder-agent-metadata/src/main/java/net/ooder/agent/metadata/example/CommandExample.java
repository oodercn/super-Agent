package net.ooder.agent.metadata.example;

import net.ooder.agent.metadata.enums.CommandTypeEnum;
import net.ooder.agent.metadata.model.Command;
import net.ooder.agent.metadata.model.McpPassthrough;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 命令体系使用示例
 * 展示如何使用Command模型实现组创建、场景创建和MCP透传功能
 */
public class CommandExample {

    public static void main(String[] args) {
        // 示例1: 创建场景命令
        Command createSceneCommand = createSceneCommand();
        System.out.println("创建场景命令: " + createSceneCommand);

        // 示例2: 创建组命令
        Command createGroupCommand = createGroupCommand();
        System.out.println("创建组命令: " + createGroupCommand);

        // 示例3: MCP透传命令 - SkillFlow访问Skill内部数据
        Command passthroughCommand = createPassthroughCommand();
        System.out.println("MCP透传命令: " + passthroughCommand);

        // 示例4: 添加组成员命令
        Command addMemberCommand = addGroupMemberCommand();
        System.out.println("添加组成员命令: " + addMemberCommand);
    }

    /**
     * 创建场景命令示例
     */
    public static Command createSceneCommand() {
        Command command = new Command(UUID.randomUUID().toString(), CommandTypeEnum.SCENE_CREATE.getCode());
        command.setType("request");
        command.setStatus("pending");

        // 设置场景参数
        Map<String, Object> params = new HashMap<>();
        params.put("scene_id", "SCENE_001");
        params.put("name", "数据上报场景");
        params.put("description", "用于设备数据上报的协作场景");
        params.put("type", "data-reporting");
        
        Map<String, Object> rules = new HashMap<>();
        rules.put("timeout", 30000);
        params.put("rules", rules);
        
        Map<String, Object> targets = new HashMap<>();
        targets.put("success_rate", 0.99);
        params.put("targets", targets);

        command.setParams(params);

        // 设置元数据
        command.getMetadata().setSenderId("mcp-agent-001");
        command.getMetadata().setTraceId(UUID.randomUUID().toString());
        command.getMetadata().setPriority("high");
        command.getMetadata().setTimeout(60000);

        return command;
    }

    /**
     * 创建组命令示例
     */
    public static Command createGroupCommand() {
        Command command = new Command(UUID.randomUUID().toString(), CommandTypeEnum.GROUP_CREATE.getCode());
        command.setType("request");
        command.setStatus("pending");

        // 设置组参数
        Map<String, Object> params = new HashMap<>();
        params.put("group_id", "GROUP_001");
        params.put("name", "数据采集组");
        params.put("description", "负责数据采集的Agent组");
        params.put("scene_id", "SCENE_001");
        params.put("type", "data-flow");

        command.setParams(params);

        // 设置元数据
        command.getMetadata().setSenderId("mcp-agent-001");
        command.getMetadata().setTraceId(UUID.randomUUID().toString());
        command.getMetadata().setPriority("medium");

        return command;
    }

    /**
     * MCP透传命令示例 - SkillFlow访问Skill内部数据
     */
    public static Command createPassthroughCommand() {
        Command command = new Command(UUID.randomUUID().toString(), CommandTypeEnum.MCP_PASSTHROUGH.getCode());
        command.setType("request");
        command.setStatus("pending");

        // 创建MCP透传数据
        McpPassthrough passthrough = new McpPassthrough();
        passthrough.setTargetType("skill");
        passthrough.setTargetId("skill-a-001");
        passthrough.setPassthroughCommand("internal.data.query");

        // 设置透传参数
        Map<String, Object> passthroughParams = new HashMap<>();
        passthroughParams.put("data_type", "sensor_data");
        passthroughParams.put("start_time", System.currentTimeMillis() - 3600000);
        passthroughParams.put("end_time", System.currentTimeMillis());
        passthroughParams.put("limit", 100);
        passthrough.setPassthroughParams(passthroughParams);

        // 设置透传上下文
        Map<String, Object> context = new HashMap<>();
        context.put("user_id", "user-001");
        context.put("permission_level", "admin");
        passthrough.setContext(context);

        // 设置透传元数据
        passthrough.getMetadata().setSkillflowId("skillflow-001");
        passthrough.getMetadata().setSkillflowNodeId("node-002");
        passthrough.getMetadata().setTimeout(30000);
        passthrough.getMetadata().setTraceId(UUID.randomUUID().toString());
        passthrough.getMetadata().setParentTraceId(UUID.randomUUID().toString());

        // 将透传数据设置到命令中
        command.setPassthrough(passthrough);

        // 设置元数据
        command.getMetadata().setSenderId("skillflow-engine-001");
        command.getMetadata().setTraceId(passthrough.getMetadata().getTraceId());
        command.getMetadata().setPriority("high");

        return command;
    }

    /**
     * 添加组成员命令示例
     */
    public static Command addGroupMemberCommand() {
        Command command = new Command(UUID.randomUUID().toString(), CommandTypeEnum.GROUP_ADD_MEMBER.getCode());
        command.setType("request");
        command.setStatus("pending");

        // 设置添加成员参数
        Map<String, Object> params = new HashMap<>();
        params.put("group_id", "GROUP_001");
        params.put("scene_id", "SCENE_001");
        params.put("agent_id", "end-agent-001");
        params.put("agent_role", "data_collector");

        command.setParams(params);

        // 设置元数据
        command.getMetadata().setSenderId("mcp-agent-001");
        command.getMetadata().setTraceId(UUID.randomUUID().toString());
        command.getMetadata().setPriority("medium");

        return command;
    }
}
