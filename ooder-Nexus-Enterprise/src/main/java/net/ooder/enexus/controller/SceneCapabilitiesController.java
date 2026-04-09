package net.ooder.enexus.controller;

import net.ooder.enexus.model.ResultModel;
import net.ooder.enexus.dto.capability.CapabilityLogDTO;
import net.ooder.enexus.dto.capability.CapabilityLogListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/scene/capabilities")
@CrossOrigin(originPatterns = "*", allowCredentials = "true", allowedHeaders = "*")
public class SceneCapabilitiesController {

    private static final Logger log = LoggerFactory.getLogger(SceneCapabilitiesController.class);

    @GetMapping("/logs")
    public ResultModel<CapabilityLogListDTO> getCapabilityLogs(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        log.info("[SceneCapabilitiesController] Get capability logs - pageNum: {}, pageSize: {}", pageNum, pageSize);
        
        List<CapabilityLogDTO> logs = new ArrayList<>();
        
        String[] names = {"智能客服", "文档问答", "数据分析", "知识检索", "工作流引擎"};
        String[] levels = {"INFO", "DEBUG", "WARN", "ERROR", "INFO"};
        String[] messages = {"调用成功", "参数解析完成", "响应时间较长", "连接超时重试", "任务完成"};
        int[] durations = {120, 85, 350, 1200, 95};
        
        for (int i = 0; i < Math.min(pageSize, 10); i++) {
            CapabilityLogDTO logEntry = new CapabilityLogDTO();
            logEntry.setCapabilityId("cap-00" + ((i % 5) + 1));
            logEntry.setCapabilityName(names[i % 5]);
            logEntry.setLevel(levels[i % 5]);
            logEntry.setMessage(messages[i % 5]);
            logEntry.setTimestamp(System.currentTimeMillis() - i * 60000);
            logEntry.setDuration(durations[i % 5]);
            logs.add(logEntry);
        }
        
        CapabilityLogListDTO result = new CapabilityLogListDTO();
        result.setList(logs);
        result.setTotal(50);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return ResultModel.success(result);
    }
}
