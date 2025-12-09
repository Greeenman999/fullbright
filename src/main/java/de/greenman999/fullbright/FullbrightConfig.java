package de.greenman999.fullbright;

import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FullbrightConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullbrightConfig.class);
    private static final String FILE_NAME = "fullbright.properties";

    private static volatile boolean toggled = false;
    private static volatile int strength = 10;

    private static final ExecutorService IO_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "fullbright-config-writer");
        t.setDaemon(true);
        return t;
    });

    public static synchronized void load() {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            LOGGER.warn("Failed to create config directory {}", configDir, e);
        }
        Path path = configDir.resolve(FILE_NAME);
        Properties props = new Properties();

        if (Files.exists(path)) {
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
                toggled = Boolean.parseBoolean(props.getProperty("toggled", "false"));
                try {
                    setStrength(Integer.parseInt(props.getProperty("strength", "10")));
                } catch (NumberFormatException e) {
                    setStrength(10);
                }

                LOGGER.info("Fullbright config loaded from {} (toggled={}, strength={})", path, toggled, strength);
            } catch (IOException e) {
                LOGGER.warn("Failed to load Fullbright config from {}", path, e);
            }
        } else {
            save();
        }
    }

    public static void saveAsync() {
        final boolean snapshotToggled = toggled;
        final int snapshotStrength = strength;
        IO_EXECUTOR.submit(() -> saveToDisk(snapshotToggled, snapshotStrength));
    }

    private static void saveToDisk(boolean toggledSnapshot, int strengthSnapshot) {
        Path configDir = FabricLoader.getInstance().getConfigDir();
        try {
            Files.createDirectories(configDir);
        } catch (IOException e) {
            LOGGER.warn("Failed to create config directory {}", configDir, e);
        }

        Path path = configDir.resolve(FILE_NAME);
        Properties props = new Properties();
        props.setProperty("toggled", Boolean.toString(toggledSnapshot));
        props.setProperty("strength", Integer.toString(strengthSnapshot));

        try (OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            props.store(out, "Fullbright config");
            LOGGER.info("Fullbright config saved to {}", path);
        } catch (IOException e) {
            LOGGER.warn("Failed to save Fullbright config to {}", path, e);
        }
    }

    public static synchronized void save() {
        saveToDisk(toggled, strength);
    }

    public static boolean isToggled() {
        return toggled;
    }

    public static int getStrength() {
        return strength;
    }

    public static synchronized void toggle() {
        toggled = !toggled;
        saveAsync();
    }

    public static synchronized void setStrength(int value) {
        int clamped = Math.clamp(value, 0, 10);
        if (clamped != value) {
            LOGGER.warn("Attempted to set strength {} out of bounds; clamping to {}", value, clamped);
        }
        strength = clamped;
        saveAsync();
    }

    public static void shutdownIoExecutor() {
        IO_EXECUTOR.shutdown();
    }
}
