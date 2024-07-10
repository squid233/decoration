package io.github.squid233.decoration;

import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.ModConcreteSlabs;
import io.github.squid233.decoration.block.ModVerticalSlabs;
import io.github.squid233.decoration.block.entity.ModBlockEntityTypes;
import io.github.squid233.decoration.item.ModItemGroups;
import io.github.squid233.decoration.item.ModItems;
import net.fabricmc.api.ModInitializer;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class Decoration implements ModInitializer {
    public static final String MOD_ID = "decoration";

    @Override
    public void onInitialize() {
        ModConcreteSlabs.registerAll();
        ModVerticalSlabs.registerAll();
        ModBlocks.registerAll();
        ModItems.registerAll();
        ModItemGroups.registerAll();
        ModBlockEntityTypes.registerAll();
    }
}
