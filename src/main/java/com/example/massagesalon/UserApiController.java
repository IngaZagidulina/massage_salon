/**
 * REST-контроллер для работы с пользователями.
 * Позволяет просматривать, создавать, обновлять и удалять пользователей (только для ADMIN).
 */
package com.example.massagesalon;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    /**
     * Конструктор контроллера пользователей.
     *
     * @param userService сервис для управления пользователями
     */
    @Autowired
    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить список всех пользователей (только для ADMIN).
     *
     * @return список пользователей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    /**
     * Получить пользователя по его ID (только для ADMIN).
     *
     * @param id идентификатор пользователя
     * @return пользователь или 404, если не найден
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Создать нового пользователя (только для ADMIN).
     *
     * @param user данные о пользователе
     * @return созданный пользователь или 400, если пользователь с таким логином уже существует
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if(userService.findByUsername(user.getUsername()) != null){
            return ResponseEntity.badRequest().build();
        }
        userService.registerUser(user);
        return ResponseEntity.ok(user);
    }

    /**
     * Обновить данные существующего пользователя (только для ADMIN).
     *
     * @param id идентификатор пользователя
     * @param userDetails новые данные
     * @return обновленный пользователь или 404, если не найден
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        userService.save(user);
        return ResponseEntity.ok(user);
    }

    /**
     * Удалить пользователя по ID (только для ADMIN).
     *
     * @param id идентификатор пользователя
     * @return пустой ответ со статусом 204 или 404, если не найден
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
