package io.github.squid233.decoration.network;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.entity.TrafficLightBlockEntity;
import io.github.squid233.decoration.block.entity.TrafficLightStep;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModNetwork {
    public static final Identifier OPEN_TRAFFIC_LIGHT_SCREEN_PACKET = Decoration.id("open_traffic_light_screen");
    public static final Identifier TRAFFIC_LIGHT_SAVE_PACKET = Decoration.id("traffic_light_save_packet");

    public static void registerAll() {
        ServerPlayNetworking.registerGlobalReceiver(TRAFFIC_LIGHT_SAVE_PACKET, (server, player, handler, buf, responseSender) -> {
            final BlockPos pos = buf.readBlockPos();
            final var list = buf.readCollection(ArrayList::new, TrafficLightStep::readList);
            server.execute(() -> {
                if (player.getWorld().getBlockEntity(pos) instanceof TrafficLightBlockEntity blockEntity) {
                    blockEntity.update(list);
                }
            });
        });
    }
}
