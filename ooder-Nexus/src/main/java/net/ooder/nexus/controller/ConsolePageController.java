package net.ooder.nexus.controller;

import net.ooder.skill.hotplug.PluginManager;
import net.ooder.skill.hotplug.model.PluginContext;
import net.ooder.skill.hotplug.model.PluginInfo;
import net.ooder.skill.hotplug.model.SkillPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/console")
public class ConsolePageController {

    private static final Logger logger = LoggerFactory.getLogger(ConsolePageController.class);

    @Autowired
    private PluginManager pluginManager;

    @GetMapping("/**")
    public ResponseEntity<Resource> getConsoleResource(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        String resourcePath = requestPath.substring("/console/".length());
        
        logger.debug("Looking for console resource: {}", resourcePath);
        
        Resource classpathResource = new ClassPathResource("static/console/" + resourcePath);
        if (classpathResource.exists()) {
            logger.debug("Found resource in classpath: {}", resourcePath);
            MediaType contentType = getContentType(resourcePath);
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(classpathResource);
        }
        
        List<PluginInfo> plugins = pluginManager.getInstalledSkills();
        if (plugins == null || plugins.isEmpty()) {
            logger.warn("No plugins loaded");
            return ResponseEntity.notFound().build();
        }
        
        for (PluginInfo pluginInfo : plugins) {
            String skillId = pluginInfo.getSkillId();
            PluginContext context = pluginManager.getPluginContext(skillId);
            
            if (context == null) {
                continue;
            }
            
            SkillPackage skillPackage = context.getSkillPackage();
            
            if (skillPackage == null) {
                continue;
            }
            
            try {
                InputStream is = skillPackage.getResource("static/console/" + resourcePath);
                if (is != null) {
                    Resource resource = new InputStreamResource(is);
                    MediaType contentType = getContentType(resourcePath);
                    
                    logger.debug("Found resource in plugin {}: {}", skillId, resourcePath);
                    
                    return ResponseEntity.ok()
                            .contentType(contentType)
                            .body(resource);
                }
            } catch (Exception e) {
                logger.debug("Error reading resource from plugin {}: {}", skillId, e.getMessage());
            }
        }
        
        logger.debug("Resource not found: {}", resourcePath);
        return ResponseEntity.notFound().build();
    }
    
    private MediaType getContentType(String path) {
        return MediaType.parseMediaType(net.ooder.nexus.util.ContentTypeUtils.getContentType(path));
    }
}
