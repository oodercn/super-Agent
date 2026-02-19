package net.ooder.sdk.api.share.impl;

import net.ooder.sdk.api.share.*;
import net.ooder.sdk.api.share.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SkillShareServiceImplTest {
    
    private SkillShareServiceImpl shareService;
    
    @Before
    public void setUp() {
        shareService = new SkillShareServiceImpl();
    }
    
    @After
    public void tearDown() {
        shareService.shutdown();
    }
    
    @Test
    public void testPrepareShare() throws Exception {
        ShareConfig config = new ShareConfig();
        config.setSkillId("skill-001");
        config.setSkillName("Test Skill");
        config.setVersion("1.0.0");
        
        SharePrepareResult result = shareService.prepareShare("skill-001", config).get(10, TimeUnit.SECONDS);
        
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.getShareId());
        assertEquals("skill-001", result.getSkillId());
        assertEquals("Test Skill", result.getSkillName());
    }
    
    @Test
    public void testSendShareInvitation() throws Exception {
        ShareConfig config = new ShareConfig();
        config.setSkillId("skill-001");
        
        SharePrepareResult prepared = shareService.prepareShare("skill-001", config).get(10, TimeUnit.SECONDS);
        
        shareService.sendShareInvitation("peer-002", prepared.getShareId()).get(10, TimeUnit.SECONDS);
        
        List<ShareInvitation> invitations = shareService.getPendingShareInvitations().get(10, TimeUnit.SECONDS);
        assertEquals(1, invitations.size());
    }
    
    @Test
    public void testAcceptShareInvitation() throws Exception {
        ShareConfig config = new ShareConfig();
        config.setSkillId("skill-001");
        
        SharePrepareResult prepared = shareService.prepareShare("skill-001", config).get(10, TimeUnit.SECONDS);
        shareService.sendShareInvitation("peer-002", prepared.getShareId()).get(10, TimeUnit.SECONDS);
        
        List<ShareInvitation> invitations = shareService.getPendingShareInvitations().get(10, TimeUnit.SECONDS);
        String invitationId = invitations.get(0).getInvitationId();
        
        shareService.acceptShareInvitation(invitationId).get(10, TimeUnit.SECONDS);
        
        List<ShareRecord> history = shareService.getShareHistory().get(10, TimeUnit.SECONDS);
        assertEquals(1, history.size());
    }
    
    @Test
    public void testDeclineShareInvitation() throws Exception {
        ShareConfig config = new ShareConfig();
        config.setSkillId("skill-001");
        
        SharePrepareResult prepared = shareService.prepareShare("skill-001", config).get(10, TimeUnit.SECONDS);
        shareService.sendShareInvitation("peer-002", prepared.getShareId()).get(10, TimeUnit.SECONDS);
        
        List<ShareInvitation> invitations = shareService.getPendingShareInvitations().get(10, TimeUnit.SECONDS);
        String invitationId = invitations.get(0).getInvitationId();
        
        shareService.declineShareInvitation(invitationId).get(10, TimeUnit.SECONDS);
        
        invitations = shareService.getPendingShareInvitations().get(10, TimeUnit.SECONDS);
        assertEquals(0, invitations.size());
    }
    
    @Test
    public void testGetShareProgress() throws Exception {
        ShareConfig config = new ShareConfig();
        config.setSkillId("skill-001");
        
        SharePrepareResult prepared = shareService.prepareShare("skill-001", config).get(10, TimeUnit.SECONDS);
        
        ShareProgress progress = shareService.getShareProgress(prepared.getShareId()).get(10, TimeUnit.SECONDS);
        
        assertNotNull(progress);
        assertEquals("PREPARED", progress.getStatus());
    }
    
    @Test
    public void testCancelShare() throws Exception {
        ShareConfig config = new ShareConfig();
        config.setSkillId("skill-001");
        
        SharePrepareResult prepared = shareService.prepareShare("skill-001", config).get(10, TimeUnit.SECONDS);
        
        shareService.cancelShare(prepared.getShareId()).get(10, TimeUnit.SECONDS);
        
        ShareProgress progress = shareService.getShareProgress(prepared.getShareId()).get(10, TimeUnit.SECONDS);
        assertNull(progress);
    }
    
    @Test
    public void testGetShareHistory() throws Exception {
        List<ShareRecord> history = shareService.getShareHistory().get(10, TimeUnit.SECONDS);
        assertNotNull(history);
    }
    
    @Test
    public void testAddShareListener() {
        ShareEventListener listener = new ShareEventListener() {
            @Override
            public void onSharePrepared(SharePrepareResult result) {}
            @Override
            public void onShareInvitationSent(String shareId, String targetPeerId) {}
            @Override
            public void onShareInvitationReceived(ShareInvitation invitation) {}
            @Override
            public void onShareInvitationAccepted(String invitationId) {}
            @Override
            public void onShareInvitationDeclined(String invitationId) {}
            @Override
            public void onShareProgress(String shareId, ShareProgress progress) {}
            @Override
            public void onShareCompleted(String shareId, boolean success) {}
            @Override
            public void onShareCancelled(String shareId) {}
            @Override
            public void onShareError(String shareId, String errorMessage) {}
        };
        
        shareService.addShareListener(listener);
        shareService.removeShareListener(listener);
    }
}
