package net.ooder.nexus.adapter.inbound.controller.personal;

import net.ooder.nexus.domain.personal.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/personal/media")
public class MediaController {

    private static final Logger log = LoggerFactory.getLogger(MediaController.class);

    private final Map<String, MediaPlatform> platformStore = new HashMap<>();
    private final List<MediaPublishTask> taskStore = new ArrayList<>();

    public MediaController() {
        initDefaultPlatforms();
    }

    private void initDefaultPlatforms() {
        MediaPlatform wechatMp = new MediaPlatform();
        wechatMp.setPlatformId("wechat_mp");
        wechatMp.setName("微信公众号");
        wechatMp.setIcon("ri-wechat-line");
        wechatMp.setStatus("PENDING_AUTH");
        wechatMp.setFeatures(Arrays.asList("article", "image"));
        platformStore.put(wechatMp.getPlatformId(), wechatMp);

        MediaPlatform weibo = new MediaPlatform();
        weibo.setPlatformId("weibo");
        weibo.setName("微博");
        weibo.setIcon("ri-weibo-line");
        weibo.setStatus("PENDING_AUTH");
        weibo.setFeatures(Arrays.asList("article", "image", "video"));
        platformStore.put(weibo.getPlatformId(), weibo);

        MediaPlatform xiaohongshu = new MediaPlatform();
        xiaohongshu.setPlatformId("xiaohongshu");
        xiaohongshu.setName("小红书");
        xiaohongshu.setIcon("ri-book-line");
        xiaohongshu.setStatus("NOT_CONFIGURED");
        xiaohongshu.setFeatures(Arrays.asList("article", "image", "video"));
        platformStore.put(xiaohongshu.getPlatformId(), xiaohongshu);

        MediaPlatform douyin = new MediaPlatform();
        douyin.setPlatformId("douyin");
        douyin.setName("抖音");
        douyin.setIcon("ri-tiktok-line");
        douyin.setStatus("NOT_CONFIGURED");
        douyin.setFeatures(Arrays.asList("video"));
        platformStore.put(douyin.getPlatformId(), douyin);

        MediaPlatform toutiao = new MediaPlatform();
        toutiao.setPlatformId("toutiao");
        toutiao.setName("头条号");
        toutiao.setIcon("ri-newspaper-line");
        toutiao.setStatus("NOT_CONFIGURED");
        toutiao.setFeatures(Arrays.asList("article", "image", "video"));
        platformStore.put(toutiao.getPlatformId(), toutiao);

        MediaPlatform zhihu = new MediaPlatform();
        zhihu.setPlatformId("zhihu");
        zhihu.setName("知乎");
        zhihu.setIcon("ri-zhihu-line");
        zhihu.setStatus("NOT_CONFIGURED");
        zhihu.setFeatures(Arrays.asList("article"));
        platformStore.put(zhihu.getPlatformId(), zhihu);
    }

    @GetMapping("/platforms")
    public Map<String, Object> getPlatforms() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("requestStatus", 200);
            result.put("data", new HashMap<String, Object>() {{
                put("platforms", new ArrayList<>(platformStore.values()));
            }});
        } catch (Exception e) {
            log.error("Failed to get media platforms", e);
            result.put("requestStatus", 500);
            result.put("message", "获取平台列表失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/platforms/{platformId}/config")
    public Map<String, Object> configPlatform(
            @PathVariable String platformId,
            @RequestBody Map<String, Object> config) {
        Map<String, Object> result = new HashMap<>();
        try {
            MediaPlatform platform = platformStore.get(platformId);
            if (platform == null) {
                result.put("requestStatus", 404);
                result.put("message", "平台不存在");
                return result;
            }

            platform.setStatus("AUTHORIZED");
            platform.setAccountName("我的账号");
            platform.setAuthExpireAt(new Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000));

            result.put("requestStatus", 200);
            result.put("message", "配置成功");
            
            Map<String, Object> data = new HashMap<>();
            data.put("platformId", platformId);
            data.put("status", platform.getStatus());
            
            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("name", platform.getAccountName());
            accountInfo.put("type", "service");
            accountInfo.put("verified", true);
            data.put("accountInfo", accountInfo);
            
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to config media platform", e);
            result.put("requestStatus", 500);
            result.put("message", "配置失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/publish")
    public Map<String, Object> publish(@RequestBody Map<String, Object> request) {
        Map<String, Object> result = new HashMap<>();
        try {
            String title = (String) request.get("title");
            String content = (String) request.get("content");
            
            @SuppressWarnings("unchecked")
            List<String> platforms = (List<String>) request.get("platforms");

            String taskId = "task-" + UUID.randomUUID().toString().substring(0, 8);

            MediaPublishTask task = new MediaPublishTask();
            task.setTaskId(taskId);
            task.setTitle(title);
            task.setContent(content);
            task.setPlatforms(platforms);
            task.setStatus("PUBLISHING");
            task.setCreatedAt(new Date());

            List<PlatformResult> results = new ArrayList<>();
            Random random = new Random();
            
            for (String platformId : platforms) {
                PlatformResult pr = new PlatformResult();
                pr.setPlatformId(platformId);
                
                boolean success = random.nextDouble() > 0.2;
                if (success) {
                    pr.setStatus("SUCCESS");
                    pr.setArticleId(platformId + "-" + UUID.randomUUID().toString().substring(0, 6));
                    pr.setUrl("https://" + platformId + ".example.com/article/" + pr.getArticleId());
                    
                    ContentStats stats = new ContentStats();
                    stats.setRead(random.nextInt(1000));
                    stats.setLike(random.nextInt(100));
                    stats.setComment(random.nextInt(20));
                    pr.setStats(stats);
                } else {
                    pr.setStatus("FAILED");
                    pr.setError("图片尺寸不符合要求");
                }
                
                results.add(pr);
            }
            
            task.setResults(results);
            taskStore.add(task);

            result.put("requestStatus", 200);
            result.put("message", "内容发布任务已创建");
            
            Map<String, Object> data = new HashMap<>();
            data.put("taskId", taskId);
            data.put("status", "PUBLISHING");
            data.put("platforms", results);
            
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to publish content", e);
            result.put("requestStatus", 500);
            result.put("message", "发布失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/records")
    public Map<String, Object> getRecords(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<MediaPublishTask> filtered = new ArrayList<>();
            for (MediaPublishTask task : taskStore) {
                filtered.add(task);
            }

            int start = page * size;
            int end = Math.min(start + size, filtered.size());
            List<MediaPublishTask> paged = start < filtered.size() ? 
                filtered.subList(start, end) : new ArrayList<>();

            Map<String, Object> data = new HashMap<>();
            data.put("total", filtered.size());
            data.put("records", paged);
            
            result.put("requestStatus", 200);
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to get publish records", e);
            result.put("requestStatus", 500);
            result.put("message", "获取记录失败: " + e.getMessage());
        }
        return result;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(
            @RequestParam(required = false) String platform,
            @RequestParam(defaultValue = "30d") String period) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalRead", 50000L);
            overview.put("totalLike", 2000L);
            overview.put("totalComment", 500L);
            overview.put("totalShare", 300L);

            List<Map<String, Object>> byPlatform = new ArrayList<>();
            
            Map<String, Object> wechatStats = new HashMap<>();
            wechatStats.put("platformId", "wechat_mp");
            wechatStats.put("name", "微信公众号");
            wechatStats.put("read", 30000L);
            wechatStats.put("like", 1500L);
            wechatStats.put("comment", 300L);
            wechatStats.put("share", 200L);
            byPlatform.add(wechatStats);

            Map<String, Object> weiboStats = new HashMap<>();
            weiboStats.put("platformId", "weibo");
            weiboStats.put("name", "微博");
            weiboStats.put("read", 20000L);
            weiboStats.put("like", 500L);
            weiboStats.put("comment", 200L);
            weiboStats.put("share", 100L);
            byPlatform.add(weiboStats);

            List<Map<String, Object>> trend = new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 7; i++) {
                Map<String, Object> day = new HashMap<>();
                day.put("date", new Date(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000));
                day.put("read", 1000 + random.nextInt(500));
                day.put("like", 40 + random.nextInt(30));
                day.put("comment", 10 + random.nextInt(10));
                trend.add(day);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("period", period);
            data.put("overview", overview);
            data.put("byPlatform", byPlatform);
            data.put("trend", trend);
            
            result.put("requestStatus", 200);
            result.put("data", data);
        } catch (Exception e) {
            log.error("Failed to get media stats", e);
            result.put("requestStatus", 500);
            result.put("message", "获取统计失败: " + e.getMessage());
        }
        return result;
    }
}
