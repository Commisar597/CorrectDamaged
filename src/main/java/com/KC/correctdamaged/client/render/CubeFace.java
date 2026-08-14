package com.KC.correctdamaged.client.render;

public enum CubeFace {

    FRONT(0, 0, -1),
    BACK(0, 0, 1),

    LEFT(-1, 0, 0),
    RIGHT(1, 0, 0),

    TOP(0, -1, 0),
    BOTTOM(0, 1, 0);

    private final float nx;
    private final float ny;
    private final float nz;

    CubeFace(float nx, float ny, float nz) {
        this.nx = nx;
        this.ny = ny;
        this.nz = nz;
    }

    public float nx() {
        return nx;
    }

    public float ny() {
        return ny;
    }

    public float nz() {
        return nz;
    }
}