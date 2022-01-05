package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbPreviewerTopics

class MgcbFileListener(
    project: Project,
) : BulkFileListener {
    private val publisher = project.messageBus.syncPublisher(MgcbPreviewerTopics.MGCB_PENDING_UPDATE_TOPIC)

    override fun after(events: MutableList<out VFileEvent>) {
        val mgcbFiles = events
            .filter { it.file?.extension == "mgcb" }
            .mapNotNull { it.file }

        for (file in mgcbFiles) {
            publisher.handle(file)
        }
    }
}