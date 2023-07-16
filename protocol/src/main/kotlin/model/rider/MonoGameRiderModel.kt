package model.rider

import com.jetbrains.rider.model.nova.ide.SolutionModel
import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

@Suppress("unused")
object MonoGameRiderModel : Ext(SolutionModel.Solution) {

    private val ToolDefinition = structdef {
        field("packageId", PredefinedType.string)
        field("commandName", PredefinedType.string)
        field("version", PredefinedType.string)
    }

    private val MgcbEditorToolset = aggregatedef("MgcbEditorToolset") {
        property("editor", ToolDefinition.nullable)
    }

    private val projectId = PredefinedType.guid

    init {
        setting(CSharp50Generator.Namespace, "Rider.Plugins.MonoGame")
        setting(Kotlin11Generator.Namespace, "me.seclerp.rider.plugins.monogame")

        field("mgcbGlobalToolset", MgcbEditorToolset)
        field("mgcbSolutionToolset", MgcbEditorToolset)
        map("mgcbProjectsToolsets", projectId, MgcbEditorToolset)
    }
}