package me.seclerp.rider.extensions.commandLine

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.jetbrains.rider.model.dotNetActiveRuntimeModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.run.FormatPreservingCommandLine
import com.jetbrains.rider.run.withRawParameters
import org.jetbrains.annotations.NonNls
import java.nio.charset.Charset

open class CommandBuilder(vararg baseCommands: @NonNls String) {
    @NonNls
    protected var generalCommandLine =
        FormatPreservingCommandLine()
            .withRawParameters(baseCommands.joinToString(" "))
            .withCharset(Charset.forName("UTF-8"))

    fun executable(executable: String) {
        generalCommandLine = generalCommandLine.withExePath(executable)
    }

    fun workingDirectory(workingDirectory: String) {
        generalCommandLine = generalCommandLine.withWorkDirectory(workingDirectory)
    }

    @NonNls
    fun param(value: String) {
        generalCommandLine = generalCommandLine.withParameters(value)
    }


    @NonNls
    fun param(name: String, value: String) {
        generalCommandLine = generalCommandLine.withParameters(name, value)
    }

    @NonNls
    fun nullableParam(value: String?) {
        if (value != null)
            generalCommandLine = generalCommandLine.withParameters(value)
    }

    @NonNls
    fun nullableParam(name: String, value: String?) {
        if (value != null)
            generalCommandLine = generalCommandLine.withParameters(name, value)
    }

    @NonNls
    fun paramWhen(key: String, condition: Boolean) {
        if (condition)
            generalCommandLine = generalCommandLine.withParameters(key)
    }

    fun environment(key: String, value: String) {
        generalCommandLine = generalCommandLine.withEnvironment(key, value)
    }

    fun build(): GeneralCommandLine = generalCommandLine
}

fun buildDotnetCommand(project: Project, vararg baseCommands: @NonNls String, toolset: DotNetToolset = DotNetToolset.NATIVE, builder: CommandBuilder.() -> Unit = {}) =
    CommandBuilder(*baseCommands)
        .apply {
            executable(toolset.resolveExecutable(project) ?: throw Exception(".NET / .NET Core is not configured, unable to run commands."))
            workingDirectory(project.solutionDirectoryPath.toString())
            environment("DOTNET_SKIP_FIRST_TIME_EXPERIENCE", "true")
            environment("DOTNET_NOLOGO", "true")
        }
        .apply(builder)
        .build()

fun buildCommand(vararg baseCommands: @NonNls String, builder: CommandBuilder.() -> Unit = {}) =
    CommandBuilder(*baseCommands).apply(builder).build()