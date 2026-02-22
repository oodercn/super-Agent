package net.ooder.nexus.service;

import java.util.List;
import java.util.Map;

public interface AdminMarketService {

    List<Map<String, Object>> getAllMarketItems();

    Map<String, Object> getMarketItemById(String itemId);

    List<Map<String, Object>> getFeaturedItems();

    List<Map<String, Object>> getCategories();

    List<Map<String, Object>> getItemsByCategory(String categoryId);

    boolean submitToMarket(String skillId, Map<String, Object> submitData);

    boolean updateMarketItem(String itemId, Map<String, Object> itemData);

    boolean removeFromMarket(String itemId);

    Map<String, Object> getMarketStatistics();
}
