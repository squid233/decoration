package io.github.squid233.decoration;

import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.ModConcreteSlabs;
import io.github.squid233.decoration.block.ModVerticalSlabs;
import io.github.squid233.decoration.block.entity.ModBlockEntityTypes;
import io.github.squid233.decoration.item.ModItemGroups;
import io.github.squid233.decoration.item.ModItems;
import io.github.squid233.decoration.network.ModNetwork;
import io.github.squid233.decoration.world.ModGameRules;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class Decoration implements ModInitializer {
    public static final String MOD_ID = "decoration";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        ModConcreteSlabs.registerAll();
        ModVerticalSlabs.registerAll();
        ModBlocks.registerAll();
        ModItems.registerAll();
        ModItemGroups.registerAll();
        ModBlockEntityTypes.registerAll();
        ModGameRules.registerAll();

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ModNetwork.sendTrafficLight(handler.player, ModGameRules.TRAFFIC_LIGHT_RED_TICKS, ModNetwork.TRAFFIC_LIGHT_RED_TICKS_PACKET);
            ModNetwork.sendTrafficLight(handler.player, ModGameRules.TRAFFIC_LIGHT_YELLOW_TICKS, ModNetwork.TRAFFIC_LIGHT_YELLOW_TICKS_PACKET);
            ModNetwork.sendTrafficLight(handler.player, ModGameRules.TRAFFIC_LIGHT_GREEN_TICKS, ModNetwork.TRAFFIC_LIGHT_GREEN_TICKS_PACKET);
        });
        ServerPlayNetworking.registerGlobalReceiver(ModNetwork.TRAFFIC_LIGHT_RED_TICKS_PACKET, (server, player, handler, buf, responseSender) ->
            ModNetwork.sendTrafficLight(player, ModGameRules.TRAFFIC_LIGHT_RED_TICKS, ModNetwork.TRAFFIC_LIGHT_RED_TICKS_PACKET));
        ServerPlayNetworking.registerGlobalReceiver(ModNetwork.TRAFFIC_LIGHT_YELLOW_TICKS_PACKET, (server, player, handler, buf, responseSender) ->
            ModNetwork.sendTrafficLight(player, ModGameRules.TRAFFIC_LIGHT_YELLOW_TICKS, ModNetwork.TRAFFIC_LIGHT_YELLOW_TICKS_PACKET));
        ServerPlayNetworking.registerGlobalReceiver(ModNetwork.TRAFFIC_LIGHT_GREEN_TICKS_PACKET, (server, player, handler, buf, responseSender) ->
            ModNetwork.sendTrafficLight(player, ModGameRules.TRAFFIC_LIGHT_GREEN_TICKS, ModNetwork.TRAFFIC_LIGHT_GREEN_TICKS_PACKET));
    }
}
