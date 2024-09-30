using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

internal class MgcbParser : MgcbParserGenerated, IParser
{
    public MgcbParser(ILexer lexer)
    {
        SetLexer(new MgcbFilteringLexer(lexer));
    }

    public IFile ParseFile()
    {
        var parsed = ParseMgcbFile();
        return parsed as IFile;
    }
}