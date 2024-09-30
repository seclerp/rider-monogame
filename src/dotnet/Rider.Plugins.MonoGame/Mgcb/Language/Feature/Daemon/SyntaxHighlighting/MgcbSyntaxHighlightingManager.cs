using JetBrains.ReSharper.Daemon.Syntax;
using JetBrains.ReSharper.Psi;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Feature.Daemon.SyntaxHighlighting;

[Language(typeof(MgcbLanguage))]
public class MgcbSyntaxHighlightingManager : SyntaxHighlightingManager
{
    public override SyntaxHighlightingProcessor CreateProcessor()
    {
        return new MgcbSyntaxHighlightingProcessor();
    }
}