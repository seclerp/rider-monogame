package me.seclerp.rider.plugins.monogame.mgcb.startup

import com.intellij.ide.util.RunOnceUtil
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import me.seclerp.rider.plugins.monogame.mgcb.services.MgcbEditorCheckService
import javax.swing.Timer

class CheckMgcbEditorInstalledActivity : StartupActivity, DumbAware {
    private val checkService = service<MgcbEditorCheckService>()

    override fun runActivity(project: Project) {
        RunOnceUtil.runOnceForApp("run-mgcb-editor-installation-checker") {
            val timer = Timer(5000) { checkService.startCheck() }

            timer.isRepeats = true
            timer.start()
            checkService.startCheck()
        }
    }
}