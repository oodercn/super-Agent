package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.stats.CapabilityStatsDTO;
import net.ooder.mvp.skill.scene.dto.stats.CapabilityRankDTO;
import net.ooder.mvp.skill.scene.dto.stats.LogEntryDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import net.ooder.mvp.skill.scene.service.CapabilityStatsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/capabilities/stats")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CapabilityStatsController {

    @Autowired
    private CapabilityStatsService statsService;

    @GetMapping("/overview")
    public ResultModel<CapabilityStatsDTO> getOverview() {
        CapabilityStatsDTO stats = statsService.getOverviewStats();
        return ResultModel.success(stats);
    }

    @GetMapping("/rank")
    public ResultModel<List<CapabilityRankDTO>> getRank(
            @RequestParam(defaultValue = "invokeCount") String sortBy,
            @RequestParam(defaultValue = "10") int limit) {
        List<CapabilityRankDTO> rank = statsService.getCapabilityRank(sortBy, limit);
        return ResultModel.success(rank);
    }

    @GetMapping("/top")
    public ResultModel<List<CapabilityRankDTO>> getTop(
            @RequestParam(defaultValue = "10") int limit) {
        List<CapabilityRankDTO> top = statsService.getTopCapabilities(limit);
        return ResultModel.success(top);
    }

    @GetMapping("/errors")
    public ResultModel<List<String>> getErrors(
            @RequestParam(defaultValue = "10") int limit) {
        List<String> errors = statsService.getRecentErrors(limit);
        return ResultModel.success(errors);
    }

    @GetMapping("/logs")
    public ResultModel<List<LogEntryDTO>> getLogs(
            @RequestParam(defaultValue = "20") int limit) {
        List<LogEntryDTO> logs = statsService.getRecentLogs(limit);
        return ResultModel.success(logs);
    }
}
