package me.seclerp.rider.plugins.monogame.mgcb.resharper

import com.jetbrains.rider.ideaInterop.fileTypes.RiderLanguageFileTypeBase
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import javax.swing.Icon

class MgcbReSharperFileType : RiderLanguageFileTypeBase(MgcbReSharperLanguage.Instance) {
    override fun getName(): String = "MonoGame Content Pipeline File (ReSharper)"
    override fun getDescription(): String = "Content Pipeline file used by MonoGame (ReSharper impl)"
    override fun getDefaultExtension(): String = "mgcb2"
    override fun getIcon(): Icon = MonoGameIcons.MgcbFile

    companion object {
        val Instance = MgcbReSharperFileType()
    }
}