package de.greenman999.fullbright;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
//? if >=1.21.11 {
import net.minecraft.resources.Identifier;
//?} else {
/*import net.minecraft.resources.ResourceLocation;
*///?}
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullbrightClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("fullbright");

    private static KeyMapping keyBinding;

    @Override
    public void onInitializeClient() {
        FullbrightConfig.load();

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.fullbright.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyMapping.Category.register(/*? >=1.21.11 {*/Identifier/*?} else {*//*ResourceLocation*//*?}*/.fromNamespaceAndPath("fullbright", "main"))
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var fullbrightCmd = ClientCommandManager.literal("fullbright")
                    .executes(context -> {
                        ModContainer modContainer = FabricLoader.getInstance().getModContainer("fullbright").orElseThrow();
                        String version = modContainer.getMetadata().getVersion().getFriendlyString();

                        context.getSource().sendFeedback(
                                Component.translatable(
                                        "fullbright.text.status",
                                        version,
                                        FullbrightConfig.isToggled() ?
                                                Component.translatable("fullbright.text.enabled").withStyle(ChatFormatting.GREEN)
                                                : Component.translatable("fullbright.text.disabled").withStyle(ChatFormatting.RED),
                                        Component.literal(FullbrightConfig.getStrength() + "").withStyle(ChatFormatting.YELLOW)
                                ).withStyle(ChatFormatting.GOLD)
                                        .append(Component.literal("\n"))
                                        .append(Component.translatable("fullbright.text.help.toggle", keyBinding.getTranslatedKeyMessage())
                                                .withStyle(ChatFormatting.GRAY)
                                        ).append(Component.literal("\n"))
                                        .append(Component.translatable("fullbright.text.help.strength")
                                                .withStyle(ChatFormatting.GRAY)
                                        )

                        );
                        return 1;
                    })
                    .then(ClientCommandManager.literal("toggle").executes(context -> {
                        FullbrightConfig.toggle();
                        context.getSource().sendFeedback(
                                Component.translatable(
                                        "fullbright.text.toggled",
                                        FullbrightConfig.isToggled() ?
                                                Component.translatable("fullbright.text.enabled").withStyle(ChatFormatting.GREEN)
                                                : Component.translatable("fullbright.text.disabled").withStyle(ChatFormatting.RED)
                                ).withStyle(ChatFormatting.GOLD)
                        );
                        return 1;
                    }))
                    .then(ClientCommandManager.literal("strength")
                            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 10)).executes(context -> {
                                int value = IntegerArgumentType.getInteger(context, "value");
                                FullbrightConfig.setStrength(value);
                                context.getSource().sendFeedback(
                                        Component.translatable(
                                                "fullbright.text.strength.set",
                                                Component.literal(FullbrightConfig.getStrength() + "").withStyle(ChatFormatting.YELLOW)
                                        ).withStyle(ChatFormatting.GOLD)
                                );
                                return 1;
                            }))
                    );

            dispatcher.register(fullbrightCmd);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {
                FullbrightConfig.toggle();
            }
        });

        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> FullbrightConfig.shutdownIoExecutor());

        Runtime.getRuntime().addShutdownHook(new Thread(FullbrightConfig::shutdownIoExecutor, "fullbright-shutdown"));

        LOGGER.info("Fullbright Initialized");
    }
}
