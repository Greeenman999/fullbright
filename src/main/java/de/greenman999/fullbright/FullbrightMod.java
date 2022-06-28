package de.greenman999.fullbright;

import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class FullbrightMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("fullbright");
	public static boolean enabled = false;
	public static File configFolder = new File(FabricLoader.getInstance().getConfigDir().toFile() + File.separator + "fullbright-1-19");
	public static File configFile = new File(configFolder,  "config.properties");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Loaded Fullbright 1.19 Mod!");

		try {
			if (!configFile.exists() || !configFile.canRead())
				saveConfig();

			var fis = new FileInputStream(configFile);
			var properties = new Properties();
			properties.load(fis);
			enabled = Boolean.parseBoolean(properties.getProperty("enabled"));
			fis.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static void saveConfig() throws IOException {
		if(!configFile.exists() || !configFile.canRead()) {
			configFolder.mkdir();
			configFile.createNewFile();
		}
		var fos = new FileOutputStream(FullbrightMod.configFile, false);
		fos.write("# Fullbright 1.19".getBytes());
		fos.write("\n".getBytes());
		fos.write(("enabled=" + FullbrightMod.enabled).getBytes());

		fos.close();
	}
}
