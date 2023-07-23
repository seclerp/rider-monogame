package me.seclerp.rider.plugins.monogame.mgcb.toolset

import me.seclerp.rider.plugins.monogame.ToolDefinition

sealed class MgcbResolvedTool {
    class Local(val definition: ToolDefinition) : MgcbResolvedTool()
    class Global(val definition: ToolDefinition) : MgcbResolvedTool()
    object None : MgcbResolvedTool()
}

fun MgcbResolvedTool.isNone() = this is MgcbResolvedTool.None
fun MgcbResolvedTool.isNotNone() = this !is MgcbResolvedTool.None