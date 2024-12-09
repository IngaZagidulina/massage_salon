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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne
    @JoinColumn(name = "masseur_id")
    private User masseur;

    @NotNull(message = "Тип массажа не может быть пустым")
    @Enumerated(EnumType.STRING)
    private MassageType serviceType;

    @NotNull(message = "Дата сеанса не может быть пустой")
    private LocalDate sessionDate;

    @NotNull(message = "Время начала не может быть пустым")
    private LocalTime startTime;

    @Positive(message = "Продолжительность должна быть положительным числом")
    private int duration;

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
