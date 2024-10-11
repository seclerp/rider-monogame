package me.seclerp.rider.plugins.monogame.mgcb.editor.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MgcbEditorActionGroup : ActionGroup() {
    private val actions = arrayOf(AddItemAction())

    override fun getChildren(p0: AnActionEvent?) = actions
}

class AddItemAction : AnAction(AllIcons.Actions.AddFile) {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(p0: AnActionEvent) {}
}