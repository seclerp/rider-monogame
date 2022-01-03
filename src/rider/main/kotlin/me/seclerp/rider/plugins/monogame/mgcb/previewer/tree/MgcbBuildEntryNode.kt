package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.util.PlatformIcons
import me.seclerp.rider.plugins.monogame.mgcb.previewer.BuildEntry
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

abstract class MgcbTreeNode(name: String, val icon: Icon) : DefaultMutableTreeNode(name)

class MgcbBuildEntryNode(name: String, val buildEntry: BuildEntry)
    : MgcbTreeNode(name, IconLoader.findIcon(name) ?: AllIcons.FileTypes.Text)

class MgcbFolderNode(name: String)
    : MgcbTreeNode(name, PlatformIcons.FOLDER_ICON)