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
    PANTOGRAPH(new PantographBlock(AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE))),
    WIRE_POLE(new WirePoleBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE))),
    CATENARY_POLE(new CatenaryPoleBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE))),
    CATENARY_BI_POLE(new CatenaryBiPoleBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE))),
    CATENARY_CROSS_POLE(new CatenaryCrossPoleBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE))),
    CATENARY_POLE_EXTRA(new CatenaryPoleExtraBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE))),
    CATENARY_PART(new CatenaryPartBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE))),
    TRAFFIC_LIGHT_3(new TrafficLightBlock.Light3(AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE).luminance(value -> 3))),
    TRAFFIC_LIGHT_2(new TrafficLightBlock.Light2(AbstractBlock.Settings.copy(TRAFFIC_LIGHT_3.toBlock()))),
    ;

    public static final List<ModBlocks> LIST = List.of(values());
    private final Block block;
    private final Identifier identifier;

    ModBlocks(Block block) {
        this.block = block;
        this.identifier = Decoration.id(name().toLowerCase(Locale.ROOT));
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
