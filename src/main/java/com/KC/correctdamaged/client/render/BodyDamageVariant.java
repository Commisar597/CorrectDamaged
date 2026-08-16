package com.KC.correctdamaged.client.render;

import com.KC.correctdamaged.client.render.customRender.CustomCube;

import java.util.List;

public record BodyDamageVariant(List<CustomCube> cubes) {

    public BodyDamageVariant(CustomCube... cubes) {
        this(List.of(cubes));
    }
}