package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import java.nio.charset.Charset

class CheckMgcbEditorInstalledCommand(project: Project) : CliCommand(
    GeneralCommandLine("mgcb-editor", "--help")
        .withCharset(Charset.forName("UTF-8"))
)