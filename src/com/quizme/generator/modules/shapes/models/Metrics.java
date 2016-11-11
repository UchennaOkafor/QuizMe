package com.quizme.generator.modules.shapes.models;

public enum Metrics {
    AREA,
    PERIMETER;

    public static Metrics getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
