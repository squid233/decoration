package io.github.squid233.decoration.block;

import io.github.squid233.decoration.block.entity.CatenaryPartBlockEntity;
import io.github.squid233.decoration.item.WireItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author squid233
 * @since 0.1.0
 */
public class CatenaryPartBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public CatenaryPartBlock(Settings settings) {
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
            case DOWN, UP, NORTH, SOUTH -> CatenaryPoleExtraBlock.Z_SHAPE;
            case WEST, EAST -> CatenaryPoleExtraBlock.X_SHAPE;
        };
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CatenaryPartBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            final ItemStack stackInHand = player.getStackInHand(hand);
            if (stackInHand.getItem() instanceof WireItem wireItem &&
                world.getBlockEntity(pos) instanceof CatenaryPartBlockEntity blockEntity) {
                final NbtCompound nbt = stackInHand.getOrCreateNbt();
                if (nbt.contains(WireItem.CONNECTION)) {
                    final BlockPos from = BlockEntity.posFromNbt(nbt.getCompound(WireItem.CONNECTION));
                    // check whether self-connecting
                    if (from.equals(pos)) {
                        return ActionResult.PASS;
                    }
                    // check start
                    if (world.getBlockEntity(from) instanceof CatenaryPartBlockEntity fromBlockEntity) {
                        if (wireItem.remover()) {
                            // end removing
                            fromBlockEntity.disconnect(wireItem.wireType(), blockEntity);
                            player.sendMessage(Text.translatable(WireItem.WIRE_DISCONNECTED), true);
                        } else {
                            // end connecting
                            fromBlockEntity.connect(wireItem.wireType(), blockEntity);
                            player.sendMessage(Text.translatable(WireItem.WIRE_CONNECTED), true);
                        }
                    }
                    nbt.remove(WireItem.CONNECTION);
                } else {
                    // start connecting/removing
                    final NbtCompound compound = new NbtCompound();
                    compound.putInt("x", pos.getX());
                    compound.putInt("y", pos.getY());
                    compound.putInt("z", pos.getZ());
                    nbt.put(WireItem.CONNECTION, compound);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof CatenaryPartBlockEntity blockEntity) {
                blockEntity.disconnectAll();
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
