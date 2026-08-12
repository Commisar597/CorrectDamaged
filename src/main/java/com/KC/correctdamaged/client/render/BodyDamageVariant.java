package com.KC.correctdamaged.client.render;

import java.util.List;

public record BodyDamageVariant(List<CustomCube> cubes) {

    public BodyDamageVariant(CustomCube... cubes) {
        this(List.of(cubes));
    }
}