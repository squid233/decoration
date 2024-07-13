package io.github.squid233.decoration.client;

import io.github.squid233.decoration.block.entity.ModBlockEntityTypes;
import io.github.squid233.decoration.block.entity.TrafficLightStep;
import io.github.squid233.decoration.client.gui.screen.TrafficLightScreen;
import io.github.squid233.decoration.client.render.block.entity.CatenaryPartBlockEntityRenderer;
import io.github.squid233.decoration.client.render.block.entity.TrafficLightBlockEntityRenderer;
import io.github.squid233.decoration.network.ModNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * @author squid233
 * @since 0.1.0
 */
@Environment(EnvType.CLIENT)
public final class DecorationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.CATENARY_PART_BLOCK_ENTITY_TYPE, CatenaryPartBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE, TrafficLightBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.TRAFFIC_LIGHT_2_BLOCK_ENTITY_TYPE, TrafficLightBlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(ModNetwork.OPEN_TRAFFIC_LIGHT_SCREEN_PACKET, (client, handler, buf, responseSender) -> {
            final BlockPos pos = buf.readBlockPos();
            final var list = buf.readCollection(ArrayList::new, TrafficLightStep::readBuf);
            client.execute(() -> client.setScreen(new TrafficLightScreen(list, pos)));
        });
    }
}
