package io.github.squid233.decoration.block.entity;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModBlockEntityTypes {
    public static final BlockEntityType<CatenaryPartBlockEntity> CATENARY_PART_BLOCK_ENTITY_TYPE =
        BlockEntityType.Builder.create(CatenaryPartBlockEntity::new, ModBlocks.CATENARY_PART.toBlock()).build(null);

    private static void register(String name, BlockEntityType<?> type) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Decoration.MOD_ID, name), type);
    }

    public static void registerAll() {
        register("catenary_part_block_entity", CATENARY_PART_BLOCK_ENTITY_TYPE);
    }
}
