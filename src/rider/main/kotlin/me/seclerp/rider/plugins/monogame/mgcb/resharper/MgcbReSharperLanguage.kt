package me.seclerp.rider.plugins.monogame.mgcb.resharper

import com.jetbrains.rider.ideaInterop.fileTypes.RiderLanguageBase
import me.seclerp.rider.plugins.monogame.mgcb.MgcbLanguage

class MgcbReSharperLanguage : RiderLanguageBase("MgcbReSharper", "MgcbReSharper") {
    override fun isCaseSensitive(): Boolean = true

    companion object {
        val Instance = MgcbReSharperLanguage()
    }
}