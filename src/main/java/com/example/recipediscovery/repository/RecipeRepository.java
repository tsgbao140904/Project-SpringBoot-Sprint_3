package com.example.recipediscovery.repository;

import com.example.recipediscovery.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface    RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUserId(Long userId);

    // user tìm kiếm
    @Query("select r from Recipe r " +
            "where r.userId = :userId " +
            "and (:categoryId is null or r.userCategoryId = :categoryId) " +
            "and (:kw = '' or r.title like %:kw% or r.ingredients like %:kw%) " +
            "order by r.createdAt desc")
    List<Recipe> searchUserRecipes(Long userId, Long categoryId, String kw);

    // 🔥 NEW: lấy theo trạng thái chia sẻ
    List<Recipe> findByShareStatus(String shareStatus);

    List<Recipe> findByShareStatusAndIsPublic(String shareStatus, Integer isPublic);

}
