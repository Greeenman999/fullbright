/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 rotgruengelb, and stonecutter-mod-template contributors
 * See the LICENSE file in the project root for license terms.
 */

package de.greenman999.fullbright.platform.neoforge;

//? neoforge {

/*import de.greenman999.fullbright.Fullbright;
import de.greenman999.fullbright.gui.ConfigScreen;
import gg.essential.elementa.WindowScreen;
import gg.essential.universal.UResolution;
import gg.essential.universal.UScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = Fullbright.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {

	private static boolean openConfigScreen = false;
	private static Screen parentScreen;

	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		Fullbright.onInitializeClient();
//		ModLoadingContext.get().registerExtensionPoint(
//				IConfigScreenFactory.class,
//				() -> (clientGui, parent) -> {
//					parent.onClose();
//					return new ConfigScreen(parent);
//				}
//		);
	}

	@SubscribeEvent // on the mod event bus only on the physical client
	public static void registerBindings(RegisterKeyMappingsEvent event) {
		// Register category
		event.registerCategory(Fullbright.MAIN_CATEGORY);

		// Register binding with category used
		event.register(Fullbright.keyBinding);
	}

//	@SubscribeEvent
//	public static void onTick(ClientTickEvent event) {
////		if(openConfigScreen) {
////			WindowScreen screen = new ConfigScreen(parentScreen);
////			screen.init(UResolution.getScaledWidth(), UResolution.getScaledHeight());
////			UScreen.displayScreen(screen);
////			openConfigScreen = false;
////			parentScreen = null;
////		}
//	}
}
*///?}
