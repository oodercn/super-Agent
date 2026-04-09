package net.ooder.nexus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String index() {
        return "redirect:/console/index.html";
    }
    
    @GetMapping("/console")
    public String console() {
        return "redirect:/console/index.html";
    }
}
