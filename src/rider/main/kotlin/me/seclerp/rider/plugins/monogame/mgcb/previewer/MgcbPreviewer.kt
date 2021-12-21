package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBSplitter
import java.beans.PropertyChangeListener
import javax.swing.JPanel


class MgcbPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    private val delimiterRegex = Regex("[/\\\\]")

    private val previewerPanel = lazy {
        val root = JBSplitter(false, 0.5f)
        root.firstComponent = JPanel()//getBuildEntriesTree()
        root.secondComponent = JPanel()
        root.dividerWidth = 3
        root
    }
//
//    private fun getBuildEntriesTree(): JTree {
//        val mgcbFile = PsiManager.getInstance(project).findFile(currentFile) as MgcbFile
//
//        val nodeCache = mutableMapOf<String, MgcbTreeNode>()
//        val rootDirectoryNode = MgcbFolderNode("Content")
//        val buildOptionValues =
//            PsiTreeUtil.getChildrenOfType(mgcbFile, MgcbOption::class.java)
//                ?.filter { it.getKey() == "/build" }
//                ?.mapNotNull { it.getValue() }
//                ?: emptyList()
//
//        for (option in buildOptionValues) {
//            process(rootDirectoryNode, option, nodeCache)
//        }
//
//        val tree = Tree(rootDirectoryNode)
//        tree.cellRenderer = MgcbNodeRenderer()
//        tree.isRootVisible = false
//        return tree
//    }
//
//    private fun process(parent: MgcbTreeNode, childPath: String, nodeCache: MutableMap<String, MgcbTreeNode>) {
//        val headNode = createChildNodeIfNeeded(parent, childPath, nodeCache)
//
//        if (headNode.parent != parent) {
//            parent.add(headNode)
//        }
//
//        if (!childPath.contains(delimiterRegex)) {
//            return
//        }
//
//        val tail = childPath.substringAfter(delimiterRegex, childPath)
//
//        process(headNode, tail, nodeCache)
//    }
//
//    private fun createChildNodeIfNeeded(parent: MgcbTreeNode, parentPath: String, childPath: String, nodeCache: MutableMap<String, MgcbTreeNode>): MgcbTreeNode {
//        val fullPath = "${parent.path}$childPath"
//        return if (nodeCache.containsKey(fullPath)) {
//            nodeCache[fullPath]!!
//        } else {
//            val headName = childPath.substringBefore(delimiterRegex, childPath)
//            val headNodeInTree = parent.children().takeIf { name == headName } as MgcbTreeNode?
//
//            if (headNodeInTree == null) {
//                val newNode = if (childPath.contains(delimiterRegex)) MgcbFolderNode(headName) else MgcbBuildEntryNode(headName)
//                nodeCache[fullPath] = newNode
//                newNode
//            } else {
//                nodeCache[fullPath] = headNodeInTree
//                headNodeInTree
//            }
//        }
//    }
////
//    private fun findNode(model: DefaultTreeModel, path: String): DefaultMutableTreeNode? {
//        val node = model.root as DefaultMutableTreeNode
//        val parts = path.split("/".toRegex()).toTypedArray()
//        return if (node.userObject.toString() == parts[0]) {
//            findNode(node, parts.copyOfRange(1, parts.size))
//        } else null
//    }
//
//    private fun findNode(node: DefaultMutableTreeNode, path: Array<String>): DefaultMutableTreeNode? {
//        if (path.isEmpty()) {
//            return node
//        }
//        val children: Enumeration<TreeNode> = node.children()
//        while (children.hasMoreElements()) {
//            val child = children.nextElement() as DefaultMutableTreeNode
//            if (child.userObject.toString() == path[0]) {
//                return findNode(child, path.copyOfRange(1, path.size))
//            }
//        }
//        return null
//    }

    override fun getComponent() = previewerPanel.value

    override fun getPreferredFocusedComponent() = component
    override fun getName(): String = "MGCB Preview"
    override fun isModified() = false
    override fun isValid() = true

    override fun setState(p0: FileEditorState) {}
    override fun addPropertyChangeListener(p0: PropertyChangeListener) {}
    override fun removePropertyChangeListener(p0: PropertyChangeListener) {}
    override fun getCurrentLocation(): FileEditorLocation? = null
    override fun dispose() {}
}