package com.example.massagesalon;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserMvcController {

    private final UserService userService;

    @Autowired
    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "Пользователь с таким логином уже существует.");
            return "register";
        }
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/assign-role")
    public String showAssignRoleForm(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "admin/assign_role";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/assign-role")
    public String assignRole(@RequestParam("username") String username, @RequestParam("role") String role, Model model) {
        User user = userService.findByUsername(username);
        if (user != null) {
            try {
                user.getAuthorities().clear();
                user.getAuthorities().add(Role.valueOf(role));
                userService.save(user);
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", "Некорректная роль: " + role);
                List<User> users = userService.findAllUsers();
                model.addAttribute("users", users);
                model.addAttribute("roles", Role.values());
                return "admin/assign_role";
            }
        } else {
            model.addAttribute("errorMessage", "Пользователь не найден");
            List<User> users = userService.findAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("roles", Role.values());
            return "admin/assign_role";
        }
        return "redirect:/admin/assign-role";
    }
}
