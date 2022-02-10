package me.seclerp.rider.plugins.monogame.mgcb.previewer

import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotifications
import com.jetbrains.rider.util.idea.getService
import me.seclerp.rider.plugins.monogame.mgcb.MgcbFileType
import me.seclerp.rider.plugins.monogame.mgcb.services.MgcbEditorCheckService
import me.seclerp.rider.plugins.monogame.mgcb.state.MgcbEditorGlobalStateService

class MgcbEditorNotificationProvider : EditorNotifications.Provider<MgcbEditorNotificationPanel>(), DumbAware {
    private val mgcbEditorCheckService = service<MgcbEditorCheckService>()

    override fun getKey(): Key<MgcbEditorNotificationPanel> = KEY

    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project
    ): MgcbEditorNotificationPanel? {
        if (!FileTypeRegistry.getInstance().isFileOfType(file, MgcbFileType.Instance)) {
            return null
        }

        val stateService = project.getService<MgcbEditorGlobalStateService>()
        if (stateService.state.installNotificationClosed) {
            return null
        }

        if (fileEditor.getUserData(HIDDEN_KEY) != null) {
            return null
        };

        if (mgcbEditorCheckService.isInstalled()) {
            return null
        }

        val panel = MgcbEditorNotificationPanel(fileEditor)
        panel.text = "Seems like MGCB Editor global tool is missing, opening in external editor disabled"
        panel.createActionLabel("Install") {
            panel.install()
            fileEditor.putUserData(HIDDEN_KEY, "true")
            EditorNotifications.updateAll()
        }
        panel.createActionLabel("Hide") {
            stateService.state.installNotificationClosed = true
            fileEditor.putUserData(HIDDEN_KEY, "true")
            EditorNotifications.updateAll()
        }
        return panel
    }

    companion object {
        private val KEY = Key.create<MgcbEditorNotificationPanel>("MgcbEditorInstall.Panel")
        private val HIDDEN_KEY = Key.create<String>("MgcbEditorInstall.Panel.Hidden")
    }
}