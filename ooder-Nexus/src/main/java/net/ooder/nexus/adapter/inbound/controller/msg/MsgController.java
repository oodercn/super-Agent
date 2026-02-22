package net.ooder.nexus.adapter.inbound.controller.msg;

import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.nexus.model.Result;
import net.ooder.nexus.service.enterprise.MessageService;
import net.ooder.nexus.service.enterprise.MessageService.TopicMessage;
import net.ooder.nexus.service.enterprise.MessageService.JMQUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息服务控制器
 *
 * <p>提供消息服务 API，委托给 MessageService 实现：</p>
 * <ul>
 *   <li>发送消息</li>
 *   <li>广播消息</li>
 *   <li>主题订阅</li>
 * </ul>
 *
 * @author ooder Team
 * @version 0.7.3
 * @since SDK 0.7.3
 */
@RestController
@RequestMapping(value = "/api/msg", produces = "application/json;charset=UTF-8")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class MsgController {

    private static final Logger log = LoggerFactory.getLogger(MsgController.class);

    @Autowired(required = false)
    private MessageService messageService;

    /**
     * 发送消息给指定用户
     */
    @PostMapping("/send")
    @ResponseBody
    public ResultModel<Map<String, Object>> sendToPerson(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String message = request.get("message");
        String sender = request.get("sender");

        log.info("Send message to person: account={}, sender={}", account, sender);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            if (messageService != null) {
                Result<TopicMessage> sendResult = messageService.sendToPerson(account, message);
                if (sendResult.isSuccess()) {
                    TopicMessage msg = sendResult.getData();
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("id", msg.getId());
                    data.put("topic", msg.getTopic());
                    data.put("message", msg.getMessage());
                    data.put("sender", msg.getSender());
                    data.put("timestamp", msg.getTimestamp());
                    result.setData(data);
                    result.setRequestStatus(200);
                    result.setMessage("发送成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("发送失败: " + sendResult.getMessage());
                }
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", "msg-" + System.currentTimeMillis());
                data.put("account", account);
                data.put("message", message);
                data.put("sender", sender);
                data.put("timestamp", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("发送成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error sending message", e);
            result.setRequestStatus(500);
            result.setMessage("发送消息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 广播消息
     */
    @PostMapping("/broadcast")
    @ResponseBody
    public ResultModel<Map<String, Object>> broadcast(@RequestBody Map<String, String> request) {
        String topic = request.get("topic");
        String message = request.get("message");
        String sender = request.get("sender");

        log.info("Broadcast message: topic={}, sender={}", topic, sender);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            if (messageService != null) {
                Result<TopicMessage> broadcastResult = messageService.broadcast(topic, message);
                if (broadcastResult.isSuccess()) {
                    TopicMessage msg = broadcastResult.getData();
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("id", msg.getId());
                    data.put("topic", msg.getTopic());
                    data.put("message", msg.getMessage());
                    data.put("sender", msg.getSender());
                    data.put("timestamp", msg.getTimestamp());
                    result.setData(data);
                    result.setRequestStatus(200);
                    result.setMessage("广播成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("广播失败: " + broadcastResult.getMessage());
                }
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", "broadcast-" + System.currentTimeMillis());
                data.put("topic", topic);
                data.put("message", message);
                data.put("sender", sender);
                data.put("timestamp", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("广播成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error broadcasting message", e);
            result.setRequestStatus(500);
            result.setMessage("广播消息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送主题消息
     */
    @PostMapping("/topic")
    @ResponseBody
    public ResultModel<Map<String, Object>> sendTopicMessage(@RequestBody Map<String, String> request) {
        String topic = request.get("topic");
        String message = request.get("message");
        String sender = request.get("sender");

        log.info("Send topic message: topic={}, sender={}", topic, sender);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            if (messageService != null) {
                Result<TopicMessage> sendResult = messageService.sendTopicMessage(topic, message);
                if (sendResult.isSuccess()) {
                    TopicMessage msg = sendResult.getData();
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("id", msg.getId());
                    data.put("topic", msg.getTopic());
                    data.put("message", msg.getMessage());
                    data.put("sender", msg.getSender());
                    data.put("timestamp", msg.getTimestamp());
                    result.setData(data);
                    result.setRequestStatus(200);
                    result.setMessage("发送成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("发送失败: " + sendResult.getMessage());
                }
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", "topic-" + System.currentTimeMillis());
                data.put("topic", topic);
                data.put("message", message);
                data.put("sender", sender);
                data.put("timestamp", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("发送成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error sending topic message", e);
            result.setRequestStatus(500);
            result.setMessage("发送主题消息失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 订阅主题
     */
    @PostMapping("/subscribe")
    @ResponseBody
    public ResultModel<Map<String, Object>> subscribe(@RequestBody Map<String, String> request) {
        String topic = request.get("topic");
        String userId = request.get("userId");

        log.info("Subscribe topic: topic={}, userId={}", topic, userId);
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            if (messageService != null) {
                Result<String> subscribeResult = messageService.subscribe(topic);
                if (subscribeResult.isSuccess()) {
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("subscriptionId", subscribeResult.getData());
                    data.put("topic", topic);
                    data.put("userId", userId);
                    data.put("subscribedAt", System.currentTimeMillis());
                    result.setData(data);
                    result.setRequestStatus(200);
                    result.setMessage("订阅成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("订阅失败: " + subscribeResult.getMessage());
                }
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("subscriptionId", "sub-" + System.currentTimeMillis());
                data.put("topic", topic);
                data.put("userId", userId);
                data.put("subscribedAt", System.currentTimeMillis());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("订阅成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error subscribing topic", e);
            result.setRequestStatus(500);
            result.setMessage("订阅失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 取消订阅
     */
    @DeleteMapping("/subscribe/{subscriptionId}")
    @ResponseBody
    public ResultModel<Boolean> unsubscribe(@PathVariable String subscriptionId) {
        log.info("Unsubscribe: subscriptionId={}", subscriptionId);
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        try {
            if (messageService != null) {
                Result<Boolean> unsubscribeResult = messageService.unsubscribe(subscriptionId);
                if (unsubscribeResult.isSuccess()) {
                    result.setData(true);
                    result.setRequestStatus(200);
                    result.setMessage("取消订阅成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("取消订阅失败: " + unsubscribeResult.getMessage());
                }
            } else {
                result.setData(true);
                result.setRequestStatus(200);
                result.setMessage("取消订阅成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error unsubscribing", e);
            result.setRequestStatus(500);
            result.setMessage("取消订阅失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取已订阅的主题列表
     */
    @GetMapping("/topics")
    @ResponseBody
    public ListResultModel<List<String>> getSubscribedTopics() {
        log.info("Get subscribed topics");
        ListResultModel<List<String>> result = new ListResultModel<List<String>>();
        try {
            if (messageService != null) {
                Result<List<String>> topicsResult = messageService.getSubscribedTopics();
                if (topicsResult.isSuccess()) {
                    result.setData(topicsResult.getData());
                    result.setSize(topicsResult.getData().size());
                    result.setRequestStatus(200);
                    result.setMessage("获取成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("获取失败: " + topicsResult.getMessage());
                }
            } else {
                List<String> mockTopics = new ArrayList<String>();
                mockTopics.add("topic://system/notifications");
                mockTopics.add("topic://im/messages");
                result.setData(mockTopics);
                result.setSize(mockTopics.size());
                result.setRequestStatus(200);
                result.setMessage("获取成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error getting subscribed topics", e);
            result.setRequestStatus(500);
            result.setMessage("获取订阅主题失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 获取用户连接信息
     */
    @GetMapping("/user/info")
    @ResponseBody
    public ResultModel<Map<String, Object>> getUserInfo() {
        log.info("Get user connection info");
        ResultModel<Map<String, Object>> result = new ResultModel<Map<String, Object>>();
        try {
            if (messageService != null) {
                Result<JMQUser> userInfoResult = messageService.getUserInfo();
                if (userInfoResult.isSuccess()) {
                    JMQUser user = userInfoResult.getData();
                    Map<String, Object> data = new HashMap<String, Object>();
                    data.put("userId", user.getUserId());
                    data.put("userName", user.getUserName());
                    data.put("sessionId", user.getSessionId());
                    data.put("clientType", user.getClientType());
                    data.put("connectTime", user.getConnectTime());
                    data.put("subscribedTopics", user.getSubscribedTopics());
                    result.setData(data);
                    result.setRequestStatus(200);
                    result.setMessage("获取成功");
                } else {
                    result.setRequestStatus(500);
                    result.setMessage("获取失败: " + userInfoResult.getMessage());
                }
            } else {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("userId", "user-001");
                data.put("userName", "测试用户");
                data.put("sessionId", "session-" + System.currentTimeMillis());
                data.put("clientType", "web");
                data.put("connectTime", System.currentTimeMillis());
                data.put("subscribedTopics", new ArrayList<String>());
                result.setData(data);
                result.setRequestStatus(200);
                result.setMessage("获取成功(模拟)");
            }
        } catch (Exception e) {
            log.error("Error getting user info", e);
            result.setRequestStatus(500);
            result.setMessage("获取用户信息失败: " + e.getMessage());
        }
        return result;
    }
}
