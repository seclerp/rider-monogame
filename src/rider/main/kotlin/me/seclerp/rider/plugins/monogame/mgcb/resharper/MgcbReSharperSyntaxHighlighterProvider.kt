package me.seclerp.rider.plugins.monogame.mgcb.resharper

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.fileTypes.SyntaxHighlighterProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MgcbReSharperSyntaxHighlighterProvider : SyntaxHighlighterProvider, SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, file: VirtualFile?): SyntaxHighlighter {
        return MgcbReSharperSyntaxHighlighter()
    }

    override fun create(fileType: FileType, project: Project?, file: VirtualFile?): SyntaxHighlighter? {
        if (fileType !is MgcbReSharperFileType) return null
        return MgcbReSharperSyntaxHighlighter()
    }
}