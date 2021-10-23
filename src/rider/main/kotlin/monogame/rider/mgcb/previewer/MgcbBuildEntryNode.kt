package monogame.rider.mgcb.previewer

import com.intellij.icons.AllIcons
import com.intellij.util.PlatformIcons
import javax.swing.Icon
import javax.swing.tree.DefaultMutableTreeNode

abstract class MgcbTreeNode(name: String, val icon: Icon) : DefaultMutableTreeNode(name)

class MgcbBuildEntryNode(name: String) : MgcbTreeNode(name, AllIcons.FileTypes.Text)
class MgcbFolderNode(name: String) : MgcbTreeNode(name, PlatformIcons.FOLDER_ICON)