package io.github.squid233.decoration.block;

import io.github.squid233.decoration.Decoration;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Locale;

/**
 * @author squid233
 * @since 0.1.0
 */
public enum ModVerticalSlabs implements ModBlockConvertible {
    WHITE_CONCRETE_VERTICAL_SLAB(Blocks.WHITE_CONCRETE),
    ORANGE_CONCRETE_VERTICAL_SLAB(Blocks.ORANGE_CONCRETE),
    MAGENTA_CONCRETE_VERTICAL_SLAB(Blocks.MAGENTA_CONCRETE),
    LIGHT_BLUE_CONCRETE_VERTICAL_SLAB(Blocks.LIGHT_BLUE_CONCRETE),
    YELLOW_CONCRETE_VERTICAL_SLAB(Blocks.YELLOW_CONCRETE),
    LIME_CONCRETE_VERTICAL_SLAB(Blocks.LIME_CONCRETE),
    PINK_CONCRETE_VERTICAL_SLAB(Blocks.PINK_CONCRETE),
    GRAY_CONCRETE_VERTICAL_SLAB(Blocks.GRAY_CONCRETE),
    LIGHT_GRAY_CONCRETE_VERTICAL_SLAB(Blocks.LIGHT_GRAY_CONCRETE),
    CYAN_CONCRETE_VERTICAL_SLAB(Blocks.CYAN_CONCRETE),
    PURPLE_CONCRETE_VERTICAL_SLAB(Blocks.PURPLE_CONCRETE),
    BLUE_CONCRETE_VERTICAL_SLAB(Blocks.BLUE_CONCRETE),
    BROWN_CONCRETE_VERTICAL_SLAB(Blocks.BROWN_CONCRETE),
    GREEN_CONCRETE_VERTICAL_SLAB(Blocks.GREEN_CONCRETE),
    RED_CONCRETE_VERTICAL_SLAB(Blocks.RED_CONCRETE),
    BLACK_CONCRETE_VERTICAL_SLAB(Blocks.BLACK_CONCRETE),
    ;

    public static final List<ModVerticalSlabs> LIST = List.of(values());
    private final Block slabBlock;
    private final Block concreteBlock;
    private final Identifier identifier;

    ModVerticalSlabs(Block concreteBlock) {
        this.concreteBlock = concreteBlock;
        this.slabBlock = new VerticalSlabBlock(AbstractBlock.Settings.copy(concreteBlock));
        this.identifier = new Identifier(Decoration.MOD_ID, name().toLowerCase(Locale.ROOT));
    }

    public static void registerAll() {
        for (ModVerticalSlabs value : LIST) {
            Registry.register(Registries.BLOCK, value.identifier(), value.toBlock());
        }
    }

    @Override
    public Block toBlock() {
        return slabBlock;
    }

    public Block concreteBlock() {
        return concreteBlock;
    }

    public Identifier identifier() {
        return identifier;
    }
}
