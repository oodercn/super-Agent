package net.ooder.skillcenter.shell.lifecycle;

import net.ooder.skillcenter.shell.AbstractCommand;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager.RegistrationResult;
import net.ooder.skillcenter.lifecycle.registration.SkillRegistrationManager.SkillRegistrationRequest;
import net.ooder.skillcenter.lifecycle.registration.impl.SkillRegistrationManagerImpl;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SkillRegisterCommand extends AbstractCommand {
    
    private SkillRegistrationManager registrationManager;
    
    public SkillRegisterCommand() {
        super();
        this.registrationManager = new SkillRegistrationManagerImpl();
    }
    
    @Override
    public String getName() {
        return "skill register";
    }
    
    @Override
    public String getDescription() {
        return "注册新技能";
    }
    
    @Override
    public void execute(String[] args) throws Exception {
        if (args.length < 1) {
            error("用法: skill register <skillFile> [options]");
            println("选项:");
            println("  --name <name>             - 技能名称");
            println("  --description <desc>       - 技能描述");
            println("  --category <category>       - 技能分类");
            println("  --version <version>        - 技能版本");
            return;
        }
        
        String skillFile = args[0];
        File file = new File(skillFile);
        
        if (!file.exists()) {
            error("技能文件不存在: " + skillFile);
            return;
        }
        
        SkillRegistrationRequest request = new SkillRegistrationRequest();
        request.setSkillId("skill-" + System.currentTimeMillis());
        request.setApplicant(System.getProperty("user.name"));
        
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("--name=")) {
                request.setSkillName(args[i].substring(7));
            } else if (args[i].startsWith("--description=")) {
                request.setDescription(args[i].substring(14));
            } else if (args[i].startsWith("--category=")) {
                request.setCategory(args[i].substring(11));
            } else if (args[i].startsWith("--version=")) {
                request.setVersion(args[i].substring(10));
            }
        }
        
        if (request.getSkillName() == null) {
            request.setSkillName(file.getName());
        }
        
        byte[] skillCode = Files.readAllBytes(file.toPath());
        request.setSkillCode(skillCode);
        
        Map<String, String> parameters = new HashMap<>();
        request.setParameters(parameters);
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("fileName", file.getName());
        metadata.put("fileSize", file.length());
        request.setMetadata(metadata);
        
        println("正在注册技能...");
        println("技能名称: " + request.getSkillName());
        println("技能文件: " + skillFile);
        
        RegistrationResult result = registrationManager.registerSkill(request);
        
        if (result.isSuccess()) {
            success("技能注册成功");
            println("技能 ID: " + result.getSkillId());
            println("状态: " + result.getStatus());
            println("时间: " + formatTimestamp(result.getTimestamp()));
        } else {
            error("技能注册失败: " + result.getMessage());
        }
    }
    
    private String formatTimestamp(long timestamp) {
        return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new java.util.Date(timestamp));
    }
}
