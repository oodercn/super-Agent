package net.ooder.skillcenter.model.impl;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillParam;
import net.ooder.skillcenter.model.SkillResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地部署技能实现
 * 用于将生成的代码部署到本地环境
 */
public class LocalDeploymentSkill implements Skill {
    private static final String SKILL_ID = "local-deployment-skill";
    private static final String SKILL_NAME = "本地部署技能";
    private static final String SKILL_DESCRIPTION = "将生成的代码部署到本地环境";
    
    private Map<String, SkillParam> params;
    
    public LocalDeploymentSkill() {
        initParams();
    }
    
    /**
     * 初始化技能参数
     */
    private void initParams() {
        params = new HashMap<>();
        
        // 项目ID参数
        params.put("projectId", new SkillParam(
                "projectId",
                "项目ID",
                String.class,
                true
        ));
        
        // 项目路径参数
        params.put("projectPath", new SkillParam(
                "projectPath",
                "项目路径",
                String.class,
                true
        ));
        
        // 构建配置参数
        params.put("buildConfig", new SkillParam(
                "buildConfig",
                "构建配置",
                Map.class,
                true
        ));
        
        // 部署配置参数
        params.put("deploymentConfig", new SkillParam(
                "deploymentConfig",
                "部署配置",
                Map.class,
                true
        ));
    }
    
    @Override
    public String getId() {
        return SKILL_ID;
    }
    
    @Override
    public String getName() {
        return SKILL_NAME;
    }
    
    @Override
    public String getDescription() {
        return SKILL_DESCRIPTION;
    }
    
    @Override
    public SkillResult execute(SkillContext context) throws SkillException {
        try {
            // 获取参数
            String projectId = (String) context.getParameter("projectId");
            String projectPath = (String) context.getParameter("projectPath");
            Map<String, Object> buildConfig = (Map<String, Object>) context.getParameter("buildConfig");
            Map<String, Object> deploymentConfig = (Map<String, Object>) context.getParameter("deploymentConfig");
            
            // 验证必填参数
            if (projectId == null || projectId.isEmpty()) {
                throw new SkillException(getId(), "项目ID不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            if (projectPath == null || projectPath.isEmpty()) {
                throw new SkillException(getId(), "项目路径不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            if (buildConfig == null || buildConfig.isEmpty()) {
                throw new SkillException(getId(), "构建配置不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            if (deploymentConfig == null || deploymentConfig.isEmpty()) {
                throw new SkillException(getId(), "部署配置不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            
            // 执行核心逻辑：本地部署
            boolean buildSuccess = executeBuild(projectPath, buildConfig);
            if (!buildSuccess) {
                throw new SkillException(getId(), "项目构建失败", SkillException.ErrorCode.EXECUTION_EXCEPTION);
            }
            
            boolean deploySuccess = executeDeployment(projectPath, deploymentConfig);
            if (!deploySuccess) {
                throw new SkillException(getId(), "项目部署失败", SkillException.ErrorCode.EXECUTION_EXCEPTION);
            }
            
            String accessUrl = generateAccessUrl(deploymentConfig);
            Map<String, Object> serviceInfo = generateServiceInfo(projectId, deploymentConfig);
            
            // 构建结果
            SkillResult result = new SkillResult();
            result.setMessage("项目本地部署成功");
            result.addData("projectId", projectId);
            result.addData("projectPath", projectPath);
            result.addData("accessUrl", accessUrl);
            result.addData("serviceInfo", serviceInfo);
            result.addData("status", "running");
            
            return result;
        } catch (SkillException e) {
            throw e;
        } catch (Exception e) {
            throw new SkillException(getId(), "本地部署失败: " + e.getMessage(), 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
        }
    }
    
    /**
     * 执行项目构建
     */
    private boolean executeBuild(String projectPath, Map<String, Object> buildConfig) {
        // 模拟构建过程
        System.out.println("Executing build in " + projectPath);
        System.out.println("Build command: " + buildConfig.get("buildCommand"));
        
        // 实际项目中，这里应该执行真实的构建命令
        // 例如：ProcessBuilder pb = new ProcessBuilder(buildCommand.split(" "));
        // pb.directory(new File(projectPath));
        // Process p = pb.start();
        // int exitCode = p.waitFor();
        // return exitCode == 0;
        
        return true; // 模拟构建成功
    }
    
    /**
     * 执行项目部署
     */
    private boolean executeDeployment(String projectPath, Map<String, Object> deploymentConfig) {
        // 模拟部署过程
        System.out.println("Executing deployment from " + projectPath);
        System.out.println("Deployment type: " + deploymentConfig.get("deploymentType"));
        
        // 实际项目中，这里应该执行真实的部署命令
        // 例如：使用Docker部署或直接启动应用
        
        return true; // 模拟部署成功
    }
    
    /**
     * 生成访问URL
     */
    private String generateAccessUrl(Map<String, Object> deploymentConfig) {
        String protocol = "http";
        String host = "localhost";
        int port = (Integer) deploymentConfig.getOrDefault("port", 8080);
        
        return protocol + "://" + host + ":" + port;
    }
    
    /**
     * 生成服务信息
     */
    private Map<String, Object> generateServiceInfo(String projectId, Map<String, Object> deploymentConfig) {
        Map<String, Object> serviceInfo = new HashMap<>();
        
        serviceInfo.put("serviceId", "service-" + projectId);
        serviceInfo.put("serviceName", "Project-" + projectId);
        serviceInfo.put("deploymentType", deploymentConfig.get("deploymentType"));
        serviceInfo.put("containerized", deploymentConfig.getOrDefault("containerized", false));
        serviceInfo.put("port", deploymentConfig.getOrDefault("port", 8080));
        serviceInfo.put("status", "running");
        serviceInfo.put("startTime", System.currentTimeMillis());
        
        return serviceInfo;
    }
    
    @Override
    public boolean isAvailable() {
        // 该技能需要本地环境支持，检查是否有必要的工具
        return checkLocalEnvironment();
    }
    
    /**
     * 检查本地环境是否支持部署
     */
    private boolean checkLocalEnvironment() {
        // 检查是否安装了必要的工具，如Java、Maven、Docker等
        // 实际项目中，这里应该执行真实的环境检查
        
        return true; // 模拟环境检查通过
    }
    
    @Override
    public Map<String, SkillParam> getParams() {
        return params;
    }
}