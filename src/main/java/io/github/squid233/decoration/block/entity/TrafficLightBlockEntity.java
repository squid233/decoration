package io.github.squid233.decoration.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author squid233
 * @since 0.2.0
 */
public abstract class TrafficLightBlockEntity extends BlockEntity {
    private static final String STEPS = "steps";
    private final List<TrafficLightStep> steps = new ArrayList<>();

    public TrafficLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static class Light3 extends TrafficLightBlockEntity {
        public Light3(BlockPos pos, BlockState state) {
            super(ModBlockEntityTypes.TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE, pos, state);
        }
    }

    public void update(List<TrafficLightStep> steps) {
        this.steps.clear();
        this.steps.addAll(steps);
        markDirty();
        if (world != null) {
            world.updateListeners(getPos(), getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        final NbtList list = new NbtList();
        for (TrafficLightStep step : steps) {
            final NbtCompound compound = new NbtCompound();
            step.writeNbt(compound);
            list.add(compound);
        }
        nbt.put(STEPS, list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        steps.clear();
        final NbtList list = nbt.getList(STEPS, NbtElement.COMPOUND_TYPE);
        for (int i = 0, size = list.size(); i < size; i++) {
            steps.add(TrafficLightStep.readNbt(list.getCompound(i)));
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public List<TrafficLightStep> steps() {
        return steps;
    }
}
