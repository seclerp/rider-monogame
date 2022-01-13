package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler

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
}