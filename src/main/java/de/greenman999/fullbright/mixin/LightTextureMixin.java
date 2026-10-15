package de.greenman999.fullbright.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.greenman999.fullbright.FullbrightConfig;
//? if >=26.1 {
/*import net.minecraft.client.renderer.LightmapRenderStateExtractor;
*///?} else {
import net.minecraft.client.renderer.LightTexture;
//?}
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Debug(export = true)
@Mixin(/*? if >=26.1 { *//*LightmapRenderStateExtractor.class*//*?} else {*/LightTexture.class/*?}*/)
public abstract class LightTextureMixin {

    @WrapOperation(
            method = /*? if >=26.1 { *//*"extract"*//*?} else {*/"updateLightTexture"/*?}*/,
            at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = /*? if >=26.1 { *//*0*//*?} else {*/1/*?}*/)
    )
    private float changeGamma(Double instance, Operation<Float> original) {
        if (FullbrightConfig.isToggled()) {
            return (float) FullbrightConfig.getStrength();
        } else {
            return original.call(instance);
        }
    }

}