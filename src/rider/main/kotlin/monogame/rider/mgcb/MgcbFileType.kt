package monogame.rider.mgcb

import com.intellij.openapi.fileTypes.LanguageFileType
import monogame.rider.MonoGameIcons
import javax.swing.Icon

class MgcbFileType : LanguageFileType(MgcbLanguage.Instance) {
    override fun getName(): String = "MonoGame Content Pipeline File"

    override fun getDescription(): String = "MonoGame Content Pipeline File"

    override fun getDefaultExtension(): String = "mgcb"

    override fun getIcon(): Icon = MonoGameIcons.MgcbFile

    companion object {
        val Instance = MgcbFileType()
    }
}