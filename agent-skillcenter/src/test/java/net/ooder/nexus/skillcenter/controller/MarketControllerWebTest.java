package net.ooder.nexus.skillcenter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ooder.skillcenter.market.SkillListing;
import net.ooder.skillcenter.market.SkillMarketManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
 * 市场API的web测试用例 - 符合 ooderNexus 规范
 */
@WebMvcTest(MarketController.class)
public class MarketControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillMarketManager marketManager;

    @Autowired
    private ObjectMapper objectMapper;

    private List<SkillListing> mockSkillListings;

    @BeforeEach
    public void setUp() throws Exception {
        mockSkillListings = new ArrayList<>();
        SkillListing skillListing = new SkillListing();
        skillListing.setSkillId("text-to-uppercase-skill");
        skillListing.setName("文本转大写技能");
        skillListing.setCategory("text-processing");
        skillListing.setRating(4.5);
        skillListing.setDownloadCount(100);
        mockSkillListings.add(skillListing);

        when(marketManager.getAllSkills()).thenReturn(mockSkillListings);
        when(marketManager.getSkillListing(anyString())).thenReturn(skillListing);
        when(marketManager.getSkillsByCategory(anyString())).thenReturn(mockSkillListings);
        when(marketManager.getPopularSkills(anyInt())).thenReturn(mockSkillListings);
        when(marketManager.getLatestSkills(anyInt())).thenReturn(mockSkillListings);
        when(marketManager.downloadSkill(anyString())).thenReturn("test data".getBytes());
        when(marketManager.rateSkill(anyString(), anyDouble(), anyString(), anyString())).thenReturn(true);
        when(marketManager.updateSkill(any())).thenReturn(true);
        when(marketManager.removeSkill(anyString())).thenReturn(true);
    }

    @Test
    public void testGetMarketSkills() throws Exception {
        String requestBody = "{}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetSkillDetails() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/text-to-uppercase-skill")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.skillId").value("text-to-uppercase-skill"));
    }

    @Test
    public void testGetSkillsByCategory() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/category/text-processing")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetPopularSkills() throws Exception {
        String requestBody = "{\"pageSize\": 10}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/popular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetLatestSkills() throws Exception {
        String requestBody = "{\"pageSize\": 10}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/latest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testDownloadSkill() throws Exception {
        String requestBody = "{\"id\": \"text-to-uppercase-skill\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/download")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.success").value(true));
    }

    @Test
    public void testRateSkill() throws Exception {
        String requestBody = "{\"id\": \"text-to-uppercase-skill\", \"rating\": 5.0, \"comment\": \"Excellent!\", \"userId\": \"user2\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/rate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.success").value(true));
    }

    @Test
    public void testAddMarketSkill() throws Exception {
        String requestBody = "{\"skillId\": \"new-skill\", \"name\": \"新技能\", \"category\": \"development\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testUpdateMarketSkill() throws Exception {
        String requestBody = "{\"name\": \"更新后的文本转大写技能\"}";
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/text-to-uppercase-skill/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    public void testDeleteMarketSkill() throws Exception {
        ResultActions resultActions = mockMvc.perform(post("/api/market/skills/text-to-uppercase-skill/delete")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("success"));
    }
}
