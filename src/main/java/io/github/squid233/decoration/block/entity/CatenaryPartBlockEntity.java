package io.github.squid233.decoration.block.entity;

import io.github.squid233.decoration.item.WireType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
 * @since 0.1.0
 */
public class CatenaryPartBlockEntity extends BlockEntity {
    public static final String CONNECTIONS = "connections";
    public static final String TARGET = "target";
    public static final String WIRE_TYPE = "wire_type";
    private final List<CatenaryConnection> connections = new ArrayList<>();

    public CatenaryPartBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CATENARY_PART_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void connect(WireType wireType, CatenaryPartBlockEntity blockEntity) {
        final CatenaryConnection connection = new CatenaryConnection(blockEntity.pos, wireType);
        if (!connections.contains(connection)) {
            connections.add(connection);
            update();
        }
        final CatenaryConnection connection1 = new CatenaryConnection(pos, wireType);
        if (!blockEntity.connections.contains(connection1)) {
            blockEntity.connections.add(connection1);
            blockEntity.update();
        }
    }

    public void disconnect(WireType wireType, CatenaryPartBlockEntity blockEntity) {
        if (connections.remove(new CatenaryConnection(blockEntity.pos, wireType))) {
            update();
        }
        if (blockEntity.connections.remove(new CatenaryConnection(pos, wireType))) {
            blockEntity.update();
        }
    }

    public void disconnectAll() {
        if (world != null) {
            for (CatenaryConnection connection : connections) {
                if (world.getBlockEntity(connection.target()) instanceof CatenaryPartBlockEntity blockEntity) {
                    for (WireType wireType : WireType.LIST) {
                        if (blockEntity.connections.remove(new CatenaryConnection(pos, wireType))) {
                            blockEntity.update();
                        }
                    }
                }
            }
        }
        connections.clear();
    }

    private void update() {
        if (world != null) {
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        final NbtList list = new NbtList();
        for (CatenaryConnection connection : connections) {
            final NbtCompound compound = new NbtCompound();

            final NbtCompound targetCompound = new NbtCompound();
            final BlockPos target = connection.target();
            targetCompound.putInt("x", target.getX());
            targetCompound.putInt("y", target.getY());
            targetCompound.putInt("z", target.getZ());

            compound.put(TARGET, targetCompound);
            compound.putString(WIRE_TYPE, connection.wireType().asString());

            list.add(compound);
        }
        nbt.put(CONNECTIONS, list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        connections.clear();
        final NbtList list = nbt.getList(CONNECTIONS, NbtElement.COMPOUND_TYPE);
        for (int i = 0, size = list.size(); i < size; i++) {
            final NbtCompound compound = list.getCompound(i);

            final NbtCompound target = compound.getCompound(TARGET);
            final String wireType = compound.getString(WIRE_TYPE);

            connections.add(new CatenaryConnection(posFromNbt(target), WireType.fromString(wireType)));
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbtWithIdentifyingData();
    }

    public List<CatenaryConnection> connections() {
        return connections;
    }
}
