package com.KC.correctdamaged.capability.visual;

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

    public LimbSide getOpposite() {
        return this == LEFT ? RIGHT : LEFT;
    }
}