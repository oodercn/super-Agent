package net.ooder.nexus.adapter.inbound.controller.personal;

import net.ooder.nexus.domain.personal.model.*;
import net.ooder.nexus.dto.personal.*;
import net.ooder.nexus.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    public ApiResponse<PlatformListDTO> getPlatforms() {
        try {
            PlatformListDTO data = new PlatformListDTO();
            data.setPlatforms(new ArrayList<>(platformStore.values()));
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get media platforms", e);
            return ApiResponse.error("获取平台列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/platforms/{platformId}/config")
    public ApiResponse<PlatformConfigResultDTO> configPlatform(
            @PathVariable String platformId,
            @RequestBody MediaPlatformConfigDTO config) {
        try {
            MediaPlatform platform = platformStore.get(platformId);
            if (platform == null) {
                return ApiResponse.notFound("平台不存在");
            }

            platform.setStatus("AUTHORIZED");
            platform.setAccountName("我的账号");
            platform.setAuthExpireAt(new Date(System.currentTimeMillis() + 90L * 24 * 60 * 60 * 1000));

            PlatformConfigResultDTO data = new PlatformConfigResultDTO();
            data.setPlatformId(platformId);
            data.setStatus(platform.getStatus());
            
            PlatformConfigResultDTO.AccountInfoDTO accountInfo = new PlatformConfigResultDTO.AccountInfoDTO();
            accountInfo.setName(platform.getAccountName());
            accountInfo.setType("service");
            accountInfo.setVerified(true);
            data.setAccountInfo(accountInfo);
            
            return ApiResponse.success("配置成功", data);
        } catch (Exception e) {
            log.error("Failed to config media platform", e);
            return ApiResponse.error("配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/publish")
    public ApiResponse<PublishResultDTO> publish(@RequestBody MediaPublishRequestDTO request) {
        try {
            String title = request.getTitle();
            String content = request.getContent();
            List<String> platforms = request.getPlatforms();

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

            PublishResultDTO data = new PublishResultDTO();
            data.setTaskId(taskId);
            data.setStatus("PUBLISHING");
            data.setPlatforms(results);
            
            return ApiResponse.success("内容发布任务已创建", data);
        } catch (Exception e) {
            log.error("Failed to publish content", e);
            return ApiResponse.error("发布失败: " + e.getMessage());
        }
    }

    @GetMapping("/records")
    public ApiResponse<PublishRecordsDTO> getRecords(
            @RequestParam(required = false) String platform,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            List<MediaPublishTask> filtered = new ArrayList<>();
            for (MediaPublishTask task : taskStore) {
                filtered.add(task);
            }

            int start = page * size;
            int end = Math.min(start + size, filtered.size());
            List<MediaPublishTask> paged = start < filtered.size() ? 
                filtered.subList(start, end) : new ArrayList<MediaPublishTask>();

            PublishRecordsDTO data = new PublishRecordsDTO();
            data.setTotal(filtered.size());
            data.setRecords(paged);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get publish records", e);
            return ApiResponse.error("获取记录失败: " + e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ApiResponse<MediaStatsDTO> getStats(
            @RequestParam(required = false) String platform,
            @RequestParam(defaultValue = "30d") String period) {
        try {
            MediaStatsDTO data = new MediaStatsDTO();
            data.setPeriod(period);

            MediaStatsDTO.MediaStatsOverviewDTO overview = new MediaStatsDTO.MediaStatsOverviewDTO();
            overview.setTotalRead(50000L);
            overview.setTotalLike(2000L);
            overview.setTotalComment(500L);
            overview.setTotalShare(300L);
            data.setOverview(overview);

            List<MediaStatsDTO.PlatformStatsDTO> byPlatform = new ArrayList<>();
            
            MediaStatsDTO.PlatformStatsDTO wechatStats = new MediaStatsDTO.PlatformStatsDTO();
            wechatStats.setPlatformId("wechat_mp");
            wechatStats.setName("微信公众号");
            wechatStats.setRead(30000L);
            wechatStats.setLike(1500L);
            wechatStats.setComment(300L);
            wechatStats.setShare(200L);
            byPlatform.add(wechatStats);

            MediaStatsDTO.PlatformStatsDTO weiboStats = new MediaStatsDTO.PlatformStatsDTO();
            weiboStats.setPlatformId("weibo");
            weiboStats.setName("微博");
            weiboStats.setRead(20000L);
            weiboStats.setLike(500L);
            weiboStats.setComment(200L);
            weiboStats.setShare(100L);
            byPlatform.add(weiboStats);

            data.setByPlatform(byPlatform);

            List<MediaStatsDTO.MediaTrendDTO> trend = new ArrayList<>();
            Random random = new Random();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < 7; i++) {
                MediaStatsDTO.MediaTrendDTO day = new MediaStatsDTO.MediaTrendDTO();
                day.setDate(sdf.format(new Date(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000)));
                day.setRead(1000 + random.nextInt(500));
                day.setLike(40 + random.nextInt(30));
                day.setComment(10 + random.nextInt(10));
                trend.add(day);
            }
            data.setTrend(trend);
            
            return ApiResponse.success(data);
        } catch (Exception e) {
            log.error("Failed to get media stats", e);
            return ApiResponse.error("获取统计失败: " + e.getMessage());
        }
    }
}
