﻿using JetBrains.ReSharper.Psi.Parsing;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

internal record struct TokenPosition<TTokenType>(
    TTokenType CurrentTokenType,
    int YyBufferIndex,
    int YyBufferStart,
    int YyBufferEnd,
    int YyLexicalState
) where TTokenType : TokenNodeType;