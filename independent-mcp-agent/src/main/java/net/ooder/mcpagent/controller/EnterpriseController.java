package net.ooder.mcpagent.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 企业功能控制器
 */
@Controller
@RequestMapping("/console/pages/enterprise")
public class EnterpriseController {
    
    /**
     * 处理企业目录路径访问，重定向到企业仪表盘
     */
    @RequestMapping("/")
    public String enterpriseHome() {
        return "redirect:enterprise-dashboard.html";
    }
}
