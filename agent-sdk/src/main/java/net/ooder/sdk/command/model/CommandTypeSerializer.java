package net.ooder.sdk.command.model;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * CommandType枚举的序列化器
 */
public class CommandTypeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object instanceof CommandType) {
            CommandType commandType = (CommandType) object;
            serializer.out.writeString(commandType.getValue());
        } else {
            serializer.out.writeNull();
        }
    }
}
