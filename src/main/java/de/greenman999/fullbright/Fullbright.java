package de.greenman999.fullbright;

import com.mojang.blaze3d.platform.InputConstants;
import de.greenman999.fullbright.platform.Platform;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
import de.greenman999.fullbright.platform.fabric.FabricPlatform;
//?} neoforge {
/*import de.greenman999.fullbright.platform.neoforge.NeoforgePlatform;
 *///?} forge {
/*import de.greenman999.fullbright.platform.forge.ForgePlatform;
 *///?}

@SuppressWarnings("LoggingSimilarMessage")
public class Fullbright {

    public static final String MOD_ID = /*$ mod_id*/ "fullbright";
    public static final String MOD_VERSION = /*$ mod_version*/ "3.2.0";
    public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/ "Fullbright";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final Platform PLATFORM = createPlatformInstance();

    private static Fullbright instance;

    public Fullbright() {
        instance = this;
    }

    //? if >=1.21.9 {
    public static final KeyMapping.Category MAIN_CATEGORY = new KeyMapping.Category(Identifier.fromNamespaceAndPath(Fullbright.MOD_ID, "main"));
    //?}

    public static KeyMapping keyBinding = new KeyMapping(
            "key.fullbright.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            //? if >=1.21.9 {
            MAIN_CATEGORY
            //?} else {
            /*"key.category.fullbright.main"
             *///?}
    );

    public static void onInitializeClient() {
        LOGGER.info("Initializing {} Client on {}", MOD_ID, Fullbright.xplat().loader());
        LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);

        FullbrightConfig.load();

        Runtime.getRuntime().addShutdownHook(new Thread(FullbrightConfig::shutdownIoExecutor, "fullbright-shutdown"));

        LOGGER.info("Fullbright Initialized");
    }

    public static Fullbright getInstance() {
        if (instance == null) {
            instance = new Fullbright();
        }
        return instance;
    }

    public static Platform xplat() {
        return PLATFORM;
    }

    private static Platform createPlatformInstance() {
        //? fabric {
        return new FabricPlatform();
        //?} neoforge {
        /*return new NeoforgePlatform();
         *///?} forge {
        /*return new ForgePlatform();
         *///?}
    }
}
