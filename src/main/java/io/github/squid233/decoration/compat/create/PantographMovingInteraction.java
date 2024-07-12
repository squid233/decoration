package io.github.squid233.decoration.compat.create;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.behaviour.SimpleBlockMovingInteraction;
import io.github.squid233.decoration.block.PantographBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class PantographMovingInteraction extends SimpleBlockMovingInteraction {
    @Override
    protected BlockState handle(PlayerEntity player, Contraption contraption, BlockPos pos, BlockState currentState) {
        return currentState.cycle(PantographBlock.OPEN);
    }
}
