using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;
using JetBrains.Util;

namespace Rider.Plugins.MonoGame.Mgcb.Language.Parsing;

public class MgcbTokenType(string representation, int index) : TokenNodeType(representation, index)
{
    public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
    {
        return new MgcbTokenNode(buffer.GetText(new TextRange(startOffset.Offset, endOffset.Offset)), this);
    }

    public override bool IsWhitespace => false;
    public override bool IsComment => false;
    public override bool IsStringLiteral => false;
    public override bool IsConstantLiteral => false;
    public override bool IsIdentifier => false;
    public override bool IsKeyword => false;

    public override string TokenRepresentation => representation;
}