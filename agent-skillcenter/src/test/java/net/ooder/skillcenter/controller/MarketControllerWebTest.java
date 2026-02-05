package net.ooder.skillcenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import net.ooder.skillcenter.market.SkillReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 市场API的web测试用例
 */
@SpringBootTest
@AutoConfigureMockMvc
public class MarketControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillMarketManager marketManager;

    @Autowired
    private ObjectMapper objectMapper;

    private List<SkillListing> mockSkillListings;
    private List<SkillReview> mockSkillReviews;
    private List<String> mockCategories;

    @BeforeEach
    public void setUp() throws Exception {
        // 初始化模拟数据
        mockSkillListings = new ArrayList<>();
        SkillListing skillListing = new SkillListing();
        skillListing.setSkillId("text-to-uppercase-skill");
        skillListing.setName("文本转大写技能");
        skillListing.setCategory("text-processing");
        skillListing.setRating(4.5);
        skillListing.setDownloadCount(100);
        mockSkillListings.add(skillListing);

        mockSkillReviews = new ArrayList<>();
        SkillReview skillReview = new SkillReview();
        skillReview.setSkillId("text-to-uppercase-skill");
        skillReview.setUserId("user1");
        skillReview.setRating(5.0);
        skillReview.setComment("Great skill!");
        skillReview.setTimestamp(System.currentTimeMillis());
        mockSkillReviews.add(skillReview);

        mockCategories = new ArrayList<>();
        mockCategories.add("text-processing");
        mockCategories.add("development");

        // 模拟市场管理器的行为
        when(marketManager.getAllSkills()).thenReturn(mockSkillListings);
        when(marketManager.getSkillListing(anyString())).thenReturn(skillListing);
        when(marketManager.searchSkills(anyString())).thenReturn(mockSkillListings);
        when(marketManager.getCategories()).thenReturn(mockCategories);
        when(marketManager.getSkillsByCategory(anyString())).thenReturn(mockSkillListings);
        when(marketManager.getPopularSkills(anyInt())).thenReturn(mockSkillListings);
        when(marketManager.getLatestSkills(anyInt())).thenReturn(mockSkillListings);
        when(marketManager.getSkillReviews(anyString())).thenReturn(mockSkillReviews);
        when(marketManager.downloadSkill(anyString())).thenReturn("test data".getBytes());
        when(marketManager.rateSkill(anyString(), anyDouble(), anyString(), anyString())).thenReturn(true);
        when(marketManager.publishSkill(any(), any())).thenReturn(true);
        when(marketManager.updateSkill(any())).thenReturn(true);
        when(marketManager.removeSkill(anyString())).thenReturn(true);
    }

    /**
     * 测试获取市场技能列表
     */
    @Test
    public void testGetMarketSkills() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].skillId").value("text-to-uppercase-skill"))
                .andExpect(jsonPath("$.data.content[0].name").value("文本转大写技能"));
    }

    /**
     * 测试获取技能详情
     */
    @Test
    public void testGetSkillDetails() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/text-to-uppercase-skill")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.skillId").value("text-to-uppercase-skill"))
                .andExpect(jsonPath("$.data.name").value("文本转大写技能"));
    }

    /**
     * 测试搜索技能
     */
    @Test
    public void testSearchSkills() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/search")
                .param("keyword", "text")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }

    /**
     * 测试获取技能分类
     */
    @Test
    public void testGetSkillCategories() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/categories")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0]").value("text-processing"))
                .andExpect(jsonPath("$.data[1]").value("development"));
    }

    /**
     * 测试根据分类获取技能列表
     */
    @Test
    public void testGetSkillsByCategory() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/category/text-processing")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1));
    }

    /**
     * 测试获取热门技能
     */
    @Test
    public void testGetPopularSkills() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/popular")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    /**
     * 测试获取最新技能
     */
    @Test
    public void testGetLatestSkills() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/latest")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    /**
     * 测试获取技能评价
     */
    @Test
    public void testGetSkillReviews() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/market/skills/text-to-uppercase-skill/reviews")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].skillId").value("text-to-uppercase-skill"))
                .andExpect(jsonPath("$.data.content[0].rating").value(5.0));
    }

    /**
     * 测试评价技能
     */
    @Test
    public void testRateSkill() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/text-to-uppercase-skill/rate")
                .param("rating", "5.0")
                .param("comment", "Excellent!")
                .param("userId", "user2")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试下载技能
     */
    @Test
    public void testDownloadSkill() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/text-to-uppercase-skill/download")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * 测试发布技能
     */
    @Test
    public void testPublishSkill() throws Exception {
        SkillListing newSkillListing = new SkillListing();
        newSkillListing.setSkillId("new-skill");
        newSkillListing.setName("新技能");
        newSkillListing.setCategory("development");

        String requestBody = objectMapper.writeValueAsString(newSkillListing);

        ResultActions resultActions = mockMvc.perform(post("/api/market/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试更新技能
     */
    @Test
    public void testUpdateSkill() throws Exception {
        SkillListing updatedSkillListing = new SkillListing();
        updatedSkillListing.setSkillId("text-to-uppercase-skill");
        updatedSkillListing.setName("更新后的文本转大写技能");

        String requestBody = objectMapper.writeValueAsString(updatedSkillListing);

        ResultActions resultActions = mockMvc.perform(put("/api/market/skills/text-to-uppercase-skill")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试删除技能
     */
    @Test
    public void testDeleteSkill() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/api/market/skills/text-to-uppercase-skill")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("success"))
                .andExpect(jsonPath("$.data").value(true));
    }
}
