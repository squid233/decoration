package io.github.squid233.decoration.data;

import io.github.squid233.decoration.Decoration;
import io.github.squid233.decoration.block.ModBlockConvertible;
import io.github.squid233.decoration.item.ModItemConvertible;
import io.github.squid233.decoration.item.ModItemGroups;
import io.github.squid233.decoration.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

import java.io.IOException;

/**
 * @author squid233
 * @since 0.1.0
 */
public sealed class LangDataGenerator extends FabricLanguageProvider {
    private final String languageCode;

    protected LangDataGenerator(FabricDataOutput dataOutput, String languageCode) {
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

    public static final class LangGenEnglish extends LangDataGenerator {
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
            add(builder, ModItems.PANTOGRAPH, "Pantograph");
            add(builder, ModItems.WIRE_POLE, "Wire pole");
            add(builder, ModItems.CATENARY_POLE, "Catenary pole");
            add(builder, ModItems.CATENARY_BI_POLE, "Catenary bi-pole");
            add(builder, ModItems.CATENARY_POLE_EXTRA, "Catenary pole (extra)");
            add(builder, ModItems.CATENARY_PART, "Catenary part");

            super.generateTranslations(builder);
        }
    }

    public static final class LangGenSzh extends LangDataGenerator {
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
            add(builder, ModItems.PANTOGRAPH, "受电弓");
            add(builder, ModItems.WIRE_POLE, "电线杆");
            add(builder, ModItems.CATENARY_POLE, "接触网柱");
            add(builder, ModItems.CATENARY_BI_POLE, "接触网柱（双向）");
            add(builder, ModItems.CATENARY_POLE_EXTRA, "接触网柱（延长）");
            add(builder, ModItems.CATENARY_PART, "接触网部件");

            super.generateTranslations(builder);
        }
    }
}
