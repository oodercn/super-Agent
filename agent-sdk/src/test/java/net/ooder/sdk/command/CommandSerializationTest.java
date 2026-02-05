package net.ooder.sdk.command;

import com.alibaba.fastjson.JSON;
import net.ooder.sdk.command.api.Command;
import net.ooder.sdk.command.commands.EndExecuteCommand;
import net.ooder.sdk.command.commands.SkillInvokeCommand;
import net.ooder.sdk.command.factory.CommandTaskFactory;
import net.ooder.sdk.command.model.CommandType;
import net.ooder.sdk.network.packet.CommandPacket;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 命令序列化和反序列化测试
 */
public class CommandSerializationTest {

    @Test
    public void testCommandSerialization() {
        // 创建命令对象
        EndExecuteCommand command = new EndExecuteCommand();
        command.setCommand("echo");
        Map<String, Object> args = new HashMap<>();
        args.put("message", "Hello World");
        command.setArgs(args);
        command.setSenderId("sender-123");
        command.setReceiverId("receiver-456");

        // 序列化命令对象为JSON
        String json = JSON.toJSONString(command);
        System.out.println("Serialized Command JSON: " + json);

        // 验证JSON包含必要的字段
        assertNotNull(json);
        assertTrue(json.contains("commandType"));
        assertTrue(json.contains("commandId"));
        assertTrue(json.contains("commandId"));
        assertTrue(json.contains("senderId"));
        assertTrue(json.contains("receiverId"));
        assertTrue(json.contains("command"));
        assertTrue(json.contains("args"));

        // 反序列化JSON为命令对象
        EndExecuteCommand deserializedCommand = JSON.parseObject(json, EndExecuteCommand.class);

        // 验证反序列化结果
        assertNotNull(deserializedCommand);
        assertEquals(command.getCommandId(), deserializedCommand.getCommandId());
        assertEquals(command.getCommandType(), deserializedCommand.getCommandType());
        assertEquals(command.getCommand(), deserializedCommand.getCommand());
        assertEquals(command.getArgs(), deserializedCommand.getArgs());
        assertEquals(command.getSenderId(), deserializedCommand.getSenderId());
        assertEquals(command.getReceiverId(), deserializedCommand.getReceiverId());
    }

    @Test
    public void testCommandPacketSerialization() {
        // 创建命令参数
        EndExecuteCommand commandParams = new EndExecuteCommand();
        commandParams.setCommand("ls");
        commandParams.setArgs(new HashMap<>());

        // 创建CommandPacket
        CommandPacket<EndExecuteCommand> packet = CommandPacket.<EndExecuteCommand>builder()
                .operation(CommandType.END_EXECUTE.getValue())
                .payload(commandParams)
                .build();

        // 序列化CommandPacket为JSON
        String json = JSON.toJSONString(packet);
        System.out.println("Serialized CommandPacket JSON: " + json);

        // 验证JSON包含必要的字段
        assertNotNull(json);
        assertTrue(json.contains("operation"));
        assertTrue(json.contains("payload"));

        // 反序列化JSON为CommandPacket
        CommandPacket<EndExecuteCommand> deserializedPacket = JSON.parseObject(json, new com.alibaba.fastjson.TypeReference<CommandPacket<EndExecuteCommand>>() {});

        // 验证反序列化结果
        assertNotNull(deserializedPacket);
        assertEquals(packet.getOperation(), deserializedPacket.getOperation());
        assertNotNull(deserializedPacket.getPayload());
        assertEquals(commandParams.getCommand(), deserializedPacket.getPayload().getCommand());
        assertEquals(commandParams.getArgs(), deserializedPacket.getPayload().getArgs());
    }

    @Test
    public void testCommandTaskFactoryCreateFromJson() throws Exception {
        // 创建命令JSON
        String json = "{\"commandType\":\"end.execute\",\"commandId\":\"test-123\",\"timestamp\":1620000000000,\"senderId\":\"sender-123\",\"receiverId\":\"receiver-456\",\"command\":\"echo\",\"args\":{\"message\":\"Hello\"}}";

        // 使用CommandTaskFactory创建命令对象
        CommandTaskFactory factory = CommandTaskFactory.getInstance();
        Command command = factory.createCommandFromJson(json);

        // 验证命令对象
        assertNotNull(command);
        assertEquals(CommandType.END_EXECUTE, command.getCommandType());
        assertEquals("test-123", command.getCommandId());
        assertEquals("sender-123", command.getSenderId());
        assertEquals("receiver-456", command.getReceiverId());

        // 验证具体命令类型
        assertTrue(command instanceof EndExecuteCommand);
        EndExecuteCommand endExecuteCommand = (EndExecuteCommand) command;
        assertEquals("echo", endExecuteCommand.getCommand());
        assertEquals("Hello", endExecuteCommand.getArgs().get("message"));
    }

    @Test
    public void testSkillInvokeCommandSerialization() {
        // 创建SkillInvokeCommand
        SkillInvokeCommand command = new SkillInvokeCommand();
        command.setSkillId("skill-789");
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        command.setParams(params);

        // 序列化命令对象为JSON
        String json = JSON.toJSONString(command);
        System.out.println("Serialized SkillInvokeCommand JSON: " + json);

        // 验证JSON包含必要的字段
        assertNotNull(json);
        assertTrue(json.contains("commandType"));
        assertTrue(json.contains("skillId"));
        assertTrue(json.contains("params"));

        // 反序列化JSON为命令对象
        SkillInvokeCommand deserializedCommand = JSON.parseObject(json, SkillInvokeCommand.class);

        // 验证反序列化结果
        assertNotNull(deserializedCommand);
        assertEquals(command.getCommandId(), deserializedCommand.getCommandId());
        assertEquals(command.getCommandType(), deserializedCommand.getCommandType());
        assertEquals(command.getSkillId(), deserializedCommand.getSkillId());
        assertEquals(command.getParams(), deserializedCommand.getParams());
    }
}
