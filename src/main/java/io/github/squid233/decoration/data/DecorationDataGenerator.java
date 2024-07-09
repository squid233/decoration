package io.github.squid233.decoration.data;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.*;
import io.github.squid233.decoration.item.ModItemConvertible;
import io.github.squid233.decoration.item.ModItemGroups;
import io.github.squid233.decoration.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author squid233
 * @since 0.1.0
 */
public final class DecorationDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelGen::new);
        pack.addProvider(LangGen.LangGenEnglish::new);
        pack.addProvider(LangGen.LangGenSzh::new);
        pack.addProvider(LootTableGen::new);
        pack.addProvider(RecipeGen::new);
        pack.addProvider(BlockTagGen::new);
    }

    private static final class ModelGen extends FabricModelProvider {
        public ModelGen(FabricDataOutput output) {
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

    private static sealed class LangGen extends FabricLanguageProvider {
        private final String languageCode;

        protected LangGen(FabricDataOutput dataOutput, String languageCode) {
            super(dataOutput, languageCode);
            this.languageCode = languageCode;
        }

        protected static void add(TranslationBuilder builder, ModBlockConvertible block, String value) {
            builder.add(block.toBlock(), value);
        }

        protected static void add(TranslationBuilder builder, ModItemConvertible item, String value) {
            builder.add(item.toItem(), value);
        }

        @Override
        public void generateTranslations(TranslationBuilder translationBuilder) {
            dataOutput.getModContainer().findPath("assets/" + Decoration.MOD_ID + "/lang/" + languageCode + ".existing.json")
                .ifPresent(path -> {
                    try {
                        translationBuilder.add(path);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to add existing language file!", e);
                    }
                });
        }

        private static final class LangGenEnglish extends LangGen {
            public LangGenEnglish(FabricDataOutput dataOutput) {
                super(dataOutput, "en_us");
            }

            @Override
            public void generateTranslations(TranslationBuilder builder) {
                builder.add(ModItemGroups.DECORATION_GROUP_KEY, "Decoration");

                add(builder, ModItems.WHITE_CONCRETE_SLAB, "White concrete slab");
                add(builder, ModItems.ORANGE_CONCRETE_SLAB, "Orange concrete slab");
                add(builder, ModItems.MAGENTA_CONCRETE_SLAB, "Magenta concrete slab");
                add(builder, ModItems.LIGHT_BLUE_CONCRETE_SLAB, "Light blue concrete slab");
                add(builder, ModItems.YELLOW_CONCRETE_SLAB, "Yellow concrete slab");
                add(builder, ModItems.LIME_CONCRETE_SLAB, "Lime concrete slab");
                add(builder, ModItems.PINK_CONCRETE_SLAB, "Pink concrete slab");
                add(builder, ModItems.GRAY_CONCRETE_SLAB, "Gray concrete slab");
                add(builder, ModItems.LIGHT_GRAY_CONCRETE_SLAB, "Light gray concrete slab");
                add(builder, ModItems.CYAN_CONCRETE_SLAB, "Cyan concrete slab");
                add(builder, ModItems.PURPLE_CONCRETE_SLAB, "Purple concrete slab");
                add(builder, ModItems.BLUE_CONCRETE_SLAB, "Blue concrete slab");
                add(builder, ModItems.BROWN_CONCRETE_SLAB, "Brown concrete slab");
                add(builder, ModItems.GREEN_CONCRETE_SLAB, "Green concrete slab");
                add(builder, ModItems.RED_CONCRETE_SLAB, "Red concrete slab");
                add(builder, ModItems.BLACK_CONCRETE_SLAB, "Black concrete slab");

                add(builder, ModItems.WHITE_CONCRETE_VERTICAL_SLAB, "White concrete vertical slab");
                add(builder, ModItems.ORANGE_CONCRETE_VERTICAL_SLAB, "Orange concrete vertical slab");
                add(builder, ModItems.MAGENTA_CONCRETE_VERTICAL_SLAB, "Magenta concrete vertical slab");
                add(builder, ModItems.LIGHT_BLUE_CONCRETE_VERTICAL_SLAB, "Light blue concrete vertical slab");
                add(builder, ModItems.YELLOW_CONCRETE_VERTICAL_SLAB, "Yellow concrete vertical slab");
                add(builder, ModItems.LIME_CONCRETE_VERTICAL_SLAB, "Lime concrete vertical slab");
                add(builder, ModItems.PINK_CONCRETE_VERTICAL_SLAB, "Pink concrete vertical slab");
                add(builder, ModItems.GRAY_CONCRETE_VERTICAL_SLAB, "Gray concrete vertical slab");
                add(builder, ModItems.LIGHT_GRAY_CONCRETE_VERTICAL_SLAB, "Light gray concrete vertical slab");
                add(builder, ModItems.CYAN_CONCRETE_VERTICAL_SLAB, "Cyan concrete vertical slab");
                add(builder, ModItems.PURPLE_CONCRETE_VERTICAL_SLAB, "Purple concrete vertical slab");
                add(builder, ModItems.BLUE_CONCRETE_VERTICAL_SLAB, "Blue concrete vertical slab");
                add(builder, ModItems.BROWN_CONCRETE_VERTICAL_SLAB, "Brown concrete vertical slab");
                add(builder, ModItems.GREEN_CONCRETE_VERTICAL_SLAB, "Green concrete vertical slab");
                add(builder, ModItems.RED_CONCRETE_VERTICAL_SLAB, "Red concrete vertical slab");
                add(builder, ModItems.BLACK_CONCRETE_VERTICAL_SLAB, "Black concrete vertical slab");

                add(builder, ModItems.PLATFORM_1, "Platform (with white line)");
                add(builder, ModItems.PLATFORM_2, "Platform");

                super.generateTranslations(builder);
            }
        }

        private static final class LangGenSzh extends LangGen {
            public LangGenSzh(FabricDataOutput dataOutput) {
                super(dataOutput, "zh_cn");
            }

            @Override
            public void generateTranslations(TranslationBuilder builder) {
                builder.add(ModItemGroups.DECORATION_GROUP_KEY, "装饰");

                add(builder, ModItems.WHITE_CONCRETE_SLAB, "白色混凝土台阶");
                add(builder, ModItems.ORANGE_CONCRETE_SLAB, "橙色混凝土台阶");
                add(builder, ModItems.MAGENTA_CONCRETE_SLAB, "品红色混凝土台阶");
                add(builder, ModItems.LIGHT_BLUE_CONCRETE_SLAB, "淡蓝色混凝土台阶");
                add(builder, ModItems.YELLOW_CONCRETE_SLAB, "黄色混凝土台阶");
                add(builder, ModItems.LIME_CONCRETE_SLAB, "黄绿色混凝土台阶");
                add(builder, ModItems.PINK_CONCRETE_SLAB, "粉色混凝土台阶");
                add(builder, ModItems.GRAY_CONCRETE_SLAB, "灰色混凝土台阶");
                add(builder, ModItems.LIGHT_GRAY_CONCRETE_SLAB, "淡灰色混凝土台阶");
                add(builder, ModItems.CYAN_CONCRETE_SLAB, "青色混凝土台阶");
                add(builder, ModItems.PURPLE_CONCRETE_SLAB, "紫色混凝土台阶");
                add(builder, ModItems.BLUE_CONCRETE_SLAB, "蓝色混凝土台阶");
                add(builder, ModItems.BROWN_CONCRETE_SLAB, "棕色混凝土台阶");
                add(builder, ModItems.GREEN_CONCRETE_SLAB, "绿色混凝土台阶");
                add(builder, ModItems.RED_CONCRETE_SLAB, "红色混凝土台阶");
                add(builder, ModItems.BLACK_CONCRETE_SLAB, "黑色混凝土台阶");

                add(builder, ModItems.WHITE_CONCRETE_VERTICAL_SLAB, "白色混凝土竖直台阶");
                add(builder, ModItems.ORANGE_CONCRETE_VERTICAL_SLAB, "橙色混凝土竖直台阶");
                add(builder, ModItems.MAGENTA_CONCRETE_VERTICAL_SLAB, "品红色混凝土竖直台阶");
                add(builder, ModItems.LIGHT_BLUE_CONCRETE_VERTICAL_SLAB, "淡蓝色混凝土竖直台阶");
                add(builder, ModItems.YELLOW_CONCRETE_VERTICAL_SLAB, "黄色混凝土竖直台阶");
                add(builder, ModItems.LIME_CONCRETE_VERTICAL_SLAB, "黄绿色混凝土竖直台阶");
                add(builder, ModItems.PINK_CONCRETE_VERTICAL_SLAB, "粉色混凝土竖直台阶");
                add(builder, ModItems.GRAY_CONCRETE_VERTICAL_SLAB, "灰色混凝土竖直台阶");
                add(builder, ModItems.LIGHT_GRAY_CONCRETE_VERTICAL_SLAB, "淡灰色混凝土竖直台阶");
                add(builder, ModItems.CYAN_CONCRETE_VERTICAL_SLAB, "青色混凝土竖直台阶");
                add(builder, ModItems.PURPLE_CONCRETE_VERTICAL_SLAB, "紫色混凝土竖直台阶");
                add(builder, ModItems.BLUE_CONCRETE_VERTICAL_SLAB, "蓝色混凝土竖直台阶");
                add(builder, ModItems.BROWN_CONCRETE_VERTICAL_SLAB, "棕色混凝土竖直台阶");
                add(builder, ModItems.GREEN_CONCRETE_VERTICAL_SLAB, "绿色混凝土竖直台阶");
                add(builder, ModItems.RED_CONCRETE_VERTICAL_SLAB, "红色混凝土竖直台阶");
                add(builder, ModItems.BLACK_CONCRETE_VERTICAL_SLAB, "黑色混凝土竖直台阶");

                add(builder, ModItems.PLATFORM_1, "站台（白线）");
                add(builder, ModItems.PLATFORM_2, "站台");

                super.generateTranslations(builder);
            }
        }
    }

    private static final class LootTableGen extends FabricBlockLootTableProvider {
        public LootTableGen(FabricDataOutput dataOutput) {
            super(dataOutput);
        }

        @Override
        public void generate() {
            for (ModItems item : ModItems.LIST) {
                if (item.toItem() instanceof BlockItem blockItem) {
                    addDrop(blockItem.getBlock(), blockItem);
                }
            }
        }
    }

    private static final class RecipeGen extends FabricRecipeProvider {
        public RecipeGen(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate(Consumer<RecipeJsonProvider> exporter) {
            for (ModConcreteSlabs slab : ModConcreteSlabs.LIST) {
                final Block concreteBlock = slab.concreteBlock();
                ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, slab.toBlock())
                    .pattern("sss")
                    .input('s', concreteBlock)
                    .criterion(FabricRecipeProvider.hasItem(concreteBlock), FabricRecipeProvider.conditionsFromItem(concreteBlock))
                    .offerTo(exporter);
            }

            for (ModVerticalSlabs slab : ModVerticalSlabs.LIST) {
                final Block concreteBlock = slab.concreteBlock();
                ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, slab.toBlock())
                    .pattern("s")
                    .pattern("s")
                    .pattern("s")
                    .input('s', concreteBlock)
                    .criterion(FabricRecipeProvider.hasItem(concreteBlock), FabricRecipeProvider.conditionsFromItem(concreteBlock))
                    .offerTo(exporter);
            }

            ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.PLATFORM_1.toBlock())
                .pattern("ac")
                .pattern(" b")
                .input('a', Items.WHITE_DYE)
                .input('c', Items.YELLOW_DYE)
                .input('b', Blocks.SMOOTH_STONE)
                .criterion(FabricRecipeProvider.hasItem(Items.WHITE_DYE), FabricRecipeProvider.conditionsFromItem(Items.WHITE_DYE))
                .criterion(FabricRecipeProvider.hasItem(Items.YELLOW_DYE), FabricRecipeProvider.conditionsFromItem(Items.YELLOW_DYE))
                .criterion(FabricRecipeProvider.hasItem(Blocks.SMOOTH_STONE), FabricRecipeProvider.conditionsFromItem(Blocks.SMOOTH_STONE))
                .offerTo(exporter);
            ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.PLATFORM_2.toBlock())
                .pattern("a")
                .pattern("b")
                .input('a', Items.YELLOW_DYE)
                .input('b', Blocks.SMOOTH_STONE)
                .criterion(FabricRecipeProvider.hasItem(Items.YELLOW_DYE), FabricRecipeProvider.conditionsFromItem(Items.YELLOW_DYE))
                .criterion(FabricRecipeProvider.hasItem(Blocks.SMOOTH_STONE), FabricRecipeProvider.conditionsFromItem(Blocks.SMOOTH_STONE))
                .offerTo(exporter);
        }
    }

    private static final class BlockTagGen extends FabricTagProvider.BlockTagProvider {
        public BlockTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
            super(output, completableFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup arg) {
            final var pickaxe = getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE);
            final var slabs = getOrCreateTagBuilder(BlockTags.SLABS);
            for (ModConcreteSlabs slab : ModConcreteSlabs.LIST) {
                final Block block = slab.toBlock();
                pickaxe.add(block);
                slabs.add(block);
            }
            for (ModVerticalSlabs v : ModVerticalSlabs.LIST) {
                pickaxe.add(v.toBlock());
            }
            for (ModBlocks block : ModBlocks.LIST) {
                pickaxe.add(block.toBlock());
            }
        }
    }
}
