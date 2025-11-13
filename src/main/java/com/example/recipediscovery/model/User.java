package com.example.recipediscovery.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;   // ❗ không mã hoá theo yêu cầu

    @Column(nullable = false, length = 20)
    private String role;       // ADMIN | USER

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";  // ACTIVE | DISABLED | BANNED

    @Column(length = 500)
    private String note; // ghi chú của admin

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    public User() {}

    @PrePersist
    void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;

        if (this.avatarUrl == null || this.avatarUrl.trim().isEmpty()) {
            this.avatarUrl = "https://ui-avatars.com/api/?name=" +
                    (this.fullName != null ? this.fullName : "User") +
                    "&background=4caf50&color=fff";
        }

        if (this.role == null || this.role.trim().isEmpty()) {
            this.role = "USER";
        }
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    // ===== GETTER / SETTER =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // ===== HELPER METHODS =====

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    public boolean isUser() {
        return "USER".equalsIgnoreCase(this.role);
    }
}
