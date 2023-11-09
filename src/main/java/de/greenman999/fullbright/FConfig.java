package de.greenman999.fullbright;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class FConfig {

    public static ConfigClassHandler<FConfig> HANDLER = ConfigClassHandler.createBuilder(FConfig.class)
            .id(new Identifier("fullbright", "fconfig"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("fullbright.json5"))
                    .setJson5(true)
                    .build())
            .build();

    @SerialEntry
    public static boolean enabled = false;

    @SerialEntry
    public static double defaultBrightness = 0.5;

    @SerialEntry
    public static double brightness = 100.0;
}
