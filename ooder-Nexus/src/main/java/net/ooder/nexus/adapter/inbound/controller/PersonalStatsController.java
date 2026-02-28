package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.personal.PersonalStatsDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PersonalStatsController {

    @GetMapping("/stats")
    public ResultModel<PersonalStatsDTO> getStats() {
        PersonalStatsDTO stats = new PersonalStatsDTO();
        stats.setSkillCount(5);
        stats.setExecutionCount(128);
        stats.setSharedCount(3);
        stats.setGroupCount(2);
        return ResultModel.success("获取成功", stats);
    }
}
