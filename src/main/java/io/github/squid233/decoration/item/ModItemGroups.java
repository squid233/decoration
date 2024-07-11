package io.github.squid233.decoration.item;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.*;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.Arrays;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModItemGroups {
    public static final RegistryKey<ItemGroup> DECORATION_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Decoration.id("decoration"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.LIGHT_BLUE_CONCRETE_SLAB.toItem()))
        .displayName(Text.translatable("itemGroup.decoration.decoration"))
        .entries((displayContext, entries) -> {
            for (ModItems v : ModItems.LIST) {
                final Item item = v.toItem();
                if (item instanceof BlockItem blockItem) {
                    final Block block = blockItem.getBlock();
                    if (block instanceof ConcreteSlabBlock ||
                        block instanceof VerticalSlabBlock) {
                        continue;
                    }
                }
                entries.add(item);
            }
        })
        .build();

    public static void registerAll() {
        register(DECORATION_GROUP_KEY, ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> addAfter(
            entries,
            Blocks.PINK_CONCRETE_POWDER,
            ModConcreteSlabs.WHITE_CONCRETE_SLAB,
            ModConcreteSlabs.LIGHT_GRAY_CONCRETE_SLAB,
            ModConcreteSlabs.GRAY_CONCRETE_SLAB,
            ModConcreteSlabs.BLACK_CONCRETE_SLAB,
            ModConcreteSlabs.BROWN_CONCRETE_SLAB,
            ModConcreteSlabs.RED_CONCRETE_SLAB,
            ModConcreteSlabs.ORANGE_CONCRETE_SLAB,
            ModConcreteSlabs.YELLOW_CONCRETE_SLAB,
            ModConcreteSlabs.LIME_CONCRETE_SLAB,
            ModConcreteSlabs.GREEN_CONCRETE_SLAB,
            ModConcreteSlabs.CYAN_CONCRETE_SLAB,
            ModConcreteSlabs.LIGHT_BLUE_CONCRETE_SLAB,
            ModConcreteSlabs.BLUE_CONCRETE_SLAB,
            ModConcreteSlabs.PURPLE_CONCRETE_SLAB,
            ModConcreteSlabs.MAGENTA_CONCRETE_SLAB,
            ModConcreteSlabs.PINK_CONCRETE_SLAB,
            ModVerticalSlabs.WHITE_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.LIGHT_GRAY_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.GRAY_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.BLACK_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.BROWN_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.RED_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.ORANGE_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.YELLOW_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.LIME_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.GREEN_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.CYAN_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.LIGHT_BLUE_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.BLUE_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.PURPLE_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.MAGENTA_CONCRETE_VERTICAL_SLAB,
            ModVerticalSlabs.PINK_CONCRETE_VERTICAL_SLAB
        ));
    }

    private static void addAfter(FabricItemGroupEntries entries, ItemConvertible afterLast, ModBlockConvertible... items) {
        entries.addAfter(afterLast, Arrays.stream(items).map(c -> new ItemStack(c.toBlock())).toList());
    }

    private static ItemGroup register(RegistryKey<ItemGroup> registryKey, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, registryKey, group);
    }
}
