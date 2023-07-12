package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import com.intellij.icons.AllIcons
import me.seclerp.rider.plugins.monogame.mgcb.previewer.BuildEntry
import javax.swing.Icon

class MgcbBuildEntryNode(name: String, icon: Icon?, val buildEntry: BuildEntry)
    : MgcbTreeNode(name, icon ?: AllIcons.FileTypes.Text)