package io.github.squid233.decoration.data;

import io.github.squid233.decoration.block.*;
import io.github.squid233.decoration.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

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
        pack.addProvider(ModelDataGenerator::new);
        pack.addProvider(LangDataGenerator.LangGenEnglish::new);
        pack.addProvider(LangDataGenerator.LangGenSzh::new);
        pack.addProvider(LootTableGen::new);
        pack.addProvider(RecipeGen::new);
        pack.addProvider(BlockTagGen::new);
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
