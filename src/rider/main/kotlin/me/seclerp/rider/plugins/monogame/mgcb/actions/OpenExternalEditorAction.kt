package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.backend.workspace.workspaceModel
import com.jetbrains.rider.model.RdProjectDescriptor
import me.seclerp.rider.extensions.commandLine.CommandBuilder
import me.seclerp.rider.extensions.commandLine.DefaultCommandExecutor
import me.seclerp.rider.extensions.commandLine.buildCommand
import me.seclerp.rider.extensions.commandLine.buildDotnetCommand
import me.seclerp.rider.extensions.workspaceModel.containingProjectDirectory
import me.seclerp.rider.extensions.workspaceModel.containingProjectEntity
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbResolvedTool
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbToolsetHost
import kotlin.io.path.Path
import kotlin.io.path.isRegularFile
import kotlin.io.path.nameWithoutExtension

@Suppress("DialogTitleCapitalization")
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

        // Whenever the exact tool location is known, prefer running it directly through the .NET CLI.
        // That is exactly what the .NET CLI itself does under the hood, and unlike the rules described
        // above, it doesn't depend on the environment at all: neither on PATH (global tools), nor on the
        // working directory being covered by the tool manifest (local tools).
        fun buildExecCommand(executablePath: String) =
            buildDotnetCommand(intellijProject, "exec") {
                param(resolveEditorAssembly(executablePath))
                configureEditorCommand()
            }

        val command =
            when (editorTool) {
                is MgcbResolvedTool.Local ->
                    editorTool.definition.executablePath?.let { buildExecCommand(it) }
                        ?: buildDotnetCommand(intellijProject, editorTool.definition.commandName) { configureEditorCommand() }
                is MgcbResolvedTool.Global ->
                    editorTool.definition.executablePath?.let { buildExecCommand(it) }
                        ?: buildCommand {
                            executable(editorTool.definition.commandName)
                            configureEditorCommand()
                        }
                is MgcbResolvedTool.None -> null
            }

        DefaultCommandExecutor.getInstance(intellijProject).executeDetached(command ?: return)
    }

    // The entry point of the 'mgcb-editor-*' tools is just a launcher: it spawns the real editor
    // application, which is shipped next to it inside the '<tool>-data' folder, and exits immediately
    // with a success code, hiding any failure of the editor itself.
    // That application is spawned through its own framework-dependent application host, which locates
    // the .NET runtime on its own, relying on DOTNET_ROOT or on one of the default install locations.
    // Neither of them is guaranteed to be there: the IDE doesn't necessarily pass the shell environment
    // to child processes, and the SDK might be installed into a custom location.
    // Running the editor assembly directly through the known 'dotnet' executable skips the application
    // host entirely, so the runtime is always resolved, and makes the editor process the one being
    // tracked, so its failures are actually visible.
    private fun resolveEditorAssembly(executablePath: String): String {
        val executable = Path(executablePath)
        val name = executable.nameWithoutExtension
        val editorAssembly = executable.resolveSibling("$name-data").resolve("$name.dll")

        return if (editorAssembly.isRegularFile()) editorAssembly.toString() else executablePath
    }
}