package model.rider

import com.jetbrains.rider.model.nova.ide.SolutionModel
import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

@Suppress("unused")
object MonoGameRiderModel : Ext(SolutionModel.Solution) {

    private val ToolDefinition = structdef {
        field("version", PredefinedType.string)
        field("toolKind", enum {
            +"None"
            +"Local"
            +"Global"
        })
    }

    private val projectId = PredefinedType.guid

    init {
        setting(CSharp50Generator.Namespace, "Rider.Plugins.MonoGame")
        setting(Kotlin11Generator.Namespace, "me.seclerp.rider.plugins.monogame")

        property("mgcbEditorToolGlobal", ToolDefinition)
        property("mgcbEditorToolSolution", ToolDefinition)
        map("mgcbEditorToolProjects", projectId, classdef("ToolDefinitionRx") {
            property("value", ToolDefinition)
        })
    }
}