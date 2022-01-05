package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.jetbrains.rider.util.idea.getService
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbProcessedUpdateListener
import me.seclerp.rider.plugins.monogame.mgcb.previewer.properties.KeyValueModel
import me.seclerp.rider.plugins.monogame.mgcb.previewer.services.MgcbAnalyzer
import me.seclerp.rider.plugins.monogame.mgcb.previewer.services.MgcbBuildTreeManager
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.MgcbBuildEntryNode
import me.seclerp.rider.plugins.monogame.mgcb.previewer.tree.MgcbTreeNode
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.removeAllRows
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JPanel
import javax.swing.table.JTableHeader

class MgcbEditorPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile,
) : UserDataHolderBase(), FileEditor {

    private val analyzerService = project.getService<MgcbAnalyzer>()
    private val mgcbTreeService = project.getService<MgcbBuildTreeManager>()

    private var model: MgcbModel? = null
    private val tree = mgcbTreeService.createEmpty()

    private val propertiesModel = KeyValueModel()
    private val processorParamsModel = KeyValueModel()

    private val connection = project.messageBus.connect()

    private val previewerPanel = lazy {
        val root = JBSplitter(false, 0.5f)

        val mgcbFile = PsiManager.getInstance(project).findFile(currentFile) as MgcbFile
        model = analyzerService.analyzeFile(mgcbFile)

        val entriesTree = getBuildEntriesTreePanel()
        val propertiesPanel = getPropertiesPanel()

        connection.subscribe(MgcbPreviewerTopics.MGCB_PROCESSED_UPDATE_TOPIC, object : MgcbProcessedUpdateListener {
            override fun handle(file: VirtualFile, mgcbModel: MgcbModel) {
                if (file == currentFile) {
                    applyMgcbModel(mgcbModel)

                    NotificationGroupManager.getInstance()
                        .getNotificationGroup(KnownNotificationGroups.mgcbFileChanges)
                        .createNotification("[Update Processed] MGCB previewer updated", file.path,
                            NotificationType.INFORMATION
                        )
                        .notify(project)
                }
            }
        })

        root.firstComponent = entriesTree
        root.secondComponent = propertiesPanel
        root.dividerWidth = 3
        root
    }

    private fun applyMgcbModel(mgcbModel: MgcbModel) {
        mgcbTreeService.updateExisting(mgcbModel, tree)
    }

    private fun getBuildEntriesTreePanel(): JPanel {
        mgcbTreeService.populateEmpty(model!!, tree)
        tree.addTreeSelectionListener {
            val mgcbTreeNode = tree.lastSelectedPathComponent as? MgcbTreeNode
            if (mgcbTreeNode != null) {
                selectedNodeChanged(mgcbTreeNode)
            }
        }

        val container = JPanel(BorderLayout())
        container.add(tree, BorderLayout.CENTER)
        return container
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

    override fun dispose() {
        connection.dispose()
    }
}