/**
 * Сервисный класс для управления сеансами массажа.
 * Выполняет бизнес-логику, связующую контроллеры и репозиторий.
 */
package com.example.massagesalon;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MassageSessionService {

    private final MassageSessionRepository repo;

    /**
     * Конструктор сервисного класса.
     *
     * @param repo репозиторий для операций с сеансами массажа
     */
    @Autowired
    public MassageSessionService(MassageSessionRepository repo) {
        this.repo = repo;
    }

    /**
     * Получает список всех сеансов массажа или фильтрует их по ключевому слову.
     *
     * @param keyword ключевое слово для поиска
     * @return список сеансов массажа
     */
    public List<MassageSession> listAll(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return repo.search(keyword);
        }
        return repo.findAll();
    }

    /**
     * Сохраняет или обновляет сеанс массажа в базе данных.
     *
     * @param session сеанс для сохранения
     */
    public void save(MassageSession session) {
        repo.save(session);
    }

    /**
     * Получает сеанс массажа по его идентификатору.
     *
     * @param id идентификатор сеанса
     * @return найденный сеанс или null, если не найден
     */
    public MassageSession get(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Удаляет сеанс массажа по его идентификатору.
     *
     * @param id идентификатор сеанса
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Находит сеансы по имени массажиста.
     *
     * @param therapistName имя массажиста
     * @return список сеансов
     */
    public List<MassageSession> findByTherapist(String therapistName) {
        return repo.findByMasseur_Username(therapistName);
    }

    /**
     * Получает все возможные типы массажа из перечисления MassageType.
     *
     * @return список типов массажа
     */
    public List<MassageType> getAllServiceTypes() {
        return Arrays.asList(MassageType.values());
    }

    /**
     * Находит сеансы по имени клиента.
     *
     * @param clientName имя клиента
     * @return список сеансов
     */
    public List<MassageSession> findByClient(String clientName) {
        return repo.findByClient_Username(clientName);
    }
}
