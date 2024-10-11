package me.seclerp.rider.plugins.monogame.mgcb.editor.listeners

import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.mgcb.editor.MgcbIntermediateModel
import me.seclerp.rider.plugins.monogame.mgcb.editor.MgcbPreviewerTopics
import java.util.*

interface MgcbProcessedUpdateListener : EventListener {
    companion object {
        @JvmField
        val TOPIC = MgcbPreviewerTopics.MGCB_PROCESSED_UPDATE_TOPIC
    }

    fun handle(file: VirtualFile, mgcbIntermediateModel: MgcbIntermediateModel) {}
}
