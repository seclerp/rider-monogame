package me.seclerp.rider.plugins.monogame.mgcb.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorNotifications
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.plugins.monogame.MgcbEditorToolset
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.ToolKind
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CheckMgcbEditorInstalledCommand
import me.seclerp.rider.plugins.monogame.mgcb.toolset.DotNetToolsVersion
import me.seclerp.rider.plugins.monogame.mgcb.toolset.KnownMgcbVersions
import me.seclerp.rider.plugins.monogame.monoGameRiderModel

@Service(Service.Level.PROJECT)
class MgcbEditorCheckService(private val intellijProject: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<MgcbEditorCheckService>()
    }

    private var isMgcbEditorInstalled = false
    private val hostModel by lazy { intellijProject.solution.monoGameRiderModel }
    private val globalToolset by lazy { MgcbPlatformSpecificToolset.fromCrossPlatformToolset(hostModel.mgcbEditorGlobalToolset) }
    private val solutionToolset by lazy { MgcbPlatformSpecificToolset.fromCrossPlatformToolset(hostModel.mgcbEditorSolutionToolset) }

    fun isInstalled(): Boolean {
        return isInstalledInToolset(solutionToolset) || isInstalledInToolset(globalToolset)
    }

    private fun isInstalledInToolset(toolset: MgcbPlatformSpecificToolset): Boolean {
        val launcher = toolset.editorLauncher.valueOrNull ?: return false
        if (launcher.toolKind == ToolKind.None) return false
        val version = DotNetToolsVersion.parse(launcher.version) ?: return false

        return when {
            version >= KnownMgcbVersions.V_381 ->
                toolset.editorLauncher.valueOrNull?.toolKind != ToolKind.None
                && toolset.editorPlatform.valueOrNull?.toolKind != ToolKind.None
            version <= KnownMgcbVersions.V_380 ->
                toolset.editorLauncher.valueOrNull?.toolKind != ToolKind.None
            else -> false
        }
    }
//    private fun isInstalledLocally() = hostModel.mgcbEditorToolSolution.valueOrNull?.toolKind != ToolKind.None
//    private fun isInstalledLocally(projectId: UUID) = hostModel.mgcbEditorToolSolution.valueOrNull?.toolKind != ToolKind.None

    fun startCheck() {
        CheckMgcbEditorInstalledCommand().executeLater {
            isMgcbEditorInstalled = it.succeeded
            EditorNotifications.updateAll()
        }
    }

    private data class MgcbPlatformSpecificToolset(
        val editorLauncher: IOptProperty<ToolDefinition>,
        val editorPlatform: IOptProperty<ToolDefinition>
    ) {
        companion object {
            fun fromCrossPlatformToolset(source: MgcbEditorToolset): MgcbPlatformSpecificToolset {
                val editorLauncher = source.editor
                val os = System.getProperty("os.name")
                val editorPlatform = when {
                    os.contains("win") ->
                        source.editorWindows
                    os.contains("nix") || os.contains("nux") || os.contains("aix") ->
                        source.editorLinux
                    os.contains("mac") ->
                        source.editorMac
                    else -> TODO()
                }

                return MgcbPlatformSpecificToolset(editorLauncher, editorPlatform)
            }
        }
    }
}