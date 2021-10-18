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
SPACES=[\ \t\f]
WHITE_SPACE=[\ \n\t\f]
COMMENT="#"[^\r\n]*

OPTION_KEY="/"[^\ \t:\r\n]+
OPTION_SEPARATOR=":"
OPTION_VALUE=[^\r\n]+

PREPROCESSOR_IDENTIFIER=[^\ \t:=\r\n]+
PREPROCESSOR_VALUE=[^\ \t:=\r\n]+
EQ="="

SET_KEYWORD="$set"
IF_KEYWORD="$if"
ENDIF_KEYWORD="$endif"

%state WAITING_OPTION_KEY
%state WAITING_OPTION_VALUE
%state WAITING_SET_KEY
%state WAITING_SET_VALUE
%state WAITING_IF_KEY
%state WAITING_IF_VALUE
%state WAITING_WHITESPACE

%%

<YYINITIAL> {COMMENT}                                       { yybegin(YYINITIAL); return MgcbTypes.COMMENT; }
<YYINITIAL> {OPTION_KEY}                                    { yybegin(WAITING_OPTION_KEY); return MgcbTypes.OPTION_KEY; }
<WAITING_OPTION_KEY> {WHITE_SPACE}*{EOL}                    { yybegin(YYINITIAL); return MgcbTypes.WHITE_SPACE; }
<WAITING_OPTION_KEY> {OPTION_SEPARATOR}                     { yybegin(WAITING_OPTION_VALUE); return MgcbTypes.OPTION_SEPARATOR; }
<WAITING_OPTION_VALUE> {WHITE_SPACE}*{EOL}                  { yybegin(YYINITIAL); return MgcbTypes.WHITE_SPACE; }
<WAITING_OPTION_VALUE> {OPTION_VALUE}                       { yybegin(WAITING_WHITESPACE); return MgcbTypes.OPTION_VALUE; }

<YYINITIAL> {SET_KEYWORD}                                   { yybegin(WAITING_SET_KEY); return MgcbTypes.SET_KEYWORD; }
<WAITING_SET_KEY> {SPACES}+                                 { yybegin(WAITING_SET_KEY); return MgcbTypes.WHITE_SPACE; }
<WAITING_SET_KEY> {PREPROCESSOR_IDENTIFIER}                 { yybegin(WAITING_SET_KEY); return MgcbTypes.PREPROCESSOR_IDENTIFIER; }
<WAITING_SET_KEY> {WHITE_SPACE}*{EOL}                       { yybegin(YYINITIAL); return MgcbTypes.WHITE_SPACE; }
<WAITING_SET_KEY> {EQ}                                      { yybegin(WAITING_SET_VALUE); return MgcbTypes.EQ; }
<WAITING_SET_VALUE> {PREPROCESSOR_VALUE}                    { yybegin(WAITING_WHITESPACE); return MgcbTypes.PREPROCESSOR_VALUE; }

<YYINITIAL> {IF_KEYWORD}                                    { yybegin(WAITING_IF_KEY); return MgcbTypes.IF_KEYWORD; }
<WAITING_IF_KEY> {SPACES}+                                  { yybegin(WAITING_IF_KEY); return MgcbTypes.WHITE_SPACE; }
<WAITING_IF_KEY> {PREPROCESSOR_IDENTIFIER}                  { yybegin(WAITING_IF_KEY); return MgcbTypes.PREPROCESSOR_IDENTIFIER; }
<WAITING_IF_KEY> {WHITE_SPACE}*{EOL}                        { yybegin(YYINITIAL); return MgcbTypes.WHITE_SPACE; }
<WAITING_IF_KEY> {EQ}                                       { yybegin(WAITING_IF_VALUE); return MgcbTypes.EQ; }
<WAITING_IF_VALUE> {PREPROCESSOR_VALUE}                     { yybegin(WAITING_WHITESPACE); return MgcbTypes.PREPROCESSOR_VALUE; }
<YYINITIAL> {ENDIF_KEYWORD}                                 { yybegin(YYINITIAL); return MgcbTypes.ENDIF_KEYWORD; }

<WAITING_WHITESPACE> ({CRLF}|{WHITE_SPACE})+                { yybegin(YYINITIAL); return MgcbTypes.WHITE_SPACE; }
<YYINITIAL> ({CRLF}|{WHITE_SPACE})+                         { yybegin(YYINITIAL); return MgcbTypes.WHITE_SPACE; }

[^]                                                         { return TokenType.BAD_CHARACTER; }