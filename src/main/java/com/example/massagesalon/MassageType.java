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

    public String getDisplayName() {
        return displayName;
    }
}
