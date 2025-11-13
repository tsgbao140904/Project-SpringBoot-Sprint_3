package com.example.recipediscovery.service;

import com.example.recipediscovery.dto.RecipeRequest;
import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepo;

    public RecipeService(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    // Lấy công thức theo user
    public List<Recipe> getRecipesByUser(Long userId) {
        return recipeRepo.findByUserId(userId);
    }

    // Tìm kiếm + Lọc theo user_category_id
    public List<Recipe> searchUserRecipes(Long userId,
                                          String keyword,
                                          Long categoryId) {

        if (keyword == null) keyword = "";
        String kwTrim = keyword.trim();

        if (kwTrim.isEmpty() && categoryId == null) {
            return getRecipesByUser(userId);
        }

        String kwLike = kwTrim.isBlank() ? "" : "%" + kwTrim + "%";

        return recipeRepo.searchUserRecipes(userId, categoryId, kwLike);
    }

    public Recipe getById(Long id) {
        return recipeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy công thức"));
    }

    public Recipe createForUser(Long userId, RecipeRequest req) {
        Recipe r = new Recipe();
        r.setUserId(userId);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        r.setCreatedAt(now);
        r.setUpdatedAt(now);

        applyRequestToEntity(req, r);
        return recipeRepo.save(r);
    }

    public Recipe updateForUser(Long id, RecipeRequest req) {
        Recipe r = getById(id);
        r.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        applyRequestToEntity(req, r);
        return recipeRepo.save(r);
    }

    public void deleteForUser(Long id) {
        recipeRepo.deleteById(id);
    }

    public void shareForUser(Long id) {
        Recipe r = getById(id);

        r.setIsPublic(1);                  // cho phép hiển thị cộng đồng
        r.setShareStatus("PENDING");       // chờ duyệt
        r.setShareRejectedReason(null);    // xóa lý do cũ

        recipeRepo.save(r);
    }


    // ================== helpers ==================
    private void applyRequestToEntity(RecipeRequest req, Recipe r) {

        // 🔥 cập nhật userCategoryId mới
        r.setUserCategoryId(req.getUserCategoryId());

        r.setTitle(req.getTitle());
        r.setIngredients(req.getIngredients());
        r.setInstructions(req.getInstructions());
        r.setCalories(req.getCalories());
        r.setCookingTime(req.getCookingTime());
        r.setServings(req.getServings());

        String finalUrl = handleImage(req.getImageUrl(), req.getImageFile());
        if (finalUrl != null && finalUrl.length() > 500) {
            finalUrl = finalUrl.substring(0, 500);
        }
        r.setImageUrl(finalUrl);
    }

    private String handleImage(String urlFromInput, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                String rootDir = System.getProperty("user.dir");
                File uploadDir = new File(rootDir, "uploads");

                if (!uploadDir.exists()) uploadDir.mkdirs();

                String originalName = file.getOriginalFilename();
                if (originalName == null) originalName = "image";

                String clean = originalName.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
                String fileName = UUID.randomUUID() + "_" + clean;

                File dest = new File(uploadDir, fileName);
                file.transferTo(dest);

                return "/images/" + fileName;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (urlFromInput == null) return null;
        String trimmed = urlFromInput.trim();
        if (trimmed.isEmpty()) return null;

        return trimmed;
    }

}
