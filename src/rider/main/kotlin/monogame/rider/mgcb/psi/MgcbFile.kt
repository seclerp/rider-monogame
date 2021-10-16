package monogame.rider.mgcb.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import monogame.rider.mgcb.MgcbFileType
import monogame.rider.mgcb.MgcbLanguage

class MgcbFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, MgcbLanguage.Instance) {
    override fun getFileType(): FileType = MgcbFileType.Instance

    override fun toString(): String = "MGCB File"
}