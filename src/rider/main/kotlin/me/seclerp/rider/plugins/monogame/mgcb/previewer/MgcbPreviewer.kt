package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.ui.treeStructure.Tree
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbDocumentListener
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbFileListener
import me.seclerp.rider.plugins.monogame.mgcb.previewer.properties.KeyValueModel
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.MgcbBuildEntryNode
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.MgcbFolderNode
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.MgcbNodeRenderer
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.MgcbTreeNode
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbOption
import me.seclerp.rider.plugins.monogame.removeAllRows
import me.seclerp.rider.plugins.monogame.substringAfterLast
import me.seclerp.rider.plugins.monogame.substringBeforeLast
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JPanel
import javax.swing.table.JTableHeader

class MgcbPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile,
    private val document: Document
) : UserDataHolderBase(), FileEditor {

    private val delimiterRegex = Regex("[/\\\\]")

    private var configuration: MgcbConfiguration? = null
    private val propertiesModel = KeyValueModel()
    private val processorParamsModel = KeyValueModel()

    private val previewerPanel = lazy {
        val root = JBSplitter(false, 0.5f)

        val mgcbFile = PsiManager.getInstance(project).findFile(currentFile) as MgcbFile
        val mgcbOptions = PsiTreeUtil.getChildrenOfType(mgcbFile, MgcbOption::class.java)
        configuration = getMgcbConfiguration(mgcbOptions)

        val entriesTree = getBuildEntriesTreePanel()
        val propertiesPanel = getPropertiesPanel()

        document.addDocumentListener(MgcbDocumentListener(project, currentFile))

        root.firstComponent = entriesTree
        root.secondComponent = propertiesPanel
        root.dividerWidth = 3
        root
    }

    private fun getMgcbConfiguration(mgcbOptions: Array<MgcbOption>?): MgcbConfiguration {
        val visitor = MgcbConfigurationVisitor()
        mgcbOptions?.forEach { it.accept(visitor) }

        return visitor.configuration
    }

    private fun getBuildEntriesTreePanel(): JPanel {
        val rootDirectoryNode = MgcbFolderNode("Content")
        val buildPaths =
            configuration?.buildEntries
                ?: emptyList()

        // 1. Get all parent folders per each path
        val parentsPerPath = buildPaths.map { getParentsHierarchy(it) }

        // 2. Merge + distinct
        val flatten = parentsPerPath.flatten().distinctBy { it.first }

        // 3. Get full parent paths
        val pathsWithParents = flatten.map { Triple(getParentPath(it.first), it.first, it.second) }

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
        tree.addTreeSelectionListener { selectedNodeChanged(tree.lastSelectedPathComponent as MgcbTreeNode) }

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
    private fun getParentsHierarchy(entry: BuildEntry) : List<Pair<String, BuildEntry>> {
        val normalizedPath = entry.contentFilepath!!.trim { it.toString().matches(delimiterRegex) }
        val pathParts = normalizedPath.split(delimiterRegex).toTypedArray()

        return mutableListOf<Pair<String, BuildEntry>>().apply {
            val aggregatedPath: StringBuilder = StringBuilder(normalizedPath.length)
            for (i in 0 until pathParts.count()) {
                val part = pathParts[i]
                if (i > 0) {
                    aggregatedPath.append("/")
                }
                val newParent = aggregatedPath.append(part).toString()
                add(Pair(newParent, entry))
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

    private fun buildNodeCache(pathsWithParents: List<Triple<String, String, BuildEntry>>): Map<String, Pair<BuildEntry?, List<String>>> {
        val nodeCache = mutableMapOf<String, Pair<BuildEntry?, MutableList<String>>>()
        for ((parent, path, entry) in pathsWithParents) {
            if (!nodeCache.containsKey(parent)) {
                nodeCache[parent] = Pair(null, mutableListOf())
            }

            nodeCache[path] = Pair(entry, mutableListOf())
            nodeCache[parent]!!.second.add(path)
        }

        return nodeCache
    }

    private fun createNodeFrom(path: String, cache: Map<String, Pair<BuildEntry?, List<String>>>): MgcbTreeNode {
        if (cache[path] == null) {
            // TODO
            throw Exception()
        }

        val cachedValue = cache[path]!!
        if (cachedValue.second.isEmpty()) {
            return MgcbBuildEntryNode(path.substringAfterLast(delimiterRegex), cachedValue.first!!)
        }

        val children = cachedValue.second.map { createNodeFrom(it, cache) }
        val folderNode = MgcbFolderNode(path.substringAfterLast(delimiterRegex))
        for (child in children) {
            folderNode.add(child)
        }

        return folderNode
    }

    private fun getPropertiesPanel(): JPanel {
        val splitter = JBSplitter(true, 0.75f)

        val propertiesTable = JBTable(propertiesModel)
        propertiesTable.tableHeader = JTableHeader(propertiesTable.columnModel)
        propertiesTable.tableHeader.reorderingAllowed = false;
        val propertiesScrollPane = JBScrollPane(propertiesTable)
        val propertiesPanel = JPanel(BorderLayout())
        propertiesPanel.add(JBLabel("Properties"), BorderLayout.NORTH)
        propertiesPanel.add(propertiesScrollPane, BorderLayout.CENTER)

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

    private fun selectedNodeChanged(node: MgcbTreeNode) {
        propertiesModel.removeAllRows()
        processorParamsModel.removeAllRows()

        when(node) {
            is MgcbBuildEntryNode -> {
                propertiesModel.addRow(Pair("Name", node.userObject.toString()))
                propertiesModel.addRow(Pair("Source Path", node.buildEntry.contentFilepath!!))
                propertiesModel.addRow(Pair("Destination Path", node.buildEntry.destinationFilepath ?: node.buildEntry.contentFilepath!!))
                propertiesModel.addRow(Pair("Importer", node.buildEntry.importer!!))
                propertiesModel.addRow(Pair("Processor", node.buildEntry.processor!!))

                node.buildEntry.processorParams.forEach {
                    processorParamsModel.addRow(Pair(it.key, it.value))
                }
            }
        }
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