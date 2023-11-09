package de.greenman999.fullbright;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.RegistryEntryArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class FullbrightMod implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("fullbright");

	public static KeyBinding keyBinding;
	private boolean loaded = false;
	private final ModMenuApiImpl modMenuApi = new ModMenuApiImpl();
	private boolean openConfigScreen = false;

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

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(literal("fullbright-config")
						.executes(context -> {
							openConfigScreen = true;
							return 1;
						})
				));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(!loaded && MinecraftClient.getInstance().options != null) {
				loaded = true;
				if(FConfig.enabled) enableFullbright();
			}
			if(openConfigScreen) {
				openConfigScreen = false;
				MinecraftClient.getInstance().setScreen(modMenuApi.getModConfigScreenFactory().create(MinecraftClient.getInstance().currentScreen));
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
