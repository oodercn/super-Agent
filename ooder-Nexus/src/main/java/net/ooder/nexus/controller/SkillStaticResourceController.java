package net.ooder.nexus.controller;

import net.ooder.skill.hotplug.PluginManager;
import net.ooder.skill.hotplug.model.PluginContext;
import net.ooder.skill.hotplug.model.SkillPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RestController
@RequestMapping("/skill-resource")
public class SkillStaticResourceController {

    private static final Logger logger = LoggerFactory.getLogger(SkillStaticResourceController.class);

    @Autowired
    private PluginManager pluginManager;

    @GetMapping("/{skillId}/**")
    public ResponseEntity<Resource> getSkillResource(
            @PathVariable("skillId") String skillId,
            HttpServletRequest request) {
        
        logger.debug("Accessing skill resource: skillId={}, path={}", skillId, request.getRequestURI());
        
        PluginContext context = pluginManager.getPluginContext(skillId);
        if (context == null) {
            logger.warn("Skill not found: {}", skillId);
            return ResponseEntity.notFound().build();
        }
        
        String requestPath = request.getRequestURI();
        String prefix = "/skill-resource/" + skillId + "/";
        String resourcePath = requestPath.substring(prefix.length());
        
        logger.debug("Resource path: {}", resourcePath);
        
        SkillPackage skillPackage = context.getSkillPackage();
        if (skillPackage == null) {
            logger.warn("SkillPackage not found for skill: {}", skillId);
            return ResponseEntity.notFound().build();
        }
        
        try {
            InputStream is = skillPackage.getResource("static/" + resourcePath);
            if (is == null) {
                logger.debug("Resource not found in JAR: static/{}", resourcePath);
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new InputStreamResource(is);
            MediaType contentType = getContentType(resourcePath);
            
            logger.debug("Returning resource: {} with content-type: {}", resourcePath, contentType);
            
            return ResponseEntity.ok()
                    .contentType(contentType)
                    .body(resource);
                    
        } catch (Exception e) {
            logger.error("Error reading resource: " + resourcePath, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    private MediaType getContentType(String path) {
        return MediaType.parseMediaType(net.ooder.nexus.util.ContentTypeUtils.getContentType(path));
    }
}
