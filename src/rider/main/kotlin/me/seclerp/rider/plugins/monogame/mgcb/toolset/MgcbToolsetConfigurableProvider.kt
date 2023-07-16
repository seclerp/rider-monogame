package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.options.ConfigurableProvider
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rd.util.reactive.compose
import me.seclerp.rider.extensions.observables.RdObservableProperty
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.ToolKind

class MgcbToolsetConfigurableProvider(private val project: Project) : ConfigurableProvider() {
    override fun createConfigurable() = MgcbToolsetConfigurable(project)

    class MgcbToolsetConfigurable(project: Project) : SearchableConfigurable {
        private val lifetime = project.lifetime.createNested()
        private val mgcbToolsetHost = MgcbToolsetHost.getInstance(project)

        private val globalToolsetInfo =
            mgcbToolsetHost.globalToolset.editorLauncher
                .compose(mgcbToolsetHost.globalToolset.editorPlatform, ::getPresentableToolsetInfo)
                .let { RdObservableProperty(it, lifetime) }

        private val solutionToolsetInfo =
            mgcbToolsetHost.solutionToolset.editorLauncher
                .compose(mgcbToolsetHost.solutionToolset.editorPlatform, ::getPresentableToolsetInfo)
                .let { RdObservableProperty(it, lifetime) }

        @Suppress("DialogTitleCapitalization")
        override fun createComponent() = panel {
            group(MonoGameUiBundle.message("settings.mgcb.editor.group")) {
                row(MonoGameUiBundle.message("settings.mgcb.editor.group.global")) {
                    label("")
                        .bindText(globalToolsetInfo)
                }
                row(MonoGameUiBundle.message("settings.mgcb.editor.group.solution")) {
                    label("")
                        .bindText(solutionToolsetInfo)
                }
            }
        }

        override fun disposeUIResources() {
            lifetime.terminate()
        }

        override fun isModified(): Boolean {
            return false
        }

        override fun apply() {
            // TODO
        }

        override fun getDisplayName() = MonoGameUiBundle.message("settings.mgcb.display.name")
        override fun getId() = "monogame.tools.mgcb"

        private fun getPresentableToolsetInfo(editorLauncher: ToolDefinition?, platformDefinition: ToolDefinition?) = buildString {
            append("<html>")
            append(getPresentableToolInfo("mgcb-editor", editorLauncher))
            if (editorLauncher != null) {
                val version = DotNetToolsVersion.parse(editorLauncher.version)
                if (version != null && version >= KnownMgcbVersions.`3_8_1` && platformDefinition != null) {
                    append("<br>")
                    append(getPresentableToolInfo(platformDefinition.packageId, platformDefinition))
                }
            }
            append("</html>")
        }

        private fun getPresentableToolInfo(fallbackName: String, tool: ToolDefinition?) = buildString {
            val name = tool?.packageId ?: fallbackName
            append("$name: ")
            if (tool == null || tool.toolKind == ToolKind.None) {
                append("Not found")
            } else {
                append(tool.version)
            }
        }
    }
}