package de.greenman999.fullbright.gui

import de.greenman999.fullbright.FullbrightConfig
import de.greenman999.fullbright.gui.components.UIButton
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.inspector.Inspector
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.FillConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.RelativeWindowConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.minus
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import gg.essential.elementa.effects.OutlineEffect
import net.minecraft.network.chat.Component
import java.awt.Color

class ConfigScreen : WindowScreen(ElementaVersion.V10, true, true, true) {

    init {
        val text = UIText(translatable("fullbright.gui.title")).constrain {
            x = CenterConstraint()
            y = 10.pixels()
        } childOf window

        val configEntries = UIContainer().constrain {
            x = CenterConstraint()
            y = 30.pixels()
            width = RelativeWindowConstraint(0.9f)
            height = RelativeWindowConstraint(1f) - 40.pixels()
        } childOf window

        val enabledConfig = createConfigEntryBlock(configEntries)
        val enabledConfigDescription = UIText(translatable("fullbright.gui.enable")).constrain {
            x = 0.pixels()
            y = CenterConstraint()
        } childOf enabledConfig
        val enableButton = UIButton(
            translatable("fullbright.gui.enable.${FullbrightConfig.isToggled()}"),
            if (FullbrightConfig.isToggled()) Color.GREEN else Color.RED
        ).constrain {
            x = 0.pixels(true)
            y = CenterConstraint()
        } childOf enabledConfig // effect OutlineEffect(Color.BLUE, 2f)

        enableButton.setOnClick {
            FullbrightConfig.toggle()
            enableButton.setText(translatable("fullbright.gui.enable.${FullbrightConfig.isToggled()}"))
            enableButton.setTextColor(if (FullbrightConfig.isToggled()) Color.GREEN else Color.RED)
        }

        val strengthConfig = createConfigEntryBlock(configEntries)
        val strengthConfigDescription = UIText(translatable("fullbright.gui.strength")).constrain {
            x = 0.pixels()
            y = CenterConstraint()
        } childOf strengthConfig

        val hoverOutline = OutlineEffect(Color.LIGHT_GRAY, 1f)
        val strengthSlider = UIBlock(Color(89, 89, 89)).constrain {
            x = 0.pixels(true)
            y = CenterConstraint()
            width = 80.pixels()
            height = 20.pixels()
        }.onMouseEnter {
            enableEffect(hoverOutline)
        }.onMouseLeave {
            removeEffect(hoverOutline)
        } childOf strengthConfig

        val strengthSliderElement = UIBlock(Color(70, 70, 70)).constrain {
            x = (8 * FullbrightConfig.getStrength() - 2.5f).pixels()
            y = 0.pixels()
            width = 5.pixels()
            height = FillConstraint()
        }.onMouseDrag { mouseX, mouseY, mouseButton ->

        } childOf strengthSlider

        Inspector(window).constrain {
            x = 10.pixels(true)
            y = 10.pixels(true)
        } childOf window
    }

    fun createConfigEntryBlock(configEntries: UIContainer): UIContainer {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
            width = RelativeConstraint(1f)
            height = ChildBasedMaxSizeConstraint() + 4.pixels()
        } childOf configEntries

        return container
    }

    fun translatable(key: String): String {
        return Component.translatable(key).string
    }
}