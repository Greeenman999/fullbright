package de.greenman999.fullbright;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FullbrightMod implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("fullbright");

	public static KeyBinding keyBinding;
	private boolean loaded = false;

	@Override
	public void onInitializeClient() {
		LOGGER.info("Loaded Fullbright Mod!");
		FConfig.HANDLER.load();

		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.fullbright.toggle",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_B,
				"category.fullbright.main"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(!loaded && MinecraftClient.getInstance().options != null) {
				loaded = true;
				if(FConfig.enabled) enableFullbright();
			}
			while (keyBinding.wasPressed()) {
				toggleFullbright();
			}
		});
	}

	public static void enableFullbright() {
		MinecraftClient.getInstance().options.getGamma().setValue(FConfig.brightness);
		FConfig.enabled = true;
	}

	public static void disableFullbright() {
		MinecraftClient.getInstance().options.getGamma().setValue(FConfig.defaultBrightness);
		FConfig.enabled = false;
	}

	public void toggleFullbright() {
		if (FConfig.enabled) {
			disableFullbright();
		} else {
			enableFullbright();
		}
		FConfig.HANDLER.save();
	}

	public static void setFullbright(boolean enabled) {
		if (enabled) {
			enableFullbright();
		} else {
			disableFullbright();
		}
		FConfig.HANDLER.save();
	}
}
