package net.ooder.skillcenter.distribution;

import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;

import java.io.*;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 技能分发管理器，负责技能的打包、分发和加载
 * 实现无状态技能分发机制
 */
public class SkillDistributionManager {
    // 单例实例
    private static SkillDistributionManager instance;
    
    // 技能缓存，key为技能ID，value为技能实例
    private Map<String, Skill> skillCache;
    
    // 技能元数据缓存
    private Map<String, SkillMetadata> skillMetadataCache;
    
    /**
     * 私有构造方法
     */
    private SkillDistributionManager() {
        this.skillCache = new ConcurrentHashMap<>();
        this.skillMetadataCache = new ConcurrentHashMap<>();
    }
    
    /**
     * 获取实例
     * @return 技能分发管理器实例
     */
    public static synchronized SkillDistributionManager getInstance() {
        if (instance == null) {
            instance = new SkillDistributionManager();
        }
        return instance;
    }
    
    /**
     * 打包技能为分发格式
     * @param skill 技能实例
     * @return 打包后的技能数据
     * @throws SkillException 打包异常
     */
    public byte[] packageSkill(Skill skill) throws SkillException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            
            // 序列化技能实例
            oos.writeObject(skill);
            oos.flush();
            
            return baos.toByteArray();
        } catch (Exception e) {
            throw new SkillException(skill.getId(), "Failed to package skill: " + e.getMessage(), 
                                     SkillException.ErrorCode.DISTRIBUTION_ERROR, e);
        }
    }
    
    /**
     * 将技能打包为Base64格式，便于网络传输
     * @param skill 技能实例
     * @return Base64编码的技能数据
     * @throws SkillException 打包异常
     */
    public String packageSkillToBase64(Skill skill) throws SkillException {
        byte[] skillData = packageSkill(skill);
        return Base64.getEncoder().encodeToString(skillData);
    }
    
    /**
     * 从打包数据中加载技能
     * @param skillData 打包的技能数据
     * @return 加载的技能实例
     * @throws SkillException 加载异常
     */
    public Skill loadSkill(byte[] skillData) throws SkillException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(skillData);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            
            // 反序列化技能实例
            Object obj = ois.readObject();
            if (obj instanceof Skill) {
                Skill skill = (Skill) obj;
                skillCache.put(skill.getId(), skill);
                return skill;
            } else {
                throw new SkillException("unknown", "Invalid skill data format", 
                                         SkillException.ErrorCode.DISTRIBUTION_ERROR);
            }
        } catch (Exception e) {
            throw new SkillException("unknown", "Failed to load skill: " + e.getMessage(), 
                                     SkillException.ErrorCode.DISTRIBUTION_ERROR, e);
        }
    }
    
    /**
     * 从Base64格式加载技能
     * @param base64Data Base64编码的技能数据
     * @return 加载的技能实例
     * @throws SkillException 加载异常
     */
    public Skill loadSkillFromBase64(String base64Data) throws SkillException {
        try {
            byte[] skillData = Base64.getDecoder().decode(base64Data);
            return loadSkill(skillData);
        } catch (Exception e) {
            throw new SkillException("unknown", "Failed to load skill from Base64: " + e.getMessage(), 
                                     SkillException.ErrorCode.DISTRIBUTION_ERROR, e);
        }
    }
    
    /**
     * 执行远程技能
     * @param skillData 技能数据
     * @param context 执行上下文
     * @return 执行结果
     * @throws SkillException 执行异常
     */
    public SkillResult executeRemoteSkill(byte[] skillData, SkillContext context) throws SkillException {
        Skill skill = loadSkill(skillData);
        return skill.execute(context);
    }
    
    /**
     * 执行远程技能（Base64格式）
     * @param base64Data Base64编码的技能数据
     * @param context 执行上下文
     * @return 执行结果
     * @throws SkillException 执行异常
     */
    public SkillResult executeRemoteSkillFromBase64(String base64Data, SkillContext context) throws SkillException {
        Skill skill = loadSkillFromBase64(base64Data);
        return skill.execute(context);
    }
    
    /**
     * 获取技能元数据
     * @param skillId 技能ID
     * @return 技能元数据
     */
    public SkillMetadata getSkillMetadata(String skillId) {
        return skillMetadataCache.get(skillId);
    }
    
    /**
     * 注册技能元数据
     * @param skill 技能实例
     */
    public void registerSkillMetadata(Skill skill) {
        SkillMetadata metadata = new SkillMetadata();
        metadata.setSkillId(skill.getId());
        metadata.setSkillName(skill.getName());
        metadata.setSkillDescription(skill.getDescription());
        metadata.setParams(skill.getParams());
        metadata.setAvailable(skill.isAvailable());
        
        skillMetadataCache.put(skill.getId(), metadata);
    }
    
    /**
     * 从缓存中获取技能
     * @param skillId 技能ID
     * @return 技能实例，不存在则返回null
     */
    public Skill getSkillFromCache(String skillId) {
        return skillCache.get(skillId);
    }
    
    /**
     * 清理技能缓存
     */
    public void clearCache() {
        skillCache.clear();
        skillMetadataCache.clear();
    }
    
    /**
     * 清理指定技能的缓存
     * @param skillId 技能ID
     */
    public void clearSkillCache(String skillId) {
        skillCache.remove(skillId);
        skillMetadataCache.remove(skillId);
    }
}
