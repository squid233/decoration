package io.github.squid233.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

/**
 * @author squid233
 * @since 0.1.0
 */
public class CatenaryCrossPoleBlock extends Block {
    public CatenaryCrossPoleBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }
}
