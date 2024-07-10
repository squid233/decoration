package io.github.squid233.decoration.block;

import io.github.squid233.decoration.block.entity.TrafficLight3BlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
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
public abstract class TrafficLightBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    private static final VoxelShape NORTH_SHAPE = createCuboidShape(5.0, 0.0, 8.0, 11.0, 16.0, 16.0);
    private static final VoxelShape EAST_SHAPE = createCuboidShape(0.0, 0.0, 5.0, 8.0, 16.0, 11.0);
    private static final VoxelShape SOUTH_SHAPE = createCuboidShape(5.0, 0.0, 0.0, 11.0, 16.0, 8.0);
    private static final VoxelShape WEST_SHAPE = createCuboidShape(8.0, 0.0, 5.0, 16.0, 16.0, 11.0);

    protected TrafficLightBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    public static class Light3 extends TrafficLightBlock {
        public Light3(Settings settings) {
            super(settings);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new TrafficLight3BlockEntity(pos, state);
        }
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

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }
}
