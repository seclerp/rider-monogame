package me.seclerp.rider.extensions.commandLine

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project

abstract class CliCommandExecutor(
    protected val intellijProject: Project
) {
    abstract fun execute(command: GeneralCommandLine)
}