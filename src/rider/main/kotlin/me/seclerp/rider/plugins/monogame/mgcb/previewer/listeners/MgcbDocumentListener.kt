package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups

class MgcbDocumentListener(private val project: Project) : DocumentListener {
    override fun documentChanged(event: DocumentEvent) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(KnownNotificationGroups.mgcbFileChanges)
            .createNotification("[Document] MGCB file changed", "Old: ${event.oldFragment}, New: ${event.newFragment}", NotificationType.INFORMATION)
            .notify(project)
    }
}