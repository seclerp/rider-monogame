package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import me.seclerp.rider.plugins.monogame.mgcb.previewer.BuildEntry

class MgcbBuildEntryNode(name: String, val buildEntry: BuildEntry)
    : MgcbTreeNode(name, IconLoader.findIcon(name) ?: AllIcons.FileTypes.Text)