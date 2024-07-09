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
public enum ModBlocks implements ModBlockConvertible {
    PLATFORM_1(new PlatformBlock(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE))),
    PLATFORM_2(new Block(AbstractBlock.Settings.copy(Blocks.SMOOTH_STONE))),
    ;

    public static final List<ModBlocks> LIST = List.of(values());
    private final Block block;
    private final Identifier identifier;

    ModBlocks(Block block) {
        this.block = block;
        this.identifier = new Identifier(Decoration.MOD_ID, name().toLowerCase(Locale.ROOT));
    }

    public static void registerAll() {
        for (ModBlocks value : LIST) {
            Registry.register(Registries.BLOCK, value.identifier(), value.toBlock());
        }
    }

    @Override
    public Block toBlock() {
        return block;
    }

    public Identifier identifier() {
        return identifier;
    }
}
