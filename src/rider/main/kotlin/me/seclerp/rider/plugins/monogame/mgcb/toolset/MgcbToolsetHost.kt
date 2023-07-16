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

    private val hostModel = intellijProject.solution.monoGameRiderModel
    val globalToolset = hostModel.mgcbGlobalToolset
    val solutionToolset = hostModel.mgcbSolutionToolset
    val editorTool = hostModel.mgcbGlobalToolset.editor
        .compose(hostModel.mgcbSolutionToolset.editor) { globalToolset, solutionToolset ->
            // Solution (local) toolset always has priority over global
            when (solutionToolset) {
                null -> globalToolset
                else -> solutionToolset
            }
        }

    private val areGlobalEditorToolsInstalled =
        globalToolset.editor.map { it != null }

    private val areSolutionEditorToolsInstalled =
        solutionToolset.editor.map { it != null }

    fun areToolsAvailable() = areSolutionEditorToolsInstalled.value || areGlobalEditorToolsInstalled.value
}

