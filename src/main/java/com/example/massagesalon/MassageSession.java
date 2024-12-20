/**
 * Сущность, представляющая собой запись о сеансе массажа в БД.
 * Содержит ссылки на клиента, массажиста, тип массажа, дату, время, длительность и цену.
 */
package com.example.massagesalon;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class MassageSession {

    /**
     * Идентификатор записи.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Клиент, записавшийся на массаж.
     */
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    /**
     * Массажист, выполняющий сеанс.
     */
    @ManyToOne
    @JoinColumn(name = "masseur_id")
    private User masseur;

    /**
     * Тип услуги массажа.
     */
    @NotNull(message = "Тип массажа не может быть пустым")
    @Enumerated(EnumType.STRING)
    private MassageType serviceType;

    /**
     * Дата сеанса массажа.
     */
    @NotNull(message = "Дата сеанса не может быть пустой")
    private LocalDate sessionDate;

    /**
     * Время начала сеанса.
     */
    @NotNull(message = "Время начала не может быть пустым")
    private LocalTime startTime;

    /**
     * Длительность сеанса в минутах.
     */
    @Positive(message = "Продолжительность должна быть положительным числом")
    private int duration;

    /**
     * Цена сеанса массажа.
     */
    @Positive(message = "Цена должна быть положительным числом")
    private double price;

    public MassageSession() {}

    @Override
    public String toString() {
        return "MassageSession [id=" + id +
                ", client=" + (client != null ? client.getUsername() : "null") +
                ", masseur=" + (masseur != null ? masseur.getUsername() : "null") +
                ", serviceType=" + serviceType +
                ", sessionDate=" + sessionDate +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", price=" + price + "]";
    }
}
