package io.github.squid233.decoration.block;

import io.github.squid233.decoration.block.entity.TrafficLightBlockEntity;
import io.github.squid233.decoration.block.entity.TrafficLightStep;
import io.github.squid233.decoration.network.ModNetwork;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author squid233
 * @since 0.1.0
 */
public abstract class TrafficLightBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    protected TrafficLightBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    public static class Light2 extends TrafficLightBlock {
        private static final VoxelShape NORTH_SHAPE = createCuboidShape(3.0, 0.0, 7.0, 13.0, 16.0, 16.0);
        private static final VoxelShape EAST_SHAPE = createCuboidShape(0.0, 0.0, 3.0, 9.0, 16.0, 13.0);
        private static final VoxelShape SOUTH_SHAPE = createCuboidShape(3.0, 0.0, 0.0, 13.0, 16.0, 9.0);
        private static final VoxelShape WEST_SHAPE = createCuboidShape(7.0, 0.0, 3.0, 16.0, 16.0, 13.0);

        public Light2(Settings settings) {
            super(settings);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new TrafficLightBlockEntity.Light2(pos, state);
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
    }

    public static class Light3 extends TrafficLightBlock {
        private static final VoxelShape NORTH_SHAPE = createCuboidShape(5.0, 0.0, 8.0, 11.0, 16.0, 16.0);
        private static final VoxelShape EAST_SHAPE = createCuboidShape(0.0, 0.0, 5.0, 8.0, 16.0, 11.0);
        private static final VoxelShape SOUTH_SHAPE = createCuboidShape(5.0, 0.0, 0.0, 11.0, 16.0, 8.0);
        private static final VoxelShape WEST_SHAPE = createCuboidShape(8.0, 0.0, 5.0, 16.0, 16.0, 11.0);

        public Light3(Settings settings) {
            super(settings);
        }

        @Nullable
        @Override
        public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
            return new TrafficLightBlockEntity.Light3(pos, state);
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
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient &&
            player instanceof ServerPlayerEntity serverPlayerEntity &&
            world.getBlockEntity(pos) instanceof TrafficLightBlockEntity blockEntity) {
            final PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(pos);
            buf.writeCollection(blockEntity.steps(), TrafficLightStep::writeBuf);
            ServerPlayNetworking.send(serverPlayerEntity, ModNetwork.OPEN_TRAFFIC_LIGHT_SCREEN_PACKET, buf);
        }
        return ActionResult.SUCCESS;
    }
}
