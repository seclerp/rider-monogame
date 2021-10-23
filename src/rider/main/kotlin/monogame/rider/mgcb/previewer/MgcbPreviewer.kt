package monogame.rider.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class MgcbPreviewer(
    project: Project,
    private val currentFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    override fun getComponent(): JComponent {
        // TODO
        return JPanel()
    }

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