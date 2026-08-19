package com.KC.correctdamaged.capability.visual;

/**
 * Перечисление сторон для парных конечностей (левая/правая сторона).
 */
public enum LimbSide {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    LimbSide(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}