package me.seclerp.rider.plugins.monogame.templates

import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.model.RdProjectTemplate
import com.jetbrains.rider.projectView.projectTemplates.NewProjectDialogContext
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesSharedModel
import com.jetbrains.rider.projectView.projectTemplates.generators.TypeListBasedProjectTemplateGenerator
import com.jetbrains.rider.projectView.projectTemplates.templateTypes.PredefinedProjectTemplateType
import com.jetbrains.rider.projectView.projectTemplates.utils.hasClassification
import me.seclerp.rider.plugins.monogame.MonoGameIcons

class MonoGameTemplateType : PredefinedProjectTemplateType() {
    override val icon = MonoGameIcons.MgcbFile
    override val name = "MonoGame"
    override val order = 50

    override fun acceptableForTemplate(projectTemplate: RdProjectTemplate): Boolean {
        return projectTemplate.hasClassification("MonoGame")
    }

    override fun createGenerator(lifetime: Lifetime, context: NewProjectDialogContext, sharedModel: ProjectTemplatesSharedModel) =
        object : TypeListBasedProjectTemplateGenerator(lifetime, context, sharedModel, projectTemplates) {
            override val defaultName = "MonoGameProject1"

            override fun getPredefinedTypes() = listOf(
                TemplateTypeWithIcon(MonoGameTemplateNames.CROSS_PLATFORM_APP, MonoGameIcons.MgcbFile),
                TemplateTypeWithIcon(MonoGameTemplateNames.WINDOWS_DESKTOP_APP, MonoGameIcons.MgcbFile),
                TemplateTypeWithIcon(MonoGameTemplateNames.ANDROID_APP, MonoGameIcons.MgcbFile),
                TemplateTypeWithIcon(MonoGameTemplateNames.IOS_APP, MonoGameIcons.MgcbFile),
                TemplateTypeWithIcon(MonoGameTemplateNames.GAME_LIB, MonoGameIcons.MgcbFile),
                TemplateTypeWithIcon(MonoGameTemplateNames.CONTENT_PIPELINE_EXTENSION, MonoGameIcons.MgcbFile),
                TemplateTypeWithIcon(MonoGameTemplateNames.SHARED_LIB, MonoGameIcons.MgcbFile),
            )

            override fun getType(template: RdProjectTemplate) = template.name
                .removePrefix("MonoGame ")
                .replace("Application", "App")
        }
}