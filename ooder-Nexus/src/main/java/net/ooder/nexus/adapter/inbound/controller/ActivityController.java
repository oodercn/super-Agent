package net.ooder.nexus.adapter.inbound.controller;

import net.ooder.nexus.common.ResultModel;
import net.ooder.nexus.dto.activity.ActivityDTO;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ActivityController {

    @GetMapping("/activities")
    public ResultModel<List<ActivityDTO>> getActivities() {
        List<ActivityDTO> activities = new ArrayList<>();
        
        ActivityDTO activity1 = new ActivityDTO();
        activity1.setTitle("执行了数据抽取任务");
        activity1.setIcon("ri-database-line");
        activity1.setTime("5分钟前");
        activities.add(activity1);
        
        ActivityDTO activity2 = new ActivityDTO();
        activity2.setTitle("分享了技能包");
        activity2.setIcon("ri-share-line");
        activity2.setTime("1小时前");
        activities.add(activity2);
        
        ActivityDTO activity3 = new ActivityDTO();
        activity3.setTitle("加入了协作群组");
        activity3.setIcon("ri-team-line");
        activity3.setTime("2小时前");
        activities.add(activity3);
        
        ActivityDTO activity4 = new ActivityDTO();
        activity4.setTitle("安装了新技能");
        activity4.setIcon("ri-lightbulb-line");
        activity4.setTime("昨天");
        activities.add(activity4);
        
        return ResultModel.success("获取成功", activities);
    }
}
