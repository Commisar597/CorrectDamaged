package com.KC.correctdamaged.client.render.customRender;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class FreeUVCubeRenderer {

    private FreeUVCubeRenderer() {}

    public record FaceUV(float u0, float v0, float u1, float v1) {
        public static FaceUV of(float u0, float v0, float u1, float v1) {
            return new FaceUV(u0, v0, u1, v1);
        }
    }

    public static void renderBox(
            PoseStack.Pose pose, VertexConsumer consumer,
            float x0, float y0, float z0,
            float x1, float y1, float z1,
            int texWidth, int texHeight,
            int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha,
            CubeUV uv
    ) {
        Vector3f a = new Vector3f(x0, y0, z0);
        Vector3f b = new Vector3f(x1, y0, z0);
        Vector3f c = new Vector3f(x1, y1, z0);
        Vector3f d = new Vector3f(x0, y1, z0);

        Vector3f e = new Vector3f(x0, y0, z1);
        Vector3f f = new Vector3f(x1, y0, z1);
        Vector3f g = new Vector3f(x1, y1, z1);
        Vector3f h = new Vector3f(x0, y1, z1);

        if (uv.front() != null) {
            quad(pose, consumer, a, b, c, d, uv.front(), texWidth, texHeight, packedLight,
                    packedOverlay, red, green, blue, alpha, 0, 0, -1);
        }

        if (uv.back() != null) {
            quad(pose, consumer, f, e, h, g, uv.back(), texWidth, texHeight, packedLight,
                    packedOverlay, red, green, blue, alpha, 0, 0, 1);
        }

        if (uv.left() != null) {
            quad(pose, consumer, e, a, d, h, uv.left(), texWidth, texHeight, packedLight,
                    packedOverlay, red, green, blue, alpha, -1, 0, 0);
        }

        if (uv.right() != null) {
            quad(pose, consumer, b, f, g, c, uv.right(), texWidth, texHeight, packedLight,
                    packedOverlay, red, green, blue, alpha, 1, 0, 0);
        }

        if (uv.top() != null) {
            quad(pose, consumer, e, f, b, a, uv.top(), texWidth, texHeight, packedLight,
                    packedOverlay, red, green, blue, alpha, 0, -1, 0);
        }

        if (uv.bottom() != null) {
            quad(pose, consumer, d, c, g, h, uv.bottom(), texWidth, texHeight, packedLight,
                    packedOverlay, red, green, blue, alpha, 0, 1, 0);
        }
    }

    private static void quad(
            PoseStack.Pose pose, VertexConsumer consumer,
            Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3,
            FaceUV uv, int texWidth, int texHeight,
            int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha,
            float nx, float ny, float nz
    ) {

        Vector3f normal = new Vector3f(nx, ny, nz);
        pose.normal().transform(normal);

        float u0 = uv.u0() / texWidth;
        float v0 = uv.v0() / texHeight;
        float u1 = uv.u1() / texWidth;
        float v1 = uv.v1() / texHeight;

        vertex(pose, consumer, p0, u0, v0, packedLight, packedOverlay, red, green, blue, alpha, normal);
        vertex(pose, consumer, p1, u1, v0, packedLight, packedOverlay, red, green, blue, alpha, normal);
        vertex(pose, consumer, p2, u1, v1, packedLight, packedOverlay, red, green, blue, alpha, normal);
        vertex(pose, consumer, p3, u0, v1, packedLight, packedOverlay, red, green, blue, alpha, normal);
    }

    private static void vertex(
            PoseStack.Pose pose, VertexConsumer consumer,
            Vector3f position, float u, float v,
            int packedLight, int packedOverlay,
            float red, float green, float blue, float alpha,
            Vector3f normal
    ) {

        Vector4f pos = new Vector4f(
                position.x() / 16.0F,
                position.y() / 16.0F,
                position.z() / 16.0F,
                1.0F
        );

        pos.mul(pose.pose());

        consumer.vertex(
                pos.x(), pos.y(), pos.z(),
                red, green, blue, alpha,
                u, v,
                packedOverlay, packedLight,
                normal.x(), normal.y(), normal.z()
        );
    }
}