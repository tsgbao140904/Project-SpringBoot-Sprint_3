package com.example.recipediscovery.controller.user;

import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.model.UserCategory;
import com.example.recipediscovery.service.RecipeService;
import com.example.recipediscovery.service.UserCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {

    private final RecipeService recipeService;
    private final UserCategoryService userCategoryService;

    public UserController(RecipeService recipeService,
                          UserCategoryService userCategoryService) {
        this.recipeService = recipeService;
        this.userCategoryService = userCategoryService;
    }

    @GetMapping("/app/home")
    public String home(@RequestParam(required = false) String q,
                       @RequestParam(name = "userCategoryId", required = false) Long categoryId,
                       HttpSession session,
                       Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");
        if (su == null) return "redirect:/login";

        List<UserCategory> categories = userCategoryService.getByUser(su.getId());
        model.addAttribute("categories", categories);

        // Gọi đúng vị trí tham số
        List<Recipe> recipes = recipeService.searchUserRecipes(
                su.getId(),
                q,
                categoryId
        );

        model.addAttribute("recipes", recipes);
        model.addAttribute("keyword", q);
        model.addAttribute("userCategoryId", categoryId);

        return "user/home";
    }
}
