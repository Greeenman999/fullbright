package de.greenman999.fullbright.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
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

    @Definition(id = "minecraft", field = "Lnet/minecraft/client/renderer/LightTexture;minecraft:Lnet/minecraft/client/Minecraft;")
    @Definition(id = "options", field = "Lnet/minecraft/client/Minecraft;options:Lnet/minecraft/client/Options;")
    @Definition(id = "gamma", method = "Lnet/minecraft/client/Options;gamma()Lnet/minecraft/client/OptionInstance;")
    @Definition(id = "get", method = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;")
    @Definition(id = "floatValue", method = "Ljava/lang/Double;floatValue()F")
    @Definition(id = "Double", type = Double.class)
    @Expression("((Double) this.minecraft.options.gamma().get()).floatValue()")
    @WrapOperation(
            method = "updateLightTexture",
            at = @At(value = "MIXINEXTRAS:EXPRESSION")
    )
    private float changeGamma(Double instance, Operation<Float> original) {
        if (FullbrightConfig.isToggled()) {
            return (float) FullbrightConfig.getStrength();
        } else {
            return original.call(instance);
        }
    }

}