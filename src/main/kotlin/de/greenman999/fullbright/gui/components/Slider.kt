/*
 * This file contains code from Vigilance
 * Copyright (C) EssentialGG
 * Original source: https://github.com/EssentialGG/Vigilance
 *
 * Originally licensed under GNU Lesser General Public License v3
 * This derivative work is licensed under GNU General Public License v3
 */

package de.greenman999.fullbright.gui.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.dsl.basicWidthConstraint
import gg.essential.elementa.dsl.basicXConstraint
import gg.essential.elementa.dsl.boundTo
import gg.essential.elementa.dsl.childOf
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.effect
import gg.essential.elementa.dsl.percent
import gg.essential.elementa.dsl.pixels
import gg.essential.elementa.dsl.toConstraint
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.USound
import java.awt.Color

class Slider(initialValue: Float) : UIContainer() {

    private var percentage = initialValue
    private var onValueChange: (Float) -> Unit = {}
    private var dragging = false
    private var grabOffset = 0f

    private val outerBox = UIBlock(Color(89, 89, 89)).constrain {
        x = basicXConstraint {
            this@Slider.getLeft() + 1f
        }
        y = CenterConstraint()
        width = basicWidthConstraint {
            this@Slider.getWidth() - 2f
        }
        height = 100.percent
    } childOf this effect OutlineEffect(Color.LIGHT_GRAY, 1f)

    private val completionBox = UIBlock().constrain {
        width = RelativeConstraint(percentage)
        height = 100.percent
        color = Color(70, 70, 70).toConstraint()
    } childOf outerBox

    val grabBox = UIBlock().constrain {
        x = basicXConstraint { completionBox.getRight() - it.getWidth() / 2f }
        y = CenterConstraint() boundTo outerBox
        width = 5.pixels()
        height = 100.percent
        color = Color(70, 70, 70).toConstraint()
    } childOf this effect OutlineEffect(Color.WHITE, 1f)

    val valueText = UIText((percentage * 10).toInt().toString(), false).constrain {
        x = basicXConstraint {
            if (percentage < 0.5f) {
                grabBox.getRight() + 3f
            } else {
                grabBox.getLeft() - it.getWidth() - 2f
            }
        }
        y = CenterConstraint() boundTo outerBox
        color = Color.WHITE.toConstraint()
    } childOf grabBox

    init {
        grabBox.onMouseClick { event ->
            USound.playButtonPress()
            dragging = true
            grabOffset = event.relativeX - (grabBox.getWidth() / 2)
            event.stopPropagation()
        }.onMouseRelease {
            dragging = false
            grabOffset = 0f
            setCurrentPercentage(percentage)
            grabBox.setColor(Color(70, 70, 70))
        }.onMouseDrag { mouseX, _, _ ->
            if (!dragging) return@onMouseDrag

            val clamped = (mouseX + grabBox.getLeft() - grabOffset).coerceIn(outerBox.getLeft()..outerBox.getRight())
            val percentage = (clamped - outerBox.getLeft()) / outerBox.getWidth()
            setCurrentPercentage(percentage, callListener = false)
        }.onMouseEnter {
            grabBox.setColor(Color(90, 90, 90))
        }.onMouseLeave {
            if (!dragging) {
                grabBox.setColor(Color(70, 70, 70))
            }
        }

        outerBox.onMouseClick { event ->
            USound.playButtonPress()
            val percentage = event.relativeX / outerBox.getWidth()
            setCurrentPercentage(percentage)
            dragging = true
        }
    }

    fun getCurrentPercentage() = percentage

    fun setCurrentPercentage(newPercentage: Float, callListener: Boolean = true) {
        percentage = newPercentage.coerceIn(0f..1f)

        completionBox.setWidth(RelativeConstraint(percentage))
        valueText.setText((percentage * 10).toInt().toString())

        if (callListener) {
            onValueChange(percentage)
        }
    }

    fun onValueChange(listener: (Float) -> Unit) {
        onValueChange = listener
    }
}