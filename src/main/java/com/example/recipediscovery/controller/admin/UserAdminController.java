package com.example.recipediscovery.controller.admin;

import com.example.recipediscovery.model.User;
import com.example.recipediscovery.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {

        Page<User> users = userService.getUsers(page);

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());

        return "admin/users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/admin/users?created";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "admin/users/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin/users?updated";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users?deleted";
    }
}
