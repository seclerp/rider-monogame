using JetBrains.ReSharper.Psi;
using JetBrains.ReSharper.Psi.ExtensionsAPI.Tree;
using JetBrains.ReSharper.Psi.Parsing;
using JetBrains.Text;

namespace Rider.Plugins.MonoGame.Effect.Psi.Lexing;

public partial class EffectTokenType
{
    public class EffectTokenNodeType : TokenNodeType
    {
        public EffectTokenNodeType(string s, int index) : base(s, index)
        {
        }

        public override LeafElementBase Create(IBuffer buffer, TreeOffset startOffset, TreeOffset endOffset)
        {
            throw new System.NotImplementedException();
        }

        public override bool IsWhitespace => false;     // this == WHITESPACE || this == NEW_LINE;
        public override bool IsComment => false;
        public override bool IsStringLiteral => false;  // this == STRING_LITERAL
        public override bool IsConstantLiteral => false;    // LITERALS[this]
        public override bool IsIdentifier => false;  // this == IDENTIFIER
        public override bool IsKeyword => false;    // KEYWORDS[this]
        public override string TokenRepresentation => "HLSL Code";
    }
}