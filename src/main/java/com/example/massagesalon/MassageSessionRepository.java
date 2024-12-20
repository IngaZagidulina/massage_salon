/**
 * Репозиторий для работы с сущностью MassageSession.
 * Предоставляет методы для CRUD операций и пользовательских запросов.
 */
package com.example.massagesalon;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MassageSessionRepository extends JpaRepository<MassageSession, Long> {

    /**
     * Поиск сеансов по ключевому слову, проверяющемуся по клиенту, массажисту, типу массажа, дате и времени.
     *
     * @param searchTerm ключевой термин для поиска
     * @return список найденных сеансов
     */
    @Query("SELECT s FROM MassageSession s WHERE CONCAT(s.client.username, ' ', s.masseur.username, ' ', s.serviceType, ' ', s.sessionDate, ' ', s.startTime) LIKE %:searchTerm%")
    List<MassageSession> search(@Param("searchTerm") String searchTerm);

    /**
     * Поиск сеансов по имени массажиста.
     *
     * @param username имя массажиста
     * @return список сеансов
     */
    List<MassageSession> findByMasseur_Username(String username);

    /**
     * Поиск сеансов по имени клиента.
     *
     * @param username имя клиента
     * @return список сеансов
     */
    List<MassageSession> findByClient_Username(String username);

    /**
     * Получить все уникальные типы массажа, представленные в записях.
     *
     * @return список уникальных типов массажа
     */
    @Query("SELECT DISTINCT s.serviceType FROM MassageSession s")
    List<MassageType> findAllServiceTypes();
}
