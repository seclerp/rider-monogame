package me.seclerp.rider.plugins.monogame.mgcb.resharper

import com.intellij.lang.Language

class MgcbReSharperLanguage : Language("MgcbReSharper", "MgcbReSharper") {
    override fun isCaseSensitive(): Boolean = true

    companion object {
        val Instance = MgcbReSharperLanguage()
    }
}

