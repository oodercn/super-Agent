package net.ooder.mvp.skill.scene.capability.invoke;

import net.ooder.mvp.skill.scene.capability.fallback.FallbackService;
import net.ooder.mvp.skill.scene.capability.model.CapabilityBinding;
import net.ooder.mvp.skill.scene.capability.model.CapabilityBindingStatus;
import net.ooder.mvp.skill.scene.capability.service.CapabilityBindingService;
import net.ooder.mvp.skill.scene.capability.connector.ConnectorFactory;
import net.ooder.mvp.skill.scene.capability.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CapabilityInvokerImpl implements CapabilityInvoker {

    private static final Logger log = LoggerFactory.getLogger(CapabilityInvokerImpl.class);

    @Autowired
    private CapabilityBindingService bindingService;

    @Autowired(required = false)
    private FallbackService fallbackService;

    @Autowired(required = false)
    private ConnectorFactory connectorFactory;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Object invoke(String capId, Map<String, Object> params) {
        InvokeResult result = invokeWithResult(capId, params);
        if (result.isSuccess()) {
            return result.getResult();
        }
        throw new RuntimeException("Capability invocation failed: " + result.getError());
    }

    @Override
    public Object invokeWithFallback(String capId, Map<String, Object> params) {
        CapabilityBinding binding = bindingService.getByCapId(capId);
        if (binding == null) {
            throw new IllegalArgumentException("Capability binding not found: " + capId);
        }

        if (fallbackService != null) {
            return fallbackService.executeWithFallback(binding, params, 
                new FallbackService.PrimaryExecutor() {
                    @Override
                    public Object execute(Object input) throws Exception {
                        return doInvoke(binding, (Map<String, Object>) input);
                    }
                });
        }

        return invoke(capId, params);
    }

    @Override
    public CompletableFuture<Object> invokeAsync(String capId, Map<String, Object> params) {
        return CompletableFuture.supplyAsync(new java.util.function.Supplier<Object>() {
            @Override
            public Object get() {
                return invoke(capId, params);
            }
        }, executor);
    }

    @Override
    public InvokeResult invokeWithResult(String capId, Map<String, Object> params) {
        long startTime = System.currentTimeMillis();
        
        try {
            CapabilityBinding binding = bindingService.getByCapId(capId);
            if (binding == null) {
                return InvokeResult.failure("Capability binding not found: " + capId, 
                    System.currentTimeMillis() - startTime);
            }

            if (!isBindingActive(binding)) {
                return InvokeResult.failure("Capability binding is not active: " + capId, 
                    System.currentTimeMillis() - startTime);
            }

            Object result = doInvoke(binding, params);
            
            InvokeResult invokeResult = InvokeResult.success(result, 
                System.currentTimeMillis() - startTime);
            invokeResult.setProviderId(binding.getProviderId());
            invokeResult.setConnectorType(binding.getConnectorType() != null ? 
                binding.getConnectorType().name() : "UNKNOWN");
            
            updateBindingStats(binding, true);
            
            return invokeResult;

        } catch (Exception e) {
            log.error("[invoke] Capability invocation failed: {} - {}", capId, e.getMessage());
            updateBindingStats(capId, false);
            return InvokeResult.failure(e.getMessage(), System.currentTimeMillis() - startTime);
        }
    }

    private Object doInvoke(CapabilityBinding binding, Map<String, Object> params) throws Exception {
        String connectorType = binding.getConnectorType() != null ? 
            binding.getConnectorType().name() : "HTTP";
        
        if (connectorFactory != null) {
            Connector connector = connectorFactory.getConnector(connectorType);
            if (connector != null) {
                return connector.invoke(binding, params);
            }
        }

        String endpoint = binding.getEndpoint();
        if (endpoint == null || endpoint.isEmpty()) {
            throw new RuntimeException("No endpoint configured for capability: " + binding.getCapId());
        }

        log.info("[doInvoke] Invoking capability {} via {} at {}", 
            binding.getCapId(), connectorType, endpoint);

        if ("HTTP".equals(connectorType) || "HTTPS".equals(connectorType)) {
            return invokeHttp(binding, params);
        } else if ("INTERNAL".equals(connectorType)) {
            return invokeInternal(binding, params);
        } else if ("WEBSOCKET".equals(connectorType)) {
            return invokeWebSocket(binding, params);
        } else {
            throw new RuntimeException("Unsupported connector type: " + connectorType);
        }
    }

    private Object invokeHttp(CapabilityBinding binding, Map<String, Object> params) {
        String endpoint = binding.getEndpoint();
        int timeout = binding.getTimeout() > 0 ? binding.getTimeout() : 30000;
        
        log.debug("[invokeHttp] POST {} with params: {}", endpoint, params.keySet());
        
        // 实际HTTP调用逻辑
        // 这里简化实现，实际应使用RestTemplate或WebClient
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", true);
        response.put("data", params);
        response.put("endpoint", endpoint);
        
        return response;
    }

    private Object invokeInternal(CapabilityBinding binding, Map<String, Object> params) {
        log.debug("[invokeInternal] Internal invoke for {}", binding.getCapId());
        
        // 内部调用逻辑
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", true);
        response.put("capId", binding.getCapId());
        response.put("params", params);
        
        return response;
    }

    private Object invokeWebSocket(CapabilityBinding binding, Map<String, Object> params) {
        log.debug("[invokeWebSocket] WebSocket invoke for {}", binding.getCapId());
        
        // WebSocket调用逻辑
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", true);
        response.put("capId", binding.getCapId());
        
        return response;
    }

    private boolean isBindingActive(CapabilityBinding binding) {
        return binding.getStatus() == CapabilityBindingStatus.ACTIVE;
    }

    private void updateBindingStats(CapabilityBinding binding, boolean success) {
        try {
            bindingService.updateInvokeStats(binding.getBindingId(), success);
        } catch (Exception e) {
            log.warn("[updateBindingStats] Failed to update stats: {}", e.getMessage());
        }
    }

    private void updateBindingStats(String capId, boolean success) {
        try {
            CapabilityBinding binding = bindingService.getByCapId(capId);
            if (binding != null) {
                updateBindingStats(binding, success);
            }
        } catch (Exception e) {
            log.warn("[updateBindingStats] Failed to update stats: {}", e.getMessage());
        }
    }
}
