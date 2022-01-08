package me.seclerp.rider.plugins.monogame.mgcb.actions.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service
class MgcbEditorCheckService(private val project: Project) {
    private var isMgcbEditorInstalled = false

    fun isInstalled() = isMgcbEditorInstalled

    fun checkInstalled() {
        // TODO Implement checking using --help probing
//        executeCommandUnderProgress(project, "Checking for MGCB installation...") {
//            val command = CheckMgcbEditorInstalledCommand()
//            val result = command.execute()
//
//            if (!result.succeeded) {
//
//            }
//        }

        isMgcbEditorInstalled = true
    }
}