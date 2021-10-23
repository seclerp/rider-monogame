package monogame.rider.mgcb.previewer

import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview

class MgcbPreviewerEditor(editor: TextEditor, previewer: MgcbPreviewer)
    : TextEditorWithPreview(editor, previewer, MgcbPreviewerEditor::javaClass.name, Layout.SHOW_EDITOR_AND_PREVIEW) {
}