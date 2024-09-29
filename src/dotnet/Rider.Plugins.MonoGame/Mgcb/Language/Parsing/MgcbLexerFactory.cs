using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public class MgcbLexerFactory : ILexerFactory
{
    public ILexer CreateLexer(IBuffer buffer) => new MgcbLexer(buffer);
}