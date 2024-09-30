using System.Collections;
using JetBrains.ReSharper.Psi.Parsing;
using Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

namespace Rider.Plugins.MonoGame.UnitTests;

public class EnumerableLexer<TTokenType>(ILexer lexer) : IEnumerable<TokenPosition<TTokenType>> where TTokenType : TokenNodeType
{
    public IEnumerator<TokenPosition<TTokenType>> GetEnumerator()
    {
        return new LexerEnumerator<TTokenType>(lexer);
    }

    IEnumerator IEnumerable.GetEnumerator()
    {
        return GetEnumerator();
    }
}

public class LexerEnumerator<TTokenType>(ILexer lexer) : IEnumerator<TokenPosition<TTokenType>> where TTokenType : TokenNodeType
{
    public bool MoveNext()
    {
        lexer.Advance();

        return lexer.TokenType != null;
    }

    public void Reset()
    {
        lexer.Start();
    }

    public TokenPosition<TTokenType> Current => (TokenPosition<TTokenType>) lexer.CurrentPosition;

    object? IEnumerator.Current => Current;

    public void Dispose()
    {
    }
}