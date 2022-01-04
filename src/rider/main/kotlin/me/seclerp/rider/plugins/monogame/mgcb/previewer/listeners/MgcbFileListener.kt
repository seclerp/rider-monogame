package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups

class MgcbFileListener(
    private val project: Project,
    private val file: VirtualFile
) : BulkFileListener {
    override fun after(events: MutableList<out VFileEvent>) {
        val currentFileEvents = events.filter { it.file == file }

        val eventsCount = currentFileEvents.count()

        if (eventsCount > 0) {
            NotificationGroupManager
                .getInstance()
                .getNotificationGroup(KnownNotificationGroups.mgcbFileChanges)
                .createNotification("[VFS] MGCB file changed", "${currentFileEvents.count()} events fired", NotificationType.INFORMATION)
                .notify(project)
        }
    }
}