package io.github.squid233.decoration.item;

import io.github.squid233.decoration.Decoration;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModItemGroups {
    public static final RegistryKey<ItemGroup> DECORATION_GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(Decoration.MOD_ID, "decoration"));
    public static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
        .icon(() -> new ItemStack(ModItems.LIGHT_BLUE_CONCRETE_SLAB.toItem()))
        .displayName(Text.translatable("itemGroup.decoration.decoration"))
        .build();

    public static void registerAll() {
        register(DECORATION_GROUP_KEY, ITEM_GROUP);
        ItemGroupEvents.modifyEntriesEvent(DECORATION_GROUP_KEY).register(entries -> {
            for (ModItems item : ModItems.LIST) {
                entries.add(item.toItem());
            }
        });
    }

    private static ItemGroup register(RegistryKey<ItemGroup> registryKey, ItemGroup group) {
        return Registry.register(Registries.ITEM_GROUP, registryKey, group);
    }
}
