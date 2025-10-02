package de.greenman999.fullbright;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullbrightClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("fullbright");

    private static KeyBinding keyBinding;

    private static boolean toggled = false;

    @Override
    public void onInitializeClient() {
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.fullbright.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                KeyBinding.Category.create(Identifier.of("fullbright", "main"))
        ));

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
