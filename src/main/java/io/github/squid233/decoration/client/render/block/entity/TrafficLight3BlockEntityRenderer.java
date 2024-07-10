package io.github.squid233.decoration.client.render.block.entity;

import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.TrafficLightBlock;
import io.github.squid233.decoration.block.entity.TrafficLight3BlockEntity;
import io.github.squid233.decoration.client.DecorationClient;
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
 * @since 0.1.0
 */
@Environment(EnvType.CLIENT)
public class TrafficLight3BlockEntityRenderer implements BlockEntityRenderer<TrafficLight3BlockEntity> {
    private static final float epsilon = 1.0e-4f;
    private static final Quaternionf QUAT = new Quaternionf();

    public TrafficLight3BlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    private enum Color {
        NONE(0, 0, 0, 0),
        RED(255, 0, 0, 11f / 16),
        YELLOW(255, 216, 0, 6f / 16),
        GREEN(0, 255, 33, 1f / 16);

        private final int red;
        private final int green;
        private final int blue;
        private final float yOffset;

        Color(int red, int green, int blue, float yOffset) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.yOffset = yOffset;
        }
    }

    @Override
    public void render(TrafficLight3BlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
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

        final long time = world.getTime() % (DecorationClient.trafficLightRedTicks + DecorationClient.trafficLightYellowTicks * 2L + DecorationClient.trafficLightGreenTicks);

        matrices.push();
        matrices.translate(0.5f, 0.0f, 0.5f);
        matrices.multiply(QUAT.rotationY((float) Math.toRadians(-direction.asRotation())));
        matrices.translate(-0.125f, 0.0f, -0.125f + epsilon);
        final MatrixStack.Entry peek = matrices.peek();

        final VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());

        final Color color = determineColor(time, direction);
        if (color != Color.NONE) {
            renderQuad(peek, buffer,
                color.yOffset,
                color.yOffset + (4f / 16),
                color.red, color.green, color.blue);
        }

        matrices.pop();
    }

    private static Color determineColor(long time, Direction direction) {
        final int redTicks = DecorationClient.trafficLightRedTicks;
        final int yellowTicks = DecorationClient.trafficLightYellowTicks;
        final int greenTicks = DecorationClient.trafficLightGreenTicks;
        if (time < redTicks) {
            if (direction.getAxis() == Direction.Axis.X) {
                return Color.RED;
            }
            return Color.GREEN;
        }
        if (time < redTicks + yellowTicks) {
            if (time % 20 < 10) {
                return Color.YELLOW;
            }
            return Color.NONE;
        }
        if (time < redTicks + yellowTicks + greenTicks) {
            if (direction.getAxis() == Direction.Axis.X) {
                return Color.GREEN;
            }
            return Color.RED;
        }
        if (time % 20 < 10) {
            return Color.YELLOW;
        }
        return Color.NONE;
    }

    private static void renderQuad(
        MatrixStack.Entry entry,
        VertexConsumer buffer,
        float y0,
        float y1,
        int red, int green, int blue
    ) {
        final Matrix4f positionMatrix = entry.getPositionMatrix();
        buffer.vertex(positionMatrix, 0.0f, y1, 0.0f).color(red, green, blue, 255).next();
        buffer.vertex(positionMatrix, 0.0f, y0, 0.0f).color(red, green, blue, 255).next();
        buffer.vertex(positionMatrix, 0.25f, y0, 0.0f).color(red, green, blue, 255).next();
        buffer.vertex(positionMatrix, 0.25f, y1, 0.0f).color(red, green, blue, 255).next();
    }
}
