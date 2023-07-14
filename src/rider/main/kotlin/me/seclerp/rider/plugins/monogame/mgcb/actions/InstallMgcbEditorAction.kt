package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CliCommandResult
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.InstallMgcbEditorCommand
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.RegisterMgcbEditorCommand
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbToolsetHost

class InstallMgcbEditorAction : AnAction() {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        if (actionEvent.project != null) {
            install(actionEvent.project!!)
        }
    }

    private fun install(project: Project) {
        val installCommand = InstallMgcbEditorCommand()
        installCommand.executeUnderProgress(project, "Installing mgcb-editor...") {
            processResult(project, it) { register(project) }
        }
    }

    private fun register(project: Project) {
        val registerCommand = RegisterMgcbEditorCommand()
        registerCommand.executeUnderProgress(project, "Registering mgcb-editor...") {
            processResult(project, it) { notifySuccess(project) }
        }
    }

    private fun processResult(project: Project, result: CliCommandResult, onSuccess: () -> Unit) {
        if (result.succeeded) {
            onSuccess()
        } else {
            notifyError(project, result.error ?: "")
        }
    }

    private fun notifySuccess(project: Project) {
        // Force re-check to re-enable buttons
        MgcbToolsetHost.getInstance(project).startCheck()

        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(KnownNotificationGroups.monoGameRider)
            .createNotification("MGCB Editor has beed successfully installed", NotificationType.INFORMATION)
            .notify(project)
    }

    private fun notifyError(project: Project, message: String) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(KnownNotificationGroups.monoGameRider)
            .createNotification("MGCB Editor install failed", NotificationType.ERROR)
            .setContent(message)
            .notify(project)
    }
}