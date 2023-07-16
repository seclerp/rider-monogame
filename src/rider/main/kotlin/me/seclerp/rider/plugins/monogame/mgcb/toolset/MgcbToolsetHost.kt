package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.compose
import com.jetbrains.rd.util.reactive.map
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.plugins.monogame.monoGameRiderModel

@Service(Service.Level.PROJECT)
class MgcbToolsetHost(private val intellijProject: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<MgcbToolsetHost>()
    }

    private val hostModel by lazy { intellijProject.solution.monoGameRiderModel }
    val globalToolset by lazy { hostModel.mgcbGlobalToolset }
    val solutionToolset by lazy { hostModel.mgcbSolutionToolset }
    val editorTool by lazy {
        hostModel.mgcbGlobalToolset.editor
            .compose(hostModel.mgcbSolutionToolset.editor) { globalToolset, solutionToolset ->
                // Solution (local) toolset always has priority over global
                when (solutionToolset) {
                    null -> globalToolset
                    else -> solutionToolset
                }
            }
    }

    fun areToolsAvailable() = editorTool.value != null
}

