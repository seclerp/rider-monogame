package me.seclerp.rider.plugins.monogame.mgcb.previewer.services

import com.intellij.openapi.components.Service
import com.intellij.psi.util.PsiTreeUtil
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbModel
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbModelBuilderVisitor
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbOption

@Service
class MgcbAnalyzer {
    fun analyzeFile(file: MgcbFile): MgcbModel {
        val mgcbOptions = PsiTreeUtil.getChildrenOfType(file, MgcbOption::class.java)
        val visitor = MgcbModelBuilderVisitor()
        mgcbOptions?.forEach { it.accept(visitor) }

        return visitor.resultingModel
    }
}