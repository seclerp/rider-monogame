package me.seclerp.rider.plugins.monogame.effect

import com.jetbrains.rider.ideaInterop.fileTypes.RiderLanguageFileTypeBase
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import javax.swing.Icon

object EffectFileType : RiderLanguageFileTypeBase(EffectLanguage) {
    override fun getName(): String = "MonoGame Effect File"

    override fun getDescription(): String = "Shader language effect used by MonoGame"

    override fun getDefaultExtension(): String = "fx"

    override fun getIcon(): Icon = MonoGameIcons.EffectFile
}