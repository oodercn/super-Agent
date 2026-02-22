package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.domain.network.model.TrafficStats;
import net.ooder.nexus.service.TrafficStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 流量统计控制器
 * 处理流量统计、带宽汇总、带宽趋势等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/traffic")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class TrafficStatsController {

    private static final Logger log = LoggerFactory.getLogger(TrafficStatsController.class);

    @Autowired
    private TrafficStatsService trafficStatsService;

    @PostMapping("/stats/list")
    @ResponseBody
    public ResultModel<List<TrafficStats>> getAllTrafficStats() {
        try {
            List<TrafficStats> stats = trafficStatsService.getAllTrafficStats();
            return ResultModel.success(stats);
        } catch (Exception e) {
            log.error("获取流量统计失败", e);
            return ResultModel.error("获取流量统计失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/summary")
    @ResponseBody
    public ResultModel<TrafficStatsService.BandwidthSummary> getBandwidthSummary() {
        try {
            TrafficStatsService.BandwidthSummary summary = trafficStatsService.getBandwidthSummary();
            return ResultModel.success(summary);
        } catch (Exception e) {
            log.error("获取带宽汇总失败", e);
            return ResultModel.error("获取带宽汇总失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/trend")
    @ResponseBody
    public ResultModel<List<TrafficStatsService.BandwidthTrend>> getBandwidthTrend(@RequestBody Map<String, String> request) {
        String period = request.getOrDefault("period", "24h");
        try {
            List<TrafficStatsService.BandwidthTrend> trends = trafficStatsService.getBandwidthTrend(period);
            return ResultModel.success(trends);
        } catch (Exception e) {
            log.error("获取带宽趋势失败", e);
            return ResultModel.error("获取带宽趋势失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/stats/update")
    @ResponseBody
    public ResultModel<TrafficStats> updateTrafficStats(@RequestBody TrafficStats stats) {
        try {
            TrafficStats updated = trafficStatsService.updateTrafficStats(stats);
            return ResultModel.success(updated);
        } catch (Exception e) {
            log.error("更新流量统计失败", e);
            return ResultModel.error("更新流量统计失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/init-default")
    @ResponseBody
    public ResultModel<Boolean> initDefaultData() {
        try {
            trafficStatsService.initDefaultData();
            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("初始化默认数据失败", e);
            return ResultModel.error("初始化默认数据失败: " + e.getMessage(), 500);
        }
    }
}
