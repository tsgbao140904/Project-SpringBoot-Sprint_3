package com.example.recipediscovery.repository;

import com.example.recipediscovery.model.MealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
    // FIX: mealPlanId không phải field trong entity, đúng là mealPlan.id
    List<MealPlanItem> findByMealPlan_Id(Long mealPlanId);
}
