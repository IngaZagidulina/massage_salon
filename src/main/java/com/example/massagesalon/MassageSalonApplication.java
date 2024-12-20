/**
 * Класс запуска Spring Boot приложения "MassageSalonApplication".
 * Запускает встроенный веб-сервер и инициализирует все необходимые компоненты.
 */
package com.example.massagesalon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MassageSalonApplication {
    public static void main(String[] args) {
        SpringApplication.run(MassageSalonApplication.class, args);
    }
}
