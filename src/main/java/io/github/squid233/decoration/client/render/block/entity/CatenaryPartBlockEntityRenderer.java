package io.github.squid233.decoration.client.render.block.entity;

import io.github.squid233.decoration.block.CatenaryPartBlock;
import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.entity.CatenaryConnection;
import io.github.squid233.decoration.block.entity.CatenaryPartBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Math;
import org.joml.Vector3f;

/**
 * @author squid233
 * @since 0.1.0
 */
@Environment(EnvType.CLIENT)
public class CatenaryPartBlockEntityRenderer implements BlockEntityRenderer<CatenaryPartBlockEntity> {
    private static final Vec3d CENTER = new Vec3d(0.5, -0.1875, 0.5);
    private static final Vector3f LERP = new Vector3f();
    private static final Vector3f LERP1 = new Vector3f();
    private static final float FALL_FACTOR = 0.05f;
    private static final int SEGMENTS = 8;

    public CatenaryPartBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(CatenaryPartBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final World world = entity.getWorld();
        if (world == null) {
            return;
        }

        final BlockPos pos = entity.getPos();

        matrices.push();

        final MatrixStack.Entry peek = matrices.peek();

        final VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getLines());

        final var offset = getOffset(world, pos).toVector3f();

        for (CatenaryConnection connection : entity.connections()) {
            switch (connection.wireType()) {
                case OVERHEAD_LINE -> {
                    final BlockPos target = connection.target();
                    final var targetOffset = getOffset(world, target)
                        .toVector3f()
                        .add(target.getX(), target.getY(), target.getZ())
                        .sub(pos.getX(), pos.getY(), pos.getZ());

                    // start
                    renderLine(
                        peek, buffer,
                        offset.x(), offset.y(), offset.z(),
                        offset.x(), offset.y() + 1.0f, offset.z()
                    );

                    // end
                    renderLine(
                        peek, buffer,
                        targetOffset.x(), targetOffset.y(), targetOffset.z(),
                        targetOffset.x(), targetOffset.y() + 1.0f, targetOffset.z()
                    );

                    // contact wire
                    renderLine(
                        peek, buffer,
                        offset.x(), offset.y(), offset.z(),
                        targetOffset.x(), targetOffset.y(), targetOffset.z()
                    );

                    // catenary wire
                    renderCatenaryWire(peek, buffer, offset, targetOffset);

                    // droppers
                    final float distance = offset.distance(targetOffset);
                    final int dropperCount = (int) (distance / 5.0);
                    for (int i = 1; i <= dropperCount; i++) {
                        final float percentage = (float) i / (dropperCount + 1);
                        final Vector3f v = offset.lerp(targetOffset, percentage, LERP);
                        renderLine(
                            peek, buffer,
                            v.x(), v.y(), v.z(),
                            v.x(), v.y() + 1.0f + fallFunction(Math.lerp(-1.0f, 1.0f, percentage)), v.z()
                        );
                    }
                }
            }
        }

        matrices.pop();
    }

    private static Vec3d getOffset(World world, BlockPos pos) {
        final BlockState state = world.getBlockState(pos);
        if (!state.isOf(ModBlocks.CATENARY_PART.toBlock())) {
            return CENTER;
        }
        final Direction direction = state.get(CatenaryPartBlock.FACING);
        return CENTER.offset(direction, 0.4375);
    }

    private static void renderLine(
        MatrixStack.Entry entry,
        VertexConsumer buffer,
        float x0, float y0, float z0,
        float x1, float y1, float z1
    ) {
        float xd = x1 - x0;
        float yd = y1 - y0;
        float zd = z1 - z0;
        float invSqrt = 1.0f / Math.sqrt(xd * xd + yd * yd + zd * zd);
        float xn = xd * invSqrt;
        float yn = yd * invSqrt;
        float zn = zd * invSqrt;
        buffer.vertex(entry.getPositionMatrix(), x0, y0, z0)
            .color(0, 0, 0, 255)
            .normal(entry.getNormalMatrix(), xn, yn, zn)
            .next();
        buffer.vertex(entry.getPositionMatrix(), x1, y1, z1)
            .color(0, 0, 0, 255)
            .normal(entry.getNormalMatrix(), xn, yn, zn)
            .next();
    }

    private static void renderCatenaryWire(
        MatrixStack.Entry entry,
        VertexConsumer buffer,
        Vector3f offset,
        Vector3f targetOffset
    ) {
        for (int i = 0; i <= SEGMENTS; i++) {
            final float startPercentage = (float) i / (SEGMENTS + 1);
            final float endPercentage = (float) (i + 1) / (SEGMENTS + 1);
            final Vector3f start = offset.lerp(targetOffset, startPercentage, LERP);
            final Vector3f end = offset.lerp(targetOffset, endPercentage, LERP1);

            // quadratic function
            final float startX = Math.lerp(-1.0f, 1.0f, startPercentage);
            final float endX = Math.lerp(-1.0f, 1.0f, endPercentage);

            renderLine(
                entry, buffer,
                start.x(), start.y() + 1.0f + fallFunction(startX), start.z(),
                end.x(), end.y() + 1.0f + fallFunction(endX), end.z()
            );
        }
    }

    private static float fallFunction(float x) {
        return FALL_FACTOR * (x * x - 1.0f);
    }

    @Override
    public boolean rendersOutsideBoundingBox(CatenaryPartBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getRenderDistance() {
        return 128;
    }
}
