package me.seclerp.rider.plugins.monogame.effect

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.fileTypes.PlainTextLanguage
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import javax.swing.Icon

class EffectFileType : LanguageFileType(PlainTextLanguage.INSTANCE) {
    override fun getName(): String = "MonoGame Effect File"

    override fun getDescription(): String = "Shader language effect used by MonoGame"

    override fun getDefaultExtension(): String = "fx"

    override fun getIcon(): Icon = MonoGameIcons.EffectFile

    companion object {
        val Instance = EffectFileType()
    }
}