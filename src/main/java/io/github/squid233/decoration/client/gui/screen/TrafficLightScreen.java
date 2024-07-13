package io.github.squid233.decoration.client.gui.screen;

import io.github.squid233.decoration.block.entity.TrafficLightStep;
import io.github.squid233.decoration.network.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

import java.util.*;

/**
 * @author squid233
 * @since 0.2.0
 */
@Environment(EnvType.CLIENT)
public class TrafficLightScreen extends Screen {
    private static final String TITLE = "text.decoration.screen.trafficLight";
    private static final String COLOR = "text.decoration.screen.trafficLight.color";
    private static final String TICKS = "text.decoration.screen.trafficLight.ticks";
    private static final String INDEX = "text.decoration.screen.trafficLight.index";
    private static final String FLASHING = "text.decoration.screen.trafficLight.flashing";
    private final List<TrafficLightStep> steps;
    private StepListWidget listWidget;
    private final BlockPos pos;

    public TrafficLightScreen(List<TrafficLightStep> steps, BlockPos pos) {
        super(Text.translatable(TITLE));
        this.steps = List.copyOf(steps);
        this.pos = pos;
    }

    public class StepWidget extends ElementListWidget.Entry<StepWidget> {
        private final List<ClickableWidget> children = new ArrayList<>();
        private final CyclingButtonWidget<Formatting> colorWidget;
        private final TextFieldWidget ticksWidget;
        private final TextFieldWidget indexWidget;
        private final TextFieldWidget flashingWidget;
        private final ButtonWidget moveUpWidget;
        private final ButtonWidget moveDownWidget;
        private final ButtonWidget deleteWidget;

        public StepWidget(TrafficLightStep step) {
            this.colorWidget = CyclingButtonWidget.<Formatting>builder(formatting -> Text.literal(formatting.asString()))
                .values(Arrays.stream(Formatting.values())
                    .filter(formatting -> formatting.isColor() || formatting == Formatting.RESET)
                    .toList())
                .initially(step.color())
                .omitKeyText()
                .build(0,
                    0,
                    75,
                    20,
                    Text.translatable(COLOR));

            this.ticksWidget = new TextFieldWidget(TrafficLightScreen.this.textRenderer,
                0,
                0,
                75,
                20,
                Text.translatable(TICKS));
            this.ticksWidget.setText(String.valueOf(step.ticks()));

            this.indexWidget = new TextFieldWidget(TrafficLightScreen.this.textRenderer,
                0,
                0,
                75,
                20,
                Text.translatable(INDEX));
            this.indexWidget.setText(String.valueOf(step.index()));

            this.flashingWidget = new TextFieldWidget(TrafficLightScreen.this.textRenderer,
                0,
                0,
                75,
                20,
                Text.translatable(FLASHING));
            this.flashingWidget.setText(String.valueOf(step.flashing()));

            this.moveUpWidget = ButtonWidget.builder(Text.literal("↑"), button -> {
                final StepListWidget list = TrafficLightScreen.this.listWidget;
                final int i = list.children().indexOf(this);
                if (i <= 0) {
                    return;
                }
                list.removeEntryWithoutScrolling(this);
                list.children().add(i - 1, this);
            }).width(20).build();
            this.moveDownWidget = ButtonWidget.builder(Text.literal("↓"), button -> {
                final StepListWidget list = TrafficLightScreen.this.listWidget;
                final int i = list.children().indexOf(this);
                if (i >= list.getEntryCount() - 1) {
                    return;
                }
                list.removeEntryWithoutScrolling(this);
                list.children().add(i + 1, this);
            }).width(20).build();
            this.deleteWidget = ButtonWidget.builder(Text.literal("-"), button ->
                TrafficLightScreen.this.listWidget.removeEntryWithoutScrolling(this)
            ).width(20).build();

            children.add(colorWidget);
            children.add(ticksWidget);
            children.add(indexWidget);
            children.add(flashingWidget);
            children.add(moveUpWidget);
            children.add(moveDownWidget);
            children.add(deleteWidget);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return children;
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int currX = x + 2;

            renderWidget(colorWidget, context, currX, y, mouseX, mouseY, tickDelta);
            currX += 75 + 2;

            renderWidget(ticksWidget, context, currX, y, mouseX, mouseY, tickDelta);
            currX += 75 + 2;

            renderWidget(indexWidget, context, currX, y, mouseX, mouseY, tickDelta);
            currX += 75 + 2;

            renderWidget(flashingWidget, context, currX, y, mouseX, mouseY, tickDelta);
            currX += 75 + 2;

            renderWidget(moveUpWidget, context, currX, y, mouseX, mouseY, tickDelta);
            currX += 20 + 2;

            renderWidget(moveDownWidget, context, currX, y, mouseX, mouseY, tickDelta);
            currX += 20 + 2;

            renderWidget(deleteWidget, context, currX, y, mouseX, mouseY, tickDelta);
        }

        private void renderWidget(ClickableWidget widget, DrawContext context, int x, int y, int mouseX, int mouseY, float tickDelta) {
            widget.setX(x);
            widget.setY(y);
            widget.render(context, mouseX, mouseY, tickDelta);
        }

        public TrafficLightStep toStep() {
            return new TrafficLightStep(colorWidget.getValue(),
                parseInt(validateString(ticksWidget.getText())),
                parseInt(validateString(indexWidget.getText())),
                parseInt(validateString(flashingWidget.getText()))
            );
        }
    }

    public class StepListWidget extends ElementListWidget<StepWidget> {
        public StepListWidget() {
            super(TrafficLightScreen.this.client, 0, 0, 0, 0, 24);
            setRenderBackground(false);
        }

        public void addStep(TrafficLightStep step) {
            addEntry(new StepWidget(step));
        }

        @Override
        protected boolean removeEntryWithoutScrolling(StepWidget entry) {
            return super.removeEntryWithoutScrolling(entry);
        }

        @Override
        protected int getEntryCount() {
            return super.getEntryCount();
        }

        @Override
        public int getRowWidth() {
            return width;
        }

        @Override
        protected int getScrollbarPositionX() {
            return width - 6;
        }
    }

    private static String validateString(String s) {
        return s.codePoints()
            .filter(value -> '0' <= value && value <= '9')
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
    }

    private static int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    protected void init() {
        super.init();

        addDrawableChild(ButtonWidget.builder(Text.literal("+"), button -> listWidget.addStep(TrafficLightStep.EMPTY))
            .width(20)
            .position(width - 8 - 20, 6 + textRenderer.fontHeight + 6)
            .build());

        final StepListWidget previousList = listWidget;
        listWidget = new StepListWidget();
        listWidget.updateSize(width, height, 6 + textRenderer.fontHeight + 6 + 20 + 6, height - 32);
        if (previousList == null) {
            for (TrafficLightStep step : steps) {
                listWidget.addStep(step);
            }
        } else {
            listWidget.children().addAll(previousList.children());
        }
        addSelectableChild(listWidget);

        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> close())
            .width(150)
            .position(width / 2 - 20 - 150, height - 6 - 20)
            .build());
        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
                final PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(pos);
                buf.writeCollection(listWidget.children(), (buf1, stepWidget) ->
                    TrafficLightStep.writeBuf(buf1, stepWidget.toStep()));
                ClientPlayNetworking.send(ModNetwork.TRAFFIC_LIGHT_SAVE_PACKET, buf);
                close();
            })
            .width(150)
            .position(width / 2 + 20, height - 6 - 20)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        listWidget.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 6, Colors.WHITE);
        final int x = listWidget.getRowLeft();
        final int y = 6 + textRenderer.fontHeight + 6 + 10;
        context.drawTextWithShadow(textRenderer, Text.translatable(COLOR), x + 2, y, Colors.WHITE);
        context.drawTextWithShadow(textRenderer, Text.translatable(TICKS), x + 2 + 75 + 2, y, Colors.WHITE); // /6*3
        context.drawTextWithShadow(textRenderer, Text.translatable(INDEX), x + 2 + (75 + 2) * 2, y, Colors.WHITE); // /6*3
        context.drawTextWithShadow(textRenderer, Text.translatable(FLASHING), x + 2 + (75 + 2) * 3, y, Colors.WHITE); // /6*5
        super.render(context, mouseX, mouseY, delta);
    }
}
