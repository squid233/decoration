package io.github.squid233.decoration.world;

import io.github.squid233.decoration.network.ModNetwork;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModGameRules {
    public static final GameRules.Key<GameRules.IntRule> TRAFFIC_LIGHT_RED_TICKS =
        GameRuleRegistry.register("trafficLightRedTicks", GameRules.Category.MISC, GameRuleFactory.createIntRule(20 * 27,
            1,
            ModNetwork::sendTrafficLightRed));
    public static final GameRules.Key<GameRules.IntRule> TRAFFIC_LIGHT_YELLOW_TICKS =
        GameRuleRegistry.register("trafficLightYellowTicks", GameRules.Category.MISC, GameRuleFactory.createIntRule(20 * 3,
            1,
            ModNetwork::sendTrafficLightYellow));
    public static final GameRules.Key<GameRules.IntRule> TRAFFIC_LIGHT_GREEN_TICKS =
        GameRuleRegistry.register("trafficLightGreenTicks", GameRules.Category.MISC, GameRuleFactory.createIntRule(20 * 27,
            1,
            ModNetwork::sendTrafficLightGreen));

    public static void registerAll() {
    }
}
