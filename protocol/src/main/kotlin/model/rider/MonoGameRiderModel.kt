package model.rider

import com.jetbrains.rider.model.nova.ide.SolutionModel
import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

@Suppress("unused")
object MonoGameRiderModel : Ext(SolutionModel.) {
    private val MgcbToolDefinition = structdef {
        field("version", PredefinedType.string)
        field("toolKind", enum {
            +"None"
            +"Local"
            +"Global"
        })
    }

    init {
        setting(CSharp50Generator.Namespace, "Rider.Plugins.MonoGame")
        setting(Kotlin11Generator.Namespace, "me.seclerp.rider.plugins.monogame")

        // TODO
    }
}