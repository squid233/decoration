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
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

/**
 * @author squid233
 * @since 0.1.0
 */
public class PlatformBlock extends HorizontalFacingBlock {
    private static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
        VoxelShapes.fullCube(),
        createCuboidShape(0.0, 12.0, -4.0, 16.0, 16.0, 0.0)
    );
    private static final VoxelShape EAST_SHAPE = VoxelShapes.union(
        VoxelShapes.fullCube(),
        createCuboidShape(16.0, 12.0, 0.0, 20.0, 16.0, 16.0)
    );
    private static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
        VoxelShapes.fullCube(),
        createCuboidShape(0.0, 12.0, 16.0, 16.0, 16.0, 20.0)
    );
    private static final VoxelShape WEST_SHAPE = VoxelShapes.union(
        VoxelShapes.fullCube(),
        createCuboidShape(-4.0, 12.0, 0.0, 0.0, 16.0, 16.0)
    );

    public PlatformBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case DOWN, UP -> VoxelShapes.fullCube();
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
        };
    }
}
