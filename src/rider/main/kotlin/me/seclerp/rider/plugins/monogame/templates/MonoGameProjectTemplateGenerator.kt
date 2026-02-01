package me.seclerp.rider.plugins.monogame.templates

import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rider.model.RdProjectTemplate
import com.jetbrains.rider.projectView.projectTemplates.NewProjectDialogContext
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesSharedModel
import com.jetbrains.rider.projectView.projectTemplates.generators.TypeListBasedProjectTemplateGenerator
import me.seclerp.rider.plugins.monogame.MonoGameIcons
import me.seclerp.rider.plugins.monogame.templates.MonoGameTemplateMetadata.Names

internal class MonoGameProjectTemplateGenerator(
    lifetime: Lifetime,
    context: NewProjectDialogContext,
    sharedModel: ProjectTemplatesSharedModel,
    projectTemplates: IOptProperty<Set<RdProjectTemplate>>
) : TypeListBasedProjectTemplateGenerator(lifetime, context, sharedModel, projectTemplates) {
    override val defaultName = "MonoGameProject1"

    override fun getPredefinedTypes() = listOf(
        TemplateTypeWithIcon(Names.CROSS_PLATFORM_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.WINDOWS_DESKTOP_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.ANDROID_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.IOS_APP, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.GAME_LIB, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.CONTENT_PIPELINE_EXTENSION, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.SHARED_LIB, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.CONTENT_BUILDER, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names._2D_START_KIT, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.BLANK_2D_START_KIT, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.CONTENT_BUILDER_2D_START_KIT, MonoGameIcons.MgcbFile),
        TemplateTypeWithIcon(Names.CONTENT_BUILDER_BLANK_START_KIT, MonoGameIcons.MgcbFile),
    )

    override fun getType(template: RdProjectTemplate) = template.name
        .removePrefix("MonoGame ")
        .replace("Application", "App")
}