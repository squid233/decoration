package io.github.squid233.decoration;

import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.ModConcreteSlabs;
import io.github.squid233.decoration.block.ModVerticalSlabs;
import io.github.squid233.decoration.block.entity.ModBlockEntityTypes;
import io.github.squid233.decoration.compat.create.CreateIntegration;
import io.github.squid233.decoration.item.ModItemGroups;
import io.github.squid233.decoration.item.ModItems;
import io.github.squid233.decoration.network.ModNetwork;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
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
        ModNetwork.registerAll();

        if (FabricLoader.getInstance().isModLoaded("create")) {
            CreateIntegration.init();
        }
    }
}
