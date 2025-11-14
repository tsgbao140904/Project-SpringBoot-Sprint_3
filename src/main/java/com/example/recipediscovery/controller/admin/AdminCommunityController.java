package com.example.recipediscovery.controller.admin;

import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.repository.RecipeRepository;
import com.example.recipediscovery.service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/admin/community")
public class AdminCommunityController {

    private final RecipeRepository recipeRepo;
    private final RecipeService recipeService;

    public AdminCommunityController(RecipeRepository recipeRepo,
                                    RecipeService recipeService) {
        this.recipeRepo = recipeRepo;
        this.recipeService = recipeService;
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

    @GetMapping("/{id}")
    public String viewDetail(@PathVariable Long id, Model model) {

        Recipe r = recipeService.getById(id);
        model.addAttribute("recipe", r);

        return "admin/community/detail";
    }

    @PostMapping("/{id}/update")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam(required = false) String reason,
                               RedirectAttributes redirect) {

        Recipe r = recipeService.getById(id);

        r.setShareStatus(status);

        if ("REJECTED".equals(status)) {
            r.setShareRejectedReason(reason);
            r.setShareApprovedAt(null);
        } else {
            r.setShareRejectedReason(null);
        }

        if ("APPROVED".equals(status)) {
            r.setShareApprovedAt(new Timestamp(System.currentTimeMillis()));
        }

        recipeRepo.save(r);

        redirect.addFlashAttribute("saved", true);

        return "redirect:/admin/community/" + id;
    }
}
