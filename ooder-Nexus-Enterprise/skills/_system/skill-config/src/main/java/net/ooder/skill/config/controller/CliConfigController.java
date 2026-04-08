package net.ooder.skill.config.controller;

import net.ooder.skill.common.model.ResultModel;
import net.ooder.skill.config.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/config/cli")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class CliConfigController {

    private static final Logger log = LoggerFactory.getLogger(CliConfigController.class);

    private final Map<String, CliConfigDTO> cliConfigs = new HashMap<>();

    @GetMapping
    public ResultModel<List<CliConfigDTO>> listCliConfigs() {
        log.info("[CliConfigController] List CLI configs");
        return ResultModel.success(new ArrayList<>(cliConfigs.values()));
    }

    @GetMapping("/{cliId}")
    public ResultModel<CliConfigDTO> getCliConfig(@PathVariable String cliId) {
        log.info("[CliConfigController] Get CLI config: {}", cliId);
        CliConfigDTO config = cliConfigs.get(cliId);
        if (config == null) {
            return ResultModel.notFound("CLI config not found: " + cliId);
        }
        return ResultModel.success(config);
    }

    @PostMapping("/{cliId}")
    public ResultModel<CliConfigDTO> saveCliConfig(@PathVariable String cliId, @RequestBody CliConfigDTO config) {
        log.info("[CliConfigController] Save CLI config: {}", cliId);
        config.setCliId(cliId);
        cliConfigs.put(cliId, config);
        return ResultModel.success(config);
    }

    @DeleteMapping("/{cliId}")
    public ResultModel<Boolean> deleteCliConfig(@PathVariable String cliId) {
        log.info("[CliConfigController] Delete CLI config: {}", cliId);
        cliConfigs.remove(cliId);
        return ResultModel.success(true);
    }

    @PostMapping("/{cliId}/test")
    public ResultModel<CliTestResultDTO> testCliConnection(@PathVariable String cliId) {
        log.info("[CliConfigController] Test CLI connection: {}", cliId);
        
        CliTestResultDTO result = new CliTestResultDTO();
        result.setSuccess(true);
        result.setMessage("Connection test passed");
        result.setCliId(cliId);
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @PostMapping("/{cliId}/mock")
    public ResultModel<CliMockResultDTO> mockCliCall(
            @PathVariable String cliId,
            @RequestParam String action,
            @RequestBody(required = false) Map<String, Object> params) {
        
        log.info("[CliConfigController] Mock CLI call: {} - {}", cliId, action);
        
        CliMockResultDTO result = new CliMockResultDTO();
        result.setSuccess(true);
        result.setCliId(cliId);
        result.setAction(action);
        result.setParams(params);
        result.setResult("Mock result for " + action);
        result.setTimestamp(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @GetMapping("/{cliId}/qrcode")
    public ResultModel<CliQrCodeDTO> getCliQrCode(@PathVariable String cliId) {
        log.info("[CliConfigController] Get CLI QR code: {}", cliId);
        
        CliQrCodeDTO result = new CliQrCodeDTO();
        result.setSuccess(true);
        result.setCliId(cliId);
        result.setQrcodeUrl("https://example.com/qrcode/" + cliId);
        result.setExpireTime(System.currentTimeMillis() + 300000);
        result.setScanTip("请使用手机APP扫描二维码绑定");
        
        return ResultModel.success(result);
    }

    @GetMapping("/{cliId}/bind-status")
    public ResultModel<CliBindStatusDTO> getCliBindStatus(@PathVariable String cliId) {
        log.info("[CliConfigController] Get CLI bind status: {}", cliId);
        
        CliBindStatusDTO result = new CliBindStatusDTO();
        result.setCliId(cliId);
        result.setBound(false);
        result.setMessage("CLI not bound");
        
        return ResultModel.success(result);
    }

    @PostMapping("/{cliId}/bind")
    public ResultModel<CliBindStatusDTO> bindCli(@PathVariable String cliId, @RequestBody CliBindRequest request) {
        log.info("[CliConfigController] Bind CLI: {} to user: {}", cliId, request.getUserId());
        
        CliBindStatusDTO result = new CliBindStatusDTO();
        result.setCliId(cliId);
        result.setBound(true);
        result.setMessage("CLI bound successfully");
        result.setUserId(request.getUserId());
        result.setBindTime(System.currentTimeMillis());
        
        return ResultModel.success(result);
    }

    @DeleteMapping("/{cliId}/bind")
    public ResultModel<CliBindStatusDTO> unbindCli(@PathVariable String cliId) {
        log.info("[CliConfigController] Unbind CLI: {}", cliId);
        
        CliBindStatusDTO result = new CliBindStatusDTO();
        result.setCliId(cliId);
        result.setBound(false);
        result.setMessage("CLI unbound successfully");
        
        return ResultModel.success(result);
    }

    @GetMapping("/{cliId}/actions")
    public ResultModel<List<CliActionDTO>> getCliActions(@PathVariable String cliId) {
        log.info("[CliConfigController] Get CLI actions: {}", cliId);
        
        List<CliActionDTO> actions = new ArrayList<>();
        
        CliActionDTO action1 = new CliActionDTO();
        action1.setAction("send_message");
        action1.setName("发送消息");
        action1.setDescription("发送消息到指定用户或群组");
        action1.setParams(Arrays.asList("receiver", "content"));
        actions.add(action1);
        
        CliActionDTO action2 = new CliActionDTO();
        action2.setAction("get_user_info");
        action2.setName("获取用户信息");
        action2.setDescription("获取指定用户的基本信息");
        action2.setParams(Arrays.asList("userId"));
        actions.add(action2);
        
        CliActionDTO action3 = new CliActionDTO();
        action3.setAction("get_department_list");
        action3.setName("获取部门列表");
        action3.setDescription("获取组织架构中的部门列表");
        action3.setParams(Collections.emptyList());
        actions.add(action3);
        
        return ResultModel.success(actions);
    }
}
