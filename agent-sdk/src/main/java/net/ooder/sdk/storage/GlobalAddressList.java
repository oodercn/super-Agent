
package net.ooder.sdk.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalAddressList {
    private static final GlobalAddressList instance = new GlobalAddressList();
    private final Map<String, AddressInfo> addressMap;
    private final Map<String, String> commandIdToAddressMap;
    private final Map<String, String> endAgentToAddressMap;
    private final Map<String, String> sceneToAddressMap;

    private GlobalAddressList() {
        this.addressMap = new ConcurrentHashMap<>();
        this.commandIdToAddressMap = new ConcurrentHashMap<>();
        this.endAgentToAddressMap = new ConcurrentHashMap<>();
        this.sceneToAddressMap = new ConcurrentHashMap<>();
    }

    public static GlobalAddressList getInstance() {
        return instance;
    }

    public void registerAddress(String path, AddressType type, String description) {
        AddressInfo info = new AddressInfo(path, type, description);
        addressMap.put(path, info);
    }

    public void registerCommandAddress(String commandId, String address) {
        commandIdToAddressMap.put(commandId, address);
        registerAddress(address, AddressType.RESULT, "Command result for: " + commandId);
    }

    public void registerEndAgentAddress(String endAgentId, String address) {
        endAgentToAddressMap.put(endAgentId, address);
        registerAddress(address, AddressType.TASK, "EndAgent task area for: " + endAgentId);
    }

    public void registerSceneAddress(String sceneId, String address) {
        sceneToAddressMap.put(sceneId, address);
        registerAddress(address, AddressType.CONFIG, "Scene configuration for: " + sceneId);
    }

    public String getAddressByCommandId(String commandId) {
        return commandIdToAddressMap.get(commandId);
    }

    public String getAddressByEndAgentId(String endAgentId) {
        return endAgentToAddressMap.get(endAgentId);
    }

    public String getAddressBySceneId(String sceneId) {
        return sceneToAddressMap.get(sceneId);
    }

    public AddressInfo getAddressInfo(String path) {
        return addressMap.get(path);
    }

    public boolean containsAddress(String path) {
        return addressMap.containsKey(path);
    }

    public void removeAddress(String path) {
        addressMap.remove(path);
    }

    public void removeCommandAddress(String commandId) {
        String address = commandIdToAddressMap.remove(commandId);
        if (address != null) {
            addressMap.remove(address);
        }
    }

    public void removeEndAgentAddress(String endAgentId) {
        String address = endAgentToAddressMap.remove(endAgentId);
        if (address != null) {
            addressMap.remove(address);
        }
    }

    public void removeSceneAddress(String sceneId) {
        String address = sceneToAddressMap.remove(sceneId);
        if (address != null) {
            addressMap.remove(address);
        }
    }

    public int getAddressCount() {
        return addressMap.size();
    }

    public void clear() {
        addressMap.clear();
        commandIdToAddressMap.clear();
        endAgentToAddressMap.clear();
        sceneToAddressMap.clear();
    }

    public enum AddressType {
        TASK("task"),
        RESULT("result"),
        CONFIG("config"),
        LOG("log");

        private final String value;

        AddressType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public static class AddressInfo {
        private final String path;
        private final AddressType type;
        private final String description;
        private final long registeredTime;

        public AddressInfo(String path, AddressType type, String description) {
            this.path = path;
            this.type = type;
            this.description = description;
            this.registeredTime = System.currentTimeMillis();
        }

        public String getPath() {
            return path;
        }

        public AddressType getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public long getRegisteredTime() {
            return registeredTime;
        }
    }
}

