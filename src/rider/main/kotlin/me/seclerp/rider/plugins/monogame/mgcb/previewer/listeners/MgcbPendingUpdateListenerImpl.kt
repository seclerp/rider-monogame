package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.rider.util.idea.getService
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbPreviewerTopics
import me.seclerp.rider.plugins.monogame.mgcb.previewer.services.MgcbAnalyzer
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile

class MgcbPendingUpdateListenerImpl(
    private val project: Project
) : MgcbPendingUpdateListener {
    private val publisher = project.messageBus.syncPublisher(MgcbPreviewerTopics.MGCB_PROCESSED_UPDATE_TOPIC)
    private val mgcbAnalyzer = project.getService<MgcbAnalyzer>()

    override fun handle(file: VirtualFile) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(KnownNotificationGroups.mgcbFileChanges)
            .createNotification("[Update Pending] MGCB file changed, processing", file.path,
                NotificationType.INFORMATION
            )
            .notify(project)

        val mgcbFile = PsiManager.getInstance(project).findFile(file) as MgcbFile
        val mgcbModel = mgcbAnalyzer.analyzeFile(mgcbFile)

        publisher.handle(file, mgcbModel)
    }
}