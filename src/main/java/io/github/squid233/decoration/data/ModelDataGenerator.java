package io.github.squid233.decoration.data;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.*;
import io.github.squid233.decoration.item.ModItems;
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

    private static void registerConcreteSlab(BlockStateModelGenerator generator, ModConcreteSlabs slab) {
        final Block slabBlock = slab.toBlock();
        final TextureMap textureMap = TexturedModel.CUBE_ALL.get(slab.concreteBlock()).getTextures();
        generator.blockStateCollector.accept(
            BlockStateModelGenerator.createSlabBlockState(
                slabBlock,
                Models.SLAB.upload(slabBlock, textureMap, generator.modelCollector),
                Models.SLAB_TOP.upload(slabBlock, textureMap, generator.modelCollector),
                ModelIds.getBlockModelId(slab.concreteBlock())
            )
        );
    }

    private static void registerPlatform1(BlockStateModelGenerator generator) {
        final Block block = ModBlocks.PLATFORM_1.toBlock();
        final Identifier extra = new Model(
            Optional.of(ModelIds.getBlockSubModelId(block, "_extra_base")),
            Optional.empty(),
            TextureKey.ALL
        ).upload(
            ModelIds.getBlockSubModelId(block, "_extra"),
            TextureMap.all(Blocks.WHITE_CONCRETE),
            generator.modelCollector
        );
        final Identifier platform2ModelId = ModelIds.getBlockModelId(ModBlocks.PLATFORM_2.toBlock());
        generator.blockStateCollector.accept(
            MultipartBlockStateSupplier.create(block)
                .with(BlockStateVariant.create().put(VariantSettings.MODEL, platform2ModelId))
                .with(When.create().set(PlatformBlock.FACING, Direction.NORTH), BlockStateVariant.create().put(VariantSettings.MODEL, extra))
                .with(When.create().set(PlatformBlock.FACING, Direction.EAST), BlockStateVariant.create().put(VariantSettings.MODEL, extra).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.UVLOCK, true))
                .with(When.create().set(PlatformBlock.FACING, Direction.SOUTH), BlockStateVariant.create().put(VariantSettings.MODEL, extra).put(VariantSettings.Y, VariantSettings.Rotation.R180).put(VariantSettings.UVLOCK, true))
                .with(When.create().set(PlatformBlock.FACING, Direction.WEST), BlockStateVariant.create().put(VariantSettings.MODEL, extra).put(VariantSettings.Y, VariantSettings.Rotation.R270).put(VariantSettings.UVLOCK, true))
        );
        generator.registerParentedItemModel(block, platform2ModelId);
    }

    private static void registerPlatform2(BlockStateModelGenerator generator) {
        final Block block = ModBlocks.PLATFORM_2.toBlock();
        generator.registerSingleton(
            block,
            new TextureMap()
                .put(TextureKey.SIDE, TextureMap.getId(Blocks.SMOOTH_STONE))
                .put(TextureKey.TOP, TextureMap.getId(block)),
            Models.CUBE_TOP
        );
    }

    private static void registerUVLockHorizontal(BlockStateModelGenerator generator, Block block, TexturedModel.Factory modelFactory) {
        final Identifier upload = modelFactory.upload(block, generator.modelCollector);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
            .put(VariantSettings.MODEL, upload)
            .put(VariantSettings.UVLOCK, true)).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
    }

    private static void registerVerticalSlab(BlockStateModelGenerator generator, ModVerticalSlabs slab) {
        final Block block = slab.toBlock();
        registerUVLockHorizontal(generator, block, TexturedModel.makeFactory(
            block1 -> TextureMap.all(slab.concreteBlock()),
            new Model(
                Optional.of(new Identifier(Decoration.MOD_ID, "block/vertical_slab_base")),
                Optional.empty(),
                TextureKey.ALL
            )
        ));
    }

    private static void registerPantograph(BlockStateModelGenerator generator) {
        final Block block = ModBlocks.PANTOGRAPH.toBlock();
        final var onModel = new Model(Optional.of(ModelIds.getBlockSubModelId(block, "_on_base")), Optional.empty(), TextureKey.ALL)
            .upload(block, "_on", TextureMap.all(block), generator.modelCollector);
        final var offModel = new Model(Optional.of(ModelIds.getBlockSubModelId(block, "_off_base")), Optional.empty(), TextureKey.ALL)
            .upload(block, "_off", TextureMap.all(block), generator.modelCollector);
        generator.blockStateCollector.accept(
            VariantsBlockStateSupplier.create(block)
                .coordinate(BlockStateVariantMap.create(PantographBlock.FACING, PantographBlock.ON)
                    .register((direction, on) -> BlockStateVariant.create().put(
                        VariantSettings.MODEL,
                        on ? onModel : offModel
                    ).put(VariantSettings.Y, switch (direction) {
                        case DOWN, NORTH, UP -> VariantSettings.Rotation.R0;
                        case SOUTH -> VariantSettings.Rotation.R180;
                        case WEST -> VariantSettings.Rotation.R270;
                        case EAST -> VariantSettings.Rotation.R90;
                    })))
        );
        generator.registerItemModel(block.asItem());
    }

    private static void registerWirePole(BlockStateModelGenerator generator) {
        final Block block = ModBlocks.WIRE_POLE.toBlock();
        generator.registerSingleton(block, TextureMap.all(Blocks.LIGHT_GRAY_CONCRETE), new Model(Optional.of(ModelIds.getBlockSubModelId(block, "_base")), Optional.empty(), TextureKey.ALL));
    }

    private static void registerCatenaryBlock(BlockStateModelGenerator generator, ModBlocks blocks) {
        final Block block = blocks.toBlock();
        registerUVLockHorizontal(generator, block, TexturedModel.makeFactory(
            block1 -> TextureMap.all(Blocks.LIGHT_GRAY_CONCRETE),
            new Model(
                Optional.of(ModelIds.getBlockSubModelId(block, "_base")),
                Optional.empty(),
                TextureKey.ALL
            )
        ));
    }

    private static void registerCatenaryHorizontalAxis(BlockStateModelGenerator generator, ModBlocks blocks) {
        final Block block = blocks.toBlock();
        final Identifier base = new Model(Optional.of(ModelIds.getBlockSubModelId(block, "_base")), Optional.empty(), TextureKey.ALL)
            .upload(block, TextureMap.all(Blocks.LIGHT_GRAY_CONCRETE), generator.modelCollector);
        generator.blockStateCollector.accept(VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(CatenaryBiPoleBlock.AXIS)
            .register(Direction.Axis.X, BlockStateVariant.create().put(VariantSettings.MODEL, base).put(VariantSettings.Y, VariantSettings.Rotation.R90).put(VariantSettings.UVLOCK, true))
            .register(Direction.Axis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, base))));
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {
        for (ModConcreteSlabs slab : ModConcreteSlabs.LIST) {
            registerConcreteSlab(generator, slab);
        }
        for (ModVerticalSlabs v : ModVerticalSlabs.LIST) {
            registerVerticalSlab(generator, v);
        }
        registerPlatform1(generator);
        registerPlatform2(generator);
        registerPantograph(generator);
        registerWirePole(generator);
        registerCatenaryBlock(generator, ModBlocks.CATENARY_POLE);
        registerCatenaryBlock(generator, ModBlocks.CATENARY_PART);
        registerCatenaryHorizontalAxis(generator, ModBlocks.CATENARY_POLE_EXTRA);
        registerCatenaryHorizontalAxis(generator, ModBlocks.CATENARY_BI_POLE);
    }

    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModItems.OVERHEAD_LINE.toItem(), Models.GENERATED);
        generator.register(ModItems.OVERHEAD_LINE_REMOVER.toItem(), Models.GENERATED);
    }
}
