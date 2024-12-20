/**
 * Сущность пользователя.
 * Хранит данные о логине, пароле, email и ролях.
 * Реализует интерфейс UserDetails для интеграции с Spring Security.
 */
package com.example.massagesalon;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальный логин пользователя.
     */
    @Column(unique = true)
    @NotBlank(message = "Логин не может быть пустым")
    private String username;

    /**
     * Пароль пользователя.
     */
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    /**
     * Адрес электронной почты пользователя.
     */
    @Email(message = "Некорректный формат электронной почты")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    /**
     * Роли пользователя, хранятся в отдельной таблице user_roles.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> authorities = new HashSet<>();

    public User() {}

    @Override
    public Collection<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
