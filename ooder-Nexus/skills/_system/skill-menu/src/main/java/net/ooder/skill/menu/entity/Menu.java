package net.ooder.skill.menu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_menu")
@Data
public class Menu {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 100)
    private String menuId;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(length = 200)
    private String title;
    
    @Column(length = 500)
    private String url;
    
    @Column(length = 100)
    private String icon;
    
    @Column(length = 50)
    private String parentId;
    
    @Column(nullable = false)
    private Integer sort = 0;
    
    @Column(length = 50)
    private String category;
    
    @Column(length = 100)
    private String requiredSkill;
    
    @Column(nullable = false)
    private Boolean visible = true;
    
    @Column(nullable = false)
    private Boolean enabled = true;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Boolean deleted = false;
    
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
