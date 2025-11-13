package com.example.recipediscovery.repository;

import com.example.recipediscovery.model.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {

    List<UserCategory> findByUserId(Long userId);

    Optional<UserCategory> findByIdAndUserId(Long id, Long userId);
}
