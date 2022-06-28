package de.greenman999.fullbright.mixin;

import de.greenman999.fullbright.FullbrightMod;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;


@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class SimpleOptionMixin {

    @Inject(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", at = @At("RETURN"), cancellable = true)
    public void removeValidation(Double double_, CallbackInfoReturnable<Optional<Double>> cir) {
        if(FullbrightMod.enabled) {
            if(double_ == 69420.0) {
                cir.setReturnValue(Optional.of(69420.0));
            }
        }
    }

}
