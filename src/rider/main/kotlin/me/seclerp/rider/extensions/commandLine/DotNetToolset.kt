package me.seclerp.rider.extensions.commandLine

import com.intellij.openapi.project.Project
import com.jetbrains.rider.model.dotNetActiveRuntimeModel
import com.jetbrains.rider.projectView.solution

enum class DotNetToolset {
    NATIVE,
    X64,
    X86
}

internal fun DotNetToolset.resolveExecutable(project: Project): String? {
    val activeRuntime = project.solution.dotNetActiveRuntimeModel.activeRuntime.valueOrNull ?: return null
    return when (this) {
        DotNetToolset.NATIVE -> activeRuntime.dotNetCliExePath
        DotNetToolset.X64 -> activeRuntime.nonNativeDotNetCliExePaths?.x64
        DotNetToolset.X86 -> activeRuntime.nonNativeDotNetCliExePaths?.x86
    }
}