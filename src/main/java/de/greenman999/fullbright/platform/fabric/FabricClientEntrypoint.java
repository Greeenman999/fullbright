/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 rotgruengelb, and stonecutter-mod-template contributors
 * See the LICENSE file in the project root for license terms.
 */

package de.greenman999.fullbright.platform.fabric;

//? fabric {

import de.greenman999.fullbright.Fullbright;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		Fullbright.onInitializeClient();
		FabricEventSubscriber.registerEvents();
	}

}
//?}
