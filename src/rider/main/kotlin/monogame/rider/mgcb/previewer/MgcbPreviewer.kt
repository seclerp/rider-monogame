package monogame.rider.mgcb.previewer

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
import monogame.rider.mgcb.psi.*
import java.beans.PropertyChangeListener
import javax.swing.JPanel
import javax.swing.JTree

class MgcbPreviewer(
    private val project: Project,
    private val currentFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

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
        val buildOptionNodes =
            PsiTreeUtil.getChildrenOfType(mgcbFile, MgcbOption::class.java)
                ?.filter { it.getKey() == "/build" }
                ?.map { MgcbBuildEntryNode(it.getValue() ?: "") }

        buildOptionNodes?.forEach { rootDirectoryNode.add(it) }

        val tree = Tree(rootDirectoryNode)
        tree.cellRenderer = MgcbNodeRenderer()
        tree.isRootVisible = false
        return tree
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