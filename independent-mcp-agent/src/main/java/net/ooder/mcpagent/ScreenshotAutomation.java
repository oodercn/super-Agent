package net.ooder.mcpagent;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScreenshotAutomation {

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
        // 设置本地WebDriver路径
        System.setProperty("webdriver.chrome.driver", "E:\\ooder\\ooder-public\\super-agent\\chromedriver.exe");
        
        // 配置Chrome选项
        ChromeOptions options = new ChromeOptions();
        options.setBinary("E:\\chrome-win64\\chrome.exe"); // 使用特定版本的Chrome
        options.addArguments("--headless"); // 无头模式运行
        options.addArguments("--window-size=1280,1024"); // 设置窗口大小
        options.addArguments("--disable-gpu"); // 禁用GPU加速
        options.addArguments("--no-sandbox"); // 禁用沙箱
        options.addArguments("--remote-allow-origins=*"); // 允许所有远程源
        options.addArguments("--remote-debugging-port=9222"); // 启用远程调试端口
        options.addArguments("--disable-dev-shm-usage"); // 禁用/dev/shm使用
        options.addArguments("--disable-extensions"); // 禁用扩展
        options.addArguments("--disable-features=site-per-process"); // 禁用站点隔离
        options.addArguments("--disable-web-security"); // 禁用Web安全
        options.addArguments("--allow-running-insecure-content"); // 允许运行不安全内容
        
        WebDriver driver = new ChromeDriver(options);
        
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
