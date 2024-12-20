/**
 * Глобальный обработчик исключений.
 * Данный класс перехватывает исключения, возникающие в приложении, и обрабатывает их, возвращая
 * пользователю понятные сообщения об ошибках или соответствующие страницы.
 */
package com.example.massagesalon;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработчик для всех исключений, не имеющих специфической обработки.
     *
     * @param ex возникшее исключение
     * @return ModelAndView, указывающий на страницу "error" с сообщением об ошибке
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        // Создаем модель и вид для отображения ошибок
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "Произошла ошибка: " + ex.getMessage());
        return mav;
    }

    /**
     * Обработчик исключений типа ResourceNotFoundException.
     *
     * @param ex исключение о том, что ресурс не найден
     * @return ResponseEntity со статусом 404 и сообщением об ошибке
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
