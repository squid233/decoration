package io.github.squid233.decoration.client;

import io.github.squid233.decoration.block.entity.ModBlockEntityTypes;
import io.github.squid233.decoration.client.render.block.entity.CatenaryPartBlockEntityRenderer;
import io.github.squid233.decoration.client.render.block.entity.TrafficLight3BlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

/**
 * @author squid233
 * @since 0.1.0
 */
@Environment(EnvType.CLIENT)
public final class DecorationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(ModBlockEntityTypes.CATENARY_PART_BLOCK_ENTITY_TYPE, CatenaryPartBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityTypes.TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE, TrafficLight3BlockEntityRenderer::new);
    }
}
