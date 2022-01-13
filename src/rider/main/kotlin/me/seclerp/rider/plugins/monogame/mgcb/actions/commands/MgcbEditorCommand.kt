package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import java.nio.charset.Charset

class MgcbEditorCommand(mgcbFilePath: String, project: Project)
    : CliCommand(GeneralCommandLine("mgcb-editor", mgcbFilePath)
                    .withCharset(Charset.forName("UTF-8")))
