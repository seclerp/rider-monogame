package me.seclerp.rider.plugins.monogame.effect

import com.intellij.lexer.DummyLexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import com.jetbrains.rider.ideaInterop.fileTypes.RiderFileElementType
import com.jetbrains.rider.ideaInterop.fileTypes.RiderParserDefinitionBase

class EffectParserDefinition : RiderParserDefinitionBase(ShaderLabFileElementType, EffectFileType) {
    companion object {
        val ShaderLabElementType = IElementType("RIDER_MGFX", EffectLanguage)
        val ShaderLabFileElementType = RiderFileElementType("RIDER_MGFX_FILE", EffectLanguage, ShaderLabElementType)
    }

    override fun createLexer(project: Project?) = DummyLexer(ShaderLabFileElementType)
}

