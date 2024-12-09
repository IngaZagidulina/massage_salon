package com.example.massagesalon;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Сервис для управления сеансами массажа.

@Service
public class MassageSessionService {

    private final MassageSessionRepository repo;

    @Autowired
    public MassageSessionService(MassageSessionRepository repo) {
        this.repo = repo;
    }

    // Получает список всех сеансов массажа, с возможностью поиска по ключевому слову.
    public List<MassageSession> listAll(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    // Сохраняет сеанс массажа.
    public void save(MassageSession session) {
        repo.save(session);
    }

    // Получает сеанс массажа по ID.
    public MassageSession get(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Удаляет сеанс массажа по ID.
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // Находит сеансы по имени терапевта.
    public List<MassageSession> findByTherapist(String therapistName) {
        return repo.findByMasseur_Username(therapistName);
    }

    // Получает список всех типов массажа из перечисления MassageType.
    public List<MassageType> getAllServiceTypes() {
        return Arrays.asList(MassageType.values());
    }

    // Находит сеансы по имени клиента.

    public List<MassageSession> findByClient(String clientName) {
        return repo.findByClient_Username(clientName);
    }
}
