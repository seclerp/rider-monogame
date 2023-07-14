package me.seclerp.rider.plugins.monogame.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.dsl.builder.panel
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle

class MonoGameSettingsConfigurable : SearchableConfigurable {
    override fun createComponent() = panel {
        row {
            label(MonoGameUiBundle.message("configurable.group.tools.monogame.settings.description"))
        }
    }

    override fun isModified() = false

    override fun apply() {}

    override fun getDisplayName() = MonoGameUiBundle.message("configurable.group.tools.monogame.settings.display.name")

    override fun getId() = "tools.monogame"
}