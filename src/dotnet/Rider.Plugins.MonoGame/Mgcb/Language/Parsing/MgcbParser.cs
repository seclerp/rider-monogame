using JetBrains.Lifetimes;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public partial class MgcbParser : IParser
{
    public IFile ParseFile()
    {
        return Lifetime.Using(lifetime =>
        {
            var builder = CreateTreeBuilder(lifetime);
            builder.ParseFile();
            return (IFile) builder.GetTree();
        });
    }
    
    private TreeBuilder CreateTreeBuilder(Lifetime lifetime)
    {
        return new JsonNewTreeBuilder(myLexer, lifetime);
    }
}