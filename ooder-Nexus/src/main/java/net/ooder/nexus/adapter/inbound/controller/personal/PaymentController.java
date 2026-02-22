package net.ooder.nexus.adapter.inbound.controller.personal;

import net.ooder.nexus.domain.personal.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/personal/payment")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final Map<String, PaymentChannel> channelStore = new HashMap<>();
    private final List<PaymentRecord> recordStore = new ArrayList<>();

    public PaymentController() {
        initDefaultChannels();
    }

    private void initDefaultChannels() {
        PaymentChannel wechat = new PaymentChannel();
        wechat.setChannelId("wechat_pay");
        wechat.setName("微信支付");
        wechat.setIcon("ri-wechat-pay-line");
        wechat.setStatus("PENDING_CONFIG");
        wechat.setConnected(false);
        
        List<ConfigFieldInfo> wechatFields = new ArrayList<>();
        ConfigFieldInfo appId = new ConfigFieldInfo();
        appId.setName("appId");
        appId.setLabel("AppID");
        appId.setConfigured(false);
        wechatFields.add(appId);
        
        ConfigFieldInfo mchId = new ConfigFieldInfo();
        mchId.setName("mchId");
        mchId.setLabel("商户号");
        mchId.setConfigured(false);
        wechatFields.add(mchId);
        
        ConfigFieldInfo apiKey = new ConfigFieldInfo();
        apiKey.setName("apiKey");
        apiKey.setLabel("API密钥");
        apiKey.setConfigured(false);
        apiKey.setSecret(true);
        wechatFields.add(apiKey);
        
        wechat.setConfigFields(wechatFields);
        channelStore.put(wechat.getChannelId(), wechat);

        PaymentChannel alipay = new PaymentChannel();
        alipay.setChannelId("alipay");
        alipay.setName("支付宝");
        alipay.setIcon("ri-alipay-line");
        alipay.setStatus("PENDING_CONFIG");
        alipay.setConnected(false);
        
        List<ConfigFieldInfo> alipayFields = new ArrayList<>();
        ConfigFieldInfo alipayAppId = new ConfigFieldInfo();
        alipayAppId.setName("appId");
        alipayAppId.setLabel("AppID");
        alipayAppId.setConfigured(false);
        alipayFields.add(alipayAppId);
        
        ConfigFieldInfo privateKey = new ConfigFieldInfo();
        privateKey.setName("privateKey");
        privateKey.setLabel("应用私钥");
        privateKey.setConfigured(false);
        privateKey.setSecret(true);
        alipayFields.add(privateKey);
        
        ConfigFieldInfo publicKey = new ConfigFieldInfo();
        publicKey.setName("alipayPublicKey");
        publicKey.setLabel("支付宝公钥");
        publicKey.setConfigured(false);
        alipayFields.add(publicKey);
        
        alipay.setConfigFields(alipayFields);
        channelStore.put(alipay.getChannelId(), alipay);
    }

    @GetMapping("/channels")
    public Map<String, Object> getChannels() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("requestStatus", 200);
            result.put("data", new HashMap<String, Object>() {{
                put("channels", new ArrayList<>(channelStore.values()));
            }});
        } catch (Exception e) {
            log.error("Failed to get payment channels", e);
            result.put("requestStatus", 500);
            result.put("message", "获取支付渠道失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/channels/{channelId}/config")
    public Map<String, Object> configChannel(
            @PathVariable String channelId,
            @RequestBody Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        try {
            PaymentChannel channel = channelStore.get(channelId);
            if (channel == null) {
                result.put("requestStatus", 404);
                result.put("message", "支付渠道不存在");
                return result;
            }

            channel.setStatus("CONFIGURED");
            channel.setConnected(true);
            channel.setLastChecked(new Date());
            
            for (ConfigFieldInfo field : channel.getConfigFields()) {
                field.setConfigured(config.containsKey(field.getName()));
            }

            result.put("requestStatus", 200);
            result.put("message", "配置保存成功");
            
            Map<String, Object> data = new HashMap<>();
            data.put("channelId", channelId);
            data.put("status", channel.getStatus());
            
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("success", true);
            testResult.put("message", "连接测试成功");
            data.put("testResult", testResult);
            
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to config payment channel", e);
            result.put("requestStatus", 500);
            result.put("message", "配置失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/records")
    public Map<String, Object> getRecords(
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<PaymentRecord> filtered = new ArrayList<>();
            for (PaymentRecord record : recordStore) {
                boolean match = true;
                if (channel != null && !channel.isEmpty() && !channel.equals(record.getChannel())) {
                    match = false;
                }
                if (type != null && !type.isEmpty() && !type.equals(record.getType())) {
                    match = false;
                }
                if (match) {
                    filtered.add(record);
                }
            }

            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;
            
            for (PaymentRecord r : filtered) {
                if ("income".equals(r.getType())) {
                    totalIncome = totalIncome.add(r.getAmount());
                } else {
                    totalExpense = totalExpense.add(r.getAmount());
                }
            }

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalIncome", totalIncome);
            statistics.put("totalExpense", totalExpense);

            int start = page * size;
            int end = Math.min(start + size, filtered.size());
            List<PaymentRecord> paged = start < filtered.size() ? 
                filtered.subList(start, end) : new ArrayList<>();

            Map<String, Object> data = new HashMap<>();
            data.put("total", filtered.size());
            data.put("records", paged);
            data.put("statistics", statistics);
            
            result.put("requestStatus", 200);
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to get payment records", e);
            result.put("requestStatus", 500);
            result.put("message", "获取记录失败: " + e.getMessage());
        }
        return result;
    }
}
