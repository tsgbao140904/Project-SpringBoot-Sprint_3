package com.example.recipediscovery.controller.user;

import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.repository.RecipeRepository;
import com.example.recipediscovery.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommunityController {

    private final RecipeRepository recipeRepo;
    private final RecipeService recipeService;

    public CommunityController(RecipeRepository recipeRepo,
                               RecipeService recipeService) {
        this.recipeRepo = recipeRepo;
        this.recipeService = recipeService;
    }

    @GetMapping("/app/community")
    public String community(@RequestParam(required = false) String q,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {

        // 1) Lấy toàn bộ dữ liệu (all)
        List<Recipe> all;
        if (q != null && !q.trim().isEmpty()) {
            all = recipeService.searchCommunityRecipes(q.trim());
        } else {
            all = recipeRepo.findByShareStatusAndIsPublic("APPROVED", 1);
        }

        // 2) Phân trang an toàn
        int pageSize = 12;
        int totalRecipes = all.size();

        int totalPages = (int) Math.ceil(totalRecipes / (double) pageSize);
        if (totalPages < 1) totalPages = 1;        // để UI không bị 0 trang

        // Clamp page để không bị start > end -> lỗi subList
        if (page < 0) page = 0;
        if (page > totalPages - 1) page = totalPages - 1;

        int start = page * pageSize;
        int end = Math.min(start + pageSize, totalRecipes);

        List<Recipe> currentRecipes = (totalRecipes == 0) ? List.of() : all.subList(start, end);

        // 3) Đẩy dữ liệu sang view
        model.addAttribute("recipes", currentRecipes);
        model.addAttribute("keyword", q);
        model.addAttribute("pageNum", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalRecipes", totalRecipes);

        return "user/community-home";
    }

    @GetMapping("/app/community/{id}")
    public String communityDetail(@PathVariable Long id,
                                  HttpSession session,
                                  Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");
        if (su == null) return "redirect:/login";

        Recipe r = recipeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy"));

        if (!"APPROVED".equals(r.getShareStatus())) {
            return "redirect:/app/community";
        }

        boolean isOwner = r.getUserId().equals(su.getId());

        model.addAttribute("recipe", r);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isCommunity", true);

        return "user/recipe-detail";
    }
}
