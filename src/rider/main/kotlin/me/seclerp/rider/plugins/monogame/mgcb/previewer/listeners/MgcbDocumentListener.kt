package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbPreviewerTopics
import javax.swing.Timer

class MgcbDocumentListener(
    project: Project,
    private val file: VirtualFile
) : DocumentListener {
    private val publisher = project.messageBus.syncPublisher(MgcbPreviewerTopics.MGCB_PENDING_UPDATE_TOPIC)
    private val timer = Timer(1000) { publisher.handle(file) }

    init {
        timer.isRepeats = false
        timer.stop()
    }

    override fun documentChanged(event: DocumentEvent) {
        timer.restart()
    }
}