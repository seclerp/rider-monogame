package monogame.rider.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import monogame.rider.mgcb.MgcbFileType

class MgcbPreviewerEditorProvider : FileEditorProvider, DumbAware {
    override fun getEditorTypeId() = "MgcbEditorWithPreviewer"
    override fun getPolicy() = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun accept(project: Project, file: VirtualFile) =
        file.fileType is MgcbFileType

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val textEditor = TextEditorProvider.getInstance().createEditor(project, file) as TextEditor
        val previewEditor = MgcbPreviewer(project, file)

        return MgcbPreviewerEditor(textEditor, previewEditor)
    }
}