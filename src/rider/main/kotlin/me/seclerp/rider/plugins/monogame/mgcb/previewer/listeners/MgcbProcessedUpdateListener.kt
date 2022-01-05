package me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners

import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbModel
import me.seclerp.rider.plugins.monogame.mgcb.previewer.MgcbPreviewerTopics
import java.util.*

interface MgcbProcessedUpdateListener : EventListener {
    companion object {
        @JvmField
        val TOPIC = MgcbPreviewerTopics.MGCB_PROCESSED_UPDATE_TOPIC
    }

    fun handle(file: VirtualFile, mgcbModel: MgcbModel) {}
}
