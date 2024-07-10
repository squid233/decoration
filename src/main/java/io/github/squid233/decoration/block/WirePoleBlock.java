package io.github.squid233.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

/**
 * @author squid233
 * @since 0.1.0
 */
public class WirePoleBlock extends Block {
    public static final VoxelShape SHAPE = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public WirePoleBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }
}
