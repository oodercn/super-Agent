package net.ooder.mcpagent;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EdgeScreenshotAutomation {

    private static class ScreenshotTask {
        String url;
        String savePath;
        String description;

        ScreenshotTask(String url, String savePath, String description) {
            this.url = url;
            this.savePath = savePath;
            this.description = description;
        }
    }

    public static void main(String[] args) {
        // 设置本地EdgeDriver路径（假设EdgeDriver在系统路径中或指定路径）
        // 注意：EdgeDriver通常随Microsoft Edge浏览器一起安装在Windows系统中
        
        // 配置Edge选项
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless"); // 无头模式运行
        options.addArguments("--window-size=1280,1024"); // 设置窗口大小
        options.addArguments("--disable-gpu"); // 禁用GPU加速
        options.addArguments("--no-sandbox"); // 禁用沙箱
        options.addArguments("--remote-allow-origins=*"); // 允许所有远程源
        options.addArguments("--disable-dev-shm-usage"); // 禁用/dev/shm使用

        WebDriver driver = new EdgeDriver(options);
        
        try {
            // 定义需要截图的任务列表
            List<ScreenshotTask> tasks = new ArrayList<>();
            
            // Home用户截图
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/index.html",
                "screenshots/home/dashboard.png",
                "Home user dashboard"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/home/home-dashboard.html",
                "screenshots/home/device-control.png",
                "Home user device control"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/home/device-list.html",
                "screenshots/home/device-list.png",
                "Home user device list"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/home/security-status.html",
                "screenshots/home/security-status.png",
                "Home user security status"
            ));
            
            // LAN管理员截图
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/lan/lan-dashboard.html",
                "screenshots/lan/dashboard.png",
                "LAN admin dashboard"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/lan/network-devices.html",
                "screenshots/lan/device-management.png",
                "LAN admin device management"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/lan/network-status.html",
                "screenshots/lan/network-topology.png",
                "LAN admin network topology"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/lan/bandwidth-monitor.html",
                "screenshots/lan/security-monitoring.png",
                "LAN admin security monitoring"
            ));
            
            // Enterprise管理员截图
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/enterprise/enterprise-dashboard.html",
                "screenshots/enterprise/dashboard.png",
                "Enterprise admin dashboard"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/command/command-dashboard.html",
                "screenshots/enterprise/analytics-dashboard.png",
                "Enterprise admin analytics dashboard"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/command/network-links.html",
                "screenshots/enterprise/multi-site-management.png",
                "Enterprise admin multi-site management"
            ));
            tasks.add(new ScreenshotTask(
                "http://localhost:8091/console/pages/system/service-management.html",
                "screenshots/enterprise/advanced-security.png",
                "Enterprise admin advanced security"
            ));
            
            // 执行截图任务
            for (ScreenshotTask task : tasks) {
                System.out.println("Capturing screenshot for: " + task.description);
                System.out.println("URL: " + task.url);
                System.out.println("Save path: " + task.savePath);
                
                try {
                    // 访问URL
                    driver.get(task.url);
                    
                    // 等待页面加载
                    Thread.sleep(2000);
                    
                    // 截图
                    File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    
                    // 确保目录存在
                    File saveFile = new File(task.savePath);
                    if (!saveFile.getParentFile().exists()) {
                        saveFile.getParentFile().mkdirs();
                    }
                    
                    // 保存截图
                    FileHandler.copy(screenshot, saveFile);
                    
                    System.out.println("✓ Screenshot saved successfully!");
                    System.out.println("File size: " + saveFile.length() + " bytes");
                } catch (Exception e) {
                    System.out.println("✗ Error capturing screenshot: " + e.getMessage());
                }
                System.out.println();
            }
            
        } finally {
            // 关闭浏览器
            driver.quit();
            System.out.println("Screenshot automation completed!");
        }
    }
}