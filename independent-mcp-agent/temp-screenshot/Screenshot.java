import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;

import java.io.File;

public class Screenshot {
    public static void main(String[] args) {
        // Set EdgeDriver path (try to find it automatically)
        
        // Configure Edge options
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--headless"); // Headless mode
        options.addArguments("--window-size=1280,1024"); // Set window size
        
        WebDriver driver = new EdgeDriver(options);
        
        try {
            // Access local service
            driver.get("http://localhost:8080");
            
            // Wait for page to load
            Thread.sleep(3000);
            
            // Take screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            
            // Save screenshot
            File saveFile = new File("E:\\ooder\\ooder-public\\super-agent\\independent-mcp-agent\\screenshots\\home\\dashboard.png");
            FileHandler.copy(screenshot, saveFile);
            
            System.out.println("Screenshot saved successfully!");
            System.out.println("File size: " + saveFile.length() + " bytes");
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
