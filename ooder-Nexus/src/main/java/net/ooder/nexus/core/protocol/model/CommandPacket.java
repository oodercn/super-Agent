package net.ooder.nexus.core.protocol.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Command Packet
 * Standard format for southbound protocol commands
 */
public class CommandPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    private CommandHeader header;
    private Map<String, Object> payload;
    private CommandSignature signature;

    public CommandPacket() {
    }

    public CommandPacket(CommandHeader header, Map<String, Object> payload) {
        this.header = header;
        this.payload = payload;
    }

    public CommandHeader getHeader() {
        return header;
    }

    public void setHeader(CommandHeader header) {
        this.header = header;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public CommandSignature getSignature() {
        return signature;
    }

    public void setSignature(CommandSignature signature) {
        this.signature = signature;
    }

    public String getProtocolType() {
        return header != null ? header.getProtocolType() : null;
    }

    public String getCommandType() {
        return header != null ? header.getCommandType() : null;
    }

    @Override
    public String toString() {
        return "CommandPacket{" +
                "header=" + header +
                ", payload=" + payload +
                ", signature=" + signature +
                '}';
    }
}
