package com.example.recipediscovery.controller.user;

import com.example.recipediscovery.dto.RecipeRequest;
import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.model.Recipe;
import com.example.recipediscovery.model.UserCategory;
import com.example.recipediscovery.service.RecipeService;
import com.example.recipediscovery.service.UserCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/app/recipes")
public class UserRecipeController {

    private final RecipeService recipeService;
    private final UserCategoryService userCategoryService;

    public UserRecipeController(RecipeService recipeService,
                                UserCategoryService userCategoryService) {
        this.recipeService = recipeService;
        this.userCategoryService = userCategoryService;
    }

    @GetMapping
    public String listRedirect() {
        return "redirect:/app/home";
    }

    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        List<UserCategory> categories = userCategoryService.getByUser(su.getId());

        model.addAttribute("recipe", new RecipeRequest());
        model.addAttribute("categories", categories);
        model.addAttribute("mode", "create");

        return "user/recipe-form";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("recipe") RecipeRequest req,
                         HttpSession session) {

        SessionUser su = (SessionUser) session.getAttribute("USER");
        recipeService.createForUser(su.getId(), req);

        return "redirect:/app/home?created";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           HttpSession session,
                           Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        Recipe r = recipeService.getById(id);

        RecipeRequest req = new RecipeRequest();
        req.setUserCategoryId(r.getUserCategoryId());
        req.setTitle(r.getTitle());
        req.setIngredients(r.getIngredients());
        req.setInstructions(r.getInstructions());
        req.setCalories(r.getCalories());
        req.setCookingTime(r.getCookingTime());
        req.setServings(r.getServings());
        req.setImageUrl(r.getImageUrl());

        List<UserCategory> categories = userCategoryService.getByUser(su.getId());

        model.addAttribute("recipe", req);
        model.addAttribute("categories", categories);
        model.addAttribute("mode", "edit");
        model.addAttribute("id", id);

        return "user/recipe-form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @ModelAttribute("recipe") RecipeRequest req) {

        recipeService.updateForUser(id, req);
        return "redirect:/app/home?updated";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        recipeService.deleteForUser(id);
        return "redirect:/app/home?deleted";
    }

    @PostMapping("/{id}/share")
    public String share(@PathVariable Long id) {
        recipeService.shareForUser(id);
        return "redirect:/app/home?shared";
    }


    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         HttpSession session,
                         Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");
        if (su == null) return "redirect:/login";

        Recipe r = recipeService.getById(id);

        boolean isOwner = r.getUserId().equals(su.getId());

        // nếu không phải owner → chỉ xem nếu đã được duyệt
        if (!isOwner && !"APPROVED".equals(r.getShareStatus())) {
            return "redirect:/app/home";
        }

        model.addAttribute("recipe", r);
        model.addAttribute("isOwner", isOwner);

        return "user/recipe-detail";
    }

}
