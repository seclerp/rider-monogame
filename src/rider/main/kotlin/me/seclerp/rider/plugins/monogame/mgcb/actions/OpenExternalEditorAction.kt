package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.MgcbEditorCommand
import me.seclerp.rider.plugins.monogame.mgcb.services.MgcbEditorCheckService

@Suppress("DialogTitleCapitalization")
class OpenExternalEditorAction : AnAction("Open in external MGCB editor", "Open in external MGCB editor", MonoGameIcons.MgcbFile) {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val project = actionEvent.project ?: return
        val file = actionEvent.dataContext.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        MgcbEditorCommand(file.path, project).executeLater()
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun update(actionEvent: AnActionEvent) {
        val project = actionEvent.project ?: return
        val mgcbEditorInstalled = MgcbEditorCheckService.getInstance(project).isInstalled()
        actionEvent.presentation.isVisible = true
        actionEvent.presentation.isEnabled = mgcbEditorInstalled
        actionEvent.presentation.text =
            if (mgcbEditorInstalled)
                "Open in external MGCB editor"
            else
                "mgcb-editor global tool is not installed"
    }
}