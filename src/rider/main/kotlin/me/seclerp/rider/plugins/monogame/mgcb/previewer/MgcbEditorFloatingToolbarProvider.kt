package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarComponent
import com.intellij.openapi.editor.toolbar.floating.FloatingToolbarProvider
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.testFramework.LightVirtualFileBase
import me.seclerp.rider.plugins.monogame.mgcb.MgcbFileType
import me.seclerp.rider.plugins.monogame.mgcb.actions.OpenExternalEditorAction

class MgcbEditorFloatingToolbarProvider : FloatingToolbarProvider {
    override val actionGroup = DefaultActionGroup(OpenExternalEditorAction())
    override val autoHideable = false

    override fun register(dataContext: DataContext, component: FloatingToolbarComponent, parentDisposable: Disposable) {
        val editor = dataContext.getData(CommonDataKeys.EDITOR) ?: return
        if (editor.isMgcbFileEditor()) {
            component.scheduleShow()
        }
    }

    private fun Editor.isMgcbFileEditor(): Boolean {
        val documentManager = FileDocumentManager.getInstance()
        val virtualFile = documentManager.getFile(document) ?: return false
        return virtualFile !is LightVirtualFileBase
            && virtualFile.isValid
            && virtualFile.fileType == MgcbFileType
    }
}