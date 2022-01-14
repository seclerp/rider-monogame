package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.ui.EditorNotificationPanel

class MgcbEditorNotificationPanel(fileEditor: FileEditor) : EditorNotificationPanel(fileEditor) {
    fun install() {
        executeAction("InstallMgcbEditorAction")
    }
}