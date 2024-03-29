package me.seclerp.rider.plugins.monogame.settings

import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.options.ConfigurableProvider
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.platform.backend.workspace.workspaceModel
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.platform.util.lifetime
import com.jetbrains.rd.util.reactive.AddRemove
import com.jetbrains.rd.util.reactive.map
import com.jetbrains.rider.model.RdProjectDescriptor
import com.jetbrains.rider.projectView.workspace.findProjects
import me.seclerp.rider.extensions.observables.RdObservableProperty
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.ToolDefinition
import me.seclerp.rider.plugins.monogame.mgcb.toolset.MgcbToolsetHost
import java.util.UUID

class MgcbToolsetConfigurableProvider(private val project: Project) : ConfigurableProvider() {
    override fun createConfigurable() = MgcbToolsetConfigurable(project)

    @Suppress("UnstableApiUsage")
    class MgcbToolsetConfigurable(private val project: Project) : SearchableConfigurable {
        private val lifetime = project.lifetime.createNested()
        private val fallbackProperty by lazy { AtomicProperty(MonoGameUiBundle.message("loading.title")) }
        private val mgcbToolsetHost by lazy { MgcbToolsetHost.getInstance(project) }

        private val globalToolsetInfo by lazy {
            if (project.isDefault) fallbackProperty
            else mgcbToolsetHost.globalToolset.editor
                .map { HtmlBuilder().append(getPresentableToolsetInfo(it)).wrapWith("code").wrapWith("html").toString() }
                .let { RdObservableProperty(it, lifetime) }
        }

        private val solutionToolsetInfo by lazy {
            if (project.isDefault) fallbackProperty
            else mgcbToolsetHost.solutionToolset.editor
                .map { HtmlBuilder().append(getPresentableToolsetInfo(it)).wrapWith("code").wrapWith("html").toString() }
                .let { RdObservableProperty(it, lifetime) }
        }

        private val projectsToolsetInfo by lazy {
            if (project.isDefault) fallbackProperty
            else {
                val toolMap = mutableMapOf<UUID, RdObservableProperty<String>>()
                val property = AtomicProperty(MonoGameUiBundle.message("loading.title"))
                mgcbToolsetHost.projectsToolset
                    .adviseAddRemove(lifetime) { event, key, value ->
                        when (event) {
                            AddRemove.Add -> {
                                value.editor.view(lifetime) { lifetime, editor ->
                                    val toolsetInfo = RdObservableProperty(value.editor.map(::getPresentableToolsetInfo), lifetime)
                                    toolMap[key] = toolsetInfo
                                    property.set(getPresentableProjectsInfo(toolMap))
                                    lifetime.onTermination { toolMap.remove(key) }
                                }
                            }

                            AddRemove.Remove -> {
                                toolMap.remove(key)
                            }
                        }

                        property.set(getPresentableProjectsInfo(toolMap))
                    }
                property
            }
        }

        @Suppress("DialogTitleCapitalization")
        override fun createComponent(): DialogPanel {
            return panel {
                group(MonoGameUiBundle.message("settings.mgcb.editor.group")) {
                    row(MonoGameUiBundle.message("settings.mgcb.editor.group.global")) {
                        label(MonoGameUiBundle.message("loading.title"))
                            .bindText(globalToolsetInfo)
                    }
                    row(MonoGameUiBundle.message("settings.mgcb.editor.group.solution")) {
                        label(MonoGameUiBundle.message("loading.title"))
                            .bindText(solutionToolsetInfo)
                    }
                    row(MonoGameUiBundle.message("settings.mgcb.editor.group.project")) {
                        label(MonoGameUiBundle.message("loading.title"))
                            .bindText(projectsToolsetInfo)
                    }
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

        private fun getPresentableToolsetInfo(editor: ToolDefinition?) =
            getPresentableToolInfo("mgcb-editor", editor)

        private fun getPresentableToolInfo(fallbackName: String, tool: ToolDefinition?) = buildString {
            val name = tool?.packageId ?: fallbackName
            append("$name: ")
            if (tool == null) {
                append("Not found")
            } else {
                append(tool.version)
            }
        }

        private fun getProjectName(id: UUID) = project.workspaceModel
            .findProjects()
            .mapNotNull { it.descriptor as? RdProjectDescriptor }
            .firstOrNull { it.originalGuid == id }
            ?.name ?: "Unknown"

        private fun getPresentableProjectsInfo(toolMap: MutableMap<UUID, RdObservableProperty<String>>): String = toolMap
            .map { "${getProjectName(it.key)}: ${it.value.get()}" }
            .joinToString("\n")
            .let { HtmlBuilder().append(it).wrapWith("code").wrapWith("html").toString() }
    }
}