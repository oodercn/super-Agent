package net.ooder.examples.skillsa2ui.controller;

import net.ooder.examples.skillsa2ui.service.VfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/vfs")
public class VfsController {

    private final VfsService vfsService;

    @Autowired
    public VfsController(VfsService vfsService) {
        this.vfsService = vfsService;
    }

    @GetMapping("/status")
    public ResponseEntity<?> getVfsStatus() {
        boolean isAvailable = vfsService.isVfsAvailable();
        return ResponseEntity.ok()
                .body("{\"status\": \"" + (isAvailable ? "available" : "unavailable") + "\", \"message\": \"VFS server is " + (isAvailable ? "available" : "unavailable") + "\"}");
    }

    @PostMapping("/sync")
    public ResponseEntity<?> syncWebDirectory() {
        vfsService.syncWebDirectory();
        return ResponseEntity.ok()
                .body("{\"status\": \"success\", \"message\": \"Web directory sync started\"}");
    }

    @PostMapping("/webpage")
    public ResponseEntity<?> updateWebPage(
            @RequestParam("path") String path,
            @RequestBody String content) {
        vfsService.updateWebPage(path, content);
        return ResponseEntity.ok()
                .body("{\"status\": \"success\", \"message\": \"Web page updated successfully\", \"path\": \"" + path + "\"}");
    }

    @DeleteMapping("/webpage")
    public ResponseEntity<?> deleteWebPage(
            @RequestParam("path") String path) {
        vfsService.deleteWebPage(path);
        return ResponseEntity.ok()
                .body("{\"status\": \"success\", \"message\": \"Web page deleted successfully\", \"path\": \"" + path + "\"}");
    }

    @GetMapping("/webpage")
    public ResponseEntity<?> getWebPage(
            @RequestParam("path") String path) {
        try {
            byte[] content = vfsService.getWebPageContent(path);
            return ResponseEntity.ok()
                    .header("Content-Type", "text/html; charset=utf-8")
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"status\": \"error\", \"message\": \"Web page not found\", \"path\": \"" + path + "\"}");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok()
                .body("{\"status\": \"ok\", \"message\": \"VFS service is running\"}");
    }
}
