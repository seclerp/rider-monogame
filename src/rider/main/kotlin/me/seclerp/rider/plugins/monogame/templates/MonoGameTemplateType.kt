package me.seclerp.rider.plugins.monogame.templates

import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.model.RdProjectTemplate
import com.jetbrains.rider.projectView.projectTemplates.NewProjectDialogContext
import com.jetbrains.rider.projectView.projectTemplates.ProjectTemplatesSharedModel
import com.jetbrains.rider.projectView.projectTemplates.templateTypes.PredefinedProjectTemplateType
import com.jetbrains.rider.projectView.projectTemplates.utils.hasClassification
import me.seclerp.rider.plugins.monogame.MonoGameIcons

internal class MonoGameTemplateType : PredefinedProjectTemplateType() {
    override val icon = MonoGameIcons.MgcbFile
    override val name = "MonoGame"
    override val order = 50

    override fun acceptableForTemplate(projectTemplate: RdProjectTemplate): Boolean {
        return projectTemplate.hasClassification("MonoGame")
    }

    override fun createGenerator(lifetime: Lifetime, context: NewProjectDialogContext, sharedModel: ProjectTemplatesSharedModel) =
        MonoGameProjectTemplateGenerator(lifetime, context, sharedModel, projectTemplates)
}