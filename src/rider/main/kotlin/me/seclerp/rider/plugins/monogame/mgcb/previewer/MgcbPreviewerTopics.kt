package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.util.messages.Topic
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbPendingUpdateListener
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbProcessedUpdateListener

object MgcbPreviewerTopics {
    @JvmField
    @Topic.ProjectLevel
    val MGCB_PENDING_UPDATE_TOPIC = Topic.create("MgcbPendingUpdateTopic", MgcbPendingUpdateListener::class.java)

    @JvmField
    @Topic.ProjectLevel
    val MGCB_PROCESSED_UPDATE_TOPIC = Topic.create("MgcbProcessedUpdateTopic", MgcbProcessedUpdateListener::class.java)
}