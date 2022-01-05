package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.codeInsight.lookup.Lookup
import com.intellij.codeInsight.lookup.LookupManagerListener
import com.intellij.util.messages.Topic
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbUpdateListener

object MgcbPreviewerTopics {
    @JvmField
    @Topic.ProjectLevel
    val MGCB_PENDING_UPDATE_TOPIC = Topic.create("MgcbPendingUpdateTopic", MgcbUpdateListener::class.java)
}