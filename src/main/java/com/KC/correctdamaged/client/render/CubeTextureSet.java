package com.KC.correctdamaged.client.render;

import net.minecraft.resources.ResourceLocation;

public record CubeTextureSet(
        CubeTexture front,
        CubeTexture back,
        CubeTexture left,
        CubeTexture right,
        CubeTexture top,
        CubeTexture bottom
) {

    public CubeTexture get(CubeFace face) {
        return switch (face) {
            case FRONT -> front;
            case BACK -> back;
            case LEFT -> left;
            case RIGHT -> right;
            case TOP -> top;
            case BOTTOM -> bottom;
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private CubeTexture front;
        private CubeTexture back;
        private CubeTexture left;
        private CubeTexture right;
        private CubeTexture top;
        private CubeTexture bottom;

        public Builder front(CubeTexture texture) {
            this.front = texture;
            return this;
        }

        public Builder back(CubeTexture texture) {
            this.back = texture;
            return this;
        }

        public Builder left(CubeTexture texture) {
            this.left = texture;
            return this;
        }

        public Builder right(CubeTexture texture) {
            this.right = texture;
            return this;
        }

        public Builder top(CubeTexture texture) {
            this.top = texture;
            return this;
        }

        public Builder bottom(CubeTexture texture) {
            this.bottom = texture;
            return this;
        }

        public Builder all(CubeTexture texture) {
            this.front = texture;
            this.back = texture;
            this.left = texture;
            this.right = texture;
            this.top = texture;
            this.bottom = texture;

            return this;
        }

        public CubeTextureSet build() {
            return new CubeTextureSet(
                    front,
                    back,
                    left,
                    right,
                    top,
                    bottom
            );
        }
    }
}