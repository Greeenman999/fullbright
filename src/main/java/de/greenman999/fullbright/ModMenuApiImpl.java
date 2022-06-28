package de.greenman999.fullbright;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.serializer.YamlConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(MinecraftClient.getInstance().currentScreen)
                    .setTitle(Text.of("Fullbright 1.19"));
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            ConfigEntryBuilder configBuilder = builder.entryBuilder();
            general.addEntry(configBuilder.startBooleanToggle(Text.of("Enabled"), FullbrightMod.enabled)
                    .setDefaultValue(false) // Recommended: Used when user click "Reset"
                    .setTooltip(Text.of("Toggle fullbright")) // Optional: Shown when the user hover over this option
                    .setSaveConsumer(newValue -> FullbrightMod.enabled = newValue) // Recommended: Called when user save the config
                    .build()); // Builds the option entry for cloth config

            builder.setSavingRunnable(() -> {
                // Serialise the config into the config file. This will be called last after all variables are updated.



                try {
                    FullbrightMod.saveConfig();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

            Screen screen = builder.build();
            return screen;
        };
    }
}
