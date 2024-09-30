using System.Text;
using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.ReSharper.Psi.Tree;
using JetBrains.Text;
using Rider.Plugins.MonoGame.Extensions;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public class MgcbTokenNode(string text, MgcbTokenType type) : LeafElementBase, ITokenNode
{
    public override int GetTextLength() => text.Length;

    public override StringBuilder GetText(StringBuilder to) => to.Apply(_ => _.Append(GetText()));

    public override IBuffer GetTextAsBuffer() => new StringBuffer(GetText());

    public override string GetText() => text;

    public override NodeType NodeType => type;
    public override PsiLanguageType Language => MgcbLanguage.Instance;
    public TokenNodeType GetTokenType() => type;

    public override string ToString()
    {
        return $"({type}, '{text}')";
    }
}