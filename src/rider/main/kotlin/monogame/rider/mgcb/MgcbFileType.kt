package monogame.rider.mgcb

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class MgcbFileType : LanguageFileType(MgcbLanguage.Instance) {
    override fun getName(): String = "MonoGame Content Pipeline File"

    override fun getDescription(): String = "MonoGame Content Pipeline File"

    override fun getDefaultExtension(): String = "mgcb"

    override fun getIcon(): Icon? = null

    companion object {
        val Instance = MgcbFileType()
    }
}