package de.greenman999.fullbright.gui.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.CoerceAtLeastConstraint
import gg.essential.elementa.constraints.ConstantColorConstraint
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.plus
import gg.essential.elementa.dsl.provideDelegate
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.USound
import java.awt.Color

class UIButton(text: String, textColor: Color) : UIContainer() {

    private var onClick: () -> Unit = {}

    private val container by UIBlock(Color(89, 89, 89)).constrain {
        width = CoerceAtLeastConstraint(ChildBasedSizeConstraint() + 20.pixels(), 80.pixels())
        height = 20.pixels()
    } childOf this

    private val uiText by UIText(text).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        color = ConstantColorConstraint(textColor)
    } childOf container

    init {
        constrain {
            width = container.getWidth().pixels()
            height = 20.pixels()
        }

        val hoverOutline = OutlineEffect(Color.LIGHT_GRAY, 1f)
        container.onMouseEnter {
            enableEffect(hoverOutline)
        }.onMouseLeave {
            removeEffect(hoverOutline)
        }.onMouseClick {
            setColor(Color(70, 70, 70))
            USound.playButtonPress()
            onClick()
        }.onMouseRelease {
            setColor(Color(89, 89, 89))
        } childOf this
    }

    fun setText(key: String) {
        uiText.setText(key)
    }

    fun setTextColor(color: Color) {
        uiText.setColor(color)
    }

    fun setOnClick(cb: () -> Unit) {
        this.onClick = cb
    }
}