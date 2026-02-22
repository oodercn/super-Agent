package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

public interface AdminCategoryService {

    List<Map<String, Object>> getAllCategories();

    Map<String, Object> getCategoryById(String categoryId);

    Map<String, Object> createCategory(Map<String, Object> categoryData);

    Map<String, Object> updateCategory(String categoryId, Map<String, Object> categoryData);

    boolean deleteCategory(String categoryId);

    List<Map<String, Object>> getCategorySkills(String categoryId);

    boolean addSkillToCategory(String categoryId, String skillId);

    boolean removeSkillFromCategory(String categoryId, String skillId);

    int getCategoryCount();
}
