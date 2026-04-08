package net.ooder.mvp.skill.scene.controller;

import net.ooder.mvp.skill.scene.capability.service.CapabilityService;
import net.ooder.mvp.skill.scene.service.SceneService;
import net.ooder.mvp.skill.scene.dto.stats.OverviewStatsDTO;
import net.ooder.mvp.skill.scene.model.ResultModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stats")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatsController {

    @Autowired
    private CapabilityService capabilityService;

    @Autowired
    private SceneService sceneService;

    @GetMapping("/users")
    public ResultModel<Integer> getUserCount() {
        int count = 1;
        return ResultModel.success(count);
    }

    @GetMapping("/overview")
    public ResultModel<OverviewStatsDTO> getOverview() {
        OverviewStatsDTO overview = new OverviewStatsDTO();
        
        int capabilityCount = capabilityService.findAll().size();
        overview.setCapabilities(capabilityCount);
        
        overview.setUsers(1);
        overview.setScenes(0);
        
        return ResultModel.success(overview);
    }
}
