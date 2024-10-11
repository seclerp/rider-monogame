package me.seclerp.rider.plugins.monogame.mgcb.editor.tree

import com.intellij.ui.treeStructure.Tree
import javax.swing.tree.DefaultTreeModel
import javax.swing.tree.TreePath

class MgcbTree(val root: MgcbFolderNode = MgcbFolderNode("Content")) : Tree(root) {
    init {
        expandsSelectedPaths = true
        cellRenderer = MgcbNodeRenderer()
        isRootVisible = true
    }

    fun reload() {
        this.treeDidChange()
        (model as DefaultTreeModel).reload()
    }

    fun clear() {
        root.removeAllChildren()
    }

    fun getExpandedUrls(): Set<String> {
        val expanded = mutableListOf<TreePath>()
        for (i in 0 until rowCount - 1) {
            val currentPath: TreePath = getPathForRow(i)
            val nextPath: TreePath = getPathForRow(i + 1)
            if (currentPath.isDescendant(nextPath)) {
                expanded.add(currentPath)
            }
        }

        return expanded.map { treePath -> treePath.path.joinToString("/") { it.toString() } }.toSet()
    }

    fun getSelectedUrl(): String? {
        return selectionPath?.path?.joinToString("/") { it.toString() }
    }

    fun restoreExpandedUrl(urls: Set<String>) {
        restoreExpandedInternal(root, urls)
    }

    fun restoreSelectedUrl(url: String) {
        selectionPath = null
        restoreSelectedUrlInternal(root, url)
    }

    private fun restoreExpandedInternal(node: MgcbTreeNode, urls: Set<String>) {
        for (i in 0 until node.childCount) {
            val childNode = node.getChildAt(i) as MgcbTreeNode
            val url = childNode.path.joinToString("/") { it.toString() }

            if (urls.contains(url)) {
                val path = TreePath(childNode.path)
                expandPath(path)
            }

            if (childNode.childCount > 0) {
                restoreExpandedInternal(childNode, urls)
            }
        }
    }

    private fun restoreSelectedUrlInternal(node: MgcbTreeNode, url: String) {
        val nodeUrl = node.path.joinToString("/") { it.toString() }

        if (nodeUrl == url) {
            selectionPath = TreePath(node.path)
            return
        }

        for (i in 0 until node.childCount) {
            val childNode = node.getChildAt(i) as MgcbTreeNode

            restoreSelectedUrlInternal(childNode, url)
        }
    }
}