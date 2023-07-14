package model.rider

import com.jetbrains.rider.model.nova.ide.SolutionModel
import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

@Suppress("unused")
object MonoGameRiderModel : Ext(SolutionModel.Solution) {

    private val ToolDefinition = structdef {
        field("packageId", PredefinedType.string)
        field("version", PredefinedType.string)
        field("toolKind", enum {
            +"None"
            +"Local"
            +"Global"
        })
    }

    private val MgcbEditorToolset = aggregatedef("MgcbEditorToolset") {
        property("editor", ToolDefinition.nullable)
        property("editorWindows", ToolDefinition.nullable)
        property("editorLinux", ToolDefinition.nullable)
        property("editorMac", ToolDefinition.nullable)
    }

    private val projectId = PredefinedType.guid

    init {
        setting(CSharp50Generator.Namespace, "Rider.Plugins.MonoGame")
        setting(Kotlin11Generator.Namespace, "me.seclerp.rider.plugins.monogame")

        field("mgcbEditorGlobalToolset", MgcbEditorToolset)
        field("mgcbEditorSolutionToolset", MgcbEditorToolset)
        map("mgcbEditorProjectsToolsets", projectId, MgcbEditorToolset)
    }
}