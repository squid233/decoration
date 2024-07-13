package io.github.squid233.decoration.client.render.block.entity;

import io.github.squid233.decoration.block.ModBlocks;
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

import java.util.List;

/**
 * @author squid233
 * @since 0.1.0
 */
@Environment(EnvType.CLIENT)
public class TrafficLight3BlockEntityRenderer implements BlockEntityRenderer<TrafficLightBlockEntity.Light3> {
    private static final float epsilon = 1.0e-4f;
    private static final Quaternionf QUAT = new Quaternionf();

    public TrafficLight3BlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public void render(TrafficLightBlockEntity.Light3 entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        final World world = entity.getWorld();
        if (world == null) {
            return;
        }

        final BlockPos pos = entity.getPos();
        final BlockState blockState = world.getBlockState(pos);
        if (!blockState.isOf(ModBlocks.TRAFFIC_LIGHT_3.toBlock())) {
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

            matrices.push();
            matrices.translate(0.5f, 0.0f, 0.5f);
            matrices.multiply(QUAT.rotationY((float) Math.toRadians(-direction.asRotation())));
            matrices.translate(-0.125f, 0.0f, -0.125f + epsilon);
            final MatrixStack.Entry peek = matrices.peek();

            final VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());

            final var step = determineStep(time, steps);
            final var color = step.color();
            if (color.isColor()) {
                final Integer colorValue = color.getColorValue();
                if (colorValue != null) {
                    final float yOffset = computeYOffset(step.index());
                    renderQuad(peek, buffer,
                        yOffset,
                        yOffset + (4f / 16),
                        0xff000000 | colorValue);
                }
            }

            matrices.pop();
        }
    }

    private static float computeYOffset(int index) {
        return (1 + index * 5) / 16f;
    }

    private static TrafficLightStep determineStep(long time, List<TrafficLightStep> steps) {
        long ticks = 0;
        for (TrafficLightStep step : steps) {
            final int ticks1 = step.ticks();
            if (ticks1 <= 0) {
                continue;
            }
            ticks += ticks1;
            if (time < ticks) {
                final int flashing = step.flashing();
                if (flashing == 0 ||
                    time % ((double) ticks1 / flashing) < ticks1 * 0.5 / flashing) {
                    return step;
                }
                return TrafficLightStep.EMPTY;
            }
        }
        return TrafficLightStep.EMPTY;
    }

    private static void renderQuad(
        MatrixStack.Entry entry,
        VertexConsumer buffer,
        float y0,
        float y1,
        int colorValue
    ) {
        final Matrix4f positionMatrix = entry.getPositionMatrix();
        buffer.vertex(positionMatrix, 0.0f, y1, 0.0f).color(colorValue).next();
        buffer.vertex(positionMatrix, 0.0f, y0, 0.0f).color(colorValue).next();
        buffer.vertex(positionMatrix, 0.25f, y0, 0.0f).color(colorValue).next();
        buffer.vertex(positionMatrix, 0.25f, y1, 0.0f).color(colorValue).next();
    }
}
