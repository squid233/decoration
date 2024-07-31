package io.github.squid233.decoration.client.render.block.entity;

import io.github.squid233.decoration.block.TrafficLightBlock;
import io.github.squid233.decoration.block.entity.TrafficLightBlockEntity;
import io.github.squid233.decoration.block.entity.TrafficLightStep;
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
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

/**
 * @author squid233
 * @since 0.2.0
 */
@Environment(EnvType.CLIENT)
public class TrafficLightBlockEntityRenderer implements BlockEntityRenderer<TrafficLightBlockEntity> {
    private static final float epsilon = 1.0e-4f;
    private static final Quaternionf QUAT = new Quaternionf();

    public TrafficLightBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(TrafficLightBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final World world = entity.getWorld();
        if (world == null) {
            return;
        }

        final BlockPos pos = entity.getPos();
        final BlockState blockState = world.getBlockState(pos);
        if (!(blockState.getBlock() instanceof TrafficLightBlock)) {
            return;
        }

        final Direction direction = blockState.get(TrafficLightBlock.FACING);

        final var steps = entity.steps();
        long totalTicks = 0;
        for (TrafficLightStep step : steps) {
            totalTicks += step.ticks();
        }

        if (totalTicks > 0) {
            final long time = world.getTime() % totalTicks;
            final float lightSize = entity.lightSize();

            matrices.push();
            matrices.translate(0.5f, 0.0f, 0.5f);
            matrices.multiply(QUAT.rotationY((float) Math.toRadians(-direction.asRotation())));
            matrices.translate(-lightSize * 0.5f / 16f, 0.0f, -(8f - entity.zOffset()) / 16 + epsilon);
            final MatrixStack.Entry peek = matrices.peek();

            final VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());

            final var step = TrafficLightBlockEntity.determineStep(time, steps, false);
            final var color = step.color();
            if (color.isColor()) {
                final Integer colorValue = color.getColorValue();
                if (colorValue != null) {
                    final float yOffset = entity.computeYOffset(step.index()) / 16f;
                    renderQuad(peek, buffer,
                        lightSize,
                        yOffset,
                        0xff000000 | colorValue);
                }
            }

            matrices.pop();
        }
    }

    private static void renderQuad(
        MatrixStack.Entry entry,
        VertexConsumer buffer,
        float size,
        float y0,
        int colorValue
    ) {
        final float scaled = size / 16f;
        final Matrix4f positionMatrix = entry.getPositionMatrix();
        buffer.vertex(positionMatrix, 0.0f, y0 + scaled, 0.0f).color(colorValue).next();
        buffer.vertex(positionMatrix, 0.0f, y0, 0.0f).color(colorValue).next();
        buffer.vertex(positionMatrix, scaled, y0, 0.0f).color(colorValue).next();
        buffer.vertex(positionMatrix, scaled, y0 + scaled, 0.0f).color(colorValue).next();
    }
}
