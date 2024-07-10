package io.github.squid233.decoration.block.entity;

import io.github.squid233.decoration.item.WireType;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.1.0
 */
public record CatenaryConnection(BlockPos target, WireType wireType) {
}
