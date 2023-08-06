package me.seclerp.rider.plugins.monogame.effect

import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.Iconable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import javax.swing.Icon

class EffectIconProvider : IconProvider(), DumbAware {
    override fun getIcon(element: PsiElement, @Iconable.IconFlags flags: Int): Icon? {
        val fileElement = element as? PsiFile
        if ((fileElement != null) && fileElement.name.endsWith(".fx", true))
            return MonoGameIcons.EffectFile
        return null
    }
}
