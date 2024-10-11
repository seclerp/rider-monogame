package me.seclerp.rider.plugins.monogame.mgcb.editor.listeners

import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.mgcb.editor.MgcbPreviewerTopics
import java.util.*

interface MgcbPendingUpdateListener : EventListener {
    companion object {
        @JvmField
        val TOPIC = MgcbPreviewerTopics.MGCB_PENDING_UPDATE_TOPIC
    }

    fun handle(file: VirtualFile) {}
}

