package com.example.recipediscovery.controller.admin;

import com.example.recipediscovery.repository.RecipeRepository;
import com.example.recipediscovery.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    public AdminController(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("pending", recipeRepository.countByShareStatus("PENDING"));
        model.addAttribute("approved", recipeRepository.countByShareStatus("APPROVED"));
        model.addAttribute("rejected", recipeRepository.countByShareStatus("REJECTED"));

        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }
}
