package net.ooder.nexus.infrastructure.repository;

import net.ooder.nexus.domain.admin.model.AdminGroupMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminGroupMemberRepository extends JpaRepository<AdminGroupMember, Long> {

    List<AdminGroupMember> findByGroupId(String groupId);

    Optional<AdminGroupMember> findByGroupIdAndUserId(String groupId, String userId);

    void deleteByGroupIdAndUserId(String groupId, String userId);

    void deleteByGroupId(String groupId);

    int countByGroupId(String groupId);
}
