package com.example.recipediscovery.dto;

import org.springframework.web.multipart.MultipartFile;

public class RecipeRequest {

    /**
     * 🔥 Category cũ đã bị loại bỏ
     * Đây là ID của UserCategory (chế độ ăn riêng của từng user)
     */
    private Long userCategoryId;

    private String title;
    private String ingredients;
    private String instructions;

    private Integer calories;
    private Integer cookingTime;
    private Integer servings;

    /**
     * Ảnh từ URL (ưu tiên khi không upload file)
     */
    private String imageUrl;

    /**
     * Ảnh upload từ máy
     */
    private MultipartFile imageFile;

    public RecipeRequest() {}

    // ======= GETTER / SETTER =======

    public Long getUserCategoryId() {
        return userCategoryId;
    }

    public void setUserCategoryId(Long userCategoryId) {
        this.userCategoryId = userCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Integer cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
