package me.seclerp.rider.plugins.monogame.templates

import com.jetbrains.rider.projectView.projectTemplates.providers.ProjectTemplateCustomizer

class MonoGameProjectTemplateCustomizer : ProjectTemplateCustomizer {
    private val monoGameTemplateTypes = setOf(MonoGameTemplateType())
    override fun getCustomProjectTemplateTypes() = monoGameTemplateTypes
}