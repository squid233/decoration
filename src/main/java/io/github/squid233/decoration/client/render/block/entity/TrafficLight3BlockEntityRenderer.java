package io.github.squid233.decoration.client.render.block.entity;

import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.TrafficLightBlock;
import io.github.squid233.decoration.block.entity.TrafficLight3BlockEntity;
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
    private static final long RED_LIGHT_TICKS = 20 * 3;
    private static final long YELLOW_LIGHT_TICKS = 20 * 3;
    private static final long GREEN_LIGHT_TICKS = 20 * 3;
    private static final float epsilon = 1.0e-4f;
    private static final Quaternionf QUAT = new Quaternionf();
    private static final Color RED = new Color(255, 0, 0, 11f / 16);
    private static final Color YELLOW = new Color(255, 216, 0, 6f / 16);
    private static final Color GREEN = new Color(0, 255, 33, 1f / 16);

    public TrafficLight3BlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    private record Color(int red, int green, int blue, float yOffset) {
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

        final long time = world.getTime() % (RED_LIGHT_TICKS + YELLOW_LIGHT_TICKS * 2 + GREEN_LIGHT_TICKS);

        matrices.push();
        matrices.translate(0.5f, 0.0f, 0.5f);
        matrices.multiply(QUAT.rotationY((float) Math.toRadians(-direction.asRotation())));
        matrices.translate(-0.125f, 0.0f, -0.125f + epsilon);
        final MatrixStack.Entry peek = matrices.peek();

        final VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getDebugQuads());

        final Color color = determineColor(time, direction);
        if (color != null) {
            renderQuad(peek, buffer,
                color.yOffset(),
                color.yOffset() + (4f / 16),
                color.red(), color.green(), color.blue());
        }

        matrices.pop();
    }

    private static Color determineColor(long time, Direction direction) {
        if (time < RED_LIGHT_TICKS) {
            if (direction.getAxis() == Direction.Axis.X) {
                return RED;
            }
            return GREEN;
        }
        if (time < RED_LIGHT_TICKS + YELLOW_LIGHT_TICKS) {
            if (time % 20 < 10) {
                return YELLOW;
            }
            return null;
        }
        if (time < RED_LIGHT_TICKS + YELLOW_LIGHT_TICKS + GREEN_LIGHT_TICKS) {
            if (direction.getAxis() == Direction.Axis.X) {
                return GREEN;
            }
            return RED;
        }
        if (time % 20 < 10) {
            return YELLOW;
        }
        return null;
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
