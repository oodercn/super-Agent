package net.ooder.nexus.infrastructure.repository;

import net.ooder.nexus.domain.admin.model.AdminGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminGroupRepository extends JpaRepository<AdminGroup, Long> {

    Optional<AdminGroup> findByGroupId(String groupId);

    List<AdminGroup> findByStatus(String status);

    boolean existsByGroupId(String groupId);

    void deleteByGroupId(String groupId);
}
