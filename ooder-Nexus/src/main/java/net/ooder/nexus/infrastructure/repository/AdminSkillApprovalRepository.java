package net.ooder.nexus.infrastructure.repository;

import net.ooder.nexus.domain.admin.model.AdminSkillApproval;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminSkillApprovalRepository extends JpaRepository<AdminSkillApproval, Long> {

    Optional<AdminSkillApproval> findBySkillId(String skillId);

    List<AdminSkillApproval> findByApprovalStatus(String status);

    boolean existsBySkillId(String skillId);

    void deleteBySkillId(String skillId);
}
