package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.JBSplitter
import com.intellij.ui.treeStructure.Tree
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbOption
import me.seclerp.rider.plugins.monogame.mgcb.psi.getKey
import me.seclerp.rider.plugins.monogame.mgcb.psi.getValue
import me.seclerp.rider.plugins.monogame.substringAfterLast
import me.seclerp.rider.plugins.monogame.substringBeforeLast
import java.beans.PropertyChangeListener
import javax.swing.JPanel
import javax.swing.JTree

class MgcbPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    private val delimiterRegex = Regex("[/\\\\]")

    private val previewerPanel = lazy {
        val root = JBSplitter(false, 0.5f)
        root.firstComponent = getBuildEntriesTree()
        root.secondComponent = JPanel()
        root.dividerWidth = 3
        root
    }

    private fun getBuildEntriesTree(): JTree {
        val mgcbFile = PsiManager.getInstance(project).findFile(currentFile) as MgcbFile

        val rootDirectoryNode = MgcbFolderNode("Content")
        val buildOptionValues =
            PsiTreeUtil.getChildrenOfType(mgcbFile, MgcbOption::class.java)
                ?.filter { it.getKey() == "/build" }
                ?.mapNotNull { it.getValue() }
                ?: emptyList()

        // 1. Get all parent folders per each path
        val parentsPerPath = buildOptionValues.map { getParentsHierarchy(it) }

        // 2. Merge + distinct
        val flatten = parentsPerPath.flatten().distinct()

        // 3. Get full parent paths
        val pathsWithParents = flatten.map { Pair(getParentPath(it), it) }

        // 4. Add each item to the corresponding parent in dict
        val nodeCache = buildNodeCache(pathsWithParents)

        // 5. Process every tree node and create corresponding tree
        val topLevelNodes = pathsWithParents
            .filter { it.first == "" }
            .map { createNodeFrom(it.second, nodeCache) }

        for (node in topLevelNodes) {
            rootDirectoryNode.add(node)
        }

        val tree = Tree(rootDirectoryNode)
        tree.cellRenderer = MgcbNodeRenderer()
        tree.isRootVisible = false
        return tree
    }

    // Will transform
    // a/b/c
    //
    // To
    // a
    // a/b
    // a/b/c
    private fun getParentsHierarchy(path: String) : List<String> {
        val normalizedPath = path.trim { it.toString().matches(delimiterRegex) }
        val pathParts = normalizedPath.split(delimiterRegex).toTypedArray()

        return mutableListOf<String>().apply {
            val aggregatedPath: StringBuilder = StringBuilder(normalizedPath.length)
            for (i in 0 until pathParts.count()) {
                val part = pathParts[i]
                if (i > 0) {
                    aggregatedPath.append("/")
                }
                val newParent = aggregatedPath.append(part).toString()
                add(newParent)
            }
        }
    }

    // Will transform
    // a/b/c
    //
    // To
    //
    // a/b
    private fun getParentPath(path: String): String {
        return path.substringBeforeLast(delimiterRegex, "")
    }

    private fun buildNodeCache(pathsWithParents: List<Pair<String, String>>): Map<String, List<String>> {
        val nodeCache = mutableMapOf<String, MutableList<String>>()
        for ((parent, path) in pathsWithParents) {
            if (!nodeCache.containsKey(parent)) {
                nodeCache[parent] = mutableListOf()
            }

            nodeCache[path] = mutableListOf()
            nodeCache[parent]!!.add(path)
        }

        return nodeCache
    }

    private fun createNodeFrom(path: String, cache: Map<String, List<String>>): MgcbTreeNode {
        if (cache[path] == null) {
            // TODO
            throw Exception()
        }

        if (cache[path]!!.isEmpty()) {
            return MgcbBuildEntryNode(path.substringAfterLast(delimiterRegex))
        }

        val children = cache[path]!!.map { createNodeFrom(it, cache) }
        val folderNode = MgcbFolderNode(path.substringAfterLast(delimiterRegex))
        for (child in children) {
            folderNode.add(child)
        }

        return folderNode
    }

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