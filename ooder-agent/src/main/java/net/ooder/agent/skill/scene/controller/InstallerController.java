package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.dto.installer.InstallerStatusDTO;
import net.ooder.mvp.skill.scene.dto.installer.InstallerStatusRequestDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

@RestController
@RequestMapping("/api/v1/installer")
public class InstallerController {
    
    private static final Logger log = LoggerFactory.getLogger(InstallerController.class);
    
    private static final String STATUS_DIR = "./data/installer";
    private static final String STATUS_FILE = "status.properties";
    
    @PostMapping("/status")
    public ResultModel<InstallerStatusDTO> saveStatus(@RequestBody InstallerStatusRequestDTO request) {
        log.info("[saveStatus] request: {}", request);
        
        try {
            Path dirPath = Paths.get(STATUS_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            Path filePath = dirPath.resolve(STATUS_FILE);
            Properties props = new Properties();
            
            if (Files.exists(filePath)) {
                try (InputStream is = Files.newInputStream(filePath)) {
                    props.load(is);
                }
            }
            
            String loop = request.getLoop();
            String status = request.getStatus();
            String completedAt = request.getCompletedAt();
            
            if (loop != null) {
                props.setProperty(loop + ".status", status != null ? status : "unknown");
                if (completedAt != null) {
                    props.setProperty(loop + ".completedAt", completedAt);
                }
            }
            
            try (OutputStream os = Files.newOutputStream(filePath)) {
                props.store(os, "Installer Status");
            }
            
            InstallerStatusDTO result = new InstallerStatusDTO();
            result.setLoop(loop);
            result.setStatus(status);
            result.setSaved(true);
            
            return ResultModel.success(result);
            
        } catch (Exception e) {
            log.error("[saveStatus] Failed to save status", e);
            return ResultModel.error("Failed to save status: " + e.getMessage());
        }
    }
    
    @GetMapping("/status")
    public ResultModel<InstallerStatusDTO> getStatus() {
        log.info("[getStatus] request");
        
        try {
            Path filePath = Paths.get(STATUS_DIR, STATUS_FILE);
            
            if (!Files.exists(filePath)) {
                InstallerStatusDTO empty = new InstallerStatusDTO();
                empty.setInitialized(false);
                return ResultModel.success(empty);
            }
            
            Properties props = new Properties();
            try (InputStream is = Files.newInputStream(filePath)) {
                props.load(is);
            }
            
            InstallerStatusDTO result = new InstallerStatusDTO();
            result.setInitialized(true);
            
            return ResultModel.success(result);
            
        } catch (Exception e) {
            log.error("[getStatus] Failed to get status", e);
            return ResultModel.error("Failed to get status: " + e.getMessage());
        }
    }
    
    @GetMapping("/status/{loop}")
    public ResultModel<InstallerStatusDTO> getLoopStatus(@PathVariable String loop) {
        log.info("[getLoopStatus] loop: {}", loop);
        
        try {
            Path filePath = Paths.get(STATUS_DIR, STATUS_FILE);
            
            if (!Files.exists(filePath)) {
                InstallerStatusDTO empty = new InstallerStatusDTO();
                empty.setLoop(loop);
                empty.setStatus("not_started");
                return ResultModel.success(empty);
            }
            
            Properties props = new Properties();
            try (InputStream is = Files.newInputStream(filePath)) {
                props.load(is);
            }
            
            InstallerStatusDTO result = new InstallerStatusDTO();
            result.setLoop(loop);
            result.setStatus(props.getProperty(loop + ".status", "not_started"));
            result.setCompletedAt(props.getProperty(loop + ".completedAt"));
            
            return ResultModel.success(result);
            
        } catch (Exception e) {
            log.error("[getLoopStatus] Failed to get status", e);
            return ResultModel.error("Failed to get status: " + e.getMessage());
        }
    }
}
