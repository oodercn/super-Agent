package net.ooder.nexus.personal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Personal Center Service Interface
 *
 * @author ooder Team
 * @since 2.0
 */
public interface PersonalCenterService {

    CompletableFuture<Map<String, Object>> getPersonalOverview();

    CompletableFuture<List<Map<String, Object>>> getMySkills();

    CompletableFuture<List<Map<String, Object>>> getExecutionHistory(int limit);

    CompletableFuture<Map<String, Object>> getIdentityInfo();

    CompletableFuture<Void> updateIdentity(Map<String, Object> identity);

    CompletableFuture<Map<String, Object>> getPersonalStats();
}
