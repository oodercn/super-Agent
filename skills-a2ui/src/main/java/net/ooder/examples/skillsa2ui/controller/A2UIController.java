package net.ooder.examples.skillsa2ui.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * OODER A2UI Controller - 处理图生代码相关的HTTP请求
 */
@RestController
@RequestMapping("/api/a2ui")
public class A2UIController {

    /**
     * 生成UI界面
     * @param request 请求参数
     * @return 生成结果
     */
    @PostMapping("/generate-ui")
    public ResponseEntity<Map<String, Object>> generateUI(@RequestBody Map<String, Object> request) {
        try {
            // 获取请求参数
            String image = (String) request.get("image");
            String format = (String) request.getOrDefault("format", "html");
            Map<String, Object> options = (Map<String, Object>) request.getOrDefault("options", new HashMap<>());

            // 验证必要参数
            if (image == null || image.isEmpty()) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("success", false);
                errorMap.put("error", "图片参数不能为空");
                return ResponseEntity.badRequest().body(errorMap);
            }

            // 模拟AI分析和代码生成过程
            // 实际项目中，这里应该调用AI模型来分析图片并生成代码
            String generatedCode = generateCodeFromImage(image, format, options);

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("code", generatedCode);
            response.put("format", format);
            response.put("options", options);
            response.put("message", "UI界面生成成功");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("error", "生成UI界面失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * 预览UI界面
     * @param request 请求参数
     * @return 预览结果
     */
    @PostMapping("/preview-ui")
    public ResponseEntity<?> previewUI(@RequestBody Map<String, Object> request) {
        try {
            // 获取请求参数
            String uiCode = (String) request.get("uiCode");
            String format = (String) request.getOrDefault("format", "html");

            // 验证必要参数
            if (uiCode == null || uiCode.isEmpty()) {
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("success", false);
                errorMap.put("error", "UI代码参数不能为空");
                return ResponseEntity.badRequest().body(errorMap);
            }

            // 对于HTML格式，直接返回HTML内容
            if ("html".equals(format)) {
                return ResponseEntity.ok()
                        .header("Content-Type", "text/html")
                        .body(uiCode);
            } else {
                // 对于其他格式，返回JSON响应
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("preview", uiCode);
                response.put("format", format);
                response.put("message", "UI界面预览成功");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("error", "预览UI界面失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * 获取组件列表
     * @return 组件列表
     */
    @GetMapping("/components")
    public ResponseEntity<Map<String, Object>> getComponents() {
        try {
            // 构建组件列表
            Map<String, Object> components = new HashMap<>();
            
            // 基础组件
            components.put("basic", new String[]{
                    "Button", "Input", "Label", "CheckBox", "RadioBox"
            });
            
            // 布局组件
            components.put("layout", new String[]{
                    "Layout", "Panel", "Group", "ContentBlock"
            });
            
            // 导航组件
            components.put("navigation", new String[]{
                    "Tabs", "MenuBar", "ToolBar"
            });
            
            // 数据展示
            components.put("data", new String[]{
                    "List", "TreeView", "TreeGrid", "Table"
            });
            
            // 表单组件
            components.put("form", new String[]{
                    "FormLayout", "DatePicker", "TimePicker", "ColorPicker"
            });
            
            // 弹窗组件
            components.put("popup", new String[]{
                    "Dialog", "PopMenu"
            });
            
            // 媒体组件
            components.put("media", new String[]{
                    "Image", "Audio", "Video"
            });
            
            // 图表组件
            components.put("chart", new String[]{
                    "ECharts", "FusionChartsXT"
            });

            // 构建响应
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("components", components);
            response.put("total", countComponents(components));
            response.put("message", "获取组件列表成功");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("success", false);
            errorMap.put("error", "获取组件列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMap);
        }
    }

    /**
     * 健康检查
     * @return 健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthMap = new HashMap<>();
        healthMap.put("success", true);
        healthMap.put("status", "healthy");
        healthMap.put("service", "ooder-a2ui");
        healthMap.put("message", "OODER A2UI 服务运行正常");
        return ResponseEntity.ok(healthMap);
    }

    /**
     * 从图片生成代码（模拟实现）
     * @param image 图片
     * @param format 格式
     * @param options 选项
     * @return 生成的代码
     */
    private String generateCodeFromImage(String image, String format, Map<String, Object> options) {
        // 模拟AI分析和代码生成
        // 实际项目中，这里应该调用AI模型来分析图片并生成代码
        
        String theme = (String) options.getOrDefault("theme", "light");
        String componentVersion = (String) options.getOrDefault("componentVersion", "1.0.0");

        switch (format) {
            case "html":
                return generateHtmlCode(theme, componentVersion);
            case "js":
                return generateJsCode(theme, componentVersion);
            case "json":
                return generateJsonCode(theme, componentVersion);
            default:
                return generateHtmlCode(theme, componentVersion);
        }
    }

    /**
     * 生成HTML代码
     * @param theme 主题
     * @param componentVersion 组件版本
     * @return HTML代码
     */
    private String generateHtmlCode(String theme, String componentVersion) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"zh-CN\">\n" +
               "<head>\n" +
               "    <meta charset=\"UTF-8\">\n" +
               "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
               "    <title>OODER A2UI 生成示例</title>\n" +
               "    <link rel=\"stylesheet\" href=\"/ood/css/default.css\">\n" +
               "    <link rel=\"stylesheet\" href=\"/ood/css/\" + theme + \"/theme.css\">\n" +
               "</head>\n" +
               "<body>\n" +
               "    <div id=\"app\">\n" +
               "        <div class=\"container\">\n" +
               "            <h1>OODER A2UI 图生代码示例</h1>\n" +
               "            <div class=\"form\">\n" +
               "                <div class=\"form-group\">\n" +
               "                    <label>用户名:</label>\n" +
               "                    <input type=\"text\" id=\"username\" placeholder=\"请输入用户名\">\n" +
               "                </div>\n" +
               "                <div class=\"form-group\">\n" +
               "                    <label>密码:</label>\n" +
               "                    <input type=\"password\" id=\"password\" placeholder=\"请输入密码\">\n" +
               "                </div>\n" +
               "                <div class=\"form-group\">\n" +
               "                    <button id=\"loginBtn\" class=\"btn btn-primary\">登录</button>\n" +
               "                    <button id=\"resetBtn\" class=\"btn btn-secondary\">重置</button>\n" +
               "                </div>\n" +
               "            </div>\n" +
               "        </div>\n" +
               "    </div>\n" +
               "    <script src=\"/ood/js/UI.js\"></script>\n" +
               "    <script>\n" +
               "        // 初始化OODER UI\n" +
               "        window.onload = function() {\n" +
               "            // 示例代码\n" +
               "            console.log('OODER A2UI 初始化完成');\n" +
               "        }\n" +
               "    </script>\n" +
               "</body>\n" +
               "</html>";
    }

    /**
     * 生成JavaScript代码
     * @param theme 主题
     * @param componentVersion 组件版本
     * @return JavaScript代码
     */
    private String generateJsCode(String theme, String componentVersion) {
        return "// OODER A2UI 组件初始化代码\n" +
               "// 主题: " + theme + "\n" +
               "// 组件版本: " + componentVersion + "\n\n" +
               "// 初始化UI组件\n" +
               "function initUI() {\n" +
               "    // 创建布局\n" +
               "    var layout = new OOD.UI.Layout({\n" +
               "        id: 'mainLayout',\n" +
               "        theme: '" + theme + "',\n" +
               "        container: 'app'\n" +
               "    });\n\n" +
               "    // 创建表单\n" +
               "    var form = new OOD.UI.FormLayout({\n" +
               "        id: 'loginForm',\n" +
               "        title: '登录表单'\n" +
               "    });\n\n" +
               "    // 添加表单字段\n" +
               "    form.addField({\n" +
               "        name: 'username',\n" +
               "        type: 'text',\n" +
               "        label: '用户名',\n" +
               "        required: true\n" +
               "    });\n\n" +
               "    form.addField({\n" +
               "        name: 'password',\n" +
               "        type: 'password',\n" +
               "        label: '密码',\n" +
               "        required: true\n" +
               "    });\n\n" +
               "    // 添加按钮\n" +
               "    var buttonGroup = new OOD.UI.ButtonLayout({\n" +
               "        id: 'buttonGroup',\n" +
               "        align: 'right'\n" +
               "    });\n\n" +
               "    buttonGroup.addButton({\n" +
               "        id: 'loginBtn',\n" +
               "        text: '登录',\n" +
               "        type: 'primary',\n" +
               "        onClick: function() {\n" +
               "            console.log('登录按钮点击');\n" +
               "        }\n" +
               "    });\n\n" +
               "    buttonGroup.addButton({\n" +
               "        id: 'resetBtn',\n" +
               "        text: '重置',\n" +
               "        type: 'secondary',\n" +
               "        onClick: function() {\n" +
               "            console.log('重置按钮点击');\n" +
               "        }\n" +
               "    });\n\n" +
               "    // 添加到布局\n" +
               "    layout.addChild(form);\n" +
               "    layout.addChild(buttonGroup);\n\n" +
               "    // 渲染布局\n" +
               "    layout.render();\n" +
               "}\n\n" +
               "// 页面加载完成后初始化\n" +
               "window.onload = initUI;";
    }

    /**
     * 生成JSON代码
     * @param theme 主题
     * @param componentVersion 组件版本
     * @return JSON代码
     */
    private String generateJsonCode(String theme, String componentVersion) {
        return "{\n" +
               "  \"ui\": {\n" +
               "    \"theme\": \"" + theme + "\",\n" +
               "    \"version\": \"" + componentVersion + "\",\n" +
               "    \"components\": [\n" +
               "      {\n" +
               "        \"type\": \"Layout\",\n" +
               "        \"id\": \"mainLayout\",\n" +
               "        \"children\": [\n" +
               "          {\n" +
               "            \"type\": \"FormLayout\",\n" +
               "            \"id\": \"loginForm\",\n" +
               "            \"title\": \"登录表单\",\n" +
               "            \"fields\": [\n" +
               "              {\n" +
               "                \"name\": \"username\",\n" +
               "                \"type\": \"text\",\n" +
               "                \"label\": \"用户名\",\n" +
               "                \"required\": true\n" +
               "              },\n" +
               "              {\n" +
               "                \"name\": \"password\",\n" +
               "                \"type\": \"password\",\n" +
               "                \"label\": \"密码\",\n" +
               "                \"required\": true\n" +
               "              }\n" +
               "            ]\n" +
               "          },\n" +
               "          {\n" +
               "            \"type\": \"ButtonLayout\",\n" +
               "            \"id\": \"buttonGroup\",\n" +
               "            \"align\": \"right\",\n" +
               "            \"buttons\": [\n" +
               "              {\n" +
               "                \"id\": \"loginBtn\",\n" +
               "                \"text\": \"登录\",\n" +
               "                \"type\": \"primary\",\n" +
               "                \"onClick\": \"handleLogin\"\n" +
               "              },\n" +
               "              {\n" +
               "                \"id\": \"resetBtn\",\n" +
               "                \"text\": \"重置\",\n" +
               "                \"type\": \"secondary\",\n" +
               "                \"onClick\": \"handleReset\"\n" +
               "              }\n" +
               "            ]\n" +
               "          }\n" +
               "        ]\n" +
               "      }\n" +
               "    ]\n" +
               "  },\n" +
               "  \"scripts\": [\n" +
               "    {\n" +
               "      \"name\": \"handleLogin\",\n" +
               "      \"code\": \"console.log('登录按钮点击');\"\n" +
               "    },\n" +
               "    {\n" +
               "      \"name\": \"handleReset\",\n" +
               "      \"code\": \"console.log('重置按钮点击');\"\n" +
               "    }\n" +
               "  ]\n" +
               "}";
    }

    /**
     * 计算组件总数
     * @param components 组件列表
     * @return 组件总数
     */
    private int countComponents(Map<String, Object> components) {
        int count = 0;
        for (Object value : components.values()) {
            if (value instanceof String[]) {
                count += ((String[]) value).length;
            }
        }
        return count;
    }
}
