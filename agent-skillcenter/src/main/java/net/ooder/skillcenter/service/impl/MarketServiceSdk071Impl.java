package net.ooder.skillcenter.service.impl;

import net.ooder.skillcenter.config.SdkConfig;
import net.ooder.skillcenter.dto.PageResult;
import net.ooder.skillcenter.dto.SkillDTO;
import net.ooder.skillcenter.dto.SkillReviewDTO;
import net.ooder.skillcenter.manager.SkillManager;
import net.ooder.skillcenter.model.Skill;
import net.ooder.skillcenter.service.MarketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "skillcenter.sdk.mode", havingValue = "sdk")
public class MarketServiceSdk071Impl implements MarketService {

    private static final Logger log = LoggerFactory.getLogger(MarketServiceSdk071Impl.class);

    private final SdkConfig sdkConfig;
    private SkillManager skillManager;
    private final Map<String, List<SkillReviewDTO>> reviewStore = new ConcurrentHashMap<>();
    private final AtomicLong reviewIdGenerator = new AtomicLong(1);

    public MarketServiceSdk071Impl(SdkConfig sdkConfig) {
        this.sdkConfig = sdkConfig;
    }

    @PostConstruct
    public void init() {
        skillManager = SkillManager.getInstance();
        log.info("MarketService initialized with SDK 0.7.1 using SkillManager");
    }

    private List<SkillDTO> getSkillsFromManager() {
        List<Skill> skills = skillManager.getAllSkills();
        List<SkillDTO> result = new ArrayList<>();
        
        if (skills != null) {
            for (Skill skill : skills) {
                SkillDTO dto = new SkillDTO();
                dto.setId(skill.getId());
                dto.setName(skill.getName());
                dto.setDescription(skill.getDescription());
                dto.setVersion("1.0.0");
                dto.setAvailable(skill.isAvailable());
                
                if (skill instanceof SkillManager.SkillInfo) {
                    SkillManager.SkillInfo info = (SkillManager.SkillInfo) skill;
                    dto.setCategory(info.getCategory());
                    dto.setStatus(info.getStatus());
                } else {
                    dto.setCategory("general");
                    dto.setStatus(skill.isAvailable() ? "active" : "inactive");
                }
                
                dto.setDownloadCount(new Random().nextInt(500));
                dto.setRating(3.0 + new Random().nextDouble() * 2.0);
                result.add(dto);
            }
        }
        return result;
    }

    @Override
    public PageResult<SkillDTO> getMarketSkills(int page, int size, String sortBy, String sortDirection) {
        List<SkillDTO> skills = getSkillsFromManager();
        skills = sortSkills(skills, sortBy, sortDirection);
        return paginate(skills, page, size);
    }

    @Override
    public SkillDTO getSkillDetails(String skillId) {
        Skill skill = skillManager.getSkill(skillId);
        if (skill == null) {
            return null;
        }
        
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setDescription(skill.getDescription());
        dto.setVersion("1.0.0");
        dto.setAvailable(skill.isAvailable());
        
        if (skill instanceof SkillManager.SkillInfo) {
            SkillManager.SkillInfo info = (SkillManager.SkillInfo) skill;
            dto.setCategory(info.getCategory());
            dto.setStatus(info.getStatus());
        } else {
            dto.setCategory("general");
            dto.setStatus(skill.isAvailable() ? "active" : "inactive");
        }
        
        return dto;
    }

    @Override
    public PageResult<SkillDTO> searchSkills(String keyword, int page, int size, String sortBy, String sortDirection) {
        List<SkillDTO> skills = getSkillsFromManager();
        if (keyword != null && !keyword.isEmpty()) {
            skills = skills.stream()
                .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    s.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        }
        skills = sortSkills(skills, sortBy, sortDirection);
        return paginate(skills, page, size);
    }

    @Override
    public List<String> getSkillCategories() {
        List<SkillDTO> skills = getSkillsFromManager();
        return skills.stream()
            .map(SkillDTO::getCategory)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public PageResult<SkillDTO> getSkillsByCategory(String category, int page, int size, String sortBy, String sortDirection) {
        List<SkillDTO> skills = getSkillsFromManager().stream()
            .filter(s -> category.equals(s.getCategory()))
            .collect(Collectors.toList());
        skills = sortSkills(skills, sortBy, sortDirection);
        return paginate(skills, page, size);
    }

    @Override
    public List<SkillDTO> getPopularSkills(int limit) {
        return getSkillsFromManager().stream()
            .sorted((a, b) -> Integer.compare(b.getDownloadCount(), a.getDownloadCount()))
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public List<SkillDTO> getLatestSkills(int limit) {
        return getSkillsFromManager().stream()
            .limit(limit)
            .collect(Collectors.toList());
    }

    @Override
    public boolean rateSkill(String skillId, double rating, String comment, String userId) {
        SkillReviewDTO review = new SkillReviewDTO();
        review.setId("review-" + reviewIdGenerator.getAndIncrement());
        review.setSkillId(skillId);
        review.setUserId(userId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(new Date());
        reviewStore.computeIfAbsent(skillId, k -> new ArrayList<>()).add(review);
        return true;
    }

    @Override
    public PageResult<SkillReviewDTO> getSkillReviews(String skillId, int page, int size, String sortBy, String sortDirection) {
        List<SkillReviewDTO> reviews = reviewStore.getOrDefault(skillId, new ArrayList<>());
        return paginate(reviews, page, size);
    }

    @Override
    public byte[] downloadSkill(String skillId) {
        log.info("Download skill: {}", skillId);
        Skill skill = skillManager.getSkill(skillId);
        if (skill != null) {
            return ("Skill data for " + skill.getName()).getBytes();
        }
        return ("Skill not found: " + skillId).getBytes();
    }

    @Override
    public boolean publishSkill(SkillDTO skill) {
        log.info("Publish skill: {}", skill.getName());
        SkillManager.SkillInfo skillInfo = new SkillManager.SkillInfo();
        skillInfo.setId(skill.getId());
        skillInfo.setName(skill.getName());
        skillInfo.setDescription(skill.getDescription());
        skillInfo.setCategory(skill.getCategory() != null ? skill.getCategory() : "general");
        skillInfo.setStatus("active");
        skillInfo.setAvailable(true);
        skillManager.registerSkill(skillInfo);
        return true;
    }

    @Override
    public boolean updateSkill(String skillId, SkillDTO skill) {
        log.info("Update skill: {}", skillId);
        return true;
    }

    @Override
    public boolean deleteSkill(String skillId) {
        log.info("Delete skill: {}", skillId);
        skillManager.unregisterSkill(skillId);
        return true;
    }

    private List<SkillDTO> sortSkills(List<SkillDTO> skills, String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.isEmpty()) return skills;
        Comparator<SkillDTO> comparator;
        switch (sortBy) {
            case "name":
                comparator = Comparator.comparing(SkillDTO::getName);
                break;
            case "rating":
                comparator = Comparator.comparing(SkillDTO::getRating);
                break;
            case "downloadCount":
                comparator = Comparator.comparing(SkillDTO::getDownloadCount);
                break;
            default:
                comparator = Comparator.comparing(SkillDTO::getId);
        }
        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }
        return skills.stream().sorted(comparator).collect(Collectors.toList());
    }

    private <T> PageResult<T> paginate(List<T> list, int page, int size) {
        int total = list.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);
        if (start >= total) return PageResult.empty();
        return PageResult.of(list.subList(start, end), total, page, size);
    }
}
