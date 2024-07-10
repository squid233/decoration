package io.github.squid233.decoration.client;

import io.github.squid233.decoration.block.entity.ModBlockEntityTypes;
import io.github.squid233.decoration.client.render.block.entity.CatenaryPartBlockEntityRenderer;
import io.github.squid233.decoration.client.render.block.entity.TrafficLight3BlockEntityRenderer;
import io.github.squid233.decoration.network.ModNetwork;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

/**
 * @author squid233
 * @since 0.1.0
 */
@Environment(EnvType.CLIENT)
public final class DecorationClient implements ClientModInitializer {
    public static int trafficLightRedTicks = 20 * 27;
    public static int trafficLightYellowTicks = 20 * 3;
    public static int trafficLightGreenTicks = 20 * 27;

    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.CATENARY_PART_BLOCK_ENTITY_TYPE, CatenaryPartBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE, TrafficLight3BlockEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(ModNetwork.TRAFFIC_LIGHT_RED_TICKS_PACKET, (client, handler, buf, responseSender) ->
            trafficLightRedTicks = buf.readInt());
        ClientPlayNetworking.registerGlobalReceiver(ModNetwork.TRAFFIC_LIGHT_YELLOW_TICKS_PACKET, (client, handler, buf, responseSender) ->
            trafficLightYellowTicks = buf.readInt());
        ClientPlayNetworking.registerGlobalReceiver(ModNetwork.TRAFFIC_LIGHT_GREEN_TICKS_PACKET, (client, handler, buf, responseSender) ->
            trafficLightGreenTicks = buf.readInt());
    }
}
