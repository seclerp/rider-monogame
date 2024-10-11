package me.seclerp.rider.plugins.monogame.mgcb.editor.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import me.seclerp.rider.plugins.monogame.mgcb.editor.MgcbIntermediateModel
import me.seclerp.rider.plugins.monogame.mgcb.editor.MgcbModelBuilderVisitor
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbOption

@Service(Service.Level.PROJECT)
class MgcbIntermediateModelProvider {
    companion object {
        fun getInstance(project: Project): MgcbIntermediateModelProvider = project.service()
    }

    fun analyzeFile(file: MgcbFile): MgcbIntermediateModel {
        val mgcbOptions = PsiTreeUtil.getChildrenOfType(file, MgcbOption::class.java)
        val visitor = MgcbModelBuilderVisitor()
        mgcbOptions?.forEach { it.accept(visitor) }

        return visitor.resultingModel
    }
}