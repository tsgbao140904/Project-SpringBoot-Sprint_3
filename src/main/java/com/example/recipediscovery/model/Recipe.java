package com.example.recipediscovery.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_category_id")
    private Long userCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_category_id", insertable = false, updatable = false)
    private UserCategory userCategory;

    @Column(nullable = false, length = 150)
    private String title;

    @Lob
    @Column(nullable = false)
    private String ingredients;

    @Lob
    @Column(nullable = false)
    private String instructions;

    private Integer calories;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    private Integer servings;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_public", nullable = false)
    private Integer isPublic;

    @Column(length = 20)
    private String status;

    @Lob
    @Column(name = "rejected_reason")
    private String rejectedReason;

    private Integer version;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // SHARE FIELDS
    @Column(name = "share_status", length = 20)
    private String shareStatus;

    @Column(name = "share_approved_at")
    private Timestamp shareApprovedAt;

    @Lob
    @Column(name = "share_rejected_reason")
    private String shareRejectedReason;

    public Recipe() {}

    @PrePersist
    void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;

        if (this.calories == null) this.calories = 0;
        if (this.cookingTime == null) this.cookingTime = 0;
        if (this.servings == null) this.servings = 1;
        if (this.isPublic == null) this.isPublic = 0;
        if (this.version == null) this.version = 0;

        // dòng này không ảnh hưởng (giữ nguyên như bạn có)
        if (this.shareStatus == null) this.shareStatus = null;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // =====================================================
    // GETTER - SETTER
    // =====================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getUserCategoryId() { return userCategoryId; }
    public void setUserCategoryId(Long userCategoryId) { this.userCategoryId = userCategoryId; }

    public UserCategory getUserCategory() { return userCategory; }
    public void setUserCategory(UserCategory userCategory) { this.userCategory = userCategory; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Integer getCalories() { return calories; }
    public void setCalories(Integer calories) { this.calories = calories; }

    public Integer getCookingTime() { return cookingTime; }
    public void setCookingTime(Integer cookingTime) { this.cookingTime = cookingTime; }

    public Integer getServings() { return servings; }
    public void setServings(Integer servings) { this.servings = servings; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getIsPublic() { return isPublic; }
    public void setIsPublic(Integer isPublic) { this.isPublic = isPublic; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectedReason() { return rejectedReason; }
    public void setRejectedReason(String rejectedReason) { this.rejectedReason = rejectedReason; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // ==== SHARE getters/setters ====

    public String getShareStatus() { return shareStatus; }
    public void setShareStatus(String shareStatus) { this.shareStatus = shareStatus; }

    public Timestamp getShareApprovedAt() { return shareApprovedAt; }
    public void setShareApprovedAt(Timestamp shareApprovedAt) { this.shareApprovedAt = shareApprovedAt; }

    public String getShareRejectedReason() { return shareRejectedReason; }
    public void setShareRejectedReason(String shareRejectedReason) { this.shareRejectedReason = shareRejectedReason; }
}
