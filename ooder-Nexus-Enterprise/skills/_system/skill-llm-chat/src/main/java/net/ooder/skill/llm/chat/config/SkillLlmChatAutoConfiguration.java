package net.ooder.skill.llm.chat.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "net.ooder.skill.llm.chat",
    "net.ooder.skill.chat"
})
public class SkillLlmChatAutoConfiguration {
}
