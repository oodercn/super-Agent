package net.ooder.nexus.adapter.inbound.controller.personal.help;

import net.ooder.nexus.common.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 个人帮助文档控制器
 *
 * <p>提供帮助文档和FAQ相关接口</p>
 *
 * @author ooder Team
 * @version 2.0.0
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/personal/help")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class PersonalHelpController {

    /**
     * 获取帮助文档内容
     *
     * @param request 包含 section 的请求
     * @return 帮助文档内容
     */
    @PostMapping("/content")
    @ResponseBody
    public ResultModel<Map<String, Object>> getHelpContent(@RequestBody Map<String, String> request) {
        try {
            String section = request.get("section");

            Map<String, Object> content = new HashMap<>();

            if (section == null || "overview".equals(section)) {
                content.put("title", "个人中心帮助");
                content.put("description", "个人中心提供技能管理、分享、分组等功能。");

                List<Map<String, Object>> sections = new ArrayList<>();

                Map<String, Object> skillSection = new HashMap<>();
                skillSection.put("id", "skills");
                skillSection.put("title", "技能管理");
                skillSection.put("description", "管理您的个人技能，包括发布、更新、删除等操作。");
                sections.add(skillSection);

                Map<String, Object> shareSection = new HashMap<>();
                shareSection.put("id", "sharing");
                shareSection.put("title", "分享管理");
                shareSection.put("description", "分享您的技能给其他用户，或接收他人分享的技能。");
                sections.add(shareSection);

                Map<String, Object> groupSection = new HashMap<>();
                groupSection.put("id", "groups");
                groupSection.put("title", "分组管理");
                groupSection.put("description", "将技能分组管理，便于分类和查找。");
                sections.add(groupSection);

                Map<String, Object> identitySection = new HashMap<>();
                identitySection.put("id", "identity");
                identitySection.put("title", "身份信息");
                identitySection.put("description", "管理您的个人资料和账户安全设置。");
                sections.add(identitySection);

                content.put("sections", sections);
            } else if ("skills".equals(section)) {
                content.put("title", "技能管理帮助");
                content.put("content", "技能管理模块允许您发布、编辑、删除个人技能。您可以通过技能列表查看所有已发布的技能，点击技能名称可以编辑详细信息。");

                List<Map<String, Object>> faqs = new ArrayList<>();

                Map<String, Object> faq1 = new HashMap<>();
                faq1.put("question", "如何发布新技能？");
                faq1.put("answer", "点击技能列表页面的'发布技能'按钮，填写技能名称、描述、版本等信息后提交即可。");
                faqs.add(faq1);

                Map<String, Object> faq2 = new HashMap<>();
                faq2.put("question", "技能版本如何管理？");
                faq2.put("answer", "在编辑技能时可以更新版本号，系统会自动保存历史版本记录。");
                faqs.add(faq2);

                content.put("faqs", faqs);
            } else if ("sharing".equals(section)) {
                content.put("title", "分享管理帮助");
                content.put("content", "分享功能允许您将技能分享给其他用户或公开分享。您也可以接收其他用户分享的技能。");

                List<Map<String, Object>> faqs = new ArrayList<>();

                Map<String, Object> faq1 = new HashMap<>();
                faq1.put("question", "如何分享技能？");
                faq1.put("answer", "在技能列表中选择要分享的技能，点击'分享'按钮，选择分享类型（公开或指定用户）后确认即可。");
                faqs.add(faq1);

                Map<String, Object> faq2 = new HashMap<>();
                faq2.put("question", "如何接收他人分享的技能？");
                faq2.put("answer", "当其他用户分享技能给您时，您会在'接收的分享'列表中看到，点击'接受'即可添加到您的技能库。");
                faqs.add(faq2);

                content.put("faqs", faqs);
            } else if ("groups".equals(section)) {
                content.put("title", "分组管理帮助");
                content.put("content", "分组功能帮助您将技能分类管理，便于快速查找和组织。");

                List<Map<String, Object>> faqs = new ArrayList<>();

                Map<String, Object> faq1 = new HashMap<>();
                faq1.put("question", "如何创建分组？");
                faq1.put("answer", "在分组管理页面点击'新建分组'，输入分组名称和描述后保存即可。");
                faqs.add(faq1);

                Map<String, Object> faq2 = new HashMap<>();
                faq2.put("question", "如何将技能添加到分组？");
                faq2.put("answer", "在技能列表中选择技能，点击'移动到分组'，选择目标分组即可。");
                faqs.add(faq2);

                content.put("faqs", faqs);
            } else if ("identity".equals(section)) {
                content.put("title", "身份信息帮助");
                content.put("content", "身份信息模块用于管理您的个人资料和账户安全。");

                List<Map<String, Object>> faqs = new ArrayList<>();

                Map<String, Object> faq1 = new HashMap<>();
                faq1.put("question", "如何修改个人信息？");
                faq1.put("answer", "在身份信息页面点击'编辑'，修改相关信息后保存即可。");
                faqs.add(faq1);

                Map<String, Object> faq2 = new HashMap<>();
                faq2.put("question", "如何修改密码？");
                faq2.put("answer", "在身份信息页面点击'修改密码'，输入旧密码和新密码后确认即可。");
                faqs.add(faq2);

                content.put("faqs", faqs);
            } else {
                return ResultModel.error("Help section not found: " + section, 404);
            }

            return ResultModel.success(content);
        } catch (Exception e) {
            return ResultModel.error("Failed to get help content: " + e.getMessage(), 500);
        }
    }

    /**
     * 搜索帮助文档
     *
     * @param request 包含 keyword 的请求
     * @return 搜索结果
     */
    @PostMapping("/search")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> searchHelp(@RequestBody Map<String, String> request) {
        try {
            String keyword = request.get("keyword");

            if (keyword == null || keyword.trim().isEmpty()) {
                return ResultModel.success(new ArrayList<>());
            }

            List<Map<String, Object>> results = new ArrayList<>();

            Map<String, Object> result1 = new HashMap<>();
            result1.put("id", "skills-001");
            result1.put("title", "如何发布技能");
            result1.put("section", "skills");
            result1.put("summary", "发布技能的详细步骤和注意事项...");
            results.add(result1);

            Map<String, Object> result2 = new HashMap<>();
            result2.put("id", "sharing-001");
            result2.put("title", "分享技能的方法");
            result2.put("section", "sharing");
            result2.put("summary", "将技能分享给其他用户的操作指南...");
            results.add(result2);

            Map<String, Object> result3 = new HashMap<>();
            result3.put("id", "groups-001");
            result3.put("title", "创建和管理分组");
            result3.put("section", "groups");
            result3.put("summary", "使用分组功能组织您的技能...");
            results.add(result3);

            return ResultModel.success(results);
        } catch (Exception e) {
            return ResultModel.error("Failed to search help: " + e.getMessage(), 500);
        }
    }

    /**
     * 获取常见问题列表
     *
     * @return FAQ列表
     */
    @PostMapping("/faq")
    @ResponseBody
    public ResultModel<List<Map<String, Object>>> getFaqList() {
        try {
            List<Map<String, Object>> faqs = new ArrayList<>();

            Map<String, Object> faq1 = new HashMap<>();
            faq1.put("id", "faq-001");
            faq1.put("question", "什么是ooder平台？");
            faq1.put("answer", "ooder是一个去中心化的技能共享平台，允许用户发布、分享和执行各种技能。");
            faq1.put("category", "general");
            faqs.add(faq1);

            Map<String, Object> faq2 = new HashMap<>();
            faq2.put("id", "faq-002");
            faq2.put("question", "如何注册账户？");
            faq2.put("answer", "访问注册页面，填写用户名、邮箱和密码即可完成注册。");
            faq2.put("category", "account");
            faqs.add(faq2);

            Map<String, Object> faq3 = new HashMap<>();
            faq3.put("id", "faq-003");
            faq3.put("question", "技能执行失败怎么办？");
            faq3.put("answer", "请检查技能配置是否正确，查看执行日志获取详细错误信息，或联系技能发布者。");
            faq3.put("category", "skills");
            faqs.add(faq3);

            Map<String, Object> faq4 = new HashMap<>();
            faq4.put("id", "faq-004");
            faq4.put("question", "如何保护我的隐私？");
            faq4.put("answer", "您可以在身份信息设置中管理隐私选项，控制哪些信息对其他用户可见。");
            faq4.put("category", "privacy");
            faqs.add(faq4);

            return ResultModel.success(faqs);
        } catch (Exception e) {
            return ResultModel.error("Failed to get FAQ list: " + e.getMessage(), 500);
        }
    }

    /**
     * 提交反馈
     *
     * @param request 包含 type、content、contact 的请求
     * @return 提交结果
     */
    @PostMapping("/feedback")
    @ResponseBody
    public ResultModel<Map<String, Object>> submitFeedback(@RequestBody Map<String, Object> request) {
        try {
            String type = (String) request.get("type");
            String content = (String) request.get("content");
            String contact = (String) request.get("contact");

            if (content == null || content.trim().isEmpty()) {
                return ResultModel.error("Feedback content is required", 400);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("feedbackId", UUID.randomUUID().toString());
            result.put("status", "submitted");
            result.put("submitTime", System.currentTimeMillis());

            return ResultModel.success(result);
        } catch (Exception e) {
            return ResultModel.error("Failed to submit feedback: " + e.getMessage(), 500);
        }
    }
}
