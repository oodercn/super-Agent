package net.ooder.enexus.controller;

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
