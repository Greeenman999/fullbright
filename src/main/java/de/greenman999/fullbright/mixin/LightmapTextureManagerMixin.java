package de.greenman999.fullbright.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.greenman999.fullbright.FullbrightClient;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {

    @WrapOperation(
            method = "update",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1)
    )
    private float modifyLightmap(Double instance, Operation<Float> original) {
        if (FullbrightClient.isToggled()) {
            return FullbrightClient.getStrength();
        } else {
            return original.call(instance);
        }
    }

}