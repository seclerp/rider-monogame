package me.seclerp.rider.plugins.monogame.mgcb.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CheckMgcbEditorInstalledCommand

@Service
class MgcbEditorCheckService(private val project: Project) {
    private var isMgcbEditorInstalled = false

    fun isInstalled() = isMgcbEditorInstalled

    fun checkLater() {
        CheckMgcbEditorInstalledCommand(project).executeLater {
            isMgcbEditorInstalled = it.succeeded
        }
    }
}