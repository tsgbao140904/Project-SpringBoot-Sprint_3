package com.example.recipediscovery.controller.user;

import com.example.recipediscovery.dto.SessionUser;
import com.example.recipediscovery.model.UserCategory;
import com.example.recipediscovery.service.UserCategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app/categories")   // prefix chung
public class UserCategoryController {

    private final UserCategoryService userCategoryService;

    public UserCategoryController(UserCategoryService userCategoryService) {
        this.userCategoryService = userCategoryService;
    }

    // 1) LIST PAGE
    @GetMapping
    public String list(HttpSession session, Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        model.addAttribute("categories",
                userCategoryService.getByUser(su.getId()));

        return "user/category-manage";
    }

    // 2) CREATE FORM
    @GetMapping("/create")
    public String createForm(Model model) {

        UserCategory empty = new UserCategory();
        model.addAttribute("category", empty);
        model.addAttribute("mode", "create");

        return "user/category-form";
    }

    // 3) HANDLE CREATE
    @PostMapping("/create")
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String icon,
                         @RequestParam(required = false) String color,
                         HttpSession session) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        userCategoryService.create(su.getId(), name, icon, color);

        return "redirect:/app/categories";
    }

    // 4) EDIT FORM
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           HttpSession session,
                           Model model) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        UserCategory cat = userCategoryService.getByIdAndUser(su.getId(), id);

        model.addAttribute("category", cat);
        model.addAttribute("mode", "edit");

        return "user/category-form";
    }

    // 5) HANDLE UPDATE
    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) String icon,
                         @RequestParam(required = false) String color,
                         HttpSession session) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        userCategoryService.update(su.getId(), id, name, icon, color);

        return "redirect:/app/categories";
    }

    // 6) DELETE
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {

        SessionUser su = (SessionUser) session.getAttribute("USER");

        userCategoryService.delete(su.getId(), id);

        return "redirect:/app/categories";
    }
}
