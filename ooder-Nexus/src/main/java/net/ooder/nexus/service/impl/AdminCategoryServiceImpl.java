package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminCategoryService;
import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.storage.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private static final Logger log = LoggerFactory.getLogger(AdminCategoryServiceImpl.class);
    private static final String CATEGORY_KEY = "admin/categories";

    private final StorageService storageService;

    @Autowired
    public AdminCategoryServiceImpl(StorageService storageService) {
        this.storageService = storageService;
        initializeDefaultCategories();
        log.info("AdminCategoryServiceImpl initialized with StorageService (SDK 0.7.1)");
    }

    private void initializeDefaultCategories() {
        List<Map<String, Object>> categories = getAllCategories();
        if (categories.isEmpty()) {
            List<Map<String, Object>> defaultCategories = new ArrayList<Map<String, Object>>();
            
            Map<String, Object> cat1 = new HashMap<String, Object>();
            cat1.put("id", "cat-001");
            cat1.put("name", "Development Tools");
            cat1.put("description", "Software development tools");
            cat1.put("skillCount", 0);
            cat1.put("createTime", System.currentTimeMillis());
            defaultCategories.add(cat1);
            
            Map<String, Object> cat2 = new HashMap<String, Object>();
            cat2.put("id", "cat-002");
            cat2.put("name", "Data Processing");
            cat2.put("description", "Data processing and analysis");
            cat2.put("skillCount", 0);
            cat2.put("createTime", System.currentTimeMillis());
            defaultCategories.add(cat2);
            
            Map<String, Object> cat3 = new HashMap<String, Object>();
            cat3.put("id", "cat-003");
            cat3.put("name", "Automation");
            cat3.put("description", "Automation and workflow tools");
            cat3.put("skillCount", 0);
            cat3.put("createTime", System.currentTimeMillis());
            defaultCategories.add(cat3);
            
            storageService.save(CATEGORY_KEY, defaultCategories);
        }
    }

    @Override
    public List<Map<String, Object>> getAllCategories() {
        log.info("Getting all categories");
        Optional<List<Map<String, Object>>> categoriesOpt = storageService.load(CATEGORY_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return categoriesOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public Map<String, Object> getCategoryById(String categoryId) {
        log.info("Getting category by id: {}", categoryId);
        List<Map<String, Object>> categories = getAllCategories();
        for (Map<String, Object> category : categories) {
            if (categoryId.equals(category.get("id"))) {
                return category;
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> createCategory(Map<String, Object> categoryData) {
        log.info("Creating category: {}", categoryData.get("name"));
        List<Map<String, Object>> categories = getAllCategories();
        String id = "cat-" + System.currentTimeMillis();
        Map<String, Object> category = new HashMap<String, Object>(categoryData);
        category.put("id", id);
        category.put("skillCount", 0);
        category.put("createTime", System.currentTimeMillis());
        categories.add(category);
        storageService.save(CATEGORY_KEY, categories);
        return category;
    }

    @Override
    public Map<String, Object> updateCategory(String categoryId, Map<String, Object> categoryData) {
        log.info("Updating category: {}", categoryId);
        List<Map<String, Object>> categories = getAllCategories();
        for (int i = 0; i < categories.size(); i++) {
            if (categoryId.equals(categories.get(i).get("id"))) {
                Map<String, Object> existing = categories.get(i);
                existing.putAll(categoryData);
                existing.put("id", categoryId);
                existing.put("updateTime", System.currentTimeMillis());
                categories.set(i, existing);
                storageService.save(CATEGORY_KEY, categories);
                return existing;
            }
        }
        return null;
    }

    @Override
    public boolean deleteCategory(String categoryId) {
        log.info("Deleting category: {}", categoryId);
        List<Map<String, Object>> categories = getAllCategories();
        boolean removed = categories.removeIf(cat -> categoryId.equals(cat.get("id")));
        if (removed) {
            storageService.save(CATEGORY_KEY, categories);
        }
        return removed;
    }

    @Override
    public List<Map<String, Object>> getCategorySkills(String categoryId) {
        log.info("Getting skills for category: {}", categoryId);
        return new ArrayList<Map<String, Object>>();
    }

    @Override
    public boolean addSkillToCategory(String categoryId, String skillId) {
        log.info("Adding skill {} to category {}", skillId, categoryId);
        List<Map<String, Object>> categories = getAllCategories();
        for (Map<String, Object> category : categories) {
            if (categoryId.equals(category.get("id"))) {
                Integer count = (Integer) category.get("skillCount");
                category.put("skillCount", count != null ? count + 1 : 1);
                storageService.save(CATEGORY_KEY, categories);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeSkillFromCategory(String categoryId, String skillId) {
        log.info("Removing skill {} from category {}", skillId, categoryId);
        List<Map<String, Object>> categories = getAllCategories();
        for (Map<String, Object> category : categories) {
            if (categoryId.equals(category.get("id"))) {
                Integer count = (Integer) category.get("skillCount");
                if (count != null && count > 0) {
                    category.put("skillCount", count - 1);
                    storageService.save(CATEGORY_KEY, categories);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCategoryCount() {
        return getAllCategories().size();
    }
}
