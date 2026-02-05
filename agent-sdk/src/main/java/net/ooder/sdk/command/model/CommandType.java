package net.ooder.sdk.command.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.commands.*;

import java.util.Arrays;
import java.util.Optional;

@JSONType()
public enum CommandType {
    // End Agent commands
    END_EXECUTE("end.execute", EndExecuteCommand.class),
    END_STATUS("end.status", EndStatusCommand.class),
    END_SET_CONFIG("end.set_config", EndSetConfigCommand.class),
    END_RESET("end.reset", EndResetCommand.class),
    END_UPGRADE("end.upgrade", EndUpgradeCommand.class),
    END_NETWORK_CONNECT("end.network.connect", null),
    END_NETWORK_DISCONNECT("end.network.disconnect", null),
    END_NETWORK_STATUS("end.network.status", null),
    
    // Skill Agent commands
    SKILL_SUBMIT("skill.submit", SkillSubmitCommand.class),
    SKILL_INVOKE("skill.invoke", SkillInvokeCommand.class),
    SKILL_STATUS("skill.status", SkillStatusCommand.class),
    SKILL_CONFIGURE("skill.configure", SkillConfigureCommand.class),
    SKILL_START("skill.start", SkillStartCommand.class),
    SKILL_STOP("skill.stop", SkillStopCommand.class),
    
    // Route Agent commands
    ROUTE_FORWARD("route.forward", RouteForwardCommand.class),
    ROUTE_STATUS("route.status", RouteStatusCommand.class),
    ROUTE_ADD("route.add", RouteAddCommand.class),
    ROUTE_REMOVE("route.remove", RouteRemoveCommand.class),
    ROUTE_LIST("route.list", RouteListCommand.class),
    ROUTE_CONFIGURE("route.configure", RouteConfigureCommand.class),
    ROUTE_AUTH_REQUEST("route.auth_request", RouteAuthRequestCommand.class),
    ROUTE_AUTH_RESPONSE("route.auth_response", RouteAuthResponseCommand.class),
    ROUTE_TASK_FORWARD("route.task_forward", RouteTaskForwardCommand.class),
    ROUTE_TASK_RESULT("route.task_result", RouteTaskResultCommand.class),
    ROUTE_ENDAGENT_REGISTER("route.endagent_register", RouteEndagentRegisterCommand.class),
    ROUTE_ENDAGENT_DEREGISTER("route.endagent_deregister", RouteEndagentDeregisterCommand.class),
    ROUTE_ENDAGENT_LIST("route.endagent_list", RouteEndagentListCommand.class),
    ROUTE_HEARTBEAT("route.heartbeat", RouteHeartbeatCommand.class),
    
    // MCP Agent commands
    MCP_REGISTER("mcp.register", McpRegisterCommand.class),
    MCP_DEREGISTER("mcp.deregister", McpDeregisterCommand.class),
    MCP_STATUS("mcp.status", McpStatusCommand.class),
    MCP_DISCOVER("mcp.discover", McpDiscoverCommand.class),
    MCP_HEARTBEAT("mcp.heartbeat", McpHeartbeatCommand.class),
    MCP_AUTHENTICATE("mcp.authenticate", McpAuthenticateCommand.class),
    MCP_AUTH_RESPONSE("mcp.auth_response", McpAuthResponseCommand.class),
    MCP_TASK_REQUEST("mcp.task_request", McpTaskRequestCommand.class),
    MCP_TASK_RESPONSE("mcp.task_response", McpTaskResponseCommand.class),
    MCP_ROUTE_QUERY("mcp.route_query", McpRouteQueryCommand.class),
    MCP_ROUTE_UPDATE("mcp.route_update", McpRouteUpdateCommand.class),
    MCP_ENDAGENT_DISCOVER("mcp.endagent_discover", McpEndagentDiscoverCommand.class),
    MCP_ENDAGENT_STATUS("mcp.endagent_status", McpEndagentStatusCommand.class),
    
    // Group commands
    GROUP_CREATE("group.create", GroupCreateCommand.class),
    GROUP_DELETE("group.delete", GroupDeleteCommand.class),
    GROUP_ADD_MEMBER("group.add_member", GroupAddMemberCommand.class),
    GROUP_REMOVE_MEMBER("group.remove_member", GroupRemoveMemberCommand.class),
    GROUP_LIST_MEMBERS("group.list_members", GroupListMembersCommand.class),
    GROUP_STATUS("group.status", GroupStatusCommand.class),
    
    // Scene commands
    SCENE_CREATE("scene.create", SceneCreateCommand.class),
    SCENE_DELETE("scene.delete", SceneDeleteCommand.class),
    SCENE_ACTIVATE("scene.activate", SceneActivateCommand.class),
    SCENE_DEACTIVATE("scene.deactivate", SceneDeactivateCommand.class),
    SCENE_STATUS("scene.status", SceneStatusCommand.class),
    
    // VFS commands
    VFS_REGISTER("vfs.register", VFSRegisterCommand.class),
    VFS_STATUS("vfs.status", VFSStatusCommand.class),
    VFS_SYNC("vfs.sync", null),
    
    // Command Response
    COMMAND_RESPONSE("command.response", CommandResponseCommand.class),
    
    // Terminal commands
    TERMINAL_DISCOVER("terminal.discover", TerminalDiscoverCommand.class),
    TERMINAL_REGISTER("terminal.register", null),
    TERMINAL_DEREGISTER("terminal.deregister", null),
    TERMINAL_STATUS("terminal.status", null),
    TERMINAL_LIST("terminal.list", null),
    TERMINAL_UPDATE("terminal.update", null),
    
    // Core commands
    GET_COMPONENTS("core.get_components", null),
    GENERATE_UI("core.generate_ui", null),
    GET_COMPONENT_DETAILS("core.get_component_details", null),
    CREATE_VIEW("core.create_view", null),
    SUBMIT_DATA("core.submit_data", null),
    EXECUTE_LLM_QUERY("core.execute_llm_query", null),
    REGISTER_END_AGENT("core.register_end_agent", null),
    BROADCAST_COMMAND("core.broadcast_command", null);

    private final String value;
    private final Class<? extends Command> commandClass;

    CommandType(String value, Class<? extends Command> commandClass) {
        this.value = value;
        this.commandClass = commandClass;
    }

    public String getValue() {
        return value;
    }

    public Class<? extends Command> getCommandClass() {
        return commandClass;
    }

    @Override
    public String toString() {
        return value;
    }
    
    /**
     * 确保序列化时使用正确的字符串值
     */
    @JSONField(serialize = false)
    public String getEnumName() {
        return name();
    }

    public static Optional<CommandType> fromValue(String value) {
        if (value == null) {
            return Optional.empty();
        }
        
        // 先尝试直接匹配枚举名称
        try {
            return Optional.of(CommandType.valueOf(value));
        } catch (IllegalArgumentException e) {
            // 如果失败，再尝试通过value匹配
            return Arrays.stream(values())
                    .filter(type -> type.value.equals(value))
                    .findFirst();
        }
    }
}