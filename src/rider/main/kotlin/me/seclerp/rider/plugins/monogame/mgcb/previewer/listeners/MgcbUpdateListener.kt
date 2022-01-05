package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbPreviewerTopics
import java.util.*

interface MgcbUpdateListener : EventListener {
    companion object {
        @JvmField
        val TOPIC = MgcbPreviewerTopics.MGCB_PENDING_UPDATE_TOPIC
    }

    fun beforeUpdate(file: VirtualFile): Unit {
    }

    fun afterUpdate(file: VirtualFile): Unit {
    }
}

class MgcbUpdateListenerImpl(
    private val project: Project
) : MgcbUpdateListener {
    override fun afterUpdate(file: VirtualFile) {
        NotificationGroupManager
            .getInstance()
            .getNotificationGroup(KnownNotificationGroups.mgcbFileChanges)
            .createNotification("[Update Pending] MGCB file changed", file.path, NotificationType.INFORMATION)
            .notify(project)
    }
}