package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorNotifications
import com.jetbrains.rd.util.reactive.compose
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.ToolKind
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CheckMgcbEditorInstalledCommand
import me.seclerp.rider.plugins.monogame.monoGameRiderModel

@Service(Service.Level.PROJECT)
class MgcbToolsetHost(private val intellijProject: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<MgcbToolsetHost>()
    }

    private var isMgcbEditorInstalled = false
    private val hostModel = intellijProject.solution.monoGameRiderModel
    val globalToolset = MgcbPlatformSpecificToolset.fromCrossPlatformToolset(hostModel.mgcbEditorGlobalToolset)
    val solutionToolset = MgcbPlatformSpecificToolset.fromCrossPlatformToolset(hostModel.mgcbEditorSolutionToolset)

    private val areSolutionEditorToolsInstalled =
        solutionToolset.editorLauncher.compose(solutionToolset.editorPlatform, ::checkTools)

    private val areGlobalEditorToolsInstalled =
        globalToolset.editorLauncher.compose(globalToolset.editorPlatform, ::checkTools)

    fun areToolsAvailable() = areSolutionEditorToolsInstalled.value || areGlobalEditorToolsInstalled.value

    private fun checkTools(editorLauncher: ToolDefinition?, platformLauncher: ToolDefinition?): Boolean {
        if (editorLauncher == null || editorLauncher.toolKind == ToolKind.None) {
            return false
        }

        val version = DotNetToolsVersion.parse(editorLauncher.version) ?: return false
        if (version <= KnownMgcbVersions.`3_8_0`) {
            // 3.8.0 and below only requires dotnet-mgcb-editor, there were no platform-specific ones
            return true
        }

        return platformLauncher != null && platformLauncher.toolKind != ToolKind.None
    }

//    private fun isInstalledLocally() = hostModel.mgcbEditorToolSolution.valueOrNull?.toolKind != ToolKind.None
//    private fun isInstalledLocally(projectId: UUID) = hostModel.mgcbEditorToolSolution.valueOrNull?.toolKind != ToolKind.None

    fun startCheck() {
        CheckMgcbEditorInstalledCommand().executeLater {
            isMgcbEditorInstalled = it.succeeded
            EditorNotifications.updateAll()
        }
    }
}

