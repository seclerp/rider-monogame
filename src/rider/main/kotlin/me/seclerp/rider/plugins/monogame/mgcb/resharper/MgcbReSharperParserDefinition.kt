package me.seclerp.rider.plugins.monogame.mgcb.resharper

import com.intellij.lexer.DummyLexer
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import com.jetbrains.rider.ideaInterop.fileTypes.RiderFileElementType
import com.jetbrains.rider.ideaInterop.fileTypes.RiderParserDefinitionBase

class MgcbReSharperParserDefinition : RiderParserDefinitionBase(MgcbFileElementType, MgcbReSharperFileType.Instance) {
    companion object {
        val MgcbElementType = IElementType("RIDER_MGCB_RESHARPER", MgcbReSharperLanguage.Instance)
        val MgcbFileElementType =
            RiderFileElementType("RIDER_MGCB_RESHARPER_FILE", MgcbReSharperLanguage.Instance, MgcbElementType)
    }

    override fun createLexer(project: Project?) = DummyLexer(MgcbFileElementType)
}