package io.github.squid233.decoration.network;

import io.github.squid233.decoration.Decoration;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.List;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModNetwork {
    public static final Identifier TRAFFIC_LIGHT_RED_TICKS_PACKET = Decoration.id("traffic_light_red_ticks");
    public static final Identifier TRAFFIC_LIGHT_YELLOW_TICKS_PACKET = Decoration.id("traffic_light_yellow_ticks");
    public static final Identifier TRAFFIC_LIGHT_GREEN_TICKS_PACKET = Decoration.id("traffic_light_green_ticks");

    public static void sendTrafficLight(ServerPlayerEntity player, GameRules.Key<GameRules.IntRule> key, Identifier identifier) {
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(player.getWorld().getGameRules().getInt(key));
        ServerPlayNetworking.send(player, identifier, buf);
    }

    public static void sendTrafficLight(MinecraftServer server, GameRules.IntRule intRule, Identifier identifier) {
        final PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(intRule.get());
        final List<ServerPlayerEntity> list = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : list) {
            ServerPlayNetworking.send(player, identifier, buf);
        }
    }

    public static void sendTrafficLightRed(MinecraftServer server, GameRules.IntRule intRule) {
        sendTrafficLight(server, intRule, TRAFFIC_LIGHT_RED_TICKS_PACKET);
    }

    public static void sendTrafficLightYellow(MinecraftServer server, GameRules.IntRule intRule) {
        sendTrafficLight(server, intRule, TRAFFIC_LIGHT_YELLOW_TICKS_PACKET);
    }

    public static void sendTrafficLightGreen(MinecraftServer server, GameRules.IntRule intRule) {
        sendTrafficLight(server, intRule, TRAFFIC_LIGHT_GREEN_TICKS_PACKET);
    }
}
