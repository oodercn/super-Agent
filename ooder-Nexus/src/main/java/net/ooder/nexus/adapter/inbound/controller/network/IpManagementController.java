package net.ooder.nexus.adapter.inbound.controller.network;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.domain.network.model.IpAllocation;
import net.ooder.nexus.domain.network.model.IpPool;
import net.ooder.nexus.service.IpManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * IP管理控制器
 * 处理IP分配CRUD、IP池配置、统计信息等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/ip")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class IpManagementController {

    private static final Logger log = LoggerFactory.getLogger(IpManagementController.class);

    @Autowired
    private IpManagementService ipManagementService;

    @PostMapping("/allocations/list")
    @ResponseBody
    public ResultModel<List<IpAllocation>> getAllAllocations() {
        try {
            List<IpAllocation> allocations = ipManagementService.getAllAllocations();
            return ResultModel.success(allocations);
        } catch (Exception e) {
            log.error("获取IP分配列表失败", e);
            return ResultModel.error("获取IP分配列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/allocations/by-type")
    @ResponseBody
    public ResultModel<List<IpAllocation>> getAllocationsByType(@RequestBody Map<String, String> request) {
        String type = request.get("type");
        try {
            List<IpAllocation> allocations = ipManagementService.getAllocationsByType(type);
            return ResultModel.success(allocations);
        } catch (Exception e) {
            log.error("获取IP分配列表失败", e);
            return ResultModel.error("获取IP分配列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/allocations/by-status")
    @ResponseBody
    public ResultModel<List<IpAllocation>> getAllocationsByStatus(@RequestBody Map<String, String> request) {
        String status = request.get("status");
        try {
            List<IpAllocation> allocations = ipManagementService.getAllocationsByStatus(status);
            return ResultModel.success(allocations);
        } catch (Exception e) {
            log.error("获取IP分配列表失败", e);
            return ResultModel.error("获取IP分配列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/allocations/create")
    @ResponseBody
    public ResultModel<IpAllocation> createAllocation(@RequestBody IpAllocation allocation) {
        try {
            IpAllocation created = ipManagementService.createAllocation(allocation);
            return ResultModel.success(created);
        } catch (Exception e) {
            log.error("创建IP分配失败", e);
            return ResultModel.error("创建IP分配失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/allocations/update")
    @ResponseBody
    public ResultModel<IpAllocation> updateAllocation(@RequestBody Map<String, Object> request) {
        String id = (String) request.get("id");
        IpAllocation allocation = (IpAllocation) request.get("allocation");
        try {
            IpAllocation updated = ipManagementService.updateAllocation(id, allocation);
            if (updated != null) {
                return ResultModel.success(updated);
            } else {
                return ResultModel.error("IP分配不存在", 404);
            }
        } catch (Exception e) {
            log.error("更新IP分配失败", e);
            return ResultModel.error("更新IP分配失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/allocations/delete")
    @ResponseBody
    public ResultModel<Boolean> deleteAllocation(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        try {
            boolean deleted = ipManagementService.deleteAllocation(id);
            if (deleted) {
                return ResultModel.success(true);
            } else {
                return ResultModel.error("IP分配不存在", 404);
            }
        } catch (Exception e) {
            log.error("删除IP分配失败", e);
            return ResultModel.error("删除IP分配失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/pool/get")
    @ResponseBody
    public ResultModel<IpPool> getIpPool() {
        try {
            IpPool pool = ipManagementService.getIpPool();
            return ResultModel.success(pool);
        } catch (Exception e) {
            log.error("获取IP池配置失败", e);
            return ResultModel.error("获取IP池配置失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/pool/update")
    @ResponseBody
    public ResultModel<IpPool> updateIpPool(@RequestBody IpPool pool) {
        try {
            IpPool updated = ipManagementService.updateIpPool(pool);
            return ResultModel.success(updated);
        } catch (Exception e) {
            log.error("更新IP池配置失败", e);
            return ResultModel.error("更新IP池配置失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/statistics")
    @ResponseBody
    public ResultModel<IpManagementService.IpStatistics> getStatistics() {
        try {
            IpManagementService.IpStatistics stats = ipManagementService.getStatistics();
            return ResultModel.success(stats);
        } catch (Exception e) {
            log.error("获取IP统计信息失败", e);
            return ResultModel.error("获取IP统计信息失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/init-default")
    @ResponseBody
    public ResultModel<Boolean> initDefaultData() {
        try {
            ipManagementService.initDefaultData();
            return ResultModel.success(true);
        } catch (Exception e) {
            log.error("初始化默认数据失败", e);
            return ResultModel.error("初始化默认数据失败: " + e.getMessage(), 500);
        }
    }
}
