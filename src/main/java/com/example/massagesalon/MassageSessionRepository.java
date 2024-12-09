package com.example.massagesalon;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MassageSessionRepository extends JpaRepository<MassageSession, Long> {

    @Query("SELECT s FROM MassageSession s WHERE CONCAT(s.client.username, ' ', s.masseur.username, ' ', s.serviceType, ' ', s.sessionDate, ' ', s.startTime) LIKE %:searchTerm%")
    List<MassageSession> search(@Param("searchTerm") String searchTerm);

    List<MassageSession> findByMasseur_Username(String username);
    List<MassageSession> findByClient_Username(String username);

    @Query("SELECT DISTINCT s.serviceType FROM MassageSession s")
    List<MassageType> findAllServiceTypes();
}
