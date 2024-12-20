/**
 * MVC-контроллер для работы с пользователями.
 * Позволяет регистрировать новых пользователей, входить в систему и назначать роли (только для ADMIN).
 */
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

    /**
     * Конструктор контроллера пользователей MVC.
     *
     * @param userService сервис для управления пользователями
     */
    @Autowired
    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображает форму регистрации.
     *
     * @param model модель
     * @return шаблон "register"
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param user   объект пользователя
     * @param result результат валидации
     * @param model  модель
     * @return редирект на "/login" при успешной регистрации или повтор формы при ошибках
     */
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

    /**
     * Отображает форму входа в систему.
     *
     * @return шаблон "login"
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Отображает страницу назначения ролей пользователям (только для ADMIN).
     *
     * @param model модель
     * @return шаблон "admin/assign_role"
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/assign-role")
    public String showAssignRoleForm(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "admin/assign_role";
    }

    /**
     * Назначает роль пользователю (только для ADMIN).
     *
     * @param username имя пользователя
     * @param role     роль для назначения
     * @param model    модель
     * @return редирект обратно на страницу назначения роли или отображение ошибки
     */
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
