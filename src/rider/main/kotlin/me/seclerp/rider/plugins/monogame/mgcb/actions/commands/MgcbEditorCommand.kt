package me.seclerp.rider.plugins.monogame.mgcb.actions.commands

class MgcbEditorCommand(mgcbFilePath: String)
    : CliCommand("mgcb-editor \"${mgcbFilePath}\"", executeDetached = true)
