/**
 * Исключение, выбрасываемое, когда ресурс не найден.
 */
package com.example.massagesalon;

public class ResourceNotFoundException extends RuntimeException {
    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message сообщение о том, какой ресурс не найден
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
