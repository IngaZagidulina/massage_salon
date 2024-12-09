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

    @Autowired
    public UserService(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getAuthorities().add(Role.ROLE_CLIENT);
        userRepo.save(user);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void save(User user) {
        userRepo.save(user);
    }

    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    public List<User> findAllTherapists() {
        return userRepo.findAll().stream()
                .filter(u -> u.getAuthorities().contains(Role.ROLE_THERAPIST))
                .collect(Collectors.toList());
    }

    public List<User> findAllClients() {
        return userRepo.findAll().stream()
                .filter(u -> u.getAuthorities().contains(Role.ROLE_CLIENT))
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        userRepo.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public User findById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

}
