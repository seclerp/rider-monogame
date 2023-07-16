package me.seclerp.rider.extensions.commandLine

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.jetbrains.rider.model.dotNetActiveRuntimeModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.run.FormatPreservingCommandLine
import com.jetbrains.rider.run.withRawParameters
import org.jetbrains.annotations.NonNls
import java.io.File
import java.nio.charset.Charset

open class DotnetCommandBuilder(private val intellijProject: Project, vararg baseCommands: @NonNls String) {
    private val activeRuntime by lazy { intellijProject.solution.dotNetActiveRuntimeModel.activeRuntime.valueOrNull }
    private val solutionDirectory = intellijProject.solutionDirectoryPath.toString()

    @NonNls
    private var generalCommandLine =
        FormatPreservingCommandLine()
            .withExePath(getDotnetExePath())
            .withRawParameters(baseCommands.joinToString(" "))
            .withCharset(Charset.forName("UTF-8"))
            .withWorkDirectory(solutionDirectory)
            .withEnvironment("DOTNET_ROOT", getDotnetRootPath())
            .withEnvironment("DOTNET_SKIP_FIRST_TIME_EXPERIENCE", "true")
            .withEnvironment("DOTNET_NOLOGO", "true")
//            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.SYSTEM)

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

    fun build(): GeneralCommandLine = generalCommandLine

    private fun getDotnetExePath() =
        activeRuntime?.dotNetCliExePath
            ?: throw Exception(".NET / .NET Core is not configured, unable to run commands.")

    private fun getDotnetRootPath() = File(getDotnetExePath()).parent
}

fun buildDotnetCommand(project: Project, vararg baseCommands: @NonNls String, builder: DotnetCommandBuilder.() -> Unit = {}) =
    DotnetCommandBuilder(project, *baseCommands).apply(builder).build()