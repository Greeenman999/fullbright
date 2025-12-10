package de.greenman999.fullbright.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIText
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels

class ConfigScreen : WindowScreen(ElementaVersion.V10) {
    init {
        val text = UIText("Config").constrain {
            x = 2.pixels()
            y = 2.pixels()
        } childOf window
    }
}