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

    @Autowired
    public MassageSessionApiController(MassageSessionService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MassageSession> getAllSessions(@RequestParam(required = false) String keyword) {
        return service.listAll(keyword);
    }

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MassageSession> createSession(@Valid @RequestBody MassageSession session) {
        service.save(session);
        return ResponseEntity.ok(session);
    }

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
