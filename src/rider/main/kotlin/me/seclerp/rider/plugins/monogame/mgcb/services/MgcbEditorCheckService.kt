package me.seclerp.rider.plugins.monogame.mgcb.services

import com.intellij.openapi.components.Service
import com.intellij.ui.EditorNotifications
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CheckMgcbEditorInstalledCommand

@Service
class MgcbEditorCheckService {
    private var isMgcbEditorInstalled = false

    fun isInstalled() = isMgcbEditorInstalled

    fun startCheck() {
        CheckMgcbEditorInstalledCommand().executeLater {
            isMgcbEditorInstalled = it.succeeded
            EditorNotifications.updateAll()
        }
    }
}