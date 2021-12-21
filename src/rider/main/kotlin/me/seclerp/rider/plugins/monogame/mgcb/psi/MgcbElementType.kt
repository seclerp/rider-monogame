package me.seclerp.rider.plugins.monogame.mgcb.psi

import com.intellij.psi.tree.IElementType
import me.seclerp.rider.plugins.monogame.mgcb.MgcbLanguage

class MgcbElementType(debugName: String) : IElementType(debugName, MgcbLanguage.Instance) {
    override fun toString(): String {
        return "MgcbElementType." + super.toString()
    }
}