package io.github.squid233.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

/**
 * @author squid233
 * @since 0.1.0
 */
public class CatenaryPoleBlock extends HorizontalFacingBlock {
    private static final VoxelShape NORTH_SHAPE = createCuboidShape(6.0, 0.0, 0.0, 10.0, 16.0, 10.0);
    private static final VoxelShape EAST_SHAPE = createCuboidShape(6.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    private static final VoxelShape SOUTH_SHAPE = createCuboidShape(6.0, 0.0, 6.0, 10.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = createCuboidShape(0.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public CatenaryPoleBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case DOWN, UP, NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }
}
