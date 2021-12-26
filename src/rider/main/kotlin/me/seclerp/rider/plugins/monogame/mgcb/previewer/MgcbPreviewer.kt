package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.impl.ToolWindowHeader
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.UIUtil
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbOption
import me.seclerp.rider.plugins.monogame.mgcb.psi.getKey
import me.seclerp.rider.plugins.monogame.mgcb.psi.getValue
import me.seclerp.rider.plugins.monogame.substringAfterLast
import me.seclerp.rider.plugins.monogame.substringBeforeLast
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JPanel
import javax.swing.table.JTableHeader

class MgcbPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    private val delimiterRegex = Regex("[/\\\\]")

    private val previewerPanel = lazy {
        val root = JBSplitter(false, 0.5f)
        val entriesTree = getBuildEntriesTreePanel()
        val propertiesPanel = getPropertiesPanel()

        root.firstComponent = entriesTree
        root.secondComponent = propertiesPanel
        root.dividerWidth = 3
        root
    }

    private fun getBuildEntriesTreePanel(): JPanel {
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

        val container = JPanel(BorderLayout())
        container.add(tree, BorderLayout.CENTER)
        return container
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

    private fun getPropertiesPanel(): JPanel {
        val splitter = JBSplitter(true, 0.75f)

        val propertiesModel = KeyValueModel(mutableListOf(
            Pair("Name", "wood.png"),
            Pair("Content Path", "Textures/wood"),
            Pair("Build Path", "Textures/wood.png"),
            Pair("Importer", "TextureImporter"),
            Pair("Processor", "TextureProcessor")
        ))

        val propertiesTable = JBTable(propertiesModel)
        propertiesTable.tableHeader = JTableHeader(propertiesTable.columnModel)
        propertiesTable.tableHeader.reorderingAllowed = false;
        val propertiesScrollPane = JBScrollPane(propertiesTable)
        val propertiesPanel = JPanel(BorderLayout())
        propertiesPanel.add(JBLabel("Properties"), BorderLayout.NORTH)
        propertiesPanel.add(propertiesScrollPane, BorderLayout.CENTER)

        val processorParamsModel = KeyValueModel(mutableListOf(Pair("ColorKeyEnabled", "false")))
        val processorParamsTable = JBTable(processorParamsModel)
        processorParamsTable.tableHeader = JTableHeader(processorParamsTable.columnModel)
        processorParamsTable.tableHeader.reorderingAllowed = false;
        val processorParamsScrollPane = JBScrollPane(processorParamsTable)
        val processorParamsPanel = JPanel(BorderLayout())
        processorParamsPanel.add(JBLabel("Processor parameters"), BorderLayout.NORTH)
        processorParamsPanel.add(processorParamsScrollPane, BorderLayout.CENTER)

        splitter.firstComponent = propertiesPanel
        splitter.secondComponent = processorParamsPanel
        splitter.dividerWidth = 3

        return splitter
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