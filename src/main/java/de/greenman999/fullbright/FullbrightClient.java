package de.greenman999.fullbright;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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

    private static boolean toggled = false;
    private static int strength = 10;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyBinding.Category.create(Identifier.of("fullbright", "main"))
        ));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("fullbright").executes(context -> {
            ModContainer modContainer = FabricLoader.getInstance().getModContainer("fullbright").orElseThrow();
            String version = modContainer.getMetadata().getVersion().getFriendlyString();

            context.getSource().sendFeedback(
                    Text.translatable(
                            "fullbright.text.status",
                            version,
                            isToggled() ?
                                    Text.translatable("fullbright.text.enabled").formatted(Formatting.GREEN)
                                    : Text.translatable("fullbright.text.disabled").formatted(Formatting.RED),
                            Text.literal(strength + "").formatted(Formatting.YELLOW)
                    ).formatted(Formatting.GOLD)
                            .append(Text.literal("\n"))
                            .append(Text.translatable("fullbright.text.help.toggle", Text.translatable(keyBinding.getBoundKeyTranslationKey()))
                            .formatted(Formatting.GRAY)
                            ).append(Text.literal("\n"))
                            .append(Text.translatable("fullbright.text.help.strength")
                                    .formatted(Formatting.GRAY)
                            )

            );

            /*context.getSource().sendFeedback(
                    Text.translatable("fullbright.text.version", version)
                            .formatted(Formatting.AQUA)
                            .append(Text.literal("\n"))
                            .append(
                                    Text.translatable("fullbright.text." + (isToggled() ? "enabled" : "disabled"))
                                            .formatted(isToggled() ? Formatting.GREEN : Formatting.RED)
                            )
            );*/

            return 1;
        })));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                toggle();
            }
        });

        LOGGER.info("Fullbright Initialized");
    }

    public static boolean isToggled() {
        return toggled;
    }

    public static void toggle() {
        toggled = !toggled;
    }
}
