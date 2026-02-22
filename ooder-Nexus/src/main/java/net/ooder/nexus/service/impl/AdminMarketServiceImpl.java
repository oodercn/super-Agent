package net.ooder.nexus.service.impl;

import net.ooder.nexus.service.AdminMarketService;
import net.ooder.sdk.api.storage.StorageService;
import net.ooder.sdk.api.storage.TypeReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminMarketServiceImpl implements AdminMarketService {

    private static final Logger log = LoggerFactory.getLogger(AdminMarketServiceImpl.class);
    private static final String MARKET_ITEMS_KEY = "admin/market-items";
    private static final String CATEGORIES_KEY = "admin/market-categories";

    private final StorageService storageService;

    @Autowired
    public AdminMarketServiceImpl(StorageService storageService) {
        this.storageService = storageService;
        initializeDefaultCategories();
        log.info("AdminMarketServiceImpl initialized with StorageService (SDK 0.7.1)");
    }

    private void initializeDefaultCategories() {
        List<Map<String, Object>> categories = getCategories();
        if (categories.isEmpty()) {
            List<Map<String, Object>> defaultCategories = new ArrayList<Map<String, Object>>();
            
            Map<String, Object> cat1 = new HashMap<String, Object>();
            cat1.put("id", "cat-001");
            cat1.put("name", "Development Tools");
            cat1.put("description", "Tools for software development");
            defaultCategories.add(cat1);
            
            Map<String, Object> cat2 = new HashMap<String, Object>();
            cat2.put("id", "cat-002");
            cat2.put("name", "Data Processing");
            cat2.put("description", "Data processing and analysis tools");
            defaultCategories.add(cat2);
            
            storageService.save(CATEGORIES_KEY, defaultCategories);
            log.info("Default market categories initialized");
        }
    }

    @Override
    public List<Map<String, Object>> getAllMarketItems() {
        Optional<List<Map<String, Object>>> listOpt = storageService.load(MARKET_ITEMS_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return listOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public Map<String, Object> getMarketItemById(String itemId) {
        List<Map<String, Object>> marketItems = getAllMarketItems();
        for (Map<String, Object> item : marketItems) {
            if (itemId.equals(item.get("id"))) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getFeaturedItems() {
        List<Map<String, Object>> marketItems = getAllMarketItems();
        List<Map<String, Object>> featured = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> item : marketItems) {
            Boolean isFeatured = (Boolean) item.get("featured");
            if (isFeatured != null && isFeatured) {
                featured.add(item);
            }
        }
        return featured;
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        Optional<List<Map<String, Object>>> listOpt = storageService.load(CATEGORIES_KEY, 
            new TypeReference<List<Map<String, Object>>>() {});
        return listOpt.orElse(new ArrayList<Map<String, Object>>());
    }

    @Override
    public List<Map<String, Object>> getItemsByCategory(String categoryId) {
        List<Map<String, Object>> marketItems = getAllMarketItems();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> item : marketItems) {
            if (categoryId.equals(item.get("categoryId"))) {
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public boolean submitToMarket(String skillId, Map<String, Object> submitData) {
        log.info("Submitting skill to market: {}", skillId);
        List<Map<String, Object>> marketItems = getAllMarketItems();
        Map<String, Object> item = new HashMap<String, Object>(submitData);
        item.put("id", "market-" + System.currentTimeMillis());
        item.put("skillId", skillId);
        item.put("status", "pending");
        item.put("submitTime", Long.valueOf(System.currentTimeMillis()));
        marketItems.add(item);
        storageService.save(MARKET_ITEMS_KEY, marketItems);
        log.info("Skill submitted to market with id: {}", item.get("id"));
        return true;
    }

    @Override
    public boolean updateMarketItem(String itemId, Map<String, Object> itemData) {
        List<Map<String, Object>> marketItems = getAllMarketItems();
        for (int i = 0; i < marketItems.size(); i++) {
            if (itemId.equals(marketItems.get(i).get("id"))) {
                Map<String, Object> existing = new HashMap<String, Object>(marketItems.get(i));
                existing.putAll(itemData);
                existing.put("id", itemId);
                existing.put("updateTime", Long.valueOf(System.currentTimeMillis()));
                marketItems.set(i, existing);
                storageService.save(MARKET_ITEMS_KEY, marketItems);
                log.info("Market item updated: {}", itemId);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeFromMarket(String itemId) {
        List<Map<String, Object>> marketItems = getAllMarketItems();
        boolean removed = marketItems.removeIf(item -> itemId.equals(item.get("id")));
        if (removed) {
            storageService.save(MARKET_ITEMS_KEY, marketItems);
            log.info("Market item removed: {}", itemId);
        }
        return removed;
    }

    @Override
    public Map<String, Object> getMarketStatistics() {
        Map<String, Object> stats = new HashMap<String, Object>();
        List<Map<String, Object>> marketItems = getAllMarketItems();
        stats.put("totalItems", Integer.valueOf(marketItems.size()));
        stats.put("featuredItems", Integer.valueOf(getFeaturedItems().size()));
        stats.put("categories", Integer.valueOf(getCategories().size()));
        return stats;
    }
}
