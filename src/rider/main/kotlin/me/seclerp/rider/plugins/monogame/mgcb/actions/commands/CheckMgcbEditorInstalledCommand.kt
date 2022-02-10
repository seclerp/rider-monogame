package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

import com.intellij.execution.configurations.GeneralCommandLine
import java.nio.charset.Charset

class CheckMgcbEditorInstalledCommand : CliCommand(
    GeneralCommandLine("mgcb-editor", "--help")
        .withCharset(Charset.forName("UTF-8"))
)