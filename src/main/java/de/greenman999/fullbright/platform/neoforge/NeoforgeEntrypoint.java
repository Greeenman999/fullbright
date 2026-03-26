/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 rotgruengelb, and stonecutter-mod-template contributors
 * See the LICENSE file in the project root for license terms.
 */

package de.greenman999.fullbright.platform.neoforge;

//? neoforge {

/*import de.greenman999.fullbright.Fullbright;
import de.greenman999.fullbright.gui.ConfigScreen;
import gg.essential.universal.UResolution;
import gg.essential.universal.UScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.function.Supplier;

@Mod(Fullbright.MOD_ID)
public class NeoforgeEntrypoint {

	public NeoforgeEntrypoint(ModContainer modContainer) throws ClassNotFoundException {
		System.out.println("constructor");
		UScreen.class.getModule().getLayer().findModule("gg.essential").get().getClassLoader().loadClass("gg.essential.universal.UScreen");
		modContainer.registerExtensionPoint(
				IConfigScreenFactory.class,
                (Supplier<IConfigScreenFactory>) () -> (clientGui, parent) -> {
                    parent.onClose();
                    return new ConfigScreen(parent);
                }
        );
	}
}
*///?}
