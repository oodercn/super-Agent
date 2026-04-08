package net.ooder.enexus.controller;

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
        if (path.endsWith(".html")) return MediaType.TEXT_HTML;
        if (path.endsWith(".css")) return MediaType.parseMediaType("text/css");
        if (path.endsWith(".js")) return MediaType.parseMediaType("application/javascript");
        if (path.endsWith(".json")) return MediaType.APPLICATION_JSON;
        if (path.endsWith(".png")) return MediaType.IMAGE_PNG;
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (path.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (path.endsWith(".svg")) return MediaType.parseMediaType("image/svg+xml");
        if (path.endsWith(".ico")) return MediaType.parseMediaType("image/x-icon");
        if (path.endsWith(".woff")) return MediaType.parseMediaType("font/woff");
        if (path.endsWith(".woff2")) return MediaType.parseMediaType("font/woff2");
        if (path.endsWith(".ttf")) return MediaType.parseMediaType("font/ttf");
        if (path.endsWith(".eot")) return MediaType.parseMediaType("application/vnd.ms-fontobject");
        if (path.endsWith(".xml")) return MediaType.APPLICATION_XML;
        if (path.endsWith(".pdf")) return MediaType.APPLICATION_PDF;
        if (path.endsWith(".zip")) return MediaType.APPLICATION_OCTET_STREAM;
        if (path.endsWith(".txt")) return MediaType.TEXT_PLAIN;
        
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
