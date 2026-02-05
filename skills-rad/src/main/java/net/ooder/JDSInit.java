package net.ooder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication(scanBasePackages = {"view", "net.ooder"})
@EnableConfigurationProperties
public class JDSInit {

    public static void main(String[] args) {
        // 检测运行环境并设置相应的系统属性
        detectAndConfigureEnvironment();

        SpringApplication application = new SpringApplication(JDSInit.class);


        // WSL环境特殊配置
        if (isWSLEnvironment()) {
            System.out.println("[INFO] 检测到WSL环境，应用WSL优化配置");

            // 设置WSL特定的系统属性
            System.setProperty("java.awt.headless", "true");
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("java.net.preferIPv4Stack", "true");

            // 如果是WSL2，应用网络优化
            if (isWSL2()) {
                System.out.println("[INFO] 检测到WSL2，应用网络优化配置");
                System.setProperty("java.net.useSystemProxies", "false");
            }
        }

        application.run(args);
    }

    /**
     * 检测并配置运行环境
     */
    private static void detectAndConfigureEnvironment() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch");

        System.out.println("[INFO] 操作系统: " + osName + " (" + osArch + ")");

        if (isWSLEnvironment()) {
            System.out.println("[INFO] 运行环境: Windows Subsystem for Linux");
            if (isWSL2()) {
                System.out.println("[INFO] WSL版本: WSL2");
            } else {
                System.out.println("[INFO] WSL版本: WSL1");
            }
        } else if (osName.contains("linux")) {
            System.out.println("[INFO] 运行环境: Linux");
        } else if (osName.contains("windows")) {
            System.out.println("[INFO] 运行环境: Windows");
        } else if (osName.contains("mac")) {
            System.out.println("[INFO] 运行环境: macOS");
        }
    }

    /**
     * 检测是否在WSL环境中运行
     *
     * @return true如果在WSL中运行
     */
    private static boolean isWSLEnvironment() {
        try {
            // 检查/proc/version文件是否包含Microsoft字样
            File procVersion = new File("/proc/version");
            if (procVersion.exists()) {
                List<String> lines = Files.readAllLines(Paths.get("/proc/version"));
                for (String line : lines) {
                    if (line.toLowerCase().contains("microsoft")) {
                        return true;
                    }
                }
            }

            // 检查WSL环境变量
            String wslDistroName = System.getenv("WSL_DISTRO_NAME");
            if (wslDistroName != null && !wslDistroName.isEmpty()) {
                return true;
            }

            // 检查是否存在WSL特有的文件
            return new File("/mnt/c").exists();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检测是否是WSL2
     *
     * @return true如果是WSL2
     */
    private static boolean isWSL2() {
        try {
            File procVersion = new File("/proc/version");
            if (procVersion.exists()) {
                List<String> lines = Files.readAllLines(Paths.get("/proc/version"));
                for (String line : lines) {
                    String lowerLine = line.toLowerCase();
                    if (lowerLine.contains("microsoft") && lowerLine.contains("wsl2")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return false;
    }
}

/**
 * 应用启动后的配置组件
 */
@Component
class WSLConfigurationRunner implements ApplicationRunner {

    @Autowired
    private Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String port = environment.getProperty("server.port", "8083");

        System.out.println("[SUCCESS] OneCode RAD 启动成功!");
        System.out.println("[SUCCESS] 访问地址:");
        System.out.println("[SUCCESS]   本地: http://localhost:" + port);

        // 尝试获取网络IP地址
        try {
            String networkIP = getNetworkIP();
            if (networkIP != null && !networkIP.isEmpty()) {
                System.out.println("[SUCCESS]   网络: http://" + networkIP + ":" + port);
            }
        } catch (Exception e) {
            // 忽略获取网络IP失败的情况
        }

        if (isWSLEnvironment()) {
            System.out.println("[INFO] WSL提示: 可以在Windows浏览器中直接访问 http://localhost:" + port);
            System.out.println("[INFO] 如果无法访问，请检查Windows防火墙设置");
        }
    }

    /**
     * 获取网络IP地址
     */
    private String getNetworkIP() {
        try {
            java.net.InetAddress localHost = java.net.InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检测是否在WSL环境中运行
     */
    private boolean isWSLEnvironment() {
        try {
            File procVersion = new File("/proc/version");
            if (procVersion.exists()) {
                List<String> lines = Files.readAllLines(Paths.get("/proc/version"));
                for (String line : lines) {
                    if (line.toLowerCase().contains("microsoft")) {
                        return true;
                    }
                }
            }
            return new File("/mnt/c").exists();
        } catch (Exception e) {
            return false;
        }
    }
}
