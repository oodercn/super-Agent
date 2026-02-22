package net.ooder.nexus.adapter.inbound.controller.p2p;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.service.P2PService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * P2P网络管理控制器
 * 处理节点发现、网络加入、技能发布订阅等操作
 *
 * @author ooder Team
 * @version 0.7.0
 * @since 0.7.0
 */
@RestController
@RequestMapping("/api/p2p/network")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class P2PNetworkController {

    @Autowired
    private P2PService p2pService;

    @PostMapping("/discovery")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> discoverNodes() {
        try {
            List<Map<String, Object>> nodes = p2pService.discoverNodes();
            return ResultModel.success(nodes);
        } catch (Exception e) {
            return ResultModel.error("节点发现失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/join")
    @ResponseBody
    public ResultModel<Boolean> joinNetwork(@RequestBody Map<String, Object> nodeInfo) {
        try {
            boolean result = p2pService.joinNetwork(nodeInfo);
            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("加入网络失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/nodes")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getNodes() {
        try {
            List<Map<String, Object>> nodes = p2pService.getNodes();
            return ResultModel.success(nodes);
        } catch (Exception e) {
            return ResultModel.error("获取节点列表失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/node/detail")
    @ResponseBody
    public ResultModel<Map<String, Object>> getNodeDetails(@RequestBody Map<String, String> request) {
        String nodeId = request.get("nodeId");
        try {
            Map<String, Object> nodeDetails = p2pService.getNodeDetails(nodeId);
            return ResultModel.success(nodeDetails);
        } catch (Exception e) {
            return ResultModel.error("获取节点详情失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/node/remove")
    @ResponseBody
    public ResultModel<Boolean> removeNode(@RequestBody Map<String, String> request) {
        String nodeId = request.get("nodeId");
        try {
            boolean result = p2pService.removeNode(nodeId);
            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("移除节点失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/skill/publish")
    @ResponseBody
    public ResultModel<Boolean> publishSkill(@RequestBody Map<String, Object> skillInfo) {
        try {
            boolean result = p2pService.publishSkill(skillInfo);
            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("发布技能失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/skill/subscribe")
    @ResponseBody
    public ResultModel<Boolean> subscribeSkill(@RequestBody Map<String, Object> request) {
        String skillId = (String) request.get("skillId");
        try {
            boolean result = p2pService.subscribeSkill(skillId);
            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("订阅技能失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/skill/market")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getSkillMarket() {
        try {
            List<Map<String, Object>> skills = p2pService.getSkillMarket();
            return ResultModel.success(skills);
        } catch (Exception e) {
            return ResultModel.error("获取技能市场失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/status")
    @ResponseBody
    public ResultModel<Map<String, Object>> getNetworkStatus() {
        try {
            Map<String, Object> status = p2pService.getNetworkStatus();
            return ResultModel.success(status);
        } catch (Exception e) {
            return ResultModel.error("获取网络状态失败: " + e.getMessage(), 500);
        }
    }

    @PostMapping("/message")
    @ResponseBody
    public ResultModel<Boolean> sendMessage(@RequestBody Map<String, Object> request) {
        String targetNodeId = (String) request.get("targetNodeId");
        Map<String, Object> message = (Map<String, Object>) request.get("message");
        try {
            boolean result = p2pService.sendMessage(targetNodeId, message);
            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("发送消息失败: " + e.getMessage(), 500);
        }
    }
}
