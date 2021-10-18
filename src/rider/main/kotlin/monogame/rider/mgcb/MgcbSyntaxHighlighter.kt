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
      MgcbTypes.OPTION_KEY -> { OPTION_KEY_KEYS }
      MgcbTypes.OPTION_SEPARATOR -> { OPTION_SEPARATOR_KEYS }
      MgcbTypes.OPTION_VALUE -> { OPTION_VALUE_KEYS }
      MgcbTypes.SET_KEYWORD -> { PREPROCESSOR_KEYWORD_KEYS }
      MgcbTypes.PREPROCESSOR_IDENTIFIER -> { PREPROCESSOR_IDENTIFIER_KEYS }
      MgcbTypes.EQ -> { PREPROCESSOR_SEPARATOR_KEYS }
      MgcbTypes.PREPROCESSOR_VALUE -> { PREPROCESSOR_VALUE_KEYS }
      MgcbTypes.COMMENT -> { COMMENT_KEYS }
      TokenType.BAD_CHARACTER -> { BAD_CHAR_KEYS }
      else -> { EMPTY_KEYS }
  }

  companion object {
    val OPTION_KEY: TextAttributesKey = createTextAttributesKey("MGCB_OPTION_KEY", DefaultLanguageHighlighterColors.IDENTIFIER)
    val OPTION_SEPARATOR: TextAttributesKey = createTextAttributesKey("MGCB_OPTION_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    val OPTION_VALUE: TextAttributesKey = createTextAttributesKey("MGCB_OPTION_VALUE", DefaultLanguageHighlighterColors.STRING)
    val PREPROCESSOR_KEYWORD: TextAttributesKey = createTextAttributesKey("MGCB_PREPROCESSOR_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
    val PREPROCESSOR_IDENTIFIER: TextAttributesKey = createTextAttributesKey("MGCB_PREPROCESSOR_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
    val PREPROCESSOR_SEPARATOR: TextAttributesKey = createTextAttributesKey("MGCB_PREPROCESSOR_SEPARATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
    val PREPROCESSOR_VALUE: TextAttributesKey = createTextAttributesKey("MGCB_PREPROCESSOR_VALUE", DefaultLanguageHighlighterColors.STRING)
    val COMMENT: TextAttributesKey = createTextAttributesKey("MGCB_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
    val BAD_CHARACTER: TextAttributesKey = createTextAttributesKey("MGCB_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)

    private val OPTION_KEY_KEYS: Array<TextAttributesKey?> = arrayOf(OPTION_KEY)
    private val OPTION_SEPARATOR_KEYS: Array<TextAttributesKey?> = arrayOf(OPTION_SEPARATOR)
    private val OPTION_VALUE_KEYS: Array<TextAttributesKey?> = arrayOf(OPTION_VALUE)
    private val PREPROCESSOR_KEYWORD_KEYS: Array<TextAttributesKey?> = arrayOf(PREPROCESSOR_KEYWORD)
    private val PREPROCESSOR_IDENTIFIER_KEYS: Array<TextAttributesKey?> = arrayOf(PREPROCESSOR_IDENTIFIER)
    private val PREPROCESSOR_SEPARATOR_KEYS: Array<TextAttributesKey?> = arrayOf(PREPROCESSOR_SEPARATOR)
    private val PREPROCESSOR_VALUE_KEYS: Array<TextAttributesKey?> = arrayOf(PREPROCESSOR_VALUE)
    private val COMMENT_KEYS: Array<TextAttributesKey?> = arrayOf(COMMENT)
    private val BAD_CHAR_KEYS: Array<TextAttributesKey?> = arrayOf(BAD_CHARACTER)
    private val EMPTY_KEYS: Array<TextAttributesKey?> = arrayOfNulls(0)
  }
}