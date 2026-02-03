package me.seclerp.rider.plugins.monogame.templates

import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rider.model.RdProjectTemplate
import com.jetbrains.rider.projectView.projectTemplates.NewProjectDialogContext
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesSharedModel
import com.jetbrains.rider.projectView.projectTemplates.components.InstallNuGetPackagesController
import com.jetbrains.rider.projectView.projectTemplates.generators.NuGetProjectTemplateGenerator
import me.seclerp.rider.plugins.monogame.MonoGameUiBundle
import org.jetbrains.annotations.Nls

internal class MonoGameProjectTemplateGenerator(
    lifetime: Lifetime,
    context: NewProjectDialogContext,
    sharedModel: ProjectTemplatesSharedModel,
    projectTemplates: IOptProperty<Set<RdProjectTemplate>>
) : NuGetProjectTemplateGenerator(lifetime, context, sharedModel, projectTemplates) {
    private val presentableTemplatesName = MonoGameUiBundle.message("new.project.templates.title")
    override val installNuGetPackageController: InstallNuGetPackagesController = object : InstallNuGetPackagesController(
        lifetime,
        context,
        presentableTemplatesName,
        shouldShowUpdatingIcon
    ) {
        override val packageName: String = "MonoGame.Templates.CSharp"
        override val suggestInstallText: @Nls String = MonoGameUiBundle.message("new.project.templates.suggestion")
    }

    override val contextHelpTitle = MonoGameUiBundle.message("new.project.templates.help", presentableTemplatesName)
    override val defaultName = "MonoGameProject1"

    init {
        installNuGetPackageController.parentInit()
        parentInit()
    }

    override fun getType(template: RdProjectTemplate) = template.name
        .removePrefix("MonoGame ")
        .replace("Application", "App")
}