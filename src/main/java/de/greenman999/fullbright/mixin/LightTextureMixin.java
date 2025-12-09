package de.greenman999.fullbright.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.greenman999.fullbright.FullbrightConfig;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(LightTexture.class)
public abstract class LightTextureMixin {

    @WrapOperation(
            method = "updateLightTexture",
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1)
    )
    private float changeGamma(Double instance, Operation<Float> original) {
        if (FullbrightConfig.isToggled()) {
            return (float) FullbrightConfig.getStrength();
        } else {
            return original.call(instance);
        }
    }

}