package monogame.rider.mgcb.psi

import com.intellij.psi.tree.IElementType
import monogame.rider.mgcb.MgcbLanguage

class MgcbElementType(debugName: String) : IElementType(debugName, MgcbLanguage.Instance) {
    override fun toString(): String {
        return "MgcbElementType." + super.toString()
    }
}