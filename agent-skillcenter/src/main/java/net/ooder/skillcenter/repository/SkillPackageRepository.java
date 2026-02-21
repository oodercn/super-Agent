package net.ooder.skillcenter.repository;

import net.ooder.skillcenter.model.SkillPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillPackageRepository extends JpaRepository<SkillPackage, Long> {

    Optional<SkillPackage> findBySkillId(String skillId);

    Optional<SkillPackage> findBySkillIdAndVersion(String skillId, String version);

    List<SkillPackage> findByType(String type);

    @Query("SELECT sp FROM SkillPackage sp JOIN sp.capabilities c WHERE c.capabilityId IN :capabilities")
    List<SkillPackage> findByCapabilities(@Param("capabilities") List<String> capabilities);

    @Query("SELECT sp FROM SkillPackage sp JOIN sp.scenes s WHERE s.name IN :scenes")
    List<SkillPackage> findByScenes(@Param("scenes") List<String> scenes);

    @Query("SELECT sp FROM SkillPackage sp WHERE sp.name LIKE %:keyword% OR sp.description LIKE %:keyword% OR sp.skillId LIKE %:keyword%")
    List<SkillPackage> findByKeyword(@Param("keyword") String keyword);

}
