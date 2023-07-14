package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.jetbrains.rd.util.reactive.IProperty
import me.seclerp.rider.plugins.monogame.MgcbEditorToolset
import me.seclerp.rider.plugins.monogame.ToolDefinition

data class MgcbPlatformSpecificToolset(
    val editorLauncher: IProperty<ToolDefinition?>,
    val editorPlatform: IProperty<ToolDefinition?>
) {
    companion object {
        fun fromCrossPlatformToolset(source: MgcbEditorToolset): MgcbPlatformSpecificToolset {
            val editorLauncher = source.editor
            val os = System.getProperty("os.name").lowercase()
            val editorPlatform = when {
                os.contains("win") ->
                    source.editorWindows
                os.contains("nix") || os.contains("nux") || os.contains("aix") ->
                    source.editorLinux
                os.contains("mac") ->
                    source.editorMac
                else -> TODO()
            }

            return MgcbPlatformSpecificToolset(editorLauncher, editorPlatform)
        }
    }
}