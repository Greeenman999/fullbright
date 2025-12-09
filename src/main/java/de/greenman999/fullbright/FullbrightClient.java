package de.greenman999.fullbright;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullbrightClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("fullbright");

    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        FullbrightConfig.load();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyBinding.Category.create(Identifier.of("fullbright", "main"))
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var fullbrightCmd = ClientCommandManager.literal("fullbright")
                    .executes(context -> {
                        ModContainer modContainer = FabricLoader.getInstance().getModContainer("fullbright").orElseThrow();
                        String version = modContainer.getMetadata().getVersion().getFriendlyString();

                        context.getSource().sendFeedback(
                                Text.translatable(
                                        "fullbright.text.status",
                                        version,
                                        FullbrightConfig.isToggled() ?
                                                Text.translatable("fullbright.text.enabled").formatted(Formatting.GREEN)
                                                : Text.translatable("fullbright.text.disabled").formatted(Formatting.RED),
                                        Text.literal(FullbrightConfig.getStrength() + "").formatted(Formatting.YELLOW)
                                ).formatted(Formatting.GOLD)
                                        .append(Text.literal("\n"))
                                        .append(Text.translatable("fullbright.text.help.toggle", keyBinding.getBoundKeyLocalizedText())
                                                .formatted(Formatting.GRAY)
                                        ).append(Text.literal("\n"))
                                        .append(Text.translatable("fullbright.text.help.strength")
                                                .formatted(Formatting.GRAY)
                                        )

                        );
                        return 1;
                    })
                    .then(ClientCommandManager.literal("toggle").executes(context -> {
                        FullbrightConfig.toggle();
                        context.getSource().sendFeedback(
                                Text.translatable(
                                        "fullbright.text.toggled",
                                        FullbrightConfig.isToggled() ?
                                                Text.translatable("fullbright.text.enabled").formatted(Formatting.GREEN)
                                                : Text.translatable("fullbright.text.disabled").formatted(Formatting.RED)
                                ).formatted(Formatting.GOLD)
                        );
                        return 1;
                    }))
                    .then(ClientCommandManager.literal("strength")
                            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 10)).executes(context -> {
                                int value = IntegerArgumentType.getInteger(context, "value");
                                FullbrightConfig.setStrength(value);
                                context.getSource().sendFeedback(
                                        Text.translatable(
                                                "fullbright.text.strength.set",
                                                Text.literal(FullbrightConfig.getStrength() + "").formatted(Formatting.YELLOW)
                                        ).formatted(Formatting.GOLD)
                                );
                                return 1;
                            }))
                    );

            dispatcher.register(fullbrightCmd);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                FullbrightConfig.toggle();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> {
            FullbrightConfig.shutdownIoExecutor();
        });

        Runtime.getRuntime().addShutdownHook(new Thread(FullbrightConfig::shutdownIoExecutor, "fullbright-shutdown"));

        LOGGER.info("Fullbright Initialized");
    }
}
