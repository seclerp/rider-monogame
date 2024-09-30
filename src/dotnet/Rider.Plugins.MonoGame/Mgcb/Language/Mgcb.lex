using System;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;
using JetBrains.Util;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

%%

%namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing
%class MgcbLexer
%unicode
%function locateToken
%virtual
%public
%type TokenNodeType
%eof{
    return;
%eof}

CRLF=\r\n|\n|\r
SPACES=[ \t\f]
WHITE_SPACE=[ \t\f]
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

%%

<YYINITIAL> {CRLF}                                          { yybegin(YYINITIAL); return MgcbTokenTypes.NEW_LINE; }
<YYINITIAL> {WHITE_SPACE}+                                  { yybegin(YYINITIAL); return MgcbTokenTypes.WHITE_SPACE; }

<YYINITIAL> {COMMENT}                                       { yybegin(YYINITIAL); return MgcbTokenTypes.COMMENT; }
<YYINITIAL> {OPTION_KEY}                                    { yybegin(WAITING_OPTION_KEY); return MgcbTokenTypes.OPTION_KEY; }
<WAITING_OPTION_KEY> {WHITE_SPACE}*                         { yybegin(YYINITIAL); return MgcbTokenTypes.WHITE_SPACE; }
<WAITING_OPTION_KEY> {OPTION_SEPARATOR}                     { yybegin(WAITING_OPTION_VALUE); return MgcbTokenTypes.OPTION_SEPARATOR; }
<WAITING_OPTION_VALUE> {WHITE_SPACE}*                       { yybegin(YYINITIAL); return MgcbTokenTypes.WHITE_SPACE; }
<WAITING_OPTION_VALUE> {OPTION_VALUE}                       { yybegin(YYINITIAL); return MgcbTokenTypes.OPTION_VALUE; }

<YYINITIAL> {SET_KEYWORD}                                   { yybegin(WAITING_SET_KEY); return MgcbTokenTypes.SET_KEYWORD; }
<WAITING_SET_KEY> {SPACES}+                                 { yybegin(WAITING_SET_KEY); return MgcbTokenTypes.WHITE_SPACE; }
<WAITING_SET_KEY> {PREPROCESSOR_IDENTIFIER}                 { yybegin(WAITING_SET_KEY); return MgcbTokenTypes.PREPROCESSOR_IDENTIFIER; }
<WAITING_SET_KEY> {WHITE_SPACE}+                            { yybegin(YYINITIAL); return MgcbTokenTypes.WHITE_SPACE; }
<WAITING_SET_KEY> {EQ}                                      { yybegin(WAITING_SET_VALUE); return MgcbTokenTypes.EQ; }
<WAITING_SET_VALUE> {PREPROCESSOR_VALUE}                    { yybegin(YYINITIAL); return MgcbTokenTypes.PREPROCESSOR_VALUE; }

<YYINITIAL> {IF_KEYWORD}                                    { yybegin(WAITING_IF_KEY); return MgcbTokenTypes.IF_KEYWORD; }
<WAITING_IF_KEY> {SPACES}+                                  { yybegin(WAITING_IF_KEY); return MgcbTokenTypes.WHITE_SPACE; }
<WAITING_IF_KEY> {PREPROCESSOR_IDENTIFIER}                  { yybegin(WAITING_IF_KEY); return MgcbTokenTypes.PREPROCESSOR_IDENTIFIER; }
<WAITING_IF_KEY> {WHITE_SPACE}+                             { yybegin(YYINITIAL); return MgcbTokenTypes.WHITE_SPACE; }
<WAITING_IF_KEY> {EQ}                                       { yybegin(WAITING_IF_VALUE); return MgcbTokenTypes.EQ; }
<WAITING_IF_VALUE> {PREPROCESSOR_VALUE}                     { yybegin(YYINITIAL); return MgcbTokenTypes.PREPROCESSOR_VALUE; }
<YYINITIAL> {ENDIF_KEYWORD}                                 { yybegin(YYINITIAL); return MgcbTokenTypes.ENDIF_KEYWORD; }

[^]                                                         { return TokenType.BAD_CHARACTER; }