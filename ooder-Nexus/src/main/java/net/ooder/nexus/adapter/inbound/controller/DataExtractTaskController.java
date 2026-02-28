package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.task.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DataExtractTaskController {

    @GetMapping("")
    public ResultModel<List<DataExtractTaskDTO>> getTasks() {
        List<DataExtractTaskDTO> tasks = new ArrayList<>();
        
        tasks.add(createTask("1", "用户数据抽取", "extract", "running", 75));
        tasks.add(createTask("2", "订单数据抽取", "extract", "completed", 100));
        tasks.add(createTask("3", "商品数据抽取", "extract", "pending", 0));
        tasks.add(createTask("4", "日志数据抽取", "extract", "running", 45));
        
        return ResultModel.success("获取成功", tasks);
    }

    @GetMapping("/stats")
    public ResultModel<DataExtractStatsDTO> getStats() {
        DataExtractStatsDTO stats = new DataExtractStatsDTO();
        stats.setTotal(4);
        stats.setRunning(2);
        stats.setCompleted(1);
        stats.setPending(1);
        stats.setFailed(0);
        
        DataExtractStatsDTO.TodayStatsDTO todayStats = new DataExtractStatsDTO.TodayStatsDTO();
        todayStats.setExtracted(12580);
        todayStats.setFailed(12);
        todayStats.setSuccessRate(99.9);
        stats.setToday(todayStats);
        
        return ResultModel.success("获取成功", stats);
    }

    private DataExtractTaskDTO createTask(String id, String name, String type, String status, int progress) {
        DataExtractTaskDTO task = new DataExtractTaskDTO();
        task.setId(id);
        task.setName(name);
        task.setType(type);
        task.setStatus(status);
        task.setProgress(progress);
        task.setCreateTime("2026-02-28 08:00:00");
        return task;
    }
}
