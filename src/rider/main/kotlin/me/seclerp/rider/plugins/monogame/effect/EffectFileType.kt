package me.seclerp.rider.plugins.monogame.effect

import com.jetbrains.rider.cpp.fileType.CppFileType
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import javax.swing.Icon

object EffectSourceFileType : CppFileType() {
    override fun getName(): String = "MonoGame Effect Source File"
    override fun getDescription(): String = "Shader language effect used by MonoGame"
    override fun getDefaultExtension(): String = "fx"
    override fun getIcon(): Icon = MonoGameIcons.EffectFile
}

object EffectHeaderFileType : CppFileType() {
    override fun getName(): String = "MonoGame Effect Header File"
    override fun getDescription(): String = "Shader language effect used by MonoGame"
    override fun getDefaultExtension(): String = "fxh"
    override fun getIcon(): Icon = MonoGameIcons.EffectFile
}