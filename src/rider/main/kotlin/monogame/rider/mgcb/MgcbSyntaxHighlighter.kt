package monogame.rider.mgcb

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import monogame.rider.mgcb.psi.MgcbTypes

class MgcbSyntaxHighlighter : SyntaxHighlighterBase() {
  override fun getHighlightingLexer(): Lexer {
    return MgcbLexerAdapter()
  }

  override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey?> =
    when (tokenType) {
      MgcbTypes.SEPARATOR -> { SEPARATOR_KEYS }
      MgcbTypes.KEY -> { KEY_KEYS }
      MgcbTypes.VALUE -> { VALUE_KEYS }
      MgcbTypes.COMMENT -> { COMMENT_KEYS }
      TokenType.BAD_CHARACTER -> { BAD_CHAR_KEYS }
      else -> { EMPTY_KEYS }
  }

  companion object {
    val SEPARATOR: TextAttributesKey = createTextAttributesKey("MGCB_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    val KEY: TextAttributesKey = createTextAttributesKey("MGCB_KEY", DefaultLanguageHighlighterColors.KEYWORD)
    val VALUE: TextAttributesKey = createTextAttributesKey("MGCB_VALUE", DefaultLanguageHighlighterColors.STRING)
    val COMMENT: TextAttributesKey = createTextAttributesKey("MGCB_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    val BAD_CHARACTER: TextAttributesKey = createTextAttributesKey("MGCB_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

    private val BAD_CHAR_KEYS: Array<TextAttributesKey?> = arrayOf(BAD_CHARACTER)
    private val SEPARATOR_KEYS: Array<TextAttributesKey?> = arrayOf(SEPARATOR)
    private val KEY_KEYS: Array<TextAttributesKey?> = arrayOf(KEY)
    private val VALUE_KEYS: Array<TextAttributesKey?> = arrayOf(VALUE)
    private val COMMENT_KEYS: Array<TextAttributesKey?> = arrayOf(COMMENT)
    private val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls(0)
  }
}