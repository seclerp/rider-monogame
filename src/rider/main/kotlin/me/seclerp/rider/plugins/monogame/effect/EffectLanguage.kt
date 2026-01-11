package me.seclerp.rider.plugins.monogame.effect

import com.jetbrains.rider.ideaInterop.fileTypes.RiderLanguageBase

object EffectLanguage : RiderLanguageBase("MGFX", "MGFX") {
    override fun isCaseSensitive(): Boolean = false
}

