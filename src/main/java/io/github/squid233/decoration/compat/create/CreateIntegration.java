package io.github.squid233.decoration.compat.create;

import com.simibubi.create.AllInteractionBehaviours;
import io.github.squid233.decoration.block.ModBlocks;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class CreateIntegration {
    public static void init() {
        AllInteractionBehaviours.registerBehaviour(ModBlocks.PANTOGRAPH.toBlock(), new PantographMovingInteraction());
    }
}
