package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project

fun executeCommandUnderProgress(
    project: Project, taskTitle: String,
    what: (Unit) -> CliCommandResult
) {
    runBackgroundableTask(taskTitle, project, false) {
        val result = what(Unit)
        if (!result.succeeded) {
            val errorTextBuilder = StringBuilder()
            errorTextBuilder.append("Command: ${result.command}")

            if (result.output.trim().isNotEmpty())
                errorTextBuilder.append("\n\nOutput:\n${result.output}")

            if (result.error?.trim()?.isNotEmpty() == true)
                errorTextBuilder.append("\n\nError:\n${result.error}")

            errorTextBuilder.append("\n\nExit code: ${result.exitCode}")
        }
    }
}