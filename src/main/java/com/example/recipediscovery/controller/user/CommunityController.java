package com.example.recipediscovery.controller.user;

import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.repository.RecipeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommunityController {

    private final RecipeRepository recipeRepo;

    public CommunityController(RecipeRepository recipeRepo) {
        this.recipeRepo = recipeRepo;
    }

    // Trang cộng đồng
    @GetMapping("/app/community")
    public String community(Model model) {
        model.addAttribute("recipes",
                recipeRepo.findByShareStatusAndIsPublic("APPROVED", 1)
        );
        return "user/community-home";
    }

    // Chi tiết công thức cộng đồng
    @GetMapping("/app/community/{id}")
    public String communityDetail(@PathVariable Long id,
                                  HttpSession session,
                                  Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");
        if (su == null) return "redirect:/login";

        Recipe r = recipeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy"));

        // Chỉ cho xem công thức đã approved
        if (!"APPROVED".equals(r.getShareStatus())) {
            return "redirect:/app/community";
        }

        boolean isOwner = r.getUserId().equals(su.getId());

        model.addAttribute("recipe", r);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isCommunity", true);

        return "user/recipe-detail";  // tái sử dụng file
    }
}
