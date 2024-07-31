package io.github.squid233.decoration.item;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.ModBlockConvertible;
import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.ModConcreteSlabs;
import io.github.squid233.decoration.block.ModVerticalSlabs;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Locale;

/**
 * @author squid233
 * @since 0.1.0
 */
public enum ModItems implements ModItemConvertible {
    WHITE_CONCRETE_SLAB(ModConcreteSlabs.WHITE_CONCRETE_SLAB),
    ORANGE_CONCRETE_SLAB(ModConcreteSlabs.ORANGE_CONCRETE_SLAB),
    MAGENTA_CONCRETE_SLAB(ModConcreteSlabs.MAGENTA_CONCRETE_SLAB),
    LIGHT_BLUE_CONCRETE_SLAB(ModConcreteSlabs.LIGHT_BLUE_CONCRETE_SLAB),
    YELLOW_CONCRETE_SLAB(ModConcreteSlabs.YELLOW_CONCRETE_SLAB),
    LIME_CONCRETE_SLAB(ModConcreteSlabs.LIME_CONCRETE_SLAB),
    PINK_CONCRETE_SLAB(ModConcreteSlabs.PINK_CONCRETE_SLAB),
    GRAY_CONCRETE_SLAB(ModConcreteSlabs.GRAY_CONCRETE_SLAB),
    LIGHT_GRAY_CONCRETE_SLAB(ModConcreteSlabs.LIGHT_GRAY_CONCRETE_SLAB),
    CYAN_CONCRETE_SLAB(ModConcreteSlabs.CYAN_CONCRETE_SLAB),
    PURPLE_CONCRETE_SLAB(ModConcreteSlabs.PURPLE_CONCRETE_SLAB),
    BLUE_CONCRETE_SLAB(ModConcreteSlabs.BLUE_CONCRETE_SLAB),
    BROWN_CONCRETE_SLAB(ModConcreteSlabs.BROWN_CONCRETE_SLAB),
    GREEN_CONCRETE_SLAB(ModConcreteSlabs.GREEN_CONCRETE_SLAB),
    RED_CONCRETE_SLAB(ModConcreteSlabs.RED_CONCRETE_SLAB),
    BLACK_CONCRETE_SLAB(ModConcreteSlabs.BLACK_CONCRETE_SLAB),
    WHITE_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.WHITE_CONCRETE_VERTICAL_SLAB),
    ORANGE_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.ORANGE_CONCRETE_VERTICAL_SLAB),
    MAGENTA_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.MAGENTA_CONCRETE_VERTICAL_SLAB),
    LIGHT_BLUE_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.LIGHT_BLUE_CONCRETE_VERTICAL_SLAB),
    YELLOW_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.YELLOW_CONCRETE_VERTICAL_SLAB),
    LIME_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.LIME_CONCRETE_VERTICAL_SLAB),
    PINK_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.PINK_CONCRETE_VERTICAL_SLAB),
    GRAY_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.GRAY_CONCRETE_VERTICAL_SLAB),
    LIGHT_GRAY_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.LIGHT_GRAY_CONCRETE_VERTICAL_SLAB),
    CYAN_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.CYAN_CONCRETE_VERTICAL_SLAB),
    PURPLE_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.PURPLE_CONCRETE_VERTICAL_SLAB),
    BLUE_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.BLUE_CONCRETE_VERTICAL_SLAB),
    BROWN_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.BROWN_CONCRETE_VERTICAL_SLAB),
    GREEN_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.GREEN_CONCRETE_VERTICAL_SLAB),
    RED_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.RED_CONCRETE_VERTICAL_SLAB),
    BLACK_CONCRETE_VERTICAL_SLAB(ModVerticalSlabs.BLACK_CONCRETE_VERTICAL_SLAB),
    PLATFORM_1(ModBlocks.PLATFORM_1),
    PLATFORM_2(ModBlocks.PLATFORM_2),
    PANTOGRAPH(ModBlocks.PANTOGRAPH),
    WIRE_POLE(ModBlocks.WIRE_POLE),
    CATENARY_POLE(ModBlocks.CATENARY_POLE),
    CATENARY_BI_POLE(ModBlocks.CATENARY_BI_POLE),
    CATENARY_CROSS_POLE(ModBlocks.CATENARY_CROSS_POLE),
    CATENARY_POLE_EXTRA(ModBlocks.CATENARY_POLE_EXTRA),
    CATENARY_PART(ModBlocks.CATENARY_PART),
    OVERHEAD_LINE(new WireItem(new Item.Settings().maxCount(1), WireType.OVERHEAD_LINE, false)),
    OVERHEAD_LINE_REMOVER(new WireItem(new Item.Settings().maxCount(1), WireType.OVERHEAD_LINE, true)),
    WIRE(new WireItem(new Item.Settings().maxCount(1), WireType.WIRE, false)),
    WIRE_REMOVER(new WireItem(new Item.Settings().maxCount(1), WireType.WIRE, true)),
    TRAFFIC_LIGHT_2(ModBlocks.TRAFFIC_LIGHT_2),
    TRAFFIC_LIGHT_3(ModBlocks.TRAFFIC_LIGHT_3),
    TRAFFIC_LIGHT_4(ModBlocks.TRAFFIC_LIGHT_4),
    ;

    public static final List<ModItems> LIST = List.of(values());
    private final Item item;
    private final Identifier identifier;

    ModItems(Item item) {
        this.item = item;
        this.identifier = Decoration.id(name().toLowerCase(Locale.ROOT));
    }

    ModItems(Block block) {
        this(new BlockItem(block, new Item.Settings()));
    }

    ModItems(ModBlockConvertible block) {
        this(block.toBlock());
    }

    public static void registerAll() {
        for (ModItems value : LIST) {
            Registry.register(Registries.ITEM, value.identifier(), value.toItem());
        }
    }

    @Override
    public Item toItem() {
        return item;
    }

    public Identifier identifier() {
        return identifier;
    }
}
