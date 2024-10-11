package me.seclerp.rider.plugins.monogame.notifications

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object Notifications {
    private object KnownNotificationGroups {
        val monoGameRider = "MonoGameRider"
    }

    fun notifyX64ToolsetRequired(project: Project) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(KnownNotificationGroups.monoGameRider)
            .createNotification(
                "X64 .NET Runtime may be required to launch MGCB Editor",
                "Please install x64 .NET Runtime to run mgcb-editor on macOS with Apple Silicon chip.",
                NotificationType.WARNING
            )
            .notify(project)
    }
}