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
    private final List<List<TrafficLightStep>> steps = new ArrayList<>();

    public TrafficLightBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static class Light2 extends TrafficLightBlockEntity {
        public Light2(BlockPos pos, BlockState state) {
            super(ModBlockEntityTypes.TRAFFIC_LIGHT_2_BLOCK_ENTITY_TYPE, pos, state);
        }

        @Override
        public float computeYOffset(int index) {
            return 6 + index * (lightSize() + 1);
        }

        @Override
        public int maxIndex() {
            return 1;
        }
    }

    public static class Light3 extends TrafficLightBlockEntity {
        public Light3(BlockPos pos, BlockState state) {
            super(ModBlockEntityTypes.TRAFFIC_LIGHT_3_BLOCK_ENTITY_TYPE, pos, state);
        }

        @Override
        public float computeYOffset(int index) {
            return 1 + index * (lightSize() + 1);
        }

        @Override
        public int maxIndex() {
            return 2;
        }
    }

    public static class Light4 extends TrafficLightBlockEntity {
        public Light4(BlockPos pos, BlockState state) {
            super(ModBlockEntityTypes.TRAFFIC_LIGHT_4_BLOCK_ENTITY_TYPE, pos, state);
        }

        @Override
        public float computeYOffset(int index) {
            return 1 + index * (lightSize() + 1);
        }

        @Override
        public int maxIndex() {
            return 3;
        }
    }

    public static TrafficLightStep determineStep(long time, List<TrafficLightStep> steps, boolean ignoreFlashing) {
        long ticks = 0;
        for (TrafficLightStep step : steps) {
            final int ticks1 = step.ticks();
            if (ticks1 <= 0) {
                continue;
            }
            ticks += ticks1;
            if (time < ticks) {
                if (ignoreFlashing) {
                    return step;
                }
                final int flashing = step.flashing();
                if (flashing == 0 ||
                    time % ((double) ticks1 / flashing) < ticks1 * 0.5 / flashing) {
                    return step;
                }
                return TrafficLightStep.EMPTY;
            }
        }
        return TrafficLightStep.EMPTY;
    }

    public void update(List<List<TrafficLightStep>> steps) {
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
        for (var stepList : steps) {
            final NbtList list1 = new NbtList();
            for (TrafficLightStep step : stepList) {
                final NbtCompound compound = new NbtCompound();
                step.writeNbt(compound);
                list1.add(compound);
            }
            list.add(list1);
        }
        nbt.put(STEPS, list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        steps.clear();
        final NbtList list = nbt.getList(STEPS, NbtElement.LIST_TYPE);
        for (int i = 0, size = list.size(); i < size; i++) {
            final List<TrafficLightStep> steps1 = new ArrayList<>(size);
            final NbtList list1 = list.getList(i);
            for (int j = 0, size1 = list1.size(); j < size1; j++) {
                steps1.add(TrafficLightStep.readNbt(list1.getCompound(j)));
            }
            steps.add(steps1);
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

    public List<List<TrafficLightStep>> steps() {
        return steps;
    }

    public float lightSize() {
        return 4f;
    }

    public float zOffset() {
        return 6f;
    }

    public abstract float computeYOffset(int index);

    public abstract int maxIndex();
}
