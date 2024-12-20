/**
 * Перечисление ролей пользователей в системе.
 * Каждая роль реализует интерфейс GrantedAuthority для интеграции со Spring Security.
 */
package com.example.massagesalon;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_THERAPIST,
    ROLE_CLIENT;

    @Override
    public String getAuthority() {
        return name();
    }
}
