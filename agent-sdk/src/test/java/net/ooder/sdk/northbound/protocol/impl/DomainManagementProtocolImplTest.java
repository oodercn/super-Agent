package net.ooder.sdk.northbound.protocol.impl;

import net.ooder.sdk.northbound.protocol.*;
import net.ooder.sdk.northbound.protocol.model.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.HashMap;

public class DomainManagementProtocolImplTest {
    
    private DomainManagementProtocolImpl domainProtocol;
    
    @Before
    public void setUp() {
        domainProtocol = new DomainManagementProtocolImpl();
    }
    
    @After
    public void tearDown() {
        domainProtocol.shutdown();
    }
    
    @Test
    public void testCreateDomain() throws Exception {
        CreateDomainRequest request = new CreateDomainRequest();
        request.setDomainName("Test Domain");
        request.setDomainType("ENTERPRISE");
        request.setOwnerId("owner-001");
        
        DomainInfo domain = domainProtocol.createDomain(request).get(10, TimeUnit.SECONDS);
        
        assertNotNull(domain);
        assertNotNull(domain.getDomainId());
        assertEquals("Test Domain", domain.getDomainName());
        assertEquals("ENTERPRISE", domain.getDomainType());
        assertEquals(DomainStatus.ACTIVE, domain.getStatus());
    }
    
    @Test
    public void testGetDomain() throws Exception {
        CreateDomainRequest request = new CreateDomainRequest();
        request.setDomainName("Test Domain");
        request.setDomainType("ENTERPRISE");
        request.setOwnerId("owner-001");
        
        DomainInfo created = domainProtocol.createDomain(request).get(10, TimeUnit.SECONDS);
        DomainInfo retrieved = domainProtocol.getDomain(created.getDomainId()).get(10, TimeUnit.SECONDS);
        
        assertNotNull(retrieved);
        assertEquals(created.getDomainId(), retrieved.getDomainId());
    }
    
    @Test
    public void testUpdateDomain() throws Exception {
        CreateDomainRequest createRequest = new CreateDomainRequest();
        createRequest.setDomainName("Test Domain");
        createRequest.setDomainType("ENTERPRISE");
        createRequest.setOwnerId("owner-001");
        
        DomainInfo created = domainProtocol.createDomain(createRequest).get(10, TimeUnit.SECONDS);
        
        UpdateDomainRequest updateRequest = new UpdateDomainRequest();
        updateRequest.setDomainName("Updated Domain");
        
        DomainInfo updated = domainProtocol.updateDomain(created.getDomainId(), updateRequest).get(10, TimeUnit.SECONDS);
        
        assertNotNull(updated);
        assertEquals("Updated Domain", updated.getDomainName());
    }
    
    @Test
    public void testDeleteDomain() throws Exception {
        CreateDomainRequest request = new CreateDomainRequest();
        request.setDomainName("Test Domain");
        request.setDomainType("ENTERPRISE");
        request.setOwnerId("owner-001");
        
        DomainInfo created = domainProtocol.createDomain(request).get(10, TimeUnit.SECONDS);
        domainProtocol.deleteDomain(created.getDomainId()).get(10, TimeUnit.SECONDS);
        
        DomainInfo deleted = domainProtocol.getDomain(created.getDomainId()).get(10, TimeUnit.SECONDS);
        assertNull(deleted);
    }
    
    @Test
    public void testAddMember() throws Exception {
        CreateDomainRequest domainRequest = new CreateDomainRequest();
        domainRequest.setDomainName("Test Domain");
        domainRequest.setDomainType("ENTERPRISE");
        domainRequest.setOwnerId("owner-001");
        
        DomainInfo domain = domainProtocol.createDomain(domainRequest).get(10, TimeUnit.SECONDS);
        
        AddMemberRequest memberRequest = new AddMemberRequest();
        memberRequest.setMemberId("member-001");
        memberRequest.setMemberName("Test Member");
        memberRequest.setDomainRole("MEMBER");
        
        domainProtocol.addMember(domain.getDomainId(), memberRequest).get(10, TimeUnit.SECONDS);
        
        java.util.List<DomainMember> members = domainProtocol.listMembers(domain.getDomainId()).get(10, TimeUnit.SECONDS);
        assertEquals(2, members.size());
    }
    
    @Test
    public void testSetDomainPolicy() throws Exception {
        CreateDomainRequest domainRequest = new CreateDomainRequest();
        domainRequest.setDomainName("Test Domain");
        domainRequest.setDomainType("ENTERPRISE");
        domainRequest.setOwnerId("owner-001");
        
        DomainInfo domain = domainProtocol.createDomain(domainRequest).get(10, TimeUnit.SECONDS);
        
        DomainPolicyConfig policy = new DomainPolicyConfig();
        policy.setAllowedSkills(Arrays.asList("skill-1", "skill-2"));
        
        domainProtocol.setDomainPolicy(domain.getDomainId(), policy).get(10, TimeUnit.SECONDS);
        
        DomainPolicyConfig retrieved = domainProtocol.getDomainPolicy(domain.getDomainId()).get(10, TimeUnit.SECONDS);
        assertNotNull(retrieved);
        assertEquals(2, retrieved.getAllowedSkills().size());
    }
}
