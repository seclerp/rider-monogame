package me.seclerp.rider.plugins.monogame.mgcb.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.ui.EditorNotifications
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.plugins.monogame.ToolKind
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CheckMgcbEditorInstalledCommand
import me.seclerp.rider.plugins.monogame.monoGameRiderModel

@Service(Service.Level.PROJECT)
class MgcbEditorCheckService(private val intellijProject: Project) {
    companion object {
        fun getInstance(project: Project) = project.service<MgcbEditorCheckService>()
    }

    private var isMgcbEditorInstalled = false
    private val hostModel by lazy { intellijProject.solution.monoGameRiderModel }

    fun isInstalled(): Boolean {
        return isInstalledLocally() || isInstalledGlobally()
    }

    private fun isInstalledGlobally() = hostModel.mgcbEditorToolGlobal.valueOrNull?.toolKind != ToolKind.None
    private fun isInstalledLocally() = hostModel.mgcbEditorToolSolution.valueOrNull?.toolKind != ToolKind.None
//    private fun isInstalledLocally(projectId: UUID) = hostModel.mgcbEditorToolSolution.valueOrNull?.toolKind != ToolKind.None

    fun startCheck() {
        CheckMgcbEditorInstalledCommand().executeLater {
            isMgcbEditorInstalled = it.succeeded
            EditorNotifications.updateAll()
        }
    }
}