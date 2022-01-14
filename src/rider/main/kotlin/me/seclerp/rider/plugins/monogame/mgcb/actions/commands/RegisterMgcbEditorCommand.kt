package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine

class RegisterMgcbEditorCommand : CliCommand(GeneralCommandLine("mgcb-editor", "--register"))