/**
 * REST-контроллер для работы с сеансами массажа через API.
 * Позволяет получать, создавать, изменять и удалять сеансы массажа.
 * Все методы доступны только пользователям с ролью ADMIN.
 */
package com.example.massagesalon;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class MassageSessionApiController {

    private final MassageSessionService service;

    /**
     * Конструктор контроллера, внедряет сервис для работы с сеансами массажа.
     *
     * @param service сервис для операций с сеансами массажа
     */
    @Autowired
    public MassageSessionApiController(MassageSessionService service) {
        this.service = service;
    }

    /**
     * Получить все сеансы массажа или отфильтрованные по ключевому слову.
     *
     * @param keyword ключевое слово для поиска (необязательный параметр)
     * @return список сеансов массажа
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MassageSession> getAllSessions(@RequestParam(required = false) String keyword) {
        return service.listAll(keyword);
    }

    /**
     * Получить сеанс массажа по его ID.
     *
     * @param id идентификатор сеанса
     * @return объект MassageSession или 404, если не найден
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MassageSession> getSessionById(@PathVariable Long id) {
        MassageSession session = service.get(id);
        if (session != null) {
            return ResponseEntity.ok(session);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Создать новый сеанс массажа.
     *
     * @param session данные о сеансе
     * @return созданный сеанс массажа
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MassageSession> createSession(@Valid @RequestBody MassageSession session) {
        service.save(session);
        return ResponseEntity.ok(session);
    }

    /**
     * Обновить существующий сеанс массажа.
     *
     * @param id идентификатор сеанса для обновления
     * @param sessionDetails новые данные для сеанса
     * @return обновленный сеанс или 404, если не найден
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MassageSession> updateSession(@PathVariable Long id, @Valid @RequestBody MassageSession sessionDetails) {
        MassageSession session = service.get(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }

        session.setClient(sessionDetails.getClient());
        session.setMasseur(sessionDetails.getMasseur());
        session.setServiceType(sessionDetails.getServiceType());
        session.setSessionDate(sessionDetails.getSessionDate());
        session.setStartTime(sessionDetails.getStartTime());
        session.setDuration(sessionDetails.getDuration());
        session.setPrice(sessionDetails.getPrice());

        service.save(session);
        return ResponseEntity.ok(session);
    }

    /**
     * Удалить сеанс массажа по ID.
     *
     * @param id идентификатор сеанса
     * @return пустой ответ со статусом 204 или 404, если не найден
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        MassageSession session = service.get(id);
        if (session == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
