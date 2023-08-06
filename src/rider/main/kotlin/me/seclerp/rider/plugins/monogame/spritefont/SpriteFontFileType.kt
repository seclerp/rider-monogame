package me.seclerp.rider.plugins.monogame.spritefont

import com.intellij.lang.xml.XMLLanguage
import com.intellij.openapi.fileTypes.LanguageFileType
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import javax.swing.Icon

object SpriteFontFileType : LanguageFileType(XMLLanguage.INSTANCE) {
    override fun getName(): String = "MonoGame SpriteFont File"

    override fun getDescription(): String = "Description of a font used by MonoGame"

    override fun getDefaultExtension(): String = "spritefont"

    override fun getIcon(): Icon = MonoGameIcons.SpriteFontFile
}