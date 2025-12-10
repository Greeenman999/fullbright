package de.greenman999.fullbright.gui

import de.greenman999.fullbright.FullbrightConfig
import de.greenman999.fullbright.gui.components.UIButton
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.RelativeWindowConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import net.minecraft.network.chat.Component
import java.awt.Color

class ConfigScreen : WindowScreen(ElementaVersion.V10, true, true, true) {

    init {
        val text = UIText(translatable("fullbright.gui.title")).constrain {
            x = CenterConstraint()
            y = 10.pixels()
        } childOf window

        val enabledConfig = createConfigEntryBlock()
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
    }

    fun createConfigEntryBlock(): UIContainer {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = 30.pixels()
            width = RelativeWindowConstraint(0.9f)
            height = ChildBasedMaxSizeConstraint() + 4.pixels()
        } childOf window

        return container
    }

    fun translatable(key: String): String {
        return Component.translatable(key).string
    }
}