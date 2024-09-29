using JetBrains.Annotations;
using JetBrains.ReSharper.Psi.Parsing;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public class MgcbFilteringLexer([NotNull] ILexer lexer) : FilteringLexer(lexer)
{
    // TODO: Revisit after splitting WHITESPACE and NEWLINE tokens, as NEWLINE are very important for parsing
    protected override bool Skip(TokenNodeType tokenType)
    {
        return tokenType.IsComment;
    }
}