package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.MgcbEditorCommand
import me.seclerp.rider.plugins.monogame.mgcb.services.MgcbEditorCheckService

@Suppress("DialogTitleCapitalization")
class OpenExternalEditorAction(
    private val project: Project,
    private val mgcbFile: VirtualFile
) : AnAction("Open in external MGCB editor", "Open in external MGCB editor", MonoGameIcons.MgcbFile) {
    private val checkService by lazy { MgcbEditorCheckService.getInstance(project) }

    override fun actionPerformed(actionEvent: AnActionEvent) {
        MgcbEditorCommand(mgcbFile.path, project).executeLater()
    }

    override fun update(actionEvent: AnActionEvent) {
        val mgcbEditorInstalled = checkService.isInstalled()
        actionEvent.presentation.isVisible = true
        actionEvent.presentation.isEnabled = mgcbEditorInstalled
        actionEvent.presentation.text =
            if (mgcbEditorInstalled)
                "Open in external MGCB editor"
            else
                "mgcb-editor global tool is not installed"
    }
}