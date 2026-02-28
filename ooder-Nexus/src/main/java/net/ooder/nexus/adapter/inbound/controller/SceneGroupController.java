package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.scene.SceneGroupDTO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/scene/groups")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SceneGroupController {

    @GetMapping("/list")
    public ResultModel<List<SceneGroupDTO>> getSceneGroups() {
        List<SceneGroupDTO> groups = new ArrayList<>();
        
        SceneGroupDTO group1 = new SceneGroupDTO();
        group1.setId("scene-1");
        group1.setName("生产环境");
        group1.setDescription("生产环境协作场景");
        group1.setMemberCount(5);
        group1.setStatus("active");
        group1.setCreateTime("2026-02-01 10:00:00");
        groups.add(group1);
        
        SceneGroupDTO group2 = new SceneGroupDTO();
        group2.setId("scene-2");
        group2.setName("测试环境");
        group2.setDescription("测试环境协作场景");
        group2.setMemberCount(3);
        group2.setStatus("active");
        group2.setCreateTime("2026-02-15 14:00:00");
        groups.add(group2);
        
        return ResultModel.success("获取成功", groups);
    }
}
