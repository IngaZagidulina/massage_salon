/**
 * Конфигурация безопасности веб-приложения с использованием Spring Security.
 * Определяет правила доступа к маршрутам, роль и страницу логина.
 */
package com.example.massagesalon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /**
     * Бин для кодирования паролей BCrypt.
     *
     * @return кодировщик паролей
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Настройка фильтра безопасности.
     *
     * @param http объект конфигурации HttpSecurity
     * @return настроенная конфигурация цепочки фильтров безопасности
     * @throws Exception при ошибках конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationSuccessHandler successHandler = new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
                    throws IOException, ServletException {
                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                String redirectUrl = "/";

                if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                    redirectUrl = "/admin";
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_THERAPIST"))) {
                    redirectUrl = "/therapist/sessions";
                } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))) {
                    redirectUrl = "/client/sessions";
                }

                response.sendRedirect(redirectUrl);
            }
        };

        http
                // Игнорируем CSRF для API запросов
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/sessions/**", "/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**", "/statistics").hasRole("ADMIN")
                        .requestMatchers("/therapist/**").hasRole("THERAPIST")
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/", "/register", "/login", "/about", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
