/*
 * Copyright (c) 2024 Ooder Team
 *
 * This software is released under the MIT License.
 * https://opensource.org/licenses/MIT
 */
package net.ooder.skillcenter.market;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SDK Skill Storage Implementation - File system based persistence
 * Uses JSON format to store skill marketplace data
 */
public class SDKSkillStorage implements SkillStorage {
    private static final Logger logger = LoggerFactory.getLogger(SDKSkillStorage.class);

    public static final String STORAGE_DIR = "skillcenter/storage";
    public static final String SKILL_LISTINGS_FILE = "skill_listings.json";
    public static final String SKILL_RATINGS_FILE = "skill_ratings.json";
    public static final String SKILL_REVIEWS_FILE = "skill_reviews.json";
    
    private Path storagePath;
    private Map<String, SkillListing> skillListingsCache;
    private Map<String, List<SkillListing>> categoryCache;
    private Map<String, SkillRatingInfo> skillRatingsCache;
    private Map<String, List<SkillReview>> skillReviewsCache;
    private boolean initialized;
    
    public SDKSkillStorage() {
        this.storagePath = Paths.get(System.getProperty("user.dir"), STORAGE_DIR);
        this.skillListingsCache = new ConcurrentHashMap<>();
        this.categoryCache = new ConcurrentHashMap<>();
        this.skillRatingsCache = new ConcurrentHashMap<>();
        this.skillReviewsCache = new ConcurrentHashMap<>();
        this.initialized = false;
    }
    
    @Override
    public void initialize() {
        try {
            // 创建存储目录
            Files.createDirectories(storagePath);
            
            // 加载数据
            loadSkillListings();
            loadSkillRatings();
            loadSkillReviews();
            
            initialized = true;
            logger.info("SDK skill storage initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to initialize SDK skill storage: {}", e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        try {
            // 保存数据
            saveSkillListings();
            saveSkillRatings();
            saveSkillReviews();

            logger.info("SDK skill storage closed successfully");
        } catch (IOException e) {
            logger.error("Failed to close SDK skill storage: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public void saveSkillListing(SkillListing listing) {
        if (listing == null) {
            return;
        }
        
        skillListingsCache.put(listing.getSkillId(), listing);
        
        // 更新分类缓存
        String category = listing.getCategory();
        categoryCache.computeIfAbsent(category, k -> new ArrayList<>())
                   .removeIf(item -> item.getSkillId().equals(listing.getSkillId()));
        categoryCache.computeIfAbsent(category, k -> new ArrayList<>()).add(listing);
        
        try {
            saveSkillListings();
        } catch (IOException e) {
            logger.error("Failed to save skill listing: {}", e.getMessage(), e);
        }
    }

    @Override
    public void saveSkillListings(List<SkillListing> listings) {
        if (listings == null || listings.isEmpty()) {
            return;
        }

        for (SkillListing listing : listings) {
            skillListingsCache.put(listing.getSkillId(), listing);

            // 更新分类缓存
            String category = listing.getCategory();
            categoryCache.computeIfAbsent(category, k -> new ArrayList<>())
                       .removeIf(item -> item.getSkillId().equals(listing.getSkillId()));
            categoryCache.computeIfAbsent(category, k -> new ArrayList<>()).add(listing);
        }

        try {
            saveSkillListings();
        } catch (IOException e) {
            logger.error("Failed to save skill listings: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public SkillListing getSkillListing(String skillId) {
        return skillListingsCache.get(skillId);
    }
    
    @Override
    public Map<String, SkillListing> getAllSkillListings() {
        return new HashMap<>(skillListingsCache);
    }
    
    @Override
    public List<SkillListing> getSkillListingsByCategory(String category) {
        return categoryCache.getOrDefault(category, new ArrayList<>());
    }
    
    @Override
    public void deleteSkillListing(String skillId) {
        SkillListing listing = skillListingsCache.remove(skillId);
        if (listing != null) {
            // 从分类缓存中移除
            String category = listing.getCategory();
            List<SkillListing> listings = categoryCache.get(category);
            if (listings != null) {
                listings.removeIf(item -> item.getSkillId().equals(skillId));
                if (listings.isEmpty()) {
                    categoryCache.remove(category);
                }
            }
            
            // 删除相关的评分信息和评价
            skillRatingsCache.remove(skillId);
            skillReviewsCache.remove(skillId);
            
            try {
                saveSkillListings();
                saveSkillRatings();
                saveSkillReviews();
            } catch (IOException e) {
                logger.error("Failed to delete skill listing: {}", e.getMessage(), e);
            }
        }
    }

    @Override
    public void saveSkillRatingInfo(SkillRatingInfo ratingInfo) {
        if (ratingInfo == null) {
            return;
        }

        skillRatingsCache.put(ratingInfo.getSkillId(), ratingInfo);

        try {
            saveSkillRatings();
        } catch (IOException e) {
            logger.error("Failed to save skill rating info: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public SkillRatingInfo getSkillRatingInfo(String skillId) {
        return skillRatingsCache.get(skillId);
    }
    
    @Override
    public void deleteSkillRatingInfo(String skillId) {
        skillRatingsCache.remove(skillId);
        skillReviewsCache.remove(skillId);
        
        try {
            saveSkillRatings();
            saveSkillReviews();
        } catch (IOException e) {
            logger.error("Failed to delete skill rating info: {}", e.getMessage(), e);
        }
    }

    @Override
    public void saveSkillReview(SkillReview review) {
        if (review == null) {
            return;
        }

        String skillId = review.getSkillId();
        skillReviewsCache.computeIfAbsent(skillId, k -> new ArrayList<>()).add(review);

        // 更新评分信息
        SkillRatingInfo ratingInfo = skillRatingsCache.computeIfAbsent(skillId, k -> new SkillRatingInfo());
        ratingInfo.setSkillId(skillId);
        ratingInfo.addReview(review);

        try {
            saveSkillRatings();
            saveSkillReviews();
        } catch (IOException e) {
            logger.error("Failed to save skill review: {}", e.getMessage(), e);
        }
    }
    
    @Override
    public List<SkillReview> getSkillReviews(String skillId) {
        return skillReviewsCache.getOrDefault(skillId, new ArrayList<>());
    }
    
    private void loadSkillListings() throws IOException {
        Path filePath = storagePath.resolve(SKILL_LISTINGS_FILE);
        if (Files.exists(filePath)) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                String content = readAll(reader);
                if (content != null && !content.trim().isEmpty()) {
                    JSONArray jsonArray = JSON.parseArray(content);
                    if (jsonArray != null) {
                        for (Object obj : jsonArray) {
                            if (obj != null) {
                                JSONObject jsonObject = (JSONObject) obj;
                                SkillListing listing = JSON.toJavaObject(jsonObject, SkillListing.class);
                                if (listing != null && listing.getSkillId() != null) {
                                    skillListingsCache.put(listing.getSkillId(), listing);
                                    
                                    // 更新分类缓存
                                    String category = listing.getCategory();
                                    if (category != null) {
                                        categoryCache.computeIfAbsent(category, k -> new ArrayList<>()).add(listing);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void saveSkillListings() throws IOException {
        Path filePath = storagePath.resolve(SKILL_LISTINGS_FILE);
        List<SkillListing> listings = new ArrayList<>(skillListingsCache.values());
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(listings));
        
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            jsonArray.writeJSONString(writer);
        }
    }
    
    private void loadSkillRatings() throws IOException {
        Path filePath = storagePath.resolve(SKILL_RATINGS_FILE);
        if (Files.exists(filePath)) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                String content = readAll(reader);
                if (content != null && !content.trim().isEmpty()) {
                    JSONArray jsonArray = JSON.parseArray(content);
                    if (jsonArray != null) {
                        for (Object obj : jsonArray) {
                            if (obj != null) {
                                JSONObject jsonObject = (JSONObject) obj;
                                SkillRatingInfo ratingInfo = JSON.toJavaObject(jsonObject, SkillRatingInfo.class);
                                if (ratingInfo != null && ratingInfo.getSkillId() != null) {
                                    skillRatingsCache.put(ratingInfo.getSkillId(), ratingInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void saveSkillRatings() throws IOException {
        Path filePath = storagePath.resolve(SKILL_RATINGS_FILE);
        List<SkillRatingInfo> ratingInfos = new ArrayList<>(skillRatingsCache.values());
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(ratingInfos));
        
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            jsonArray.writeJSONString(writer);
        }
    }
    
    private void loadSkillReviews() throws IOException {
        Path filePath = storagePath.resolve(SKILL_REVIEWS_FILE);
        if (Files.exists(filePath)) {
            try (Reader reader = Files.newBufferedReader(filePath)) {
                String content = readAll(reader);
                if (content != null && !content.trim().isEmpty()) {
                    JSONArray jsonArray = JSON.parseArray(content);
                    if (jsonArray != null) {
                        for (Object obj : jsonArray) {
                            if (obj != null) {
                                JSONObject jsonObject = (JSONObject) obj;
                                SkillReview review = JSON.toJavaObject(jsonObject, SkillReview.class);
                                if (review != null && review.getSkillId() != null) {
                                    String skillId = review.getSkillId();
                                    skillReviewsCache.computeIfAbsent(skillId, k -> new ArrayList<>()).add(review);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void saveSkillReviews() throws IOException {
        Path filePath = storagePath.resolve(SKILL_REVIEWS_FILE);
        List<SkillReview> allReviews = new ArrayList<>();
        for (List<SkillReview> reviews : skillReviewsCache.values()) {
            allReviews.addAll(reviews);
        }
        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(allReviews));
        
        try (Writer writer = Files.newBufferedWriter(filePath)) {
            jsonArray.writeJSONString(writer);
        }
    }
    
    /**
     * 将Reader转换为String
     * @param reader Reader对象
     * @return 读取的字符串
     * @throws IOException IO异常
     */
    private String readAll(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        int n;
        while ((n = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, n);
        }
        return sb.toString();
    }
}
