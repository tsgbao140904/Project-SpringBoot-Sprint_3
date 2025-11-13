package com.example.recipediscovery.controller.admin;

import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/admin/community")
public class AdminCommunityController {

    private final RecipeRepository recipeRepo;

    public AdminCommunityController(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "pending") String tab, Model model) {

        List<Recipe> data = switch (tab) {
            case "approved" -> recipeRepo.findByShareStatus("APPROVED");
            case "rejected" -> recipeRepo.findByShareStatus("REJECTED");
            default -> recipeRepo.findByShareStatus("PENDING");
        };

        model.addAttribute("recipes", data);
        model.addAttribute("tab", tab);

        return "admin/community/list";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {

        Recipe r = recipeRepo.findById(id).orElseThrow();

        r.setShareStatus("APPROVED");
        r.setShareApprovedAt(new Timestamp(System.currentTimeMillis()));
        r.setShareRejectedReason(null);

        recipeRepo.save(r);

        return "redirect:/admin/community?tab=pending&success";
    }

    @PostMapping("/{id}/reject")
    public String reject(
            @PathVariable Long id,
            @RequestParam("reason") String reason
    ) {
        Recipe r = recipeRepo.findById(id).orElseThrow();

        r.setShareStatus("REJECTED");
        r.setShareRejectedReason(reason);
        r.setShareApprovedAt(null);

        recipeRepo.save(r);

        return "redirect:/admin/community?tab=pending&rejected";
    }
}
