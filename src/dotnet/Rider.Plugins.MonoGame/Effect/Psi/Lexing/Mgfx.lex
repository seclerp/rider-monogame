using System;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;
using JetBrains.Util;

%%

%unicode

%init{
  currentTokenType = null;
%init}

%namespace Rider.Plugins.MonoGame.Effect.Psi.Lexing
%class EffectLexerGenerated
%implements IIncrementalLexer
%function _locateToken
%virtual
%public
%type TokenNodeType
%ignorecase

%eofval{
  currentTokenType = null; return currentTokenType;
%eofval}

EFFECT_CODE = .*

%%

<YYINITIAL> {EFFECT_CODE}    { return EffectTokenType.EFFECT_CODE; }
