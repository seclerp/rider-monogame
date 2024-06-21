package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import me.seclerp.rider.plugins.monogame.mgcb.previewer.listeners.MgcbDocumentListener

class MgcbEditorWithPreview(
    private val project: Project,
    private val file: VirtualFile,
    textEditor: TextEditor,
    previewer: MgcbEditorPreviewer
) : TextEditorWithPreview(textEditor, previewer, MgcbEditorWithPreview::javaClass.name, Layout.SHOW_EDITOR_AND_PREVIEW) {
    private val documentListener = MgcbDocumentListener(project, file)

    init {
        textEditor.editor.document.addDocumentListener(documentListener)
    }

    override fun isShowFloatingToolbar() = false

    override fun dispose() {
        textEditor.editor.document.removeDocumentListener(documentListener)

        super.dispose()
    }
}