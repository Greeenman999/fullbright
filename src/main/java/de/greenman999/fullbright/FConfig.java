package de.greenman999.fullbright;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class FConfig {

    public static ConfigClassHandler<FConfig> HANDLER = ConfigClassHandler.createBuilder(FConfig.class)
            .id(new Identifier("fullbright", "fconfig"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("fullbright.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public static boolean enabled = false;

    @SerialEntry
    public static double defaultBrightness = 0.5;

    @SerialEntry
    public static double brightness = 100.0;

    public static Screen openConfigScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("fullbright.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("fullbright.config.categories.main.name"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("fullbright.config.categories.main.options.enabled.name"))
                                .description(OptionDescription.of(Text.translatable("fullbright.config.categories.main.options.enabled.description")))
                                .binding(false, () -> FConfig.enabled, FullbrightMod::setFullbright)
                                .controller(opt -> BooleanControllerBuilder.create(opt)
                                        .coloured(true)
                                        .yesNoFormatter())
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.translatable("fullbright.config.categories.main.options.brightness.name"))
                                .description(OptionDescription.of(Text.translatable("fullbright.config.categories.main.options.brightness.description")))
                                .binding(100.0, () -> FConfig.brightness, newVal -> FConfig.brightness = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(0.0, 100.0)
                                        .step(1.0))
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.translatable("fullbright.config.categories.main.options.default-brightness.name"))
                                .description(OptionDescription.of(Text.translatable("fullbright.config.categories.main.options.default-brightness.description")))
                                .binding(0.5, () -> FConfig.defaultBrightness, newVal -> FConfig.defaultBrightness = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(0.0, 1.0)
                                        .step(0.1))
                                .build())
                        .option(LabelOption.createBuilder()
                                .line(Text.translatable("fullbright.config.categories.main.options.keybindings", FullbrightMod.keyBinding.getBoundKeyLocalizedText()))
                                .line(Text.translatable("fullbright.config.categories.main.options.keybindings.notice").formatted(Formatting.GRAY))
                                .build())
                        .option(ButtonOption.createBuilder()
                                .name(Text.translatable("fullbright.config.categories.main.options.open-controls.name"))
                                .description(OptionDescription.of(Text.translatable("fullbright.config.categories.main.options.open-controls.description")))
                                .text(Text.empty())
                                .action((screen, opt) -> MinecraftClient.getInstance().setScreen(new KeybindsScreen(parent, MinecraftClient.getInstance().options)))
                                .build())
                        .build())
                .build().generateScreen(parent);
    }
}
