package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.backend.workspace.workspaceModel
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.model.dotNetActiveRuntimeModel
import com.jetbrains.rider.projectView.solution
import me.seclerp.rider.extensions.commandLine.*
import me.seclerp.rider.extensions.workspaceModel.containingProjectDirectory
import me.seclerp.rider.extensions.workspaceModel.containingProjectEntity
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbResolvedTool
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbToolsetHost
import me.seclerp.rider.plugins.monogame.notifications.Notifications

@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class OpenExternalEditorAction : AnAction(MonoGameIcons.MgcbFile) {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val intellijProject = actionEvent.project ?: return
        val file = actionEvent.dataContext.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val dotnetProject = intellijProject.workspaceModel.containingProjectEntity(file, intellijProject) ?: return
        val descriptor = dotnetProject.descriptor as? RdProjectDescriptor ?: return
        val dotnetProjectDirectory = intellijProject.workspaceModel.containingProjectDirectory(file, intellijProject) ?: return
        val editorTool = MgcbToolsetHost.getInstance(intellijProject).getEditorTool(descriptor)
        runEditor(intellijProject, editorTool, dotnetProjectDirectory, file)
    }

    override fun getActionUpdateThread() = ActionUpdateThread.EDT

    override fun update(actionEvent: AnActionEvent) {
        actionEvent.presentation.isEnabledAndVisible = false
        val intellijProject = actionEvent.project ?: return
        val file = actionEvent.dataContext.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val dotnetProject = intellijProject.workspaceModel.containingProjectEntity(file, intellijProject) ?: return
        val descriptor = dotnetProject.descriptor as? RdProjectDescriptor ?: return
        val mgcbEditorInstalled = MgcbToolsetHost.getInstance(intellijProject).areToolsAvailable(descriptor)
        actionEvent.presentation.isVisible = true
        actionEvent.presentation.isEnabled = mgcbEditorInstalled
        actionEvent.presentation.text =
            if (mgcbEditorInstalled)
                MonoGameUiBundle.message("command.mgcb.open.title")
            else
                MonoGameUiBundle.message("command.mgcb.open.missing.editor.title")
    }

    // .NET tools have different rules for running local and global tools.
    // For global tool, it should be executed as a stand-alone program, like that:
    // > mgcb-editor
    // For local tool, it should be executed as a sub-command for 'dotnet', like that:
    // > dotnet mgcb-editor
    // or in more explicit way:
    // > dotnet tool run mgcb-editor
    // Source: https://learn.microsoft.com/en-us/dotnet/core/tools/global-tools#use-a-tool
    private fun runEditor(intellijProject: Project, editorTool: MgcbResolvedTool, projectDirectory: VirtualFile, contentFile: VirtualFile) {
        fun CommandBuilder.configureEditorCommand() {
            workingDirectory(projectDirectory.path)
            param(contentFile.path)
        }

        fun getToolset(): DotNetToolset {
            return when {
                // MGCB Editor doesn't work on Apple Silicon
                SystemInfo.isMac && SystemInfo.isAarch64 -> {
                    if (intellijProject.solution.dotNetActiveRuntimeModel.activeRuntime.valueOrNull?.nonNativeDotNetCliExePaths?.x64.isNullOrEmpty()) {
                        Notifications.notifyX64ToolsetRequired(intellijProject)
                        DotNetToolset.NATIVE
                    } else {
                        DotNetToolset.X64
                    }
                }
                else -> DotNetToolset.NATIVE
            }
        }

        val command =
            when (editorTool) {
                is MgcbResolvedTool.Local -> buildDotnetCommand(intellijProject, editorTool.definition.commandName, toolset = getToolset()) { configureEditorCommand() }
                is MgcbResolvedTool.Global -> buildCommand(editorTool.definition.commandName) { configureEditorCommand() }
                is MgcbResolvedTool.None -> null
            }

        DefaultCommandExecutor.getInstance(intellijProject).execute(command ?: return)
    }
}