package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rider.util.idea.getService
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.MgcbEditorCommand
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.executeCommandUnderProgress
import me.seclerp.rider.plugins.monogame.mgcb.services.MgcbEditorCheckService

@Suppress("DialogTitleCapitalization")
class OpenExternalEditorAction(
    project: Project,
    private val mgcbFile: VirtualFile
) : AnAction("Open in external MGCB editor", "Open in external MGCB editor", MonoGameIcons.MgcbFile) {
    private val mgcbEditorCheckService = project.getService<MgcbEditorCheckService>()

    override fun actionPerformed(actionEvent: AnActionEvent) {
        executeCommandUnderProgress(
            actionEvent.project!!,
            "Opening external editor...",
        ) {
            val command = MgcbEditorCommand(mgcbFile.path)
            command.execute()
        }
    }

    override fun update(actionEvent: AnActionEvent) {
        val mgcbEditorInstalled = mgcbEditorCheckService.isInstalled()
        actionEvent.presentation.isVisible = true
        actionEvent.presentation.isEnabled = mgcbEditorInstalled
        actionEvent.presentation.text =
            if (mgcbEditorInstalled)
                "Open in external MGCB editor"
            else
                "mgcb-editor global tool is not installed"
    }
}