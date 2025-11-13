package com.example.recipediscovery.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_categories")
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "color_code", length = 7)
    private String colorCode;

    @Column(length = 50)
    private String icon;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        if (this.colorCode == null) this.colorCode = "#4caf50";
        if (this.icon == null) this.icon = "utensils";
    }

    // GETTER SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColorCode() { return colorCode; }
    public void setColorCode(String colorCode) { this.colorCode = colorCode; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
