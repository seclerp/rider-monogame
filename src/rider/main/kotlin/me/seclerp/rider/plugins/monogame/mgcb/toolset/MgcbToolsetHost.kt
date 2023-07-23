package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rd.util.reactive.compose
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.monoGameRiderModel
import java.util.*

@Service(Service.Level.PROJECT)
class MgcbToolsetHost(private val intellijProject: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<MgcbToolsetHost>()
    }

    private val hostModel by lazy { intellijProject.solution.monoGameRiderModel }

    init {
        hostModel.mgcbProjectsToolsets.view(intellijProject.lifetime) { toolsetRuntime, key, value ->
            toolsetRuntime.bracketIfAlive({
                value.editor.view(toolsetRuntime) { editorLifetime, editor ->
                    projectsEditorTools[key] = when(editor) {
                        null -> MgcbResolvedTool.None
                        else -> MgcbResolvedTool.Local(editor)
                    }
                }
            }, { projectsEditorTools.remove(key) })
        }
    }

    val globalToolset by lazy { hostModel.mgcbGlobalToolset }
    val solutionToolset by lazy { hostModel.mgcbSolutionToolset }
    val projectsToolset by lazy { hostModel.mgcbProjectsToolsets }
    val editorTool by lazy {
        hostModel.mgcbGlobalToolset.editor
            .compose(hostModel.mgcbSolutionToolset.editor) { globalToolset, solutionToolset ->
                // Solution (local) toolset always has priority over global
                when {
                    solutionToolset is ToolDefinition -> MgcbResolvedTool.Local(solutionToolset)
                    globalToolset is ToolDefinition -> MgcbResolvedTool.Global(globalToolset)
                    else -> MgcbResolvedTool.None
                }
            }
    }
    private val projectsEditorTools = mutableMapOf<UUID, MgcbResolvedTool>()

    fun getEditorTool() = editorTool.value

    fun getEditorTool(project: RdProjectDescriptor): MgcbResolvedTool {
        val projectTool = projectsEditorTools[project.originalGuid]
        if (projectTool == null || projectTool.isNone())
            return getEditorTool()
        return projectTool
    }

    fun areToolsAvailable() = getEditorTool() !is MgcbResolvedTool.None

    fun areToolsAvailable(project: RdProjectDescriptor) = getEditorTool(project) !is MgcbResolvedTool.None
}

