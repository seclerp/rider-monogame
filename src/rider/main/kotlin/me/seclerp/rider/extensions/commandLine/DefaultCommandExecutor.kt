package me.seclerp.rider.extensions.commandLine

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.concurrency.AppExecutorUtil
import me.seclerp.rider.plugins.monogame.KnownNotificationGroups
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import java.util.concurrent.TimeUnit

@Service(Service.Level.PROJECT)
class DefaultCommandExecutor(
    intellijProject: Project
) : CliCommandExecutor(intellijProject) {
    companion object {
        fun getInstance(project: Project) = project.service<DefaultCommandExecutor>()

        // Detached commands are expected to outlive the launch, so only an exit happening right after
        // the start is treated as a launch failure worth reporting to the user.
        private const val DETACHED_LAUNCH_FAILURE_WINDOW_MS = 30_000L
        private const val DETACHED_OUTPUT_LIMIT = 8 * 1024
        private const val DETACHED_ERROR_MESSAGE_LIMIT = 1024
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

    override fun executeDetached(command: GeneralCommandLine) {
        val process =
            try {
                command
                    .toProcessBuilder()
                    .redirectErrorStream(true)
                    .start()
            } catch (e: Exception) {
                failed(command.commandLineString, e)
                return
            }

        val startedAt = System.currentTimeMillis()
        logger.info("Executing detached command '${command.commandLineString}' in '${command.workDirectory}' (PID ${process.pid()})")

        AppExecutorUtil.getAppExecutorService().execute {
            val output = drain(process)
            val exitCode = process.waitFor()
            val elapsedMs = System.currentTimeMillis() - startedAt

            // The output is always reported, even for a successful exit: tools like the MGCB editor are
            // launched through a stub process which exits with 0 immediately after spawning the real
            // application, so its failures would be lost otherwise.
            val report = buildString {
                append("Detached command '${command.commandLineString}' (PID ${process.pid()}) exited with code $exitCode in $elapsedMs ms")
                if (output.isNotBlank())
                    append("\n\tOUTPUT: ${output.trim()}")
            }

            if (exitCode == 0)
                logger.info(report)
            else
                logger.warn(report)

            // The command has been running for a while, so it was launched successfully and the user
            // is not waiting for any feedback anymore - don't distract them with a notification.
            if (exitCode == 0 || elapsedMs > DETACHED_LAUNCH_FAILURE_WINDOW_MS)
                return@execute

            val message =
                if (output.isBlank())
                    MonoGameUiBundle.message("command.execution.error.message.code", exitCode)
                else
                    MonoGameUiBundle.message("command.execution.error.message.detached", exitCode, asHtml(output.trim().take(DETACHED_ERROR_MESSAGE_LIMIT)))

            notifyFailed(message)
        }
    }

    // The stream has to be drained until the end even when the output is not needed anymore, otherwise
    // a long-running detached process may block on a full pipe.
    private fun drain(process: Process): String {
        val captured = StringBuilder()
        process.inputStream.bufferedReader().use { reader ->
            val buffer = CharArray(DEFAULT_BUFFER_SIZE)
            while (true) {
                val count = reader.read(buffer)
                if (count < 0)
                    break

                val available = DETACHED_OUTPUT_LIMIT - captured.length
                if (available > 0)
                    captured.appendRange(buffer, 0, minOf(count, available))
            }
        }

        return captured.toString()
    }

    private fun failed(command: String, exception: Exception) {
        logger.error(buildString {
            append("Command '$command' failed\n")
            append("\tException: ${exception.stackTraceToString()}")
        })
        notifyFailed(MonoGameUiBundle.message("command.execution.error.message.exception", exception.message ?: ""))
    }

    private fun failed(command: String, stdout: String, stderr: String, exitCode: Int) {
        logger.error(buildString {
            append("Command '$command' failed with exit code $exitCode\n")
            append("\tSTDOUT: $stdout\n")
            append("\tSTDERR: $stderr")
        })
        notifyFailed(MonoGameUiBundle.message("command.execution.error.message.code", exitCode))
    }

    private fun asHtml(text: String) = StringUtil.escapeXmlEntities(text).replace("\n", "<br/>")

    private fun notifyFailed(message: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(KnownNotificationGroups.monoGameRider)
            .createNotification(MonoGameUiBundle.message("command.execution.error.title"), message, NotificationType.ERROR)
            .notify(intellijProject)
    }
}