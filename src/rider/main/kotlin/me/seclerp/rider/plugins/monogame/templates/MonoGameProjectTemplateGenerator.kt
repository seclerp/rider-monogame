package me.seclerp.rider.plugins.monogame.templates

import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import com.intellij.ui.components.panels.NonOpaquePanel
import com.intellij.ui.components.panels.VerticalLayout
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.panel
import com.intellij.util.ui.JBUI
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rider.model.RdProjectTemplate
import com.jetbrains.rider.projectView.projectTemplates.NewProjectDialogContext
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesSharedModel
import com.jetbrains.rider.projectView.projectTemplates.generators.TypeListBasedProjectTemplateGenerator
import me.seclerp.rider.extensions.ClipboardUtil
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import me.seclerp.rider.plugins.monogame.templates.MonoGameTemplateMetadata.Names
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.JLabel
import javax.swing.SwingConstants

internal class MonoGameProjectTemplateGenerator(
    lifetime: Lifetime,
    context: NewProjectDialogContext,
    sharedModel: ProjectTemplatesSharedModel,
    projectTemplates: IOptProperty<Set<RdProjectTemplate>>
) : TypeListBasedProjectTemplateGenerator(lifetime, context, sharedModel, projectTemplates) {
    init {
        context.isReady.afterChange {
            val templates = tryGetAcceptableTemplates()
            when {
                it && templates.isNullOrEmpty() -> setTemplatesMissingState()
                it && !templates.isNullOrEmpty() -> setReadyState()
                else -> setLoadingState()
            }
        }
    }

    override val defaultName = "MonoGameProject1"

    override fun getPredefinedTypes() = listOf(
        TemplateTypeWithIcon(Names.CROSS_PLATFORM_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.WINDOWS_DESKTOP_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.ANDROID_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.IOS_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.GAME_LIB, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.CONTENT_PIPELINE_EXTENSION, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.SHARED_LIB, MonoGameIcons.MgcbFile),
    )

    override fun getType(template: RdProjectTemplate) = template.name
        .removePrefix("MonoGame ")
        .replace("Application", "App")

    private var myLoadingRow: Row? = null
    private var myTemplatesMissingRow: Row? = null
    private var myTemplatesRow: Row? = null

    override fun createTemplateSpecificPanelAfterLanguage(): DialogPanel {
        return panel {
            myLoadingRow = createLoadingRow().apply { visible(false)}
            myTemplatesMissingRow = createMissingTemplatesRow().apply { visible(false)}
            myTemplatesRow = createReadyRow().apply { visible(false)}
            setLoadingState()
        }
    }

    private fun setLoadingState() {
        myTemplatesRow?.visible(false)
        myTemplatesMissingRow?.visible(false)
        myLoadingRow?.visible(true)
    }

    private fun setReadyState() {
        myLoadingRow?.visible(false)
        myTemplatesMissingRow?.visible(false)
        myTemplatesRow?.visible(true)
    }

    private fun setTemplatesMissingState() {
        myLoadingRow?.visible(false)
        myTemplatesRow?.visible(false)
        myTemplatesMissingRow?.visible(true)
    }

    private fun Panel.createLoadingRow() = row(" ") {
        cell(JLabel(MonoGameUiBundle.message("templates.loading.templates"), AnimatedIcon.Default(), SwingConstants.LEFT))
    }

    private fun Panel.createReadyRow() = row {
        cell(super.createTemplateSpecificPanelAfterLanguage())
    }

    private fun Panel.createMissingTemplatesRow() = row(" ") {
        val installCommand = "dotnet new install MonoGame.Templates.CSharp"
        cell(CustomNotificationPanel(EditorNotificationPanel.Status.Warning))
            .applyToComponent {
                text(MonoGameUiBundle.message("templates.no.templates.found"))
                addBottomItem(JLabel(MonoGameUiBundle.message("templates.no.templates.install.hint")).apply {
                    border = JBUI.Borders.emptyTop(8)
                })
                addBottomItem(CodeSnippetTextField(installCommand, 50))
            }
            .resizableColumn()
        topGap(TopGap.SMALL)
    }

    private class CustomNotificationPanel(status: Status) : EditorNotificationPanel(status) {
        private val myBottomPanel = NonOpaquePanel(VerticalLayout(8, VerticalLayout.FILL))

        init {
            add(myBottomPanel, BorderLayout.SOUTH)
        }

        fun addBottomItem(comp: Component) {
            myBottomPanel.add(comp)
        }
    }

    private class CodeSnippetTextField(text: String, columns: Int = 20) : ExtendableTextField(text, columns) {
        init {
            isEditable = false
            font = EditorColorsManager.getInstance().globalScheme.getFont(EditorFontType.PLAIN)
            addExtension(CopyToClipboardExtension())
        }

        private inner class CopyToClipboardExtension : ExtendableTextComponent.Extension {
            override fun getIcon(hovered: Boolean) = AllIcons.General.Copy

            override fun getActionOnClick() = Runnable {
                ClipboardUtil.copyToClipboard(this@CodeSnippetTextField.text)
            }
        }
    }
}