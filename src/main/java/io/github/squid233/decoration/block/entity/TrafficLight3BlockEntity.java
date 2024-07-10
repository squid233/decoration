package io.github.squid233.decoration.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.1.0
 */
public class TrafficLight3BlockEntity extends BlockEntity {
    public TrafficLight3BlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE, pos, state);
    }
}
