package me.seclerp.rider.extensions.commandLine

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.jetbrains.rd.util.string.printToString
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import java.io.BufferedReader
import java.io.IOException
import java.util.concurrent.TimeUnit

@Service
class DefaultCommandExecutor(
    intellijProject: Project
) : CliCommandExecutor(intellijProject) {
    companion object {
        fun getInstance(project: Project) = project.service<DefaultCommandExecutor>()
    }
    private val logger = logger<DefaultCommandExecutor>()

    override fun execute(command: GeneralCommandLine) {
        runBackgroundableTask(MonoGameUiBundle.message("command.execution.title"), intellijProject, false) {
            try {
                command.toProcessBuilder()
                val process = command
                    .toProcessBuilder()
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .start()
                process.waitFor(30, TimeUnit.MINUTES)
                val exitCode = process.exitValue()
                val output = process.inputStream.bufferedReader().readText()
                val error = process.errorStream.bufferedReader().readText()

//                val executionResult = ExecUtil.execAndGetOutput(command)
//                val output = executionResult.stdout
//                val error = executionResult.stderr
//                val exitCode = executionResult.exitCode
                if (exitCode != 0) {
                    failed(command.commandLineString, output, error, exitCode)
                }
            } catch (e: Exception) {
                failed(command.commandLineString, e)
            }
        }
    }

    private fun failed(command: String, exception: Exception) {
        logger.error(buildString {
            append("Command '$command' failed\n")
            append("\tException: ${exception.stackTraceToString()}")
        })
        Messages.showErrorDialog(
            MonoGameUiBundle.message("command.execution.error.message.exception", exception.message ?: ""),
            MonoGameUiBundle.message("command.execution.error.title"))
    }

    private fun failed(command: String, stdout: String, stderr: String, exitCode: Int) {
        logger.error(buildString {
            append("Command '$command' failed with exit code $exitCode\n")
            append("\tSTDOUT: $stdout\n")
            append("\tSTDERR: $stderr")
        })
        Messages.showErrorDialog(
            MonoGameUiBundle.message("command.execution.error.message.code", exitCode),
            MonoGameUiBundle.message("command.execution.error.title"))
    }
}