package net.ooder.skillcenter.model.impl;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillParam;
import net.ooder.skillcenter.model.SkillResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 文本转大写技能实现
 */
public class TextToUppercaseSkill implements Skill {
    private static final String SKILL_ID = "text-to-uppercase-skill";
    private static final String SKILL_NAME = "文本转大写技能";
    private static final String SKILL_DESCRIPTION = "将输入文本转换为大写格式";
    
    private Map<String, SkillParam> params;
    
    public TextToUppercaseSkill() {
        initParams();
    }
    
    /**
     * 初始化技能参数
     */
    private void initParams() {
        params = new HashMap<>();
        
        // 输入文本参数
        params.put("inputText", new SkillParam(
                "inputText",
                "输入文本",
                String.class,
                true
        ));
        
        // 输出前缀参数
        params.put("prefix", new SkillParam(
                "prefix",
                "输出前缀",
                String.class,
                false,
                "RESULT: "
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
            String inputText = (String) context.getParameter("inputText");
            String prefix = (String) context.getParameter("prefix");
            
            // 使用默认值
            if (prefix == null) {
                prefix = "RESULT: ";
            }
            
            // 验证必填参数
            if (inputText == null || inputText.isEmpty()) {
                throw new SkillException(getId(), "输入文本不能为空", SkillException.ErrorCode.PARAMETER_ERROR);
            }
            
            // 执行核心逻辑：文本转大写
            String resultText = prefix + inputText.toUpperCase();
            
            // 构建结果
            SkillResult result = new SkillResult();
            result.setMessage("文本转换成功");
            result.addData("resultText", resultText);
            result.addData("originalText", inputText);
            result.addData("prefix", prefix);
            
            return result;
        } catch (SkillException e) {
            throw e;
        } catch (Exception e) {
            throw new SkillException(getId(), "文本转换失败: " + e.getMessage(), 
                                     SkillException.ErrorCode.EXECUTION_EXCEPTION, e);
        }
    }
    
    @Override
    public boolean isAvailable() {
        // 该技能不需要外部依赖，始终可用
        return true;
    }
    
    @Override
    public Map<String, SkillParam> getParams() {
        return params;
    }
}
