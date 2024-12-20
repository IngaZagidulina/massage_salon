/**
 * Перечисление типов массажа.
 * Содержит различные виды массажа с локализованными названиями.
 */
package com.example.massagesalon;

public enum MassageType {
    РАССЛАБЛЯЮЩИЙ("Расслабляющий массаж"),
    ТЕРАПЕВТИЧЕСКИЙ("Терапевтический массаж"),
    СПОРТИВНЫЙ("Спортивный массаж"),
    ГЛУБОКИЙ("Глубокий массаж тканей"),
    ТАЙСКИЙ("Тайский массаж");

    private final String displayName;

    MassageType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Получить локализованное название типа массажа.
     *
     * @return строковое представление типа массажа
     */
    public String getDisplayName() {
        return displayName;
    }
}
