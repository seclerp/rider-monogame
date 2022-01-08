package me.seclerp.rider.plugins.monogame.mgcb.startup

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.jetbrains.rider.util.idea.getService
import me.seclerp.rider.plugins.monogame.mgcb.actions.services.MgcbEditorCheckService

class CheckMgcbEditorInstalledActivity : StartupActivity, DumbAware {
    override fun runActivity(project: Project) {
        val checkService = project.getService<MgcbEditorCheckService>()
        checkService.checkInstalled()
    }
}