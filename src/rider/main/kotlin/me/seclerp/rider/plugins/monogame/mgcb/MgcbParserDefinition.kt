package me.seclerp.rider.plugins.monogame.mgcb

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import me.seclerp.rider.plugins.monogame.mgcb.parser.MgcbParser
import me.seclerp.rider.plugins.monogame.mgcb.psi.EffectPsiFile
import me.seclerp.rider.plugins.monogame.mgcb.psi.MgcbTypes

class MgcbParserDefinition : ParserDefinition {
    val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)

    val COMMENTS = TokenSet.create(MgcbTypes.COMMENT)

    val FILE = IFileElementType(MgcbLanguage.Instance)

    override fun createLexer(project: Project?): Lexer = MgcbLexerAdapter()

    override fun getWhitespaceTokens(): TokenSet = WHITE_SPACES

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createParser(project: Project?): PsiParser = MgcbParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun createFile(viewProvider: FileViewProvider): PsiFile = EffectPsiFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MAY

    override fun createElement(node: ASTNode?): PsiElement = MgcbTypes.Factory.createElement(node)
}