/**
 * Сервисный класс для работы с пользователями.
 * Реализует UserDetailsService для интеграции со Spring Security.
 * Предоставляет методы для поиска, регистрации, сохранения и удаления пользователей.
 */
package com.example.massagesalon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор сервиса пользователей.
     *
     * @param userRepo        репозиторий пользователей
     * @param passwordEncoder кодировщик паролей
     */
    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрация нового пользователя с присвоением роли CLIENT.
     *
     * @param user пользователь для регистрации
     */
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getAuthorities().add(Role.ROLE_CLIENT);
        userRepo.save(user);
    }

    /**
     * Найти пользователя по логину.
     *
     * @param username логин
     * @return пользователь или null, если не найден
     */
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    /**
     * Сохранить данные о пользователе в базе.
     *
     * @param user пользователь для сохранения
     */
    public void save(User user) {
        userRepo.save(user);
    }

    /**
     * Найти всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Найти всех пользователей с ролью THERAPIST.
     *
     * @return список терапевтов
     */
    public List<User> findAllTherapists() {
        return userRepo.findAll().stream()
                .filter(u -> u.getAuthorities().contains(Role.ROLE_THERAPIST))
                .collect(Collectors.toList());
    }

    /**
     * Найти всех пользователей с ролью CLIENT.
     *
     * @return список клиентов
     */
    public List<User> findAllClients() {
        return userRepo.findAll().stream()
                .filter(u -> u.getAuthorities().contains(Role.ROLE_CLIENT))
                .collect(Collectors.toList());
    }

    /**
     * Удалить пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    /**
     * Загрузить пользователя по логину (метод для Spring Security).
     *
     * @param username логин
     * @return UserDetails найденного пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    /**
     * Найти пользователя по его идентификатору.
     *
     * @param id идентификатор
     * @return пользователь или null, если не найден
     */
    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

}
