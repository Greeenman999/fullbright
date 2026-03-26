/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 rotgruengelb, and stonecutter-mod-template contributors
 * See the LICENSE file in the project root for license terms.
 */

package de.greenman999.fullbright.platform.fabric;

//? fabric {

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import de.greenman999.fullbright.Fullbright;
import de.greenman999.fullbright.FullbrightConfig;
import de.greenman999.fullbright.gui.ConfigScreen;
import gg.essential.universal.UScreen;
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
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class FabricEventSubscriber {

	private static boolean scheduleOpenConfig = false;

	public static void registerEvents() {
		KeyBindingHelper.registerKeyBinding(Fullbright.keyBinding);

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
										.append(Component.translatable("fullbright.text.help.toggle", Fullbright.keyBinding.getTranslatedKeyMessage())
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
					)
					.then(ClientCommandManager.literal("config").executes(context -> {
						scheduleOpenConfig = true;
						return 1;
					}));

			dispatcher.register(fullbrightCmd);
		});

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (scheduleOpenConfig) {
				scheduleOpenConfig = false;
				UScreen.displayScreen(new ConfigScreen());
			}
			while (Fullbright.keyBinding.consumeClick()) {
				FullbrightConfig.toggle();
			}
		});

		ClientLifecycleEvents.CLIENT_STOPPING.register(client -> FullbrightConfig.shutdownIoExecutor());
	}
}
//?}
