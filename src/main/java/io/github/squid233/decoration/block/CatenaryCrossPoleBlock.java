package io.github.squid233.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

/**
 * @author squid233
 * @since 0.1.0
 */
public class CatenaryCrossPoleBlock extends Block {
    private static final VoxelShape SHAPE = VoxelShapes.union(
        createCuboidShape(0.0, 0.0, 6.0, 16.0, 16.0, 10.0),
        createCuboidShape(6.0, 0.0, 0.0, 10.0, 16.0, 16.0)
    );

    public CatenaryCrossPoleBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
