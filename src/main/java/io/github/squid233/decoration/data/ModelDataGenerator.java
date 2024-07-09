package io.github.squid233.decoration.data;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.ModBlocks;
import io.github.squid233.decoration.block.ModConcreteSlabs;
import io.github.squid233.decoration.block.ModVerticalSlabs;
import io.github.squid233.decoration.block.PlatformBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Optional;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class ModelDataGenerator extends FabricModelProvider {
    public ModelDataGenerator(FabricDataOutput output) {
        super(output);
    }

    private void registerConcreteSlab(BlockStateModelGenerator blockStateModelGenerator, ModConcreteSlabs slab) {
        final Block slabBlock = slab.toBlock();
        final TextureMap textureMap = TexturedModel.CUBE_ALL.get(slab.concreteBlock()).getTextures();
        blockStateModelGenerator.blockStateCollector.accept(
            BlockStateModelGenerator.createSlabBlockState(
                slabBlock,
                Models.SLAB.upload(slabBlock, textureMap, blockStateModelGenerator.modelCollector),
                Models.SLAB_TOP.upload(slabBlock, textureMap, blockStateModelGenerator.modelCollector),
                ModelIds.getBlockModelId(slab.concreteBlock())
            )
        );
    }

    private void registerPlatform1(BlockStateModelGenerator blockStateModelGenerator) {
        final Block block = ModBlocks.PLATFORM_1.toBlock();
        final Identifier extra = new Model(
            Optional.of(new Identifier(Decoration.MOD_ID, "block/platform_1_extra_base")),
            Optional.empty(),
            TextureKey.ALL
        ).upload(
            new Identifier(Decoration.MOD_ID, "block/platform_1_extra"),
            TextureMap.all(Blocks.WHITE_CONCRETE),
            blockStateModelGenerator.modelCollector
        );
        final Identifier platform2ModelId = ModelIds.getBlockModelId(ModBlocks.PLATFORM_2.toBlock());
        blockStateModelGenerator.blockStateCollector.accept(
            MultipartBlockStateSupplier.create(block)
                .with(BlockStateVariant.create().put(VariantSettings.MODEL, platform2ModelId))
                .with(When.create().set(PlatformBlock.FACING, Direction.NORTH), BlockStateVariant.create().put(VariantSettings.MODEL, extra))
                .with(When.create().set(PlatformBlock.FACING, Direction.EAST), BlockStateVariant.create().put(VariantSettings.MODEL, extra).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.UVLOCK, true))
                .with(When.create().set(PlatformBlock.FACING, Direction.SOUTH), BlockStateVariant.create().put(VariantSettings.MODEL, extra).put(VariantSettings.Y, VariantSettings.Rotation.R180).put(VariantSettings.UVLOCK, true))
                .with(When.create().set(PlatformBlock.FACING, Direction.WEST), BlockStateVariant.create().put(VariantSettings.MODEL, extra).put(VariantSettings.Y, VariantSettings.Rotation.R270).put(VariantSettings.UVLOCK, true))
        );
        blockStateModelGenerator.registerParentedItemModel(block, platform2ModelId);
    }

    private void registerPlatform2(BlockStateModelGenerator blockStateModelGenerator) {
        final Block block = ModBlocks.PLATFORM_2.toBlock();
        blockStateModelGenerator.registerSingleton(
            block,
            new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getId(Blocks.SMOOTH_STONE))
                .put(TextureKey.TOP, TextureMap.getId(block)),
            Models.CUBE_TOP
        );
    }

    private void registerVerticalSlab(BlockStateModelGenerator blockStateModelGenerator, ModVerticalSlabs slab) {
        final Block block = slab.toBlock();
        blockStateModelGenerator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
                .put(VariantSettings.MODEL, new Model(
                    Optional.of(new Identifier(Decoration.MOD_ID, "block/vertical_slab_base")),
                    Optional.empty(),
                    TextureKey.ALL
                ).upload(block, TextureMap.all(slab.concreteBlock()), blockStateModelGenerator.modelCollector))
            ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
        );
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (ModConcreteSlabs slab : ModConcreteSlabs.LIST) {
            registerConcreteSlab(blockStateModelGenerator, slab);
        }
        for (ModVerticalSlabs v : ModVerticalSlabs.LIST) {
            registerVerticalSlab(blockStateModelGenerator, v);
        }
        registerPlatform1(blockStateModelGenerator);
        registerPlatform2(blockStateModelGenerator);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }
}
