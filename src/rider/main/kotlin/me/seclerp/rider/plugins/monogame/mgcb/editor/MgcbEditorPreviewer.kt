package me.seclerp.rider.plugins.monogame.mgcb.editor

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.ui.JBColor
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.JBUI
import me.seclerp.rider.plugins.monogame.mgcb.editor.actions.MgcbEditorActionGroup
import me.seclerp.rider.plugins.monogame.mgcb.editor.listeners.MgcbProcessedUpdateListener
import me.seclerp.rider.plugins.monogame.mgcb.editor.properties.MgcbProperty
import me.seclerp.rider.plugins.monogame.mgcb.editor.properties.MgcbPropertyTableModel
import me.seclerp.rider.plugins.monogame.mgcb.editor.properties.MgcbPropertyTable
import me.seclerp.rider.plugins.monogame.mgcb.editor.services.MgcbIntermediateModelProvider
import me.seclerp.rider.plugins.monogame.mgcb.editor.services.MgcbIntermediateModelApplier
import me.seclerp.rider.plugins.monogame.mgcb.editor.tree.MgcbBuildEntryNode
import me.seclerp.rider.plugins.monogame.mgcb.editor.tree.MgcbTree
import me.seclerp.rider.plugins.monogame.mgcb.editor.tree.MgcbTreeNode
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbFile
import me.seclerp.rider.plugins.monogame.removeAllRows
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JPanel

class MgcbEditorPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile,
) : UserDataHolderBase(), FileEditor {

    private val mgcbModelApplier = MgcbIntermediateModelApplier.getInstance(project)

    private var model: MgcbIntermediateModel? = null
    private val tree = MgcbTree()

    private val propertiesModel = MgcbPropertyTableModel()
    private val processorParamsModel = MgcbPropertyTableModel()

    private val connection = project.messageBus.connect()

    private val previewerPanel by lazy {
        val root = JBSplitter(false, 0.5f).apply {
            dividerWidth = 2
            val schema = EditorColorsManager.getInstance().globalScheme
            divider.background = JBColor.lazy {
                schema.getColor(EditorColors.PREVIEW_BORDER_COLOR) ?: schema.defaultBackground
            }
        }

        val mgcbFile = PsiManager.getInstance(project).findFile(currentFile) as MgcbFile
        model = MgcbIntermediateModelProvider.getInstance(project).analyzeFile(mgcbFile)

        val entriesTree = getBuildEntriesTreePanel()
        val propertiesPanel = getPropertiesPanel()

        connection.subscribe(MgcbPreviewerTopics.MGCB_PROCESSED_UPDATE_TOPIC, object : MgcbProcessedUpdateListener {
            override fun handle(file: VirtualFile, mgcbIntermediateModel: MgcbIntermediateModel) {
                if (file == currentFile) {
                    applyMgcbModel(mgcbIntermediateModel)
                }
            }
        })

        root.apply {
            firstComponent = entriesTree
            secondComponent = propertiesPanel
        }
    }

    private val container: JPanel by lazy {
        JPanel(BorderLayout()).apply {
            add(createActionToolbar().component, BorderLayout.NORTH)
            add(previewerPanel, BorderLayout.CENTER)
        }
    }

    private fun createActionToolbar() =
        ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, MgcbEditorActionGroup(), true)

    private fun applyMgcbModel(mgcbIntermediateModel: MgcbIntermediateModel) {
        mgcbModelApplier.updateTree(mgcbIntermediateModel, tree)
    }

    private fun getBuildEntriesTreePanel(): JPanel {
        mgcbModelApplier.updateTree(model!!, tree)

        tree.addTreeSelectionListener {
            selectedNodeChanged(tree.lastSelectedPathComponent as? MgcbTreeNode)
        }

        return JBUI.Panels.simplePanel(tree)
    }

    private fun getPropertiesPanel(): JPanel {
        val propertiesPanel = createKeyValueTable("Properties", propertiesModel)
        val processorParamsPanel = createKeyValueTable("Processor parameters", processorParamsModel)

        return JBSplitter(true, 0.75f).apply {
            firstComponent = propertiesPanel
            secondComponent = processorParamsPanel
            dividerWidth = 2
            val schema = EditorColorsManager.getInstance().globalScheme
            divider.background = JBColor.lazy {
                schema.getColor(EditorColors.PREVIEW_BORDER_COLOR) ?: schema.defaultBackground
            }
        }
    }

    private fun selectedNodeChanged(node: MgcbTreeNode?) {
        propertiesModel.removeAllRows()
        processorParamsModel.removeAllRows()

        when(node) {
            is MgcbBuildEntryNode -> {
                propertiesModel.apply {
                    addRow(MgcbProperty("Name", node.userObject.toString()))
                    addRow(MgcbProperty("Source Path", node.buildEntry.contentFilepath ?: ""))
                    addRow(MgcbProperty("Destination Path", node.buildEntry.destinationFilepath ?: node.buildEntry.contentFilepath ?: ""))
                    addRow(MgcbProperty("Importer", node.buildEntry.importer ?: ""))
                    addRow(MgcbProperty("Processor", node.buildEntry.processor ?: ""))
                }

                processorParamsModel.apply {
                    node.buildEntry.processorParams.forEach {
                        addRow(MgcbProperty(it.key, it.value))
                    }
                }
            }
        }
    }

    private fun createKeyValueTable(label: String, model: MgcbPropertyTableModel): JPanel {
        val propertiesTable = MgcbPropertyTable(model).apply {
            border = JBUI.Borders.empty(JBUI.insets(JBUIScale.scale(8)))
        }
        val title = JBLabel(label).apply {
            border = JBUI.Borders.empty(JBUI.insets(JBUIScale.scale(8)))
        }

        return JBUI.Panels.simplePanel(JBScrollPane(propertiesTable))
            .addToTop(title)
    }

    override fun getComponent() = container

    override fun getPreferredFocusedComponent() = component
    override fun getName(): String = "MGCB Editor"
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