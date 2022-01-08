package me.seclerp.rider.plugins.monogame.mgcb.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MgcbPreviewerActionGroup(
    project: Project,
    mgcbFile: VirtualFile
) : ActionGroup() {
    private val actions = arrayOf<AnAction>(OpenExternalEditorAction(project, mgcbFile))

    override fun getChildren(p0: AnActionEvent?): Array<AnAction> = actions
    override fun canBePerformed(context: DataContext): Boolean = true
}