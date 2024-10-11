package me.seclerp.rider.plugins.monogame.mgcb.editor

import com.intellij.util.messages.Topic
import me.seclerp.rider.plugins.monogame.mgcb.editor.listeners.MgcbPendingUpdateListener
import me.seclerp.rider.plugins.monogame.mgcb.editor.listeners.MgcbProcessedUpdateListener

object MgcbPreviewerTopics {
    @JvmField
    @Topic.ProjectLevel
    val MGCB_PENDING_UPDATE_TOPIC = Topic.create("MgcbPendingUpdateTopic", MgcbPendingUpdateListener::class.java)

    @JvmField
    @Topic.ProjectLevel
    val MGCB_PROCESSED_UPDATE_TOPIC = Topic.create("MgcbProcessedUpdateTopic", MgcbProcessedUpdateListener::class.java)
}