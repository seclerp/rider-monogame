package me.seclerp.rider.plugins.monogame.mgcb.services

import com.intellij.openapi.application.ex.ApplicationUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import me.seclerp.rider.plugins.monogame.mgcb.actions.commands.CheckMgcbEditorInstalledCommand

@Service
class MgcbEditorCheckService(private val project: Project) {
    private var isMgcbEditorInstalled = false

    fun isInstalled() = isMgcbEditorInstalled

    fun checkInstalled() {
        ApplicationUtil.tryRunReadAction {
            val command = CheckMgcbEditorInstalledCommand()
            val result = command.execute()
            isMgcbEditorInstalled = result.succeeded
        }
    }
}