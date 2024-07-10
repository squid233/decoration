package io.github.squid233.decoration.block.entity;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModBlockEntityTypes {
    public static final BlockEntityType<CatenaryPartBlockEntity> CATENARY_PART_BLOCK_ENTITY_TYPE =
        BlockEntityType.Builder.create(CatenaryPartBlockEntity::new, ModBlocks.CATENARY_PART.toBlock()).build(null);
    public static final BlockEntityType<TrafficLight3BlockEntity> TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE =
        BlockEntityType.Builder.create(TrafficLight3BlockEntity::new, ModBlocks.TRAFFIC_LIGHT_3.toBlock()).build(null);

    private static void register(String name, BlockEntityType<?> type) {
        Registry.register(Registries.BLOCK_ENTITY_TYPE, Decoration.id(name), type);
    }

    public static void registerAll() {
        register("catenary_part_block_entity", CATENARY_PART_BLOCK_ENTITY_TYPE);
        register("traffic_light_3_block_entity", TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE);
    }
}
