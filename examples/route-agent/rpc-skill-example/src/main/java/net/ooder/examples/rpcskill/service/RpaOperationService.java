package net.ooder.examples.rpaskill.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ooder.examples.rpaskill.config.RpaConfig;
import net.ooder.examples.rpaskill.model.RpaRequest;
import net.ooder.examples.rpaskill.model.RpaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * RPA Operation Service
 * Handles RPA operations to external URLs with the specified parameters
 * 作者：ooderAI agent team   V0.6.0
 */
@Service
public class RpaOperationService {

    private final RpaConfig rpaConfig;
    private final WebClient webClient;
    private final SnAuthService snAuthService;

    @Autowired
    public RpaOperationService(RpaConfig rpaConfig, SnAuthService snAuthService) {
        this.rpaConfig = rpaConfig;
        this.webClient = WebClient.builder().build();
        this.snAuthService = snAuthService;
    }

    /**
     * Performs RPA operation with the given request parameters
     * If any parameter is null, uses the default value from configuration
     * @param request RPA request parameters
     * @return RPA response with the result
     */
    public RpaResponse performRpaOperation(RpaRequest request) {
        try {
            // Use request parameters or default values from configuration
            String token = request.getToken() != null ? request.getToken() : rpaConfig.getToken();
            String url = request.getUrl() != null ? request.getUrl() : rpaConfig.getUrl();
            String script = request.getScript() != null ? request.getScript() : rpaConfig.getScript();
            String jsonF = request.getJsonF() != null ? request.getJsonF() : rpaConfig.getJsonF();

            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("token", token);
            requestBody.put("script", script);
            requestBody.put("jsonF", jsonF);

            // Perform HTTP POST request to the external URL
            // Note: In a real implementation, this would be an actual HTTP call
            // For demo purposes, we'll simulate the response based on jsonF format
            
            // Parse the jsonF to understand the expected response structure
            JSONObject jsonFObj = JSON.parseObject(jsonF);
            JSONObject resultSchema = jsonFObj.getJSONObject("result");

            // Simulate successful response based on the schema
            Map<String, Object> responseData = simulateResponseData(resultSchema);

            // Create and return RPA response
            return RpaResponse.success(responseData, "RPA operation completed successfully");

        } catch (Exception e) {
            // Handle any exceptions
            return RpaResponse.error("RPA operation failed: " + e.getMessage());
        }
    }

    /**
     * Performs RPA operation with a specific SN for authorization
     * @param request RPA request parameters
     * @param sn Serial Number for authorization
     * @return RPA response with the result
     * @throws SnAuthService.UnauthorizedException if SN is not authorized
     */
    public RpaResponse performRpaOperationWithAuth(RpaRequest request, String sn) throws SnAuthService.UnauthorizedException {
        // Validate SN using the injected service
        snAuthService.validateSnWithException(sn);
        
        // Perform RPA operation
        return performRpaOperation(request);
    }

    /**
     * Simulates response data based on the provided JSON schema
     * @param schema JSON schema defining the expected response structure
     * @return Simulated response data
     */
    private Map<String, Object> simulateResponseData(JSONObject schema) {
        Map<String, Object> data = new HashMap<>();

        // Check if schema has properties
        if (schema.containsKey("properties")) {
            JSONObject properties = schema.getJSONObject("properties");
            
            // Iterate through each property and generate sample data
            for (String propertyName : properties.keySet()) {
                JSONObject propertySchema = properties.getJSONObject(propertyName);
                String type = propertySchema.getString("type");
                
                // Generate sample value based on type
                switch (type) {
                    case "string":
                        data.put(propertyName, "sample_" + propertyName);
                        break;
                    case "number":
                        data.put(propertyName, 123);
                        break;
                    case "integer":
                        data.put(propertyName, 456);
                        break;
                    case "boolean":
                        data.put(propertyName, true);
                        break;
                    case "object":
                        // Recursively generate object data
                        data.put(propertyName, simulateResponseData(propertySchema));
                        break;
                    case "array":
                        // Generate a simple array with one sample item
                        data.put(propertyName, new Object[]{"sample_item"});
                        break;
                    default:
                        data.put(propertyName, "sample_value");
                }
            }
        }

        return data;
    }
}
