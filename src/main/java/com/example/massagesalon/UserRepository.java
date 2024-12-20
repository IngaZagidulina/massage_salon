/**
 * Репозиторий для сущности User.
 * Позволяет выполнять CRUD операции с пользователями.
 */
package com.example.massagesalon;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Найти пользователя по логину.
     *
     * @param username логин пользователя
     * @return пользователь или null, если не найден
     */
    User findByUsername(String username);
}
