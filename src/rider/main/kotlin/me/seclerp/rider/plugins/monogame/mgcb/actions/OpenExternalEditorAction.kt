package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.workspaceModel.ide.workspaceModel
import com.jetbrains.rider.model.RdProjectDescriptor
import me.seclerp.rider.extensions.commandLine.DefaultCommandExecutor
import me.seclerp.rider.extensions.commandLine.buildDotnetCommand
import me.seclerp.rider.extensions.workspaceModel.containingProjectDirectory
import me.seclerp.rider.extensions.workspaceModel.containingProjectEntity
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbToolsetHost

@Suppress("DialogTitleCapitalization", "UnstableApiUsage")
class OpenExternalEditorAction : AnAction(MonoGameIcons.MgcbFile) {
    override fun actionPerformed(actionEvent: AnActionEvent) {
        val intellijProject = actionEvent.project ?: return
        val file = actionEvent.dataContext.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val dotnetProject = intellijProject.workspaceModel.containingProjectEntity(file, intellijProject) ?: return
        val descriptor = dotnetProject.descriptor as? RdProjectDescriptor ?: return
        val dotnetProjectDirectory = intellijProject.workspaceModel.containingProjectDirectory(file, intellijProject) ?: return
        val editorCommand = MgcbToolsetHost.getInstance(intellijProject).getEditorTool(descriptor)?.commandName ?: return
        val command = buildDotnetCommand(intellijProject, editorCommand) {
            workingDirectory(dotnetProjectDirectory.path)
            param(file.path)
        }
        DefaultCommandExecutor.getInstance(intellijProject).execute(command)
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
}