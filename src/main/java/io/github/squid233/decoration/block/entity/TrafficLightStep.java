package io.github.squid233.decoration.block.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Formatting;

/**
 * @author squid233
 * @since 0.2.0
 */
public record TrafficLightStep(Formatting color, int ticks, int index, int flashing) {
    public static final TrafficLightStep EMPTY = new TrafficLightStep(Formatting.RESET, 0, 0, 0);
    public static final String COLOR = "color";
    public static final String TICKS = "ticks";
    public static final String INDEX = "index";
    public static final String FLASHING = "flashing";

    public void writeNbt(NbtCompound compound) {
        compound.putString(COLOR, color.asString());
        compound.putInt(TICKS, ticks);
        compound.putInt(INDEX, index);
        compound.putInt(FLASHING, flashing);
    }

    public static TrafficLightStep readNbt(NbtCompound compound) {
        return new TrafficLightStep(
            Formatting.byName(compound.getString(COLOR)),
            compound.getInt(TICKS),
            compound.getInt(INDEX),
            compound.getInt(FLASHING)
        );
    }

    public static void writeBuf(PacketByteBuf buf, TrafficLightStep step) {
        buf.writeString(step.color.asString());
        buf.writeInt(step.ticks);
        buf.writeInt(step.index);
        buf.writeInt(step.flashing);
    }

    public static TrafficLightStep readBuf(PacketByteBuf buf) {
        return new TrafficLightStep(
            Formatting.byName(buf.readString()),
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        );
    }
}
