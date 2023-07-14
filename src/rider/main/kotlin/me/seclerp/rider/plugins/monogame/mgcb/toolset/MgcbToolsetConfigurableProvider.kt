package me.seclerp.rider.plugins.monogame.mgcb.toolset

import com.intellij.openapi.Disposable
import com.intellij.openapi.observable.properties.ObservableProperty
import com.intellij.openapi.options.ConfigurableProvider
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.createLifetime
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IPropertyView
import com.jetbrains.rd.util.reactive.compose
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.ToolKind

class RdObservableProperty<T>(private val property: IPropertyView<T>, private val propertyLifetime: Lifetime) : ObservableProperty<T> {
    override fun get() = property.value

    override fun afterChange(parentDisposable: Disposable?, listener: (T) -> Unit) {
        val lifetime = parentDisposable?.createLifetime() ?: propertyLifetime
        property.change.advise(lifetime, listener)
    }
}

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

        override fun createComponent() = panel {
            row("Global tools") {
                label("")
                    .bindText(globalToolsetInfo)
            }
            row("Solution-level tools") {
                label("")
                    .bindText(solutionToolsetInfo)
            }
        }

        override fun isModified(): Boolean {
            return false
        }

        override fun apply() {
            // TODO
        }

        override fun getDisplayName() = MonoGameUiBundle.message("configurable.group.tools.monogame.mgcb.display.name")
        override fun getId() = "monogame.tools.mgcb"

        private fun getPresentableToolsetInfo(editorLauncher: ToolDefinition?, platformDefinition: ToolDefinition?) = buildString {
            append(getPresentableToolInfo("mgcb-editor", editorLauncher))
            if (editorLauncher != null && editorLauncher.toolKind == ToolKind.None && platformDefinition != null) {
                append(", ")
                append(getPresentableToolInfo(platformDefinition.packageId, editorLauncher))
            }
        }

        private fun getPresentableToolInfo(name: String, tool: ToolDefinition?) = buildString {
            append("$name: ")
            if (tool == null || tool.toolKind == ToolKind.None) {
                append("Not found")
            } else {
                append(tool.version)
            }
        }
    }
}