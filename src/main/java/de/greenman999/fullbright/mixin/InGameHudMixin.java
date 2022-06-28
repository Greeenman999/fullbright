package de.greenman999.fullbright.mixin;

import de.greenman999.fullbright.FullbrightMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "render", at = @At("RETURN"))
    public void changeGamma(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if(FullbrightMod.enabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.getGamma().setValue(69420.0);
        }else {
            MinecraftClient client = MinecraftClient.getInstance();
            client.options.getGamma().setValue(1.0);
        }
    }

}
