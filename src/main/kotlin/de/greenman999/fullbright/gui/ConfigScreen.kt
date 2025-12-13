package de.greenman999.fullbright.gui

import com.mojang.blaze3d.platform.InputConstants
import de.greenman999.fullbright.FullbrightClient
import de.greenman999.fullbright.FullbrightConfig
import de.greenman999.fullbright.gui.components.Slider
import de.greenman999.fullbright.gui.components.UIButton
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.universal.USound
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.awt.Color

class ConfigScreen(val parent: Screen? = null) : WindowScreen(ElementaVersion.V10, true, true, true) {

    init {
        UIText(translatable("fullbright.gui.title")).constrain {
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

        UIText(translatable("fullbright.gui.enable")).constrain {
            x = 0.pixels()
            y = CenterConstraint()
        } childOf enabledConfig

        UIButton(
            translatable("fullbright.gui.enable.${FullbrightConfig.isToggled()}"),
            if (FullbrightConfig.isToggled()) Color.GREEN else Color.RED
        ).constrain {
            x = 0.pixels(true)
            y = CenterConstraint()
        }.onClick { enableButton ->
            FullbrightConfig.toggle()
            enableButton.setText(translatable("fullbright.gui.enable.${FullbrightConfig.isToggled()}"))
            enableButton.setTextColor(if (FullbrightConfig.isToggled()) Color.GREEN else Color.RED)
        } childOf enabledConfig // effect OutlineEffect(Color.BLUE, 2f)

        val strengthConfig = createConfigEntryBlock(configEntries)
        UIText(translatable("fullbright.gui.strength")).constrain {
            x = 0.pixels()
            y = CenterConstraint()
        } childOf strengthConfig

        val strengthSlider = Slider(
            FullbrightConfig.getStrength() / 10f
        ).constrain {
            x = 0.pixels(true)
            y = CenterConstraint()
            width = 80.pixels()
            height = 20.pixels()
        } childOf strengthConfig

        strengthSlider.onValueChange { strength ->
            val intStrength = (strength * 10f).toInt()
            FullbrightConfig.setStrength(intStrength)
        }

        val keybindBlock = createConfigEntryBlock(configEntries)
        UIText(translatable("fullbright.gui.keybind")).constrain {
            x = 0.pixels()
            y = CenterConstraint()
        } childOf keybindBlock

        var selectingKey = false
        fun updateKeybindText(button: UIButton) {
            if (selectingKey) {
                button.setText("> %s <".format(FullbrightClient.keyBinding.translatedKeyMessage.string.uppercase()))
            } else {
                button.setText(FullbrightClient.keyBinding.translatedKeyMessage.string.uppercase())
            }
        }
        val keybindButton = UIButton(
            FullbrightClient.keyBinding.translatedKeyMessage.string.uppercase(),
            Color.WHITE
        ).constrain {
            x = 0.pixels(true)
            y = CenterConstraint()
        }.onClick {
            USound.playButtonPress()
            selectingKey = !selectingKey
            updateKeybindText(it)
        } childOf keybindBlock

        window.onKeyType { keyChar, keyCode ->
            if (!selectingKey) return@onKeyType
            if (keyCode == InputConstants.KEY_ESCAPE) return@onKeyType

            FullbrightClient.keyBinding.setKey(InputConstants.Type.KEYSYM.getOrCreate(keyCode))
            KeyMapping.resetMapping()
            selectingKey = false
            updateKeybindText(keybindButton as UIButton)
        }

        /*Inspector(window).constrain {
            x = 10.pixels(true)
            y = 10.pixels(true)
        } childOf window*/
    }

    fun createConfigEntryBlock(configEntries: UIContainer): UIContainer {
        val container = UIContainer().constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
            width = RelativeConstraint(1f)
            height = CoerceAtLeastConstraint(ChildBasedMaxSizeConstraint() + 4.pixels(), 20.pixels())
        } childOf configEntries

        return container
    }

    fun translatable(key: String, argument: Component? = null): String {
        return argument?.let { Component.translatable(key, it) }?.string ?: Component.translatable(key).string
    }

    override fun onClose() {
        super.onClose()
        parent?.let { Minecraft.getInstance().setScreen(it) }
    }
}