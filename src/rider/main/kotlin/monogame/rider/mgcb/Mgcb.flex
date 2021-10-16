package monogame.rider.mgcb;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import monogame.rider.mgcb.psi.MgcbTypes;
import com.intellij.psi.TokenType;

%%

%class MgcbLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
EOL=[\r\n]
WHITE_SPACE=[\ \n\t\f]
COMMENT="#"[^\r\n]*
OPTION_KEY="/"[^:\r\n]*
OPTION_SEPARATOR=":"
OPTION_VALUE=[^\r\n]+

%state WAITING_VALUE

%%

<YYINITIAL> {COMMENT}                                       { yybegin(YYINITIAL); return MgcbTypes.COMMENT; }
<YYINITIAL> {OPTION_KEY}                                    { yybegin(YYINITIAL); return MgcbTypes.KEY; }
<YYINITIAL> {OPTION_SEPARATOR}                              { yybegin(WAITING_VALUE); return MgcbTypes.SEPARATOR; }
<WAITING_VALUE> {WHITE_SPACE}*{EOL}                         { yybegin(YYINITIAL); return MgcbTypes.VALUE; }
<WAITING_VALUE> {OPTION_VALUE}                              { yybegin(YYINITIAL); return MgcbTypes.VALUE; }
({CRLF}|{WHITE_SPACE})+                                     { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }