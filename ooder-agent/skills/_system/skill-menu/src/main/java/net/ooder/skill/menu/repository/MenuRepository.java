package net.ooder.skill.menu.repository;

import net.ooder.skill.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    
    Optional<Menu> findByMenuId(String menuId);
    
    List<Menu> findByParentIdOrderBySortAsc(String parentId);
    
    List<Menu> findByParentIdIsNullOrderBySortAsc();
    
    List<Menu> findByEnabledTrueAndDeletedFalseOrderBySortAsc();
    
    List<Menu> findByCategoryOrderBySortAsc(String category);
    
    @Query("SELECT m FROM Menu m WHERE m.enabled = true AND m.deleted = false AND m.visible = true ORDER BY m.sort ASC")
    List<Menu> findAllVisible();
    
    @Query("SELECT m FROM Menu m WHERE m.deleted = false ORDER BY m.sort ASC")
    List<Menu> findAllNotDeleted();
    
    @Query("SELECT m FROM Menu m WHERE m.parentId IS NULL AND m.enabled = true AND m.deleted = false AND m.visible = true ORDER BY m.sort ASC")
    List<Menu> findRootMenus();
    
    @Query("SELECT m FROM Menu m WHERE m.parentId = :parentId AND m.enabled = true AND m.deleted = false AND m.visible = true ORDER BY m.sort ASC")
    List<Menu> findVisibleByParentId(@Param("parentId") String parentId);
    
    boolean existsByMenuId(String menuId);
    
    void deleteByMenuId(String menuId);
}
