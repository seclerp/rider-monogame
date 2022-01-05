package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import com.intellij.ui.treeStructure.Tree
import javax.swing.tree.DefaultTreeModel

class MgcbTree(val root: MgcbFolderNode = MgcbFolderNode("Content")) : Tree(root) {
    fun reload() {
        (model as DefaultTreeModel).reload()
    }
}