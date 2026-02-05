package net.ooder.nexus.controller;

import net.ooder.nexus.model.Result;
import net.ooder.nexus.model.network.NetworkSetting;
import net.ooder.nexus.model.network.IPAddress;
import net.ooder.nexus.model.network.IPBlacklist;
import net.ooder.nexus.service.INexusService;
import net.ooder.nexus.service.NexusServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/network/config")
public class NetworkConfigController {

    private static final Logger log = LoggerFactory.getLogger(NetworkConfigController.class);

    @Autowired
    private NexusServiceFactory serviceFactory;

    public NetworkConfigController() {
        log.info("NetworkConfigController initialized");
    }

    private INexusService getService() {
        return serviceFactory.getService();
    }
    
    @GetMapping("/settings/{settingType}")
    public Result<NetworkSetting> getNetworkSetting(@PathVariable String settingType) {
        log.info("Get network setting requested: {}", settingType);
        return getService().getNetworkSetting(settingType);
    }

    @GetMapping("/settings")
    public Result<List<NetworkSetting>> getAllNetworkSettings() {
        log.info("Get all network settings requested");
        return getService().getAllNetworkSettings();
    }

    @PutMapping("/settings/{settingType}")
    public Result<NetworkSetting> updateNetworkSetting(
            @PathVariable String settingType, @RequestBody Map<String, Object> settingData) {
        log.info("Update network setting requested: {}, data: {}", settingType, settingData);
        return getService().updateNetworkSetting(settingType, settingData);
    }

    @GetMapping("/ip/addresses")
    public Result<List<IPAddress>> getIPAddresses(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        log.info("Get IP addresses requested: type={}, status={}", type, status);
        return getService().getIPAddresses(type, status);
    }

    @PostMapping("/ip/addresses")
    public Result<IPAddress> addStaticIPAddress(@RequestBody Map<String, Object> ipData) {
        log.info("Add static IP address requested: {}", ipData);
        return getService().addStaticIPAddress(ipData);
    }

    @DeleteMapping("/ip/addresses/{ipId}")
    public Result<IPAddress> deleteIPAddress(@PathVariable String ipId) {
        log.info("Delete IP address requested: {}", ipId);
        return getService().deleteIPAddress(ipId);
    }

    @GetMapping("/ip/blacklist")
    public Result<List<IPBlacklist>> getIPBlacklist() {
        log.info("Get IP blacklist requested");
        return getService().getIPBlacklist();
    }

    @PostMapping("/ip/blacklist")
    public Result<IPBlacklist> addIPToBlacklist(@RequestBody Map<String, Object> blacklistData) {
        log.info("Add IP to blacklist requested: {}", blacklistData);
        return getService().addIPToBlacklist(blacklistData);
    }

    @DeleteMapping("/ip/blacklist/{blacklistId}")
    public Result<IPBlacklist> removeIPFromBlacklist(@PathVariable String blacklistId) {
        log.info("Remove IP from blacklist requested: {}", blacklistId);
        return getService().removeIPFromBlacklist(blacklistId);
    }
}
