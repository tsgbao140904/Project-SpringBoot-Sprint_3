package com.example.recipediscovery.repository;

import com.example.recipediscovery.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Dùng khi login
    Optional<User> findByEmail(String email);

    // Kiểm tra khi tạo mới
    boolean existsByEmail(String email);

    // Kiểm tra trùng email khi update
    boolean existsByEmailAndIdNot(String email, Long excludeId);

    // Tìm kiếm theo tên hoặc email (phục vụ Admin)
    @Query("SELECT u FROM User u " +
            "WHERE (:kw IS NULL OR :kw = '' " +
            "   OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :kw, '%')) " +
            "   OR LOWER(u.email) LIKE LOWER(CONCAT('%', :kw, '%')) )")
    Page<User> searchUsers(String kw, Pageable pageable);

    // Lọc theo role (ADMIN/USER)
    Page<User> findByRole(String role, Pageable pageable);

    // Danh sách sắp xếp theo createdAt DESC
    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
