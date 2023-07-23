package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.compose
import com.jetbrains.rd.util.reactive.map
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.monoGameRiderModel

@Service(Service.Level.PROJECT)
class MgcbToolsetHost(private val intellijProject: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<MgcbToolsetHost>()
    }

    private val hostModel by lazy { intellijProject.solution.monoGameRiderModel }
    val globalToolset by lazy { hostModel.mgcbGlobalToolset }
    val solutionToolset by lazy { hostModel.mgcbSolutionToolset }
    private val editorTool by lazy {
        hostModel.mgcbGlobalToolset.editor
            .compose(hostModel.mgcbSolutionToolset.editor) { globalToolset, solutionToolset ->
                // Solution (local) toolset always has priority over global
                when (solutionToolset) {
                    null -> globalToolset
                    else -> solutionToolset
                }
            }
    }
    val projectTools by lazy { hostModel.mgcbProjectsToolsets }

    fun getEditorTool() = editorTool.value

    fun getEditorTool(project: RdProjectDescriptor): ToolDefinition? {
        val projectTool = projectTools[project.originalGuid] ?: return getEditorTool()
        return projectTool.editor.value
    }

    fun areToolsAvailable() = editorTool.value != null

    fun areToolsAvailable(project: RdProjectDescriptor): Boolean {
        val projectTool = projectTools[project.originalGuid]
        if (projectTool != null) {
            return true
        }

        return areToolsAvailable()
    }
}

