package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

@Suppress("UnstableApiUsage")
open class CliCommand(
    private val command: GeneralCommandLine,
) {
    fun executeLater(listener: (CliCommandResult) -> Unit = {}) {
        try {
            val handler = OSProcessHandler(command)
            handler.addProcessListener(MgcbProcessAdapter(command.commandLineString, listener))
            handler.startNotify()
        } catch(e: Exception) {
            e.printStackTrace()
            listener(CliCommandResult(command.commandLineString, -1, e.toString(), false))
        }
    }

    fun executeUnderProgress(project: Project, title: String, listener: (CliCommandResult) -> Unit = {}) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(project, title, false) {
            override fun run(progress: ProgressIndicator) {
                try {
                    val output = ExecUtil.execAndGetOutput(command)
                    val commandResult = CliCommandResult(command.commandLineString, output.exitCode, output.stdout, output.exitCode == 0, output.stderr)
                    listener(commandResult)
                } catch(e: Exception) {
                    e.printStackTrace()
                    listener(CliCommandResult(command.commandLineString, -1, e.toString(), false))
                }
            }
        })
    }
}