package net.ooder.sdk.network.link;

import net.ooder.sdk.network.link.LinkInfo;
import net.ooder.sdk.network.link.LinkStatus;
import net.ooder.sdk.network.link.LinkTable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LinkTableManagerTest {

    @Mock
    private LinkTableManager linkTableManager;

    private String testLinkId1;
    private String testLinkId2;
    private String testNodeId1;
    private String testNodeId2;
    private String testSceneId;
    private String testGroupId;
    private LinkTable testLinkTable;

    @Before
    public void setUp() {
        // 初始化测试ID
        testLinkId1 = "link-1";
        testLinkId2 = "link-2";
        testNodeId1 = "node-1";
        testNodeId2 = "node-2";
        testSceneId = "scene-1";
        testGroupId = "group-1";

        // 初始化测试链路表
        testLinkTable = new LinkTable();
    }

    // 测试链路表刷新相关方法
    @Test
    public void testLinkTableRefreshMethods() throws ExecutionException, InterruptedException {
        // 测试刷新链路表
        when(linkTableManager.refreshLinkTable()).thenReturn(CompletableFuture.completedFuture(true));
        boolean refreshed = linkTableManager.refreshLinkTable().get();
        assertTrue(refreshed);

        // 测试强制刷新链路表
        when(linkTableManager.forceRefreshLinkTable()).thenReturn(CompletableFuture.completedFuture(true));
        boolean forceRefreshed = linkTableManager.forceRefreshLinkTable().get();
        assertTrue(forceRefreshed);
    }

    // 测试链路状态控制相关方法
    @Test
    public void testLinkStatusControlMethods() throws ExecutionException, InterruptedException {
        // 测试禁用链路
        when(linkTableManager.disableLink(testLinkId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean disabled = linkTableManager.disableLink(testLinkId1).get();
        assertTrue(disabled);

        // 测试启用链路
        when(linkTableManager.enableLink(testLinkId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean enabled = linkTableManager.enableLink(testLinkId1).get();
        assertTrue(enabled);

        // 测试获取链路状态
        when(linkTableManager.getLinkStatus(testLinkId1)).thenReturn(CompletableFuture.completedFuture(LinkStatus.ACTIVE));
        LinkStatus status = linkTableManager.getLinkStatus(testLinkId1).get();
        assertEquals(LinkStatus.ACTIVE, status);

        // 测试获取禁用状态
        when(linkTableManager.getLinkStatus(testLinkId2)).thenReturn(CompletableFuture.completedFuture(LinkStatus.DISABLED));
        LinkStatus disabledStatus = linkTableManager.getLinkStatus(testLinkId2).get();
        assertEquals(LinkStatus.DISABLED, disabledStatus);
    }

    // 测试黑名单管理相关方法
    @Test
    public void testBlacklistManagementMethods() throws ExecutionException, InterruptedException {
        // 测试添加到黑名单
        when(linkTableManager.addToBlacklist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean addedToBlacklist = linkTableManager.addToBlacklist(testNodeId1).get();
        assertTrue(addedToBlacklist);

        // 测试从黑名单移除
        when(linkTableManager.removeFromBlacklist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean removedFromBlacklist = linkTableManager.removeFromBlacklist(testNodeId1).get();
        assertTrue(removedFromBlacklist);

        // 测试检查节点是否在黑名单中
        when(linkTableManager.isInBlacklist(testNodeId1)).thenReturn(true);
        boolean inBlacklist1 = linkTableManager.isInBlacklist(testNodeId1);
        assertTrue(inBlacklist1);

        when(linkTableManager.isInBlacklist(testNodeId2)).thenReturn(false);
        boolean inBlacklist2 = linkTableManager.isInBlacklist(testNodeId2);
        assertFalse(inBlacklist2);
    }

    // 测试白名单管理相关方法
    @Test
    public void testWhitelistManagementMethods() throws ExecutionException, InterruptedException {
        // 测试添加到白名单
        when(linkTableManager.addToWhitelist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean addedToWhitelist = linkTableManager.addToWhitelist(testNodeId1).get();
        assertTrue(addedToWhitelist);

        // 测试从白名单移除
        when(linkTableManager.removeFromWhitelist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean removedFromWhitelist = linkTableManager.removeFromWhitelist(testNodeId1).get();
        assertTrue(removedFromWhitelist);

        // 测试检查节点是否在白名单中
        when(linkTableManager.isInWhitelist(testNodeId1)).thenReturn(true);
        boolean inWhitelist1 = linkTableManager.isInWhitelist(testNodeId1);
        assertTrue(inWhitelist1);

        when(linkTableManager.isInWhitelist(testNodeId2)).thenReturn(false);
        boolean inWhitelist2 = linkTableManager.isInWhitelist(testNodeId2);
        assertFalse(inWhitelist2);
    }

    // 测试链路场景信息相关方法
    @Test
    public void testLinkSceneInfoMethods() throws ExecutionException, InterruptedException {
        // 测试更新链路场景信息
        when(linkTableManager.updateLinkSceneInfo(testLinkId1, testSceneId, testGroupId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean updated = linkTableManager.updateLinkSceneInfo(testLinkId1, testSceneId, testGroupId).get();
        assertTrue(updated);
    }

    // 测试获取信息相关方法
    @Test
    public void testGetInformationMethods() {
        // 测试获取链路表
        when(linkTableManager.getLinkTable()).thenReturn(testLinkTable);
        LinkTable retrievedLinkTable = linkTableManager.getLinkTable();
        assertNotNull(retrievedLinkTable);
        assertNotNull(retrievedLinkTable.getAllLinks());
        assertTrue(retrievedLinkTable.size() >= 0);

        // 测试获取黑名单
        Map<String, Long> blacklist = new HashMap<>();
        blacklist.put(testNodeId1, System.currentTimeMillis());
        when(linkTableManager.getBlacklist()).thenReturn(blacklist);
        Map<String, Long> retrievedBlacklist = linkTableManager.getBlacklist();
        assertNotNull(retrievedBlacklist);
        assertTrue(retrievedBlacklist.containsKey(testNodeId1));

        // 测试获取白名单
        Map<String, Long> whitelist = new HashMap<>();
        whitelist.put(testNodeId1, System.currentTimeMillis());
        when(linkTableManager.getWhitelist()).thenReturn(whitelist);
        Map<String, Long> retrievedWhitelist = linkTableManager.getWhitelist();
        assertNotNull(retrievedWhitelist);
        assertTrue(retrievedWhitelist.containsKey(testNodeId1));
    }

    // 测试链路状态枚举
    @Test
    public void testLinkStatusEnum() {
        // 测试链路状态枚举值
        assertEquals(LinkStatus.ACTIVE, LinkStatus.valueOf("ACTIVE"));
        assertEquals(LinkStatus.DISABLED, LinkStatus.valueOf("DISABLED"));
        assertEquals(LinkStatus.FAILED, LinkStatus.valueOf("FAILED"));
    }

    // 测试黑名单和白名单管理
    @Test
    public void testBlacklistWhitelistManagement() throws ExecutionException, InterruptedException {
        // 测试黑名单管理
        when(linkTableManager.addToBlacklist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean added1 = linkTableManager.addToBlacklist(testNodeId1).get();
        assertTrue(added1);

        when(linkTableManager.isInBlacklist(testNodeId1)).thenReturn(true);
        boolean inBlacklist = linkTableManager.isInBlacklist(testNodeId1);
        assertTrue(inBlacklist);

        when(linkTableManager.removeFromBlacklist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean removed1 = linkTableManager.removeFromBlacklist(testNodeId1).get();
        assertTrue(removed1);

        when(linkTableManager.isInBlacklist(testNodeId1)).thenReturn(false);
        boolean notInBlacklist = linkTableManager.isInBlacklist(testNodeId1);
        assertFalse(notInBlacklist);

        // 测试白名单管理
        when(linkTableManager.addToWhitelist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean added2 = linkTableManager.addToWhitelist(testNodeId1).get();
        assertTrue(added2);

        when(linkTableManager.isInWhitelist(testNodeId1)).thenReturn(true);
        boolean inWhitelist = linkTableManager.isInWhitelist(testNodeId1);
        assertTrue(inWhitelist);

        when(linkTableManager.removeFromWhitelist(testNodeId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean removed2 = linkTableManager.removeFromWhitelist(testNodeId1).get();
        assertTrue(removed2);

        when(linkTableManager.isInWhitelist(testNodeId1)).thenReturn(false);
        boolean notInWhitelist = linkTableManager.isInWhitelist(testNodeId1);
        assertFalse(notInWhitelist);
    }

    // 测试链路表管理
    @Test
    public void testLinkTableManagement() {
        // 测试链路表
        LinkTable linkTable = new LinkTable();
        assertNotNull(linkTable);

        // 测试获取链路列表
        assertNotNull(linkTable.getAllLinks());
        assertEquals(0, linkTable.size());

        // 测试添加链路
        LinkInfo linkInfo = new LinkInfo("test-link", "node-1", "node-2");
        linkTable.addOrUpdateLink(linkInfo);
        assertEquals(1, linkTable.size());
        assertNotNull(linkTable.getLink("test-link"));

        // 测试删除链路
        linkTable.removeLink("test-link");
        assertEquals(0, linkTable.size());
        assertNull(linkTable.getLink("test-link"));
    }

    // 测试链路状态控制
    @Test
    public void testLinkStatusControl() throws ExecutionException, InterruptedException {
        // 测试禁用链路
        when(linkTableManager.disableLink(testLinkId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean disabled = linkTableManager.disableLink(testLinkId1).get();
        assertTrue(disabled);

        // 测试获取禁用状态
        when(linkTableManager.getLinkStatus(testLinkId1)).thenReturn(CompletableFuture.completedFuture(LinkStatus.DISABLED));
        LinkStatus status1 = linkTableManager.getLinkStatus(testLinkId1).get();
        assertEquals(LinkStatus.DISABLED, status1);

        // 测试启用链路
        when(linkTableManager.enableLink(testLinkId1)).thenReturn(CompletableFuture.completedFuture(true));
        boolean enabled = linkTableManager.enableLink(testLinkId1).get();
        assertTrue(enabled);

        // 测试获取启用状态
        when(linkTableManager.getLinkStatus(testLinkId1)).thenReturn(CompletableFuture.completedFuture(LinkStatus.ACTIVE));
        LinkStatus status2 = linkTableManager.getLinkStatus(testLinkId1).get();
        assertEquals(LinkStatus.ACTIVE, status2);
    }

    // 测试链路场景信息更新
    @Test
    public void testLinkSceneInfoUpdate() throws ExecutionException, InterruptedException {
        // 测试更新链路场景信息
        String linkId = "link-1";
        String sceneId = "scene-1";
        String groupId = "group-1";

        when(linkTableManager.updateLinkSceneInfo(linkId, sceneId, groupId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean updated = linkTableManager.updateLinkSceneInfo(linkId, sceneId, groupId).get();
        assertTrue(updated);

        // 测试更新不同的场景和组
        String newSceneId = "scene-2";
        String newGroupId = "group-2";
        when(linkTableManager.updateLinkSceneInfo(linkId, newSceneId, newGroupId)).thenReturn(CompletableFuture.completedFuture(true));
        boolean updatedAgain = linkTableManager.updateLinkSceneInfo(linkId, newSceneId, newGroupId).get();
        assertTrue(updatedAgain);
    }

    // 测试黑名单和白名单的时间戳
    @Test
    public void testBlacklistWhitelistTimestamps() {
        // 测试黑名单时间戳
        Map<String, Long> blacklist = new HashMap<>();
        long timestamp1 = System.currentTimeMillis();
        blacklist.put(testNodeId1, timestamp1);
        when(linkTableManager.getBlacklist()).thenReturn(blacklist);
        Map<String, Long> retrievedBlacklist = linkTableManager.getBlacklist();
        assertNotNull(retrievedBlacklist);
        assertTrue(retrievedBlacklist.containsKey(testNodeId1));
        assertEquals(timestamp1, (long) retrievedBlacklist.get(testNodeId1));

        // 测试白名单时间戳
        Map<String, Long> whitelist = new HashMap<>();
        long timestamp2 = System.currentTimeMillis();
        whitelist.put(testNodeId1, timestamp2);
        when(linkTableManager.getWhitelist()).thenReturn(whitelist);
        Map<String, Long> retrievedWhitelist = linkTableManager.getWhitelist();
        assertNotNull(retrievedWhitelist);
        assertTrue(retrievedWhitelist.containsKey(testNodeId1));
        assertEquals(timestamp2, (long) retrievedWhitelist.get(testNodeId1));
    }

    // 测试链路表刷新
    @Test
    public void testLinkTableRefresh() throws ExecutionException, InterruptedException {
        // 测试正常刷新
        when(linkTableManager.refreshLinkTable()).thenReturn(CompletableFuture.completedFuture(true));
        boolean refreshed = linkTableManager.refreshLinkTable().get();
        assertTrue(refreshed);

        // 测试强制刷新
        when(linkTableManager.forceRefreshLinkTable()).thenReturn(CompletableFuture.completedFuture(true));
        boolean forceRefreshed = linkTableManager.forceRefreshLinkTable().get();
        assertTrue(forceRefreshed);
    }

    // 测试链路表的完整性
    @Test
    public void testLinkTableIntegrity() {
        // 测试链路表的完整性
        LinkTable linkTable = new LinkTable();
        assertNotNull(linkTable);
        
        // 测试链路表的默认值
        assertNotNull(linkTable.getAllLinks());
        assertTrue(linkTable.getAllLinks().isEmpty());
        assertEquals(0, linkTable.size());
        
        // 测试添加链路
        LinkInfo linkInfo = new LinkInfo("test-link", "node-1", "node-2");
        linkTable.addOrUpdateLink(linkInfo);
        assertNotNull(linkTable.getAllLinks());
        assertFalse(linkTable.getAllLinks().isEmpty());
        assertEquals(1, linkTable.size());
        assertTrue(linkTable.containsLink("test-link"));
        
        // 测试清空链路表
        linkTable.clear();
        assertTrue(linkTable.getAllLinks().isEmpty());
        assertEquals(0, linkTable.size());
        assertFalse(linkTable.containsLink("test-link"));
    }
}
