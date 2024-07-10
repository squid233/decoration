package io.github.squid233.decoration.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author squid233
 * @since 0.1.0
 */
public class WireItem extends Item {
    public static final String TOOLTIP = "item.decoration.wire_item.tooltip";
    public static final String WIRE_CONNECTED = "item.decoration.wire_item.connected";
    public static final String WIRE_DISCONNECTED = "item.decoration.wire_item.disconnected";
    public static final String CONNECTION = "connection";
    private final WireType wireType;
    private final boolean remover;

    public WireItem(Settings settings, WireType wireType, boolean remover) {
        super(settings);
        this.wireType = wireType;
        this.remover = remover;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (user.isSneaking()) {
            final ItemStack stackInHand = user.getStackInHand(hand);
            stackInHand.removeSubNbt(CONNECTION);
            return TypedActionResult.success(stackInHand);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        final NbtCompound subNbt = stack.getSubNbt(CONNECTION);
        if (subNbt != null) {
            tooltip.add(Text.translatable(TOOLTIP,
                subNbt.getInt("x"),
                subNbt.getInt("y"),
                subNbt.getInt("z")).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        }
    }

    public WireType wireType() {
        return wireType;
    }

    public boolean remover() {
        return remover;
    }
}
