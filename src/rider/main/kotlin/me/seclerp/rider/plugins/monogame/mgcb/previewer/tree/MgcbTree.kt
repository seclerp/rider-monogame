package me.seclerp.rider.plugins.monogame.mgcb.previewer.tree

import com.intellij.ui.treeStructure.Tree
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath

class MgcbTree(val root: MgcbFolderNode = MgcbFolderNode("Content")) : Tree(root) {
    init {
        this.expandsSelectedPaths = true
    }

    fun reload() {
        this.treeDidChange()
        (model as DefaultTreeModel).reload()
    }

    fun clear() {
        root.removeAllChildren()
    }

    fun getExpandedPaths(): Collection<TreePath> {
        val expanded = mutableListOf<TreePath>()
        for (i in 0 until rowCount - 1) {
            val currentPath: TreePath = getPathForRow(i)
            val nextPath: TreePath = getPathForRow(i + 1)
            if (currentPath.isDescendant(nextPath)) {
                expanded.add(currentPath)
            }
        }

        return expanded
    }

    fun expandNodes(treePaths: Collection<TreePath>) {
        for (path in treePaths) {
            expandPath(path)
        }
    }
}