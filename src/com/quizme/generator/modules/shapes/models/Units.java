package com.quizme.generator.modules.shapes.models;

public enum Units {
    CM,
    M;

    public static Units getRandom() {
        return values()[(int) (Math.random() * values().length)];
    }
}
