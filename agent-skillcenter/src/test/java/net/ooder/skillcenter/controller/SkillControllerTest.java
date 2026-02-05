package net.ooder.skillcenter.controller;

import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.model.SkillContext;
import net.ooder.skillcenter.model.SkillException;
import net.ooder.skillcenter.model.SkillResult;
import net.ooder.skillcenter.model.PageResponse;
import net.ooder.skillcenter.model.ApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class SkillControllerTest {

    @InjectMocks
    private SkillController skillController;

    @Mock
    private SkillManager skillManager;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllSkills_Default() {
        List<Skill> mockSkills = new ArrayList<>();
        when(skillManager.getAllSkills()).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<PageResponse<Skill>>> response = skillController.getAllSkills(1, 10, null, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        verify(skillManager, times(1)).getAllSkills();
    }

    @Test
    public void testGetAllSkills_WithPagination() {
        List<Skill> mockSkills = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            mockSkills.add(mock(Skill.class));
        }
        when(skillManager.getAllSkills()).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<PageResponse<Skill>>> response = skillController.getAllSkills(2, 10, null, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PageResponse<Skill> pageResponse = response.getBody().getData();
        assertEquals(2, pageResponse.getPage());
        assertEquals(10, pageResponse.getSize());
        assertEquals(25, pageResponse.getTotalElements());
        assertEquals(10, pageResponse.getData().size());
    }

    @Test
    public void testGetAllSkills_WithSorting() {
        List<Skill> mockSkills = new ArrayList<>();
        when(skillManager.getAllSkills()).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<PageResponse<Skill>>> response = skillController.getAllSkills(1, 10, "name", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(skillManager, times(1)).getAllSkills();
    }

    @Test
    public void testGetSkill_Success() {
        Skill mockSkill = mock(Skill.class);
        when(skillManager.getSkill("skill-1")).thenReturn(mockSkill);

        ResponseEntity<ApiResponse<Skill>> response = skillController.getSkill("skill-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        verify(skillManager, times(1)).getSkill("skill-1");
    }

    @Test
    public void testGetSkill_NotFound() {
        when(skillManager.getSkill("invalid-skill")).thenReturn(null);

        ResponseEntity<ApiResponse<Skill>> response = skillController.getSkill("invalid-skill");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(404, response.getBody().getCode());
        assertEquals("Skill not found", response.getBody().getMessage());
    }

    @Test
    public void testExecuteSkill_Success() {
        SkillResult mockResult = new SkillResult(SkillResult.Status.SUCCESS, "Execution successful");
        mockResult.addData("output", "test output");

        when(skillManager.executeSkill(eq("skill-1"), any(SkillContext.class))).thenReturn(mockResult);

        Map<String, Object> parameters = new java.util.HashMap<>();
        parameters.put("input", "test input");

        SkillController.ExecuteSkillRequest request = new SkillController.ExecuteSkillRequest();
        request.setParameters(parameters);

        ResponseEntity<ApiResponse<SkillResult>> response = skillController.executeSkill("skill-1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        verify(skillManager, times(1)).executeSkill(eq("skill-1"), any(SkillContext.class));
    }

    @Test
    public void testExecuteSkill_Failure() {
        when(skillManager.executeSkill(eq("skill-1"), any(SkillContext.class)))
            .thenThrow(new SkillException("skill-1", "Execution failed", 500));

        Map<String, Object> parameters = new java.util.HashMap<>();
        SkillController.ExecuteSkillRequest request = new SkillController.ExecuteSkillRequest();
        request.setParameters(parameters);

        ResponseEntity<ApiResponse<SkillResult>> response = skillController.executeSkill("skill-1", request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getCode());
        assertEquals("Execution failed", response.getBody().getMessage());
    }

    @Test
    public void testGetCategories() {
        List<Skill> mockSkills = new ArrayList<>();
        when(skillManager.getAllSkills()).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<List<String>>> response = skillController.getCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        verify(skillManager, times(1)).getAllSkills();
    }

    @Test
    public void testGetSkillsByCategory() {
        List<Skill> mockSkills = new ArrayList<>();
        when(skillManager.getSkillsByCategory("test-category")).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<PageResponse<Skill>>> response = skillController.getSkillsByCategory("test-category", 1, 10, null, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        verify(skillManager, times(1)).getSkillsByCategory("test-category");
    }

    @Test
    public void testDeleteSkill_Success() {
        doNothing().when(skillManager).unregisterSkill("skill-1");

        ResponseEntity<ApiResponse<Boolean>> response = skillController.deleteSkill("skill-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals(true, response.getBody().getData());
        verify(skillManager, times(1)).unregisterSkill("skill-1");
    }

    @Test
    public void testDeleteSkill_Failure() {
        doThrow(new RuntimeException("Delete failed")).when(skillManager).unregisterSkill("skill-1");

        ResponseEntity<ApiResponse<Boolean>> response = skillController.deleteSkill("skill-1");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getCode());
    }

    @Test
    public void testPublishSkill_Success() {
        Skill mockSkill = mock(Skill.class);
        doNothing().when(skillManager).registerSkill(mockSkill);

        ResponseEntity<ApiResponse<Boolean>> response = skillController.publishSkill(mockSkill);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals(true, response.getBody().getData());
        verify(skillManager, times(1)).registerSkill(mockSkill);
    }

    @Test
    public void testPublishSkill_Failure() {
        Skill mockSkill = mock(Skill.class);
        doThrow(new RuntimeException("Publish failed")).when(skillManager).registerSkill(mockSkill);

        ResponseEntity<ApiResponse<Boolean>> response = skillController.publishSkill(mockSkill);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getCode());
    }

    @Test
    public void testUpdateSkill_Success() {
        Skill mockSkill = mock(Skill.class);
        when(mockSkill.getId()).thenReturn("skill-1");
        doNothing().when(skillManager).updateSkill(mockSkill);

        ResponseEntity<ApiResponse<Boolean>> response = skillController.updateSkill("skill-1", mockSkill);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals(true, response.getBody().getData());
        verify(skillManager, times(1)).updateSkill(mockSkill);
    }

    @Test
    public void testUpdateSkill_Failure() {
        Skill mockSkill = mock(Skill.class);
        doThrow(new RuntimeException("Update failed")).when(skillManager).updateSkill(mockSkill);

        ResponseEntity<ApiResponse<Boolean>> response = skillController.updateSkill("skill-1", mockSkill);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(500, response.getBody().getCode());
    }

    @Test
    public void testGetSkillsByCategory_WithPagination() {
        List<Skill> mockSkills = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mockSkills.add(mock(Skill.class));
        }
        when(skillManager.getSkillsByCategory("test-category")).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<PageResponse<Skill>>> response = skillController.getSkillsByCategory("test-category", 2, 5, null, "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PageResponse<Skill> pageResponse = response.getBody().getData();
        assertEquals(2, pageResponse.getPage());
        assertEquals(5, pageResponse.getSize());
        assertEquals(20, pageResponse.getTotalElements());
        assertEquals(5, pageResponse.getData().size());
    }

    @Test
    public void testGetSkillsByCategory_WithSorting() {
        List<Skill> mockSkills = new ArrayList<>();
        when(skillManager.getSkillsByCategory("test-category")).thenReturn(mockSkills);

        ResponseEntity<ApiResponse<PageResponse<Skill>>> response = skillController.getSkillsByCategory("test-category", 1, 10, "name", "desc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(skillManager, times(1)).getSkillsByCategory("test-category");
    }
}
