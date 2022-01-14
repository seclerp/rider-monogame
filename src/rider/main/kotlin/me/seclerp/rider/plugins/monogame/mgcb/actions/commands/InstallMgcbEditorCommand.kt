package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine

class InstallMgcbEditorCommand : CliCommand(
    GeneralCommandLine("dotnet", "tool", "install", "-g", "dotnet-mgcb-editor"))